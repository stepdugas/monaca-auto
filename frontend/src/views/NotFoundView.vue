<template>
  <PageLayout>
    <div class="flex-1 flex items-center justify-center min-h-[70vh] px-4">
      <div class="text-center">
        <p class="text-8xl font-extrabold text-primary-500">404</p>
        <h1 class="text-3xl font-bold text-gray-900 mt-4">Page Not Found</h1>
        <p class="text-gray-500 mt-3 max-w-sm mx-auto">
          The page you're looking for doesn't exist or has been moved.
        </p>
        <RouterLink to="/" class="btn-primary mt-8 inline-flex">Back to Home</RouterLink>
      </div>
    </div>
  </PageLayout>
</template>

<script setup>
import { RouterLink } from 'vue-router'
import PageLayout from '../components/layout/PageLayout.vue'
</script>
