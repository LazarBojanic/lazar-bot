import { ref, computed } from 'vue';
import { defineStore } from 'pinia';

const Color = {
  WHITE: 'white',
  RED: 'red',
  YELLOW: 'yellow',
  GREEN: 'green'
};
class CharacterTile{
  constructor(index, character, color){
    this.index = index;
    this.character = character;
    this.color = color;
  }
}
class Word{
  constructor(characterTile0, characterTile1, characterTile2, characterTile3, characterTile4){
    this.word = [
      characterTile0,
      characterTile1,
      characterTile2,
      characterTile3,
      characterTile4
    ]
  }
  static emptyWord(){
    return new Word(
      new CharacterTile(0, 'none', Color.WHITE),
      new CharacterTile(1, 'none', Color.WHITE),
      new CharacterTile(2, 'none', Color.WHITE),
      new CharacterTile(3, 'none', Color.WHITE),
      new CharacterTile(4, 'none', Color.WHITE))
  }
}

export const useWordStore = defineStore('word', () => {
  const boardWordList = ref([]);
  const currentWord = ref(Word.emptyWord());
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
    new CharacterTile(27, 'ENTER', Color.WHITE)
  ]);
  const rows = computed(() => {
    return [
      keyboard.value.slice(0, 10),
      keyboard.value.slice(10, 19),
      keyboard.value.slice(19, 28),
    ]
  })
  return { boardWordList, currentWord, keyboard, rows };
});
