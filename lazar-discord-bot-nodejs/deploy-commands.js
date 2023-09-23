const { REST, Routes } = require('discord.js');
const fs = require('node:fs');
const path = require('node:path');
const CREDENTIALS_PATH_STRING = "credentials.json";
const COMMANDS_PATH_STRING = "commands";
async function main(){
    const credentials = await loadCredentials(CREDENTIALS_PATH_STRING);
    const rest = new REST().setToken(credentials.token);
    await deployCommands(credentials, rest, COMMANDS_PATH_STRING);
}
async function loadCredentials(credentialsPathString) {
    try {
      const credentialsJson = await fs.promises.readFile(credentialsPathString, 'utf8');
      const credentials = JSON.parse(credentialsJson);
      return credentials;
    } catch (error) {
      console.error('Error:', error);
      throw error;
    }
  }
async function deployCommands(credentials, rest, commandsPathString){
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
            Routes.applicationCommands(credentials.client_id, credentials.guild_id),
            { body: commands },
        );

        console.log(`Successfully reloaded ${data.length} application (/) commands.`);
    } 
    catch (error) {
        // And of course, make sure you catch and log any errors!
        console.error(error);
    }
}

main();