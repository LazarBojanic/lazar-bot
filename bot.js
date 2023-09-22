const tmi = require('tmi.js');
const fs = require('fs');
const opener = require('opener');
const readline = require('readline');
const TOKEN_FILE = 'token.json';
const CREDENTIALS_FILE = 'credentials.json';

async function main() {
  let credentials, client, token;
    try{
        credentials = await loadCredentials(CREDENTIALS_FILE);
        token = await loadToken(TOKEN_FILE);
        client = getClient(credentials, token);
    }
    catch(error){
        console.log('Cached token is invalid or missing.');
        console.log('Using new token.');
        const authorizationUrl = getAuthorizationUrl(credentials);
        opener(authorizationUrl);
        const consoleInterface = readline.createInterface({
            input: process.stdin,
            output: process.stdout,
        });
        consoleInterface.question('Paste the token from the URL: ', async (newToken) => {
            consoleInterface.close();
            token = newToken;
            await fs.promises.writeFile(TOKEN_FILE, JSON.stringify({ token }), 'utf8');
            client = getClient(credentials, token);
        });
    }
    console.log('Using cached token.');
    client.connect().catch(console.error);
    registerMethods(client);
}
async function loadToken(filePath){
    const tokenJson = await fs.promises.readFile(filePath, 'utf8');
    return JSON.parse(tokenJson).token;
}
async function loadCredentials(filePath) {
  try {
    const credentialsJson = await fs.promises.readFile(filePath, 'utf8');
    const credentials = JSON.parse(credentialsJson);
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
    options: { debug: true, messagesLogLevel: 'info' },
    connection: {
      reconnect: true,
      secure: true,
    },
    identity: {
      username: credentials.username,
      password: `oauth:${token}`,
    },
    channels: [credentials.channel],
  });
}

function registerMethods(client){
  client.on('message', (channel, tags, message, self) => {
    const chatMessage = message.toString().trim();
    if (self) return;
    switch (chatMessage) {
      case '!getCurrentSolution':
        fetch('http://94.189.193.50:5003/api/solutions/getCurrent')
        .then(res => res.json())
        .then(wordObj => {
            console.log(JSON.stringify(wordObj))
            client.say(channel, `Current solution is: ${wordObj.word}.`);
        });
        break;
      default:
        break;
    }
  });
}

main();
