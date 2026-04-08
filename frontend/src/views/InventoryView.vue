<!--
  InventoryView — full inventory page with filter bar and paginated car grid.
-->
<template>
  <PageLayout>
    <!-- Page header banner -->
    <div class="bg-dark-800 pt-28 pb-12">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h1 class="text-3xl md:text-4xl font-extrabold text-white">Browse Inventory</h1>
        <p class="text-gray-400 mt-2">
          <span v-if="total !== null">
            <span class="font-semibold text-white">{{ total }}</span> vehicle{{ total !== 1 ? 's' : '' }} found
            <span v-if="hasActiveFilters" class="text-gray-300">(filtered)</span>
          </span>
          <span v-else>Loading…</span>
        </p>
      </div>
    </div>

    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10">

      <!-- Filter bar -->
      <FilterBar v-model="filters" class="mb-8" />

      <!-- Loading skeletons -->
      <div v-if="loading" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-8">
        <div v-for="n in 9" :key="n" class="card animate-pulse">
          <div class="h-52 bg-gray-200"></div>
          <div class="p-5 space-y-3">
            <div class="h-4 bg-gray-200 rounded w-3/4"></div>
            <div class="h-3 bg-gray-200 rounded w-1/2"></div>
            <div class="h-8 bg-gray-200 rounded w-full mt-4"></div>
          </div>
        </div>
      </div>

      <!-- Error state -->
      <div v-else-if="error" class="text-center py-24 text-gray-500">
        <p class="text-lg">Failed to load inventory. Please try again later.</p>
        <button class="btn-primary mt-6" @click="fetchInventory">Retry</button>
      </div>

      <!-- Empty state -->
      <div v-else-if="cars.length === 0" class="text-center py-24 text-gray-500">
        <p class="text-lg font-medium">No vehicles matched your filters.</p>
        <p class="mt-2 text-sm">Try adjusting or clearing the filters above.</p>
      </div>

      <!-- Car grid -->
      <div v-else class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-8">
        <div v-for="(car, i) in cars" :key="car.id" class="relative group" data-aos="fade-up" :data-aos-delay="(i % 3) * 80">
          <CarCard :car="car" />
          <!-- Edit button for admins -->
          <button
            v-if="isAdmin"
            @click="openEditModal(car)"
            class="absolute top-3 right-3 p-2 bg-primary-600 hover:bg-primary-700 text-white rounded-lg opacity-0 group-hover:opacity-100 transition-all transform group-hover:scale-100 scale-90 origin-top-right shadow-lg"
            title="Edit vehicle"
          >
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
            </svg>
          </button>
        </div>
      </div>

      <!-- Edit Modal -->
      <EditCarModal
        :open="editModalOpen"
        :car="selectedCar"
        @close="editModalOpen = false"
        @save="onCarUpdated"
      />

      <!-- Pagination -->
      <div v-if="totalPages > 1" class="flex items-center justify-center gap-2 mt-12">
        <button
          class="px-4 py-2 rounded-lg border font-medium text-sm transition-colors"
          :class="page === 0 ? 'opacity-40 cursor-not-allowed' : 'hover:bg-gray-100'"
          :disabled="page === 0"
          @click="page--"
        >
          ← Prev
        </button>

        <span class="text-sm text-gray-500 px-4">
          Page {{ page + 1 }} of {{ totalPages }}
        </span>

        <button
          class="px-4 py-2 rounded-lg border font-medium text-sm transition-colors"
          :class="page >= totalPages - 1 ? 'opacity-40 cursor-not-allowed' : 'hover:bg-gray-100'"
          :disabled="page >= totalPages - 1"
          @click="page++"
        >
          Next →
        </button>
      </div>
    </div>
  </PageLayout>
</template>

<script setup>
import { ref, watch, computed, onMounted } from 'vue'
import PageLayout from '../components/layout/PageLayout.vue'
import FilterBar  from '../components/inventory/FilterBar.vue'
import CarCard    from '../components/inventory/CarCard.vue'
import EditCarModal from '../components/EditCarModal.vue'
import { getInventory } from '../api'

const cars       = ref([])
const loading    = ref(true)
const error      = ref(false)
const total      = ref(null)
const page       = ref(0)
const totalPages = ref(1)
const PAGE_SIZE  = 12

const filters = ref({})
const isAdmin = ref(false)
const editModalOpen = ref(false)
const selectedCar = ref(null)

const hasActiveFilters = computed(() => Object.keys(filters.value).length > 0)

async function fetchInventory() {
  loading.value = true
  error.value   = false
  try {
    const res = await getInventory({ ...filters.value, page: page.value, size: PAGE_SIZE })
    const data = res.data

    // Handle both Spring Page<> responses and plain arrays
    if (data.content !== undefined) {
      cars.value       = data.content
      total.value      = data.totalElements
      totalPages.value = data.totalPages
    } else {
      cars.value       = data
      total.value      = data.length
      totalPages.value = 1
    }
  } catch {
    error.value = true
  } finally {
    loading.value = false
  }
}

// Re-fetch when filters or page changes, reset to page 0 on filter change
watch(filters, () => { page.value = 0; fetchInventory() }, { deep: true })
watch(page, fetchInventory)

function openEditModal(car) {
  selectedCar.value = car
  editModalOpen.value = true
}

function onCarUpdated(updatedCar) {
  // Update the car in the list
  const idx = cars.value.findIndex(c => c.id === updatedCar.id)
  if (idx !== -1) {
    cars.value[idx] = updatedCar
  }
  selectedCar.value = null
}

onMounted(() => {
  isAdmin.value = !!localStorage.getItem('admin_token')
  fetchInventory()
})
</script>
