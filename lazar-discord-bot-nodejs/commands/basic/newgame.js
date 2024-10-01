const { SlashCommandBuilder } = require('discord.js');
require('dotenv').config()
const ip = process.env.SERVER_IP
module.exports = {
	data: new SlashCommandBuilder()
		.setName('newgame')
		.setDescription('Starts a new wordle game.'),
	category: 'basic',
	async execute(interaction) {
		try{
			const username = interaction.user.username;
			const newGameRes = await fetch(`${ip}/api/game/new?username=${username}`)
			const newGameObj = await newGameRes.json();
			await interaction.reply({content: `Starting new game: ${newGameObj.status}`, ephemeral: true});
		}
		catch(error){
			console.error(error);
			await interaction.reply({content: `Error: ${error}`, ephemeral: true});
		}
	},
};