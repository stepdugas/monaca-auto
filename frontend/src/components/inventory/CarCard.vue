<!--
  CarCard — reusable vehicle card used on the Inventory page and
  the Featured Inventory section on the homepage.
-->
<template>
  <RouterLink :to="`/inventory/${car.id}`" class="card group block">

    <!-- Vehicle image -->
    <div class="relative overflow-hidden h-52 bg-gray-100">
      <img
        :src="primaryImage"
        :alt="`${car.year} ${car.make} ${car.model}`"
        class="w-full h-full object-cover object-center
               group-hover:scale-105 transition-transform duration-500"
        loading="lazy"
      />

      <!-- Condition badge -->
      <span
        class="absolute top-3 left-3 text-xs font-semibold px-2.5 py-1 rounded-full uppercase tracking-wide"
        :class="conditionClass"
      >
        {{ car.condition || 'Used' }}
      </span>

      <!-- Status badge (if sold / pending) -->
      <span
        v-if="car.status && car.status !== 'available'"
        class="absolute top-3 right-3 text-xs font-semibold px-2.5 py-1 rounded-full uppercase tracking-wide bg-red-500 text-white"
      >
        {{ car.status }}
      </span>
    </div>

    <!-- Card body -->
    <div class="p-5">
      <!-- Year / Make / Model -->
      <h3 class="font-bold text-gray-900 text-lg leading-snug group-hover:text-primary-600 transition-colors">
        {{ car.year }} {{ car.make }} {{ car.model }}
      </h3>

      <!-- Trim / VIN hint -->
      <p v-if="car.trim" class="text-sm text-gray-500 mt-0.5">{{ car.trim }}</p>

      <!-- Specs row -->
      <div class="flex items-center gap-4 mt-3 text-sm text-gray-500">
        <span v-if="car.mileage" class="flex items-center gap-1.5">
          <!-- Speedometer icon -->
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
              d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"/>
          </svg>
          {{ formatMileage(car.mileage) }} mi
        </span>
        <span v-if="car.transmission" class="flex items-center gap-1.5">
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
              d="M8 7h12m0 0l-4-4m4 4l-4 4m0 6H4m0 0l4 4m-4-4l4-4"/>
          </svg>
          {{ car.transmission }}
        </span>
      </div>

      <!-- Price -->
      <div class="flex items-center justify-between mt-4">
        <p class="text-2xl font-extrabold text-primary-600">
          {{ formatPrice(car.price) }}
        </p>
        <span class="text-xs text-primary-500 font-semibold group-hover:underline">
          View Details →
        </span>
      </div>
    </div>
  </RouterLink>
</template>

<script setup>
import { computed } from 'vue'
import { RouterLink } from 'vue-router'

const props = defineProps({
  car: {
    type: Object,
    required: true,
  },
})

// Use the first image tagged is_primary, fall back to any image, then Unsplash placeholder
const primaryImage = computed(() => {
  const images = props.car.images ?? []
  const primary = images.find(img => img.isPrimary) ?? images[0]
  return primary?.imageUrl
    ?? 'https://images.unsplash.com/photo-1583121274602-3e2820c69888?auto=format&fit=crop&w=800&q=70'
})

const conditionClass = computed(() => ({
  'New':       'bg-emerald-500 text-white',
  'Used':      'bg-gray-700 text-white',
  'Certified': 'bg-primary-500 text-white',
}[props.car.condition] ?? 'bg-gray-700 text-white'))

function formatPrice(price) {
  if (price == null) return 'Call for Price'
  return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD', maximumFractionDigits: 0 }).format(price)
}

function formatMileage(miles) {
  return new Intl.NumberFormat('en-US').format(miles)
}
</script>
