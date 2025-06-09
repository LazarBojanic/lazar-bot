<template>
  <v-container class="pa-0" fluid>
    <div v-for="(row, i) in wordStore.rows" :key="i">
      <v-row class="justify-center" dense>
        <v-col cols="auto" v-for="characterTile in row" :key="characterTile.index">
          <KeyComponent :characterTile="characterTile"/>
        </v-col>
      </v-row>
    </div>
  </v-container>

</template>

<script setup>
import KeyComponent from '@/components/KeyComponent.vue';
import { useWordStore } from '@/stores/word.js';
const wordStore = useWordStore();
</script>

<style scoped>

</style>
