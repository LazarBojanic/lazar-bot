const { SlashCommandBuilder } = require('discord.js');
const fs = require('fs')
const util = require('../../util')
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
		const userTryObjRes = await fetch(`http://94.189.193.50:5003/api/solutions/guess`, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(userTryRequest)
		})
		const userTryObj = await userTryObjRes.json();
		if(userTryObj.reason == 'game_ended'){
			await interaction.reply({content: 'Game ended. You can start a new one with /newgame', ephemeral: true});
		}
		else if(userTryObj.reason == 'solution_not_valid'){
			await interaction.reply({content: `Guess: ${guessWord}, not valid!`, ephemeral: true});
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
			await interaction.reply({content: `${guessWord}: ${letterStatusesEmojis}`, ephemeral: true});

			const boardImageRes = await fetch(`http://localhost:5003/api/game/getBoardForUser?username=${username}`, {
				method: 'GET',
				headers: {
					'Content-Type': 'image/png'
				}
			})
			const boardImageBuffer = await boardImageRes.arrayBuffer();
			const boardImagePath = `./assets/board_${username}.png`;
            fs.writeFile(boardImagePath, Buffer.from(boardImageBuffer), async (err) => {
                await interaction.followUp({files: [{
					attachment: boardImagePath,
					name: `board_${username}.png`
				  }], ephemeral: true})
            });

			const keyboardImageRes = await fetch(`http://localhost:5003/api/game/getKeyboardForUser?username=${username}`, {
				method: 'GET',
				headers: {
					'Content-Type': 'image/png'
				}
			})
			const keyboardImageBuffer = await keyboardImageRes.arrayBuffer();
			const keyboardImagePath = `./assets/keyboard_${username}.png`;
            fs.writeFile(keyboardImagePath, Buffer.from(keyboardImageBuffer), async (err) => {
                await interaction.followUp({files: [{
					attachment: keyboardImagePath,
					name: `keyboard_${username}.png`
				  }], ephemeral: true})
            });


			const userSessionRes = await fetch(`http://94.189.193.50:5003/api/game/checkGameStatus?username=${username}`, {
				method: 'GET',
				headers: {
					'Content-Type': 'application/json'
				}
			})
			const userSessionObj = await userSessionRes.json();
			if(userSessionObj.status == 'unsolved'){
				await interaction.followUp({content: `Remaining tries: ${userSessionObj.remaining_tries}`, ephemeral: true});
			}
			else if(userSessionObj.status == 'solved'){
				await interaction.followUp({content: `You win! The word was: ${userSessionObj.word}`, ephemeral: true});
				const dictionaryWordRes = await fetch(`http://94.189.193.50:5003/api/dictionaryWords/getSimpleByWord?word=${userSessionObj.word}`, {
					method: 'GET',
					headers: {
						'Content-Type': 'application/json'
					}
				})
				const dictionaryWordObj = await dictionaryWordRes.json();
				let dictionaryWordFormattedString = `Word: ${dictionaryWordObj.word}\n`;
				dictionaryWordObj.meanings.forEach(meaning => {
					dictionaryWordFormattedString = dictionaryWordFormattedString.concat(`\tPart of Speech: ${meaning.part_of_speech}\n`)
					meaning.definitions.forEach(definition => {
						dictionaryWordFormattedString = dictionaryWordFormattedString.concat(`\t\tDefinition: ${definition}\n`)
					})		
				})
				await util.sendLargeMessage(interaction, dictionaryWordFormattedString);

			}
			else if(userSessionObj.status == 'game_over'){
				await interaction.followUp({content: `Game over, the word was: ${userSessionObj.word}`, ephemeral: true});
				const dictionaryWordRes = await fetch(`http://94.189.193.50:5003/api/dictionaryWords/getSimpleByWord?word=${userSessionObj.word}`, {
					method: 'GET',
					headers: {
						'Content-Type': 'application/json'
					}
				})
				const dictionaryWordObj = await dictionaryWordRes.json();
				let dictionaryWordFormattedString = `Word: ${dictionaryWordObj.word}\n`;
				dictionaryWordObj.meanings.forEach(meaning => {
					dictionaryWordFormattedString = dictionaryWordFormattedString.concat(`\tPart of Speech: ${meaning.part_of_speech}\n`)
					meaning.definitions.forEach(definition => {
						dictionaryWordFormattedString = dictionaryWordFormattedString.concat(`\t\tDefinition: ${definition}\n`)
					})		
				})
				await util.sendLargeMessage(interaction, dictionaryWordFormattedString);
			}

		}
	},
};