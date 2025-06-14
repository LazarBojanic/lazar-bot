const { SlashCommandBuilder, MessageFlags } = require('discord.js');
const fs = require('fs')
const util = require('../../util')
require('dotenv').config()
const ip = process.env.SERVER_IP
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
		try{
			const username = interaction.user.username;
			const guessWord = interaction.options.getString('guessword', true).toUpperCase();
			console.log(guessWord);
			const userTryRequest = {
				username: username,
				word: guessWord
			}
			const userTryObjRes = await fetch(`${ip}game/guess`, {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json'
				},
				body: JSON.stringify(userTryRequest)
			})
			const userTryObj = await userTryObjRes.json();
			if(userTryObj.reason === 'gameEnded'){
				await interaction.reply({content: 'Game ended. You can start a new one with /newgame', flags: MessageFlags.Ephemeral});
			}
			else if(userTryObj.reason === 'solutionNotValid'){
				await interaction.reply({content: `Guess: ${guessWord}, not valid!`, flags: MessageFlags.Ephemeral});
			}
			else{
				let letterStatusesEmojis = '';
				for(let i = 0; i < userTryObj.validatedWord.letters.length; i++){
					switch(userTryObj.validatedWord.letters[i].status){
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
				await interaction.reply({content: `${guessWord}: ${letterStatusesEmojis}`, flags: MessageFlags.Ephemeral});
				const boardImageRes = await fetch(`${ip}game/get-board-image?username=${username}`, {
					method: 'GET',
					headers: {
						'Content-Type': 'image/png'
					}
				})
				const boardImageBuffer = await boardImageRes.arrayBuffer();
				await interaction.followUp({files: [{
					attachment: Buffer.from(boardImageBuffer),
					name: `board_${username}.png`
				}], ephemeral: true})

				const keyboardImageRes = await fetch(`${ip}game/get-keyboard-image?username=${username}`, {
					method: 'GET',
					headers: {
						'Content-Type': 'image/png'
					}
				})
				const keyboardImageBuffer = await keyboardImageRes.arrayBuffer();
				await interaction.followUp({files: [{
					attachment: Buffer.from(keyboardImageBuffer),
					name: `keyboard_${username}.png`
				}], ephemeral: true})

				const userSessionRes = await fetch(`${ip}game/check-game-status?username=${username}`, {
					method: 'GET',
					headers: {
						'Content-Type': 'application/json'
					}
				})
				const userSessionObj = await userSessionRes.json();
				if(userSessionObj.status === 'unsolved'){
					await interaction.followUp({content: `Remaining tries: ${userSessionObj.remainingTries}`, flags: MessageFlags.Ephemeral});
				}
				else{
					const dictionaryWordRes = await fetch(`${ip}dictionary-words/get-simple?word=${userSessionObj.word}`, {
						method: 'GET',
						headers: {
							'Content-Type': 'application/json'
						}
					})
					const dictionaryWordObj = await dictionaryWordRes.json();
					let dictionaryWordFormattedString = `Word: ${dictionaryWordObj.word}\n`;
					if(userSessionObj.status === 'solved'){
						await interaction.followUp({content: `You win! The word was: ${userSessionObj.word}`, flags: MessageFlags.Ephemeral});
						dictionaryWordObj.meanings.forEach(meaning => {
							dictionaryWordFormattedString = dictionaryWordFormattedString.concat(`\tPart of Speech: ${meaning.partOfSpeech}\n`)
							meaning.definitions.forEach(definition => {
								dictionaryWordFormattedString = dictionaryWordFormattedString.concat(`\t\tDefinition: ${definition}\n`)
							})
						})
						await util.sendLargeMessage(interaction, dictionaryWordFormattedString);
					}
					else if(userSessionObj.status === 'gameOver'){
						await interaction.followUp({content: `Game over, the word was: ${userSessionObj.word}`, flags: MessageFlags.Ephemeral});
						dictionaryWordObj.meanings.forEach(meaning => {
							dictionaryWordFormattedString = dictionaryWordFormattedString.concat(`\tPart of Speech: ${meaning.partOfSpeech}\n`)
							meaning.definitions.forEach(definition => {
								dictionaryWordFormattedString = dictionaryWordFormattedString.concat(`\t\tDefinition: ${definition}\n`)
							})
						})
						await util.sendLargeMessage(interaction, dictionaryWordFormattedString);
					}
				}
			}
		}
		catch(error){
			console.error(error);
	        await interaction.reply(`Error: ${error}`);
		}
	}
};