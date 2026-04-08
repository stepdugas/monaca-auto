<!--
  AdminCarsView — Inventory management table.
  Lists all vehicles with Edit / Delete actions and an Add New button.
-->
<template>
  <div>
    <!-- Page header -->
    <div class="flex items-center justify-between mb-6">
      <div>
        <h1 class="text-2xl font-bold text-gray-900">Inventory</h1>
        <p class="text-gray-500 text-sm mt-1">{{ total }} vehicles total</p>
      </div>
      <RouterLink to="/admin/cars/new" class="btn-primary text-sm py-2 px-5">
        + Add Vehicle
      </RouterLink>
    </div>

    <!-- Search -->
    <div class="mb-5">
      <input
        v-model="search"
        class="form-input max-w-xs"
        placeholder="Search make, model, VIN…"
        @input="fetchCars"
      />
    </div>

    <!-- Loading -->
    <div v-if="loading" class="space-y-3">
      <div v-for="n in 6" :key="n" class="h-14 bg-gray-200 rounded-xl animate-pulse"></div>
    </div>

    <!-- Table -->
    <div v-else-if="cars.length" class="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
      <div class="overflow-x-auto">
        <table class="w-full text-sm">
          <thead class="bg-gray-50 text-xs uppercase tracking-wide text-gray-500 border-b border-gray-100">
            <tr>
              <th class="text-left px-5 py-3 w-12"></th>
              <th class="text-left px-5 py-3">Vehicle</th>
              <th class="text-left px-5 py-3">Price</th>
              <th class="text-left px-5 py-3">Mileage</th>
              <th class="text-left px-5 py-3">Status</th>
              <th class="text-left px-5 py-3 hidden lg:table-cell">VIN</th>
              <th class="px-5 py-3"></th>
            </tr>
          </thead>
          <tbody class="divide-y divide-gray-100">
            <tr
              v-for="car in cars"
              :key="car.id"
              class="hover:bg-gray-50 transition-colors group"
            >
              <!-- Thumbnail -->
              <td class="px-4 py-3">
                <div class="w-10 h-10 rounded-lg bg-gray-100 overflow-hidden flex-shrink-0">
                  <img
                    v-if="car.primaryImageUrl"
                    :src="car.primaryImageUrl"
                    :alt="`${car.make} ${car.model}`"
                    class="w-full h-full object-cover"
                  />
                  <div v-else class="w-full h-full flex items-center justify-center">
                    <svg class="w-5 h-5 text-gray-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"
                        d="M9 17a2 2 0 11-4 0 2 2 0 014 0zM19 17a2 2 0 11-4 0 2 2 0 014 0zM13 16V6a1 1 0 00-1-1H4a1 1 0 00-1 1v10l2 1h8zM13 16l2 1h4l2-1v-5l-3.5-5.5H13v5z"/>
                    </svg>
                  </div>
                </div>
              </td>
              <td class="px-5 py-3">
                <p class="font-semibold text-gray-900">{{ car.year }} {{ car.make }} {{ car.model }}</p>
                <p v-if="car.trim" class="text-xs text-gray-400 mt-0.5">{{ car.trim }}</p>
              </td>
              <td class="px-5 py-3 text-gray-900 font-semibold">{{ formatPrice(car.price) }}</td>
              <td class="px-5 py-3 text-gray-500">{{ car.mileage ? Number(car.mileage).toLocaleString() + ' mi' : '—' }}</td>
              <td class="px-5 py-3">
                <span
                  class="text-xs font-semibold px-2.5 py-1 rounded-full capitalize"
                  :class="{
                    'bg-emerald-100 text-emerald-700': car.status === 'available',
                    'bg-amber-100 text-amber-700': car.status === 'pending',
                    'bg-red-100 text-red-700': car.status === 'sold',
                  }"
                >
                  {{ car.status ?? 'available' }}
                </span>
              </td>
              <td class="px-5 py-3 text-gray-400 font-mono text-xs hidden lg:table-cell">{{ car.vin || '—' }}</td>
              <td class="px-5 py-3">
                <div class="flex items-center gap-3 justify-end">
                  <RouterLink
                    :to="`/inventory/${car.id}`"
                    target="_blank"
                    class="text-gray-400 hover:text-gray-600 transition-colors"
                    title="Preview listing"
                  >
                    <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 6H6a2 2 0 00-2 2v10a2 2 0 002 2h10a2 2 0 002-2v-4M14 4h6m0 0v6m0-6L10 14"/>
                    </svg>
                  </RouterLink>
                  <RouterLink
                    :to="`/admin/cars/${car.id}/edit`"
                    class="text-primary-600 hover:text-primary-800 font-medium text-xs"
                  >
                    Edit
                  </RouterLink>
                  <button
                    class="text-red-500 hover:text-red-700 font-medium text-xs"
                    @click="confirmDelete(car)"
                  >
                    Delete
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Empty state -->
    <div v-else class="text-center py-20 text-gray-400">
      <p class="text-lg font-medium">No vehicles yet.</p>
      <RouterLink to="/admin/cars/new" class="btn-primary mt-5 inline-flex text-sm">
        Add Your First Vehicle
      </RouterLink>
    </div>

    <!-- Delete confirmation modal -->
    <Teleport to="body">
      <div
        v-if="deleteTarget"
        class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4"
      >
        <div class="bg-white rounded-2xl p-8 max-w-sm w-full shadow-2xl">
          <h3 class="text-lg font-bold text-gray-900 mb-2">Delete Vehicle?</h3>
          <p class="text-gray-500 text-sm">
            Are you sure you want to delete
            <strong>{{ deleteTarget.year }} {{ deleteTarget.make }} {{ deleteTarget.model }}</strong>?
            This cannot be undone.
          </p>
          <div class="flex gap-3 mt-6">
            <button
              class="flex-1 py-2 rounded-lg border text-sm font-medium hover:bg-gray-50"
              @click="deleteTarget = null"
            >
              Cancel
            </button>
            <button
              class="flex-1 py-2 rounded-lg bg-red-500 text-white text-sm font-medium hover:bg-red-600"
              :disabled="deleting"
              @click="doDelete"
            >
              {{ deleting ? 'Deleting…' : 'Delete' }}
            </button>
          </div>
        </div>
      </div>
    </Teleport>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { RouterLink } from 'vue-router'
import { getInventory, deleteCar } from '../../api'

const cars         = ref([])
const loading      = ref(true)
const total        = ref(0)
const search       = ref('')
const deleteTarget = ref(null)
const deleting     = ref(false)

async function fetchCars() {
  loading.value = true
  try {
    const res = await getInventory({ search: search.value, size: 200 })
    const data = res.data
    cars.value = data.content ?? data
    total.value = data.totalElements ?? data.length
  } finally {
    loading.value = false
  }
}

function confirmDelete(car) { deleteTarget.value = car }

async function doDelete() {
  deleting.value = true
  try {
    await deleteCar(deleteTarget.value.id)
    cars.value = cars.value.filter(c => c.id !== deleteTarget.value.id)
    total.value--
    deleteTarget.value = null
  } finally {
    deleting.value = false
  }
}

function formatPrice(price) {
  if (price == null) return '—'
  return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD', maximumFractionDigits: 0 }).format(price)
}

onMounted(fetchCars)
</script>
