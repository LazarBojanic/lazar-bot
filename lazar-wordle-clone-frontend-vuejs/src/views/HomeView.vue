<template>
    <main>
        <v-col>
            <BoardComponent />
            <KeyboardComponent />
        </v-col>
        <v-text-field label="Username" hint="Enter Username" v-model="username"></v-text-field>
        <v-btn @click="handleNewGame">New Game</v-btn>
    </main>
</template>

<script setup>
import KeyboardComponent from '@/components/KeyboardComponent.vue';
import BoardComponent from '@/components/BoardComponent.vue';
import { onMounted, ref } from 'vue';
import { useWordleStore } from '@/stores/wordle.js';
const wordleStore = useWordleStore();

onMounted(() => {
    //wordleStore.getCurrentBoard()
    wordleStore.newGame()
})

function handleNewGame(){
    console.log('New Game');
}

</script>

<style scoped></style>
