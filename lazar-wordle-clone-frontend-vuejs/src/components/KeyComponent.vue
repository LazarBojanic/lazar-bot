<template>
  <v-btn @click="test" class="ma-1" :color="props.characterTile.color" small>{{ props.characterTile.character }}</v-btn>
</template>

<script setup>
  const props = defineProps({
    characterTile: Object
  })
  function test(){
    console.log('Pressed letter: ' + props.characterTile.character);
  }
</script>

<style scoped>

</style>
