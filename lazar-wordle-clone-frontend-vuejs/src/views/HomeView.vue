<template>
    <main>
        <v-col>
            <BoardComponent />
            <KeyboardComponent />
        </v-col>
        <v-col @submitted.once.prevent>
            <p>{{wordleStore.userTryResponse}}</p>
        </v-col>
    </main>
</template>

<script setup>
import KeyboardComponent from '@/components/KeyboardComponent.vue';
import BoardComponent from '@/components/BoardComponent.vue';
import { onMounted } from 'vue';
import { useWordleStore } from '@/stores/wordle.js';
const wordleStore = useWordleStore();

onMounted(() => {
    wordleStore.newGame()
})
function updateUI(){
    console.log('updatedUI');
}

</script>

<style scoped></style>
