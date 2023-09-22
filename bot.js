const tmi = require('tmi.js');
const fs = require('fs');
var opener = require('opener')
const readline = require('readline')

async function main(){
    const credentials = await loadCredentials('credentials.json');
    const authorizationUrl = getAuthorizationUrl(credentials);
    opener(authorizationUrl);
    const consoleInterface = readline.createInterface({
        input: process.stdin,
        output: process.stdout,
    });
    consoleInterface.question('Paste the token from the URL: ', (token) => {
        consoleInterface.close();
        const client = getClient(credentials, token);
        client.connect().catch(console.error);
        registerMethods(client);
    });
}
async function loadCredentials(filePath) {
    try {
      const credentialsJson = await fs.promises.readFile(filePath, 'utf8'); // Read the file as a string
      const credentials = JSON.parse(credentialsJson); // Parse the string into a JSON object
      return credentials;
    } catch (error) {
      console.error('Error:', error);
      throw error;
    }
  }
function getAuthorizationUrl(credentials){
    return `${credentials.authorize_endpoint}?force_verify=${credentials.force_verify}&client_id=${credentials.client_id}&redirect_uri=${credentials.redirect_uri}&response_type=${credentials.response_type}&scope=${credentials.scope}`;
}
function getClient(credentials, token){
    return new tmi.Client({
        options: {debug: true, messagesLogLevel: 'info'},
        connection: {
            reconnect: true,
            secure: true
        },
        identity: {
            username: credentials.username,
            password: `oauth:${token}`
        },
        channels: [credentials.channel]
    })
}
function registerMethods(client){
    client.on('message', (channel, tags, message, self) => {
        const chatMessage = message.toString().toLowerCase();
        if (self) return;
        switch (chatMessage) {
            case '!test':
                client.say(channel, `Hello, ${tags.username}!`);
                break;
            default:
                break;
        }
    });
}
main();