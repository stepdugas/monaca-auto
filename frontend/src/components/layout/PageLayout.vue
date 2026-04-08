<!--
  PageLayout — wraps every public-facing page with TheHeader + TheFooter.
  All public views should use this as their root element.
-->
<template>
  <div class="min-h-screen flex flex-col">
    <TheHeader />
    <main class="flex-1">
      <slot />
    </main>
    <TheFooter />
  </div>
</template>

<script setup>
import TheHeader from './TheHeader.vue'
import TheFooter from './TheFooter.vue'
</script>
