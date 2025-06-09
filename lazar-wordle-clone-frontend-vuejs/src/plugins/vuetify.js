import { createVuetify } from 'vuetify';

import 'vuetify/styles';
import { aliases, mdi } from 'vuetify/iconsets/mdi';
import * as components from 'vuetify/components';
import * as directives from 'vuetify/directives';

const mainLightTheme = {
  dark: false,
  colors: {
    primary: '#0fcdd2',
    secondary: '#ffd5ea',
    accent: '#82B1FF',
    error: '#FF5252',
    info: '#2196F3',
    success: '#4CAF50',
    warning: '#FFC107',
  },
};

const mainDarkTheme = {
  dark: true,
  colors: {
    primary: '#b91381',
    secondary: '#390457',
    accent: '#82B1FF',
    error: '#FF5252',
    info: '#2196F3',
    success: '#4CAF50',
    warning: '#FFC107',
  },
};

export default createVuetify({
  icons: {
    defaultSet: 'mdi',
    aliases,
    sets: {
      mdi,
    },
  },
  components,
  directives,
  theme: {
    defaultTheme: 'mainDarkTheme',
    themes: {
      mainLightTheme,
      mainDarkTheme,
    },
  },
});
