import { ref, computed } from 'vue';
import { defineStore } from 'pinia';

const SERVER_IP = import.meta.env.VITE_SERVER_IP;

const Color = {
    WHITE: 'white',
    RED: 'red',
    YELLOW: 'yellow',
    GREEN: 'green',
};
class CharacterTile {
    constructor(index, character, color) {
        this.index = index;
        this.character = character;
        this.color = color;
    }
}
class Word {
    constructor(characterTile0, characterTile1, characterTile2, characterTile3, characterTile4) {
        this.characterTiles = [
            characterTile0,
            characterTile1,
            characterTile2,
            characterTile3,
            characterTile4
        ];
    }
    static emptyWord() {
        return new Word(
            new CharacterTile(0, '', Color.WHITE),
            new CharacterTile(1, '', Color.WHITE),
            new CharacterTile(2, '', Color.WHITE),
            new CharacterTile(3, '', Color.WHITE),
            new CharacterTile(4, '', Color.WHITE),
        );
    }
}

export const useWordleStore = defineStore('wordle', () => {
    const board = ref([
        Word.emptyWord(),
        Word.emptyWord(),
        Word.emptyWord(),
        Word.emptyWord(),
        Word.emptyWord(),
        Word.emptyWord(),
    ]);
    const username = ref('dumbe01');
    const currentRow = ref(0);
    const currentCol = ref(0);
    const currentWord = ref(Word.emptyWord());
    const userTryResponse = ref({});
    let currentBoard = ref({});

    const keyboard = ref([
        new CharacterTile(0, 'A', Color.WHITE),
        new CharacterTile(1, 'B', Color.WHITE),
        new CharacterTile(2, 'C', Color.WHITE),
        new CharacterTile(3, 'D', Color.WHITE),
        new CharacterTile(4, 'E', Color.WHITE),
        new CharacterTile(5, 'F', Color.WHITE),
        new CharacterTile(6, 'G', Color.WHITE),
        new CharacterTile(7, 'H', Color.WHITE),
        new CharacterTile(8, 'I', Color.WHITE),
        new CharacterTile(9, 'J', Color.WHITE),
        new CharacterTile(10, 'K', Color.WHITE),
        new CharacterTile(11, 'L', Color.WHITE),
        new CharacterTile(12, 'M', Color.WHITE),
        new CharacterTile(13, 'N', Color.WHITE),
        new CharacterTile(14, 'O', Color.WHITE),
        new CharacterTile(15, 'P', Color.WHITE),
        new CharacterTile(16, 'Q', Color.WHITE),
        new CharacterTile(17, 'R', Color.WHITE),
        new CharacterTile(18, 'S', Color.WHITE),
        new CharacterTile(19, 'DEL', Color.WHITE),
        new CharacterTile(20, 'T', Color.WHITE),
        new CharacterTile(21, 'U', Color.WHITE),
        new CharacterTile(22, 'V', Color.WHITE),
        new CharacterTile(23, 'W', Color.WHITE),
        new CharacterTile(24, 'X', Color.WHITE),
        new CharacterTile(25, 'Y', Color.WHITE),
        new CharacterTile(26, 'Z', Color.WHITE),
        new CharacterTile(27, 'ENTER', Color.WHITE),
    ]);
    const keyBoardRows = computed(() => {
        return [
            keyboard.value.slice(0, 10),
            keyboard.value.slice(10, 19),
            keyboard.value.slice(19, 28),
        ];
    });

    function addLetter(char) {
        if (currentCol.value < 5) {
            currentWord.value.characterTiles[currentCol.value].character = char;
            board.value[currentRow.value].characterTiles[currentCol.value].character = char;
            currentCol.value++;
        }
    }
    function deleteLetter() {
        if (currentCol.value > 0) {
            currentCol.value--;
            currentWord.value.characterTiles[currentCol.value].character = '';
            board.value[currentRow.value].characterTiles[currentCol.value].character = '';
        }
    }
    async function submitWord() {
        if (currentCol.value >= 5) {
            const word = currentWord.value.characterTiles.map((t) => t.character).join('');
            console.log('Submitting:', word);
            const body = {
                username: username.value,
                word: word,
            }
            const res = await fetch(`${SERVER_IP}game/guess`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(body)
            });
            const data = await res.json();
            userTryResponse.value = data;
            if(userTryResponse.value.solutionIsValid){
                currentRow.value++;
                currentCol.value = 0;
                currentWord.value = Word.emptyWord();
            }
            else{
                console.log('Invalid word');
            }
        }
    }
    function updateBoard() {

    }
    async function getCurrentBoard() {
        console.log('Getting current board.');
        const res = await fetch(`${SERVER_IP}game/get-board?username=${username.value}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        const data = await res.json();
        currentBoard.value = data.userTryList;
        console.log(currentBoard);
    }
    async function newGame() {
        console.log('Starting new game.');
        const res = await fetch(`${SERVER_IP}game/new-game?username=${username.value}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        const data = await res.json();
        console.log(data);
    }

    return {
        board,
        currentRow,
        currentCol,
        currentWord,
        keyboard,
        keyBoardRows,
        username,
        userTryResponse,
        getCurrentBoard,
        updateBoard,
        addLetter,
        deleteLetter,
        submitWord,
        newGame
    };
});
