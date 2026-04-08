<!--
  FeaturedInventory — shows a 3-column grid of the newest/featured vehicles
  on the homepage. Fetches from the API on mount.
-->
<template>
  <section class="py-20 bg-gray-50">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">

      <!-- Section header -->
      <div class="text-center mb-14" data-aos="fade-up">
        <h2 class="section-title">Featured Vehicles</h2>
        <p class="section-subtitle mx-auto">
          Hand-picked from our latest inventory. New arrivals added weekly.
        </p>
      </div>

      <!-- Loading skeleton -->
      <div v-if="loading" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-8">
        <div
          v-for="n in 3"
          :key="n"
          class="card animate-pulse"
        >
          <div class="h-52 bg-gray-200"></div>
          <div class="p-5 space-y-3">
            <div class="h-4 bg-gray-200 rounded w-3/4"></div>
            <div class="h-3 bg-gray-200 rounded w-1/2"></div>
            <div class="h-8 bg-gray-200 rounded w-full mt-4"></div>
          </div>
        </div>
      </div>

      <!-- Error state -->
      <div v-else-if="error" class="text-center py-16 text-gray-500">
        <p>Unable to load inventory right now. Please try again later.</p>
      </div>

      <!-- Car grid -->
      <div v-else class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-8">
        <CarCard
          v-for="(car, i) in cars"
          :key="car.id"
          :car="car"
          :data-aos="'fade-up'"
          :data-aos-delay="i * 100"
        />
      </div>

      <!-- CTA -->
      <div class="text-center mt-12" data-aos="fade-up">
        <RouterLink to="/inventory" class="btn-primary text-base px-10 py-4">
          View Full Inventory →
        </RouterLink>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { RouterLink } from 'vue-router'
import { getInventory } from '../../api'
import CarCard from '../inventory/CarCard.vue'

const cars = ref([])
const loading = ref(true)
const error = ref(false)

onMounted(async () => {
  try {
    const res = await getInventory({ size: 6, sort: 'createdAt,desc' })
    cars.value = res.data.content ?? res.data
  } catch {
    error.value = true
  } finally {
    loading.value = false
  }
})
</script>
