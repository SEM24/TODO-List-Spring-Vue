import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import {createVuetify} from "vuetify/framework";
import {components, directives} from "vuetify/dist/vuetify.js";

const app = createApp(App)

const vuetify = createVuetify({
  components,
  directives,
  theme: {
    defaultTheme: 'light'
  }
})
app.use(createPinia())
app.use(router)
app.use(vuetify)
app.mount('#app')
