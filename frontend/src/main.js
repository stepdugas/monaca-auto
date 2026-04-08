import { createApp } from 'vue'
import './assets/main.css'
import App from './App.vue'
import router from './router'

// AOS — Animate On Scroll library
import AOS from 'aos'
import 'aos/dist/aos.css'

AOS.init({
  duration: 700,    // animation duration in ms
  easing: 'ease-out-cubic',
  once: true,       // animate only on first scroll into view
  offset: 60,       // trigger offset (px from element bottom)
})

createApp(App).use(router).mount('#app')
