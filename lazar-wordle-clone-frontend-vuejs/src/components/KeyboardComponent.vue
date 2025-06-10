<template>
    <v-container class="pa-0" fluid>
        <div v-for="(keyboardRow, i) in wordleStore.keyBoardRows" :key="i">
            <v-row class="justify-center" dense>
                <v-col cols="auto" v-for="characterTile in keyboardRow" :key="characterTile.index">
                    <KeyComponent :characterTile="characterTile" />
                </v-col>
            </v-row>
        </div>
    </v-container>
</template>

<script setup>
import KeyComponent from '@/components/KeyComponent.vue';
import { useWordleStore } from '@/stores/wordle.js';
const wordleStore = useWordleStore();
</script>

<style scoped></style>
