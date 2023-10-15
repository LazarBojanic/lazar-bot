const { Client, Collection, Events, GatewayIntentBits } = require('discord.js');
const fs = require('fs');
const path = require('path');
require('dotenv').config()

function main(){
    const client = getClient(process.env.TOKEN);
    registerCommands(client, process.env.COMMANDS_PATH);
    registerEvents(client, process.env.EVENTS_PATH);
}
function getClient(token){
    const client = new Client({ intents: [GatewayIntentBits.Guilds] });
    
    client.login(token);
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