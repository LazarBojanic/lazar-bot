const { SlashCommandBuilder } = require('discord.js');

module.exports = {
	data: new SlashCommandBuilder()
		.setName('newgame')
		.setDescription('Starts a new wordle game.'),
	category: 'basic',
	async execute(interaction) {
		const username = interaction.user.username;
		const newGameRes = await fetch(`http://94.189.193.50:5003/api/game/new?username=${username}`)
		const newGameObj = await newGameRes.json();
		await interaction.reply({content: `Starting new game: ${newGameObj.status}`, ephemeral: true});
	},
};