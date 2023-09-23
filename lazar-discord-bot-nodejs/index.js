const { Client, Collection, Events, GatewayIntentBits } = require('discord.js');
const fs = require('fs');
const path = require('path');
const CREDENTIALS_PATH_STRING = "credentials.json";
const COMMANDS_PATH_STRING = "commands";
const EVENTS_PATH_STRING = "events";
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

async function main(){
    const credentials = await loadCredentials(CREDENTIALS_PATH_STRING);
    const client = getClient(credentials);
    registerCommands(client, COMMANDS_PATH_STRING);
    registerEvents(client, EVENTS_PATH_STRING);
}
function getClient(credentials){
    const client = new Client({ intents: [GatewayIntentBits.Guilds] });
    
    client.login(credentials.token);
    return client;
}
function registerCommands(client, commandsPathString){
    client.commands = new Collection();
    const foldersPath = path.join(__dirname, commandsPathString);
    const commandFolders = fs.readdirSync(foldersPath);

    for (const folder of commandFolders) {
        const commandsPath = path.join(foldersPath, folder);
        const commandFiles = fs.readdirSync(commandsPath).filter(file => file.endsWith('.js'));
        for (const file of commandFiles) {
            const filePath = path.join(commandsPath, file);
            const command = require(filePath);
            if ('data' in command && 'execute' in command) {
                client.commands.set(command.data.name, command);
            } else {
                console.log(`[WARNING] The command at ${filePath} is missing a required "data" or "execute" property.`);
            }
        }
    }
    
}
function registerEvents(client, eventsPathString){
    const eventsPath = path.join(__dirname, eventsPathString);
    const eventFiles = fs.readdirSync(eventsPath).filter(file => file.endsWith('.js'));

    for (const file of eventFiles) {
        const filePath = path.join(eventsPath, file);
        const event = require(filePath);
        if (event.once) {
            client.once(event.name, (...args) => event.execute(...args));
        } else {
            client.on(event.name, (...args) => event.execute(...args));
        }
    }
}
main();