<template>
  <v-btn @click="handleClick" class="ma-1" :color="props.characterTile.color" small>{{
    props.characterTile.character
  }}</v-btn>
</template>

<script setup>
import { useWordleStore } from '@/stores/wordle.js';
const wordleStore = useWordleStore();

const props = defineProps({
  characterTile: Object,
});
function handleClick() {
  const char = props.characterTile.character;
  if (char === 'DEL') {
    wordleStore.deleteLetter();
  }
  else if (char === 'ENTER'){
    wordleStore.submitWord();
  }
  else{
    wordleStore.addLetter(char);
  }
}
</script>

<style scoped></style>
