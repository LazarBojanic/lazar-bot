const { REST, Routes } = require('discord.js');
const fs = require('fs');
const path = require('path');
require('dotenv').config()

async function main(){
    const rest = new REST().setToken(process.env.TOKEN);
    await deployCommands(process.env.CLIENT_ID, process.env.GUILD_ID, rest, process.env.COMMANDS_PATH);
}

async function deployCommands(clientID, guildID, rest, commandsPathString){
    try{
        const commands = [];
        // Grab all the command folders from the commands directory you created earlier
        const foldersPath = path.join(__dirname, commandsPathString);
        const commandFolders = fs.readdirSync(foldersPath);

        for (const folder of commandFolders) {
            // Grab all the command files from the commands directory you created earlier
            const commandsPath = path.join(foldersPath, folder);
            const commandFiles = fs.readdirSync(commandsPath).filter(file => file.endsWith('.js'));
            // Grab the SlashCommandBuilder#toJSON() output of each command's data for deployment
            for (const file of commandFiles) {
                const filePath = path.join(commandsPath, file);
                const command = require(filePath);
                if ('data' in command && 'execute' in command) {
                    commands.push(command.data.toJSON());
                } else {
                    console.log(`[WARNING] The command at ${filePath} is missing a required "data" or "execute" property.`);
                }
            }
        }
        console.log(`Started refreshing ${commands.length} application (/) commands.`);

        // The put method is used to fully refresh all commands in the guild with the current set
        const data = await rest.put(
            Routes.applicationGuildCommands(clientID, guildID),
            { body: commands },
        );

        console.log(`Successfully reloaded ${data.length} application (/) commands.`);
    }
    catch(e){
        console.error(e);
    }
}

main();