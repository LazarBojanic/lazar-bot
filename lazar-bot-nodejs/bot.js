const tmi = require('tmi.js');
const fs = require('fs');
const opener = require('opener');
const readline = require('readline');

const TOKEN_FILE = 'token.json';
const CREDENTIALS_FILE = 'credentials.json';

async function loadToken(filePath){
    try{
        const tokenJson = await fs.promises.readFile(filePath, 'utf8');
        return JSON.parse(tokenJson).token;
    }
    catch(error){
        console.error('Token file not found, proceeding with authorization flow.');
        throw error;
    }
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
function registerMethods(client, botChannel){
  client.on('message', (channel, tags, message, self) => {
    const chatterUsername = tags.username;
    const chatMessage = message.toString().trim();
    if (self) return;
    if(chatMessage == '!newGame'){
      fetch(`http://94.189.193.50:5003/api/solutions/new?username=${chatterUsername}`)
      .then(res => res.json())
      .then(res => {
          client.say(channel, `Starting new game: ${res.status}`);
      });
    }
    else if(chatMessage.includes('!guess')){
      const guessWord = chatMessage.split(' ')[1].toUpperCase();
      const userTryRequest = {
        username: chatterUsername,
        word: guessWord
      }
      fetch(`http://94.189.193.50:5003/api/solutions/check`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(userTryRequest)
      })
      .then(res => res.json())
      .then(res => {
        console.log('res: ' + JSON.stringify(res));
        if(res.reason == 'game_ended'){
          client.say(channel, 'Game ended. You can start a new one with !newGame');
        }
        else if(res.reason == 'solution_not_valid'){
          client.say(channel, `Guess: ${guessWord}, not valid!`);
        }
        else{
          let letterStatusesEmojis = '';
          for(let i = 0; i < res.validated_word.letters.length; i++){
            switch(res.validated_word.letters[i].status){
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
          client.say(channel, `${guessWord}: ${letterStatusesEmojis}`);

          fetch(`http://94.189.193.50:5003/api/solutions/checkGameStatus?username=${chatterUsername}`, {
            method: 'GET',
            headers: {
              'Content-Type': 'application/json'
            }
          })
          .then(res => res.json())
          .then(userSessionObj => {
            if(userSessionObj.status == 'unsolved'){
              client.say(channel, `Remaining tries: ${userSessionObj.remaining_tries}`);
            }
            else if(userSessionObj.status == 'solved'){
              client.say(channel, `You win! The word was: ${userSessionObj.word}`);
            }
            else if(userSessionObj.status == 'game_over'){
              client.say(channel, `Game over, the word was: ${userSessionObj.word}`);
            }
          })
        }
      })
    }
  });
}
async function main() {
    let credentials, token, client;
      try{
          credentials = await loadCredentials(CREDENTIALS_FILE);
          token = await loadToken(TOKEN_FILE);
          console.log('credentials: ' + JSON.stringify(credentials));
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
          token = await new Promise((resolve) => {
              consoleInterface.question('Paste the token from the URL: ', (newToken) => {
                consoleInterface.close();
                resolve(newToken);
              });
            });
  
          await fs.promises.writeFile(TOKEN_FILE, JSON.stringify({ token }), 'utf8');
      }
      finally{
          console.log('Using cached token.');
          await client.connect();
          registerMethods(client, credentials.username);
      }
  }

main();
