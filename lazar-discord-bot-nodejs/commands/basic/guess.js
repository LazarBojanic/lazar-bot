const { SlashCommandBuilder } = require('discord.js');

module.exports = {
	data: new SlashCommandBuilder()
		.setName('guess')
		.setDescription('Guess the current wordle word.')
		.addStringOption(option =>
			option.setName('guessword')
				.setDescription('Word guess.')
				.setRequired(true)),
	category: 'basic',
	async execute(interaction) {
		const username = interaction.user.username;
		const guessWord = interaction.options.getString('guessword', true).toUpperCase();
		console.log(guessWord);
		const userTryRequest = {
			username: username,
			word: guessWord
		}
		const userTryObjRes = await fetch(`http://94.189.193.50:5003/api/solutions/check`, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(userTryRequest)
		})
		const userTryObj = await userTryObjRes.json();
		if(userTryObj.reason == 'game_ended'){
			await interaction.reply('Game ended. You can start a new one with /newgame');
		}
		else if(userTryObj.reason == 'solution_not_valid'){
			await interaction.reply(`Guess: ${guessWord}, not valid!`);
		}
		else{
			let letterStatusesEmojis = '';
			for(let i = 0; i < userTryObj.validated_word.letters.length; i++){
				switch(userTryObj.validated_word.letters[i].status){
				case 'R':
					letterStatusesEmojis = letterStatusesEmojis.concat('❌');
					break;
				case 'Y':
					letterStatusesEmojis = letterStatusesEmojis.concat('💡');
					break;
				case 'G':
					letterStatusesEmojis = letterStatusesEmojis.concat('✅');
					break;
				}
			}
			await interaction.reply(`${guessWord}: ${letterStatusesEmojis}`);
			const userSessionObjRes = await fetch(`http://94.189.193.50:5003/api/solutions/checkGameStatus?username=${username}`, {
				method: 'GET',
				headers: {
					'Content-Type': 'application/json'
				}
			})
			const userSessionObj = await userSessionObjRes.json();
			if(userSessionObj.status == 'unsolved'){
				await interaction.followUp(`Remaining tries: ${userSessionObj.remaining_tries}`);
			}
			else if(userSessionObj.status == 'solved'){
				await interaction.followUp(`You win! The word was: ${userSessionObj.word}`);
			}
			else if(userSessionObj.status == 'game_over'){
				await interaction.followUp(`Game over, the word was: ${userSessionObj.word}`);
			}
		}
	},
};