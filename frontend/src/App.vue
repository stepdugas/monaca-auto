<template>
  <!-- RouterView renders whichever page component matches the current route -->
  <RouterView />
</template>

<script setup>
import { RouterView } from 'vue-router'
import { onMounted } from 'vue'
import { fetchSiteSettings, siteSettings } from './composables/useSiteSettings'

onMounted(async () => {
  await fetchSiteSettings()
  document.title = siteSettings.businessName
})
</script>
