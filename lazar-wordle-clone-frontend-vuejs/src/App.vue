<template>
  <v-app>
    <v-app-bar app color="primary" dark elevate-on-scroll>
      <v-toolbar-title class="text-h6 font-weight-bold">Wordle</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn
        v-for="item in navItems"
        :key="item.title"
        :to="item.route"
        variant="text"
        class="text-white"
        router
      >
        <v-icon start>{{ item.icon }}</v-icon>
        {{ item.title }}
      </v-btn>
      <v-btn @click="toggleTheme" variant="text" class="text-white">
        <v-icon start>mdi-theme-light-dark</v-icon>
        Toggle Theme
      </v-btn>
    </v-app-bar>

    <v-main>
      <v-container fluid>
        <RouterView />
      </v-container>
    </v-main>
  </v-app>
</template>

<script setup>
import { RouterView } from 'vue-router';
import { useTheme } from 'vuetify';

const theme = useTheme();

function toggleTheme() {
  theme.global.name.value =
    theme.global.name.value === 'mainLightTheme' ? 'mainDarkTheme' : 'mainLightTheme';
}

const navItems = [
  { title: 'Home', route: '/', icon: 'mdi-home' },
  { title: 'About', route: '/about', icon: 'mdi-information' },
];
</script>

<style scoped></style>
