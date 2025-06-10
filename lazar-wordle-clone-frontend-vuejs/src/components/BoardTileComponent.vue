<template>
  <v-sheet
    class="d-flex align-center justify-center ma-1"
    :color="props.characterTile.color"
    height="3rem"
    width="3rem"
    elevation="1"
  >
    {{ props.characterTile.character }}
  </v-sheet>
</template>

<script setup>
const props = defineProps({
  characterTile: Object,
});
</script>

<style scoped></style>
