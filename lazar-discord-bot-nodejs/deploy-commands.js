const { REST, Routes } = require('discord.js');
const fs = require('fs');
const path = require('path');
require('dotenv').config()

async function main(){
    const rest = new REST().setToken(process.env.TOKEN);
    await deployCommands(process.env.CLIENT_ID, process.env.GUILD_ID, rest, process.env.COMMANDS_PATH);
}

async function deployCommands(client_id, guild_id, rest, commandsPathString){
    const commands = [];
    const foldersPath = path.join(__dirname, commandsPathString);
    const commandFolders = fs.readdirSync(foldersPath);
    for (const folder of commandFolders) {
        const commandsPath = path.join(foldersPath, folder);
        const commandFiles = fs.readdirSync(commandsPath).filter(file => file.endsWith('.js'));
        for (const file of commandFiles) {
            const filePath = path.join(commandsPath, file);
            const command = require(filePath);
            if ('data' in command && 'execute' in command) {
                commands.push(command.data.toJSON());
            } 
            else {
                console.log(`[WARNING] The command at ${filePath} is missing a required "data" or "execute" property.`);
            }
        }
    }
    try {
        console.log(`Started refreshing ${commands.length} application (/) commands.`);
            const data = await rest.put(
            Routes.applicationCommands(client_id, guild_id),
            { body: commands },
        );

        console.log(`Successfully reloaded ${data.length} application (/) commands.`);
    } 
    catch (error) {
        console.error(error);
    }
}

main();