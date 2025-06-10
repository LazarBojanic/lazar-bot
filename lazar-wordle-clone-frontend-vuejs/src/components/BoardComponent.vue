<template>
  <v-container class="pa-0" fluid>
    <div v-for="(word, i) in wordleStore.board" :key="i">
      <v-row class="justify-center" dense>
        <v-col cols="auto" v-for="characterTile in word.characterTiles" :key="characterTile.index">
          <BoardTileComponent :characterTile="characterTile" />
        </v-col>
      </v-row>
    </div>
  </v-container>
</template>

<script setup>
import { useWordleStore } from '@/stores/wordle.js';
import BoardTileComponent from '@/components/BoardTileComponent.vue';
const wordleStore = useWordleStore();
</script>

<style scoped></style>
