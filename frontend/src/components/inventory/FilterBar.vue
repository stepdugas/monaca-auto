<!--
  FilterBar — collapsible filter panel for the Inventory page.
  Emits `update:filters` whenever any filter changes.
-->
<template>
  <div class="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">

    <!-- Header / toggle -->
    <button
      class="w-full flex items-center justify-between px-6 py-4 font-semibold text-gray-800
             hover:bg-gray-50 transition-colors md:cursor-default"
      @click="open = !open"
    >
      <span class="flex items-center gap-2">
        <!-- Filter icon -->
        <svg class="w-5 h-5 text-primary-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
            d="M3 4a1 1 0 011-1h16a1 1 0 011 1v2.586a1 1 0 01-.293.707l-6.414 6.414a1 1 0 00-.293.707V17l-4 4v-6.586a1 1 0 00-.293-.707L3.293 7.293A1 1 0 013 6.586V4z"/>
        </svg>
        Filter Vehicles
        <span v-if="activeFilterCount" class="text-xs bg-primary-500 text-white font-bold px-2 py-0.5 rounded-full">
          {{ activeFilterCount }}
        </span>
      </span>
      <svg
        class="w-5 h-5 text-gray-400 transition-transform md:hidden"
        :class="open ? 'rotate-180' : ''"
        fill="none" stroke="currentColor" viewBox="0 0 24 24"
      >
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"/>
      </svg>
    </button>

    <!-- Search bar -->
    <div class="px-6 py-3 border-b border-gray-100 bg-gray-50">
      <input
        v-model="local.search"
        type="text"
        class="w-full px-4 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-primary-500"
        placeholder="Search by make, model, VIN..."
        @input="emit"
      />
    </div>

    <!-- Filters grid -->
    <div
      class="transition-all duration-300 overflow-hidden"
      :class="open || isDesktop ? 'max-h-screen' : 'max-h-0'"
    >
      <div class="px-6 pb-6 pt-2 grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-5 gap-4">

        <!-- Make -->
        <div>
          <label class="form-label">Make</label>
          <input
            v-model="local.make"
            class="form-input"
            placeholder="e.g. Toyota"
            @input="emit"
          />
        </div>

        <!-- Model -->
        <div>
          <label class="form-label">Model</label>
          <input
            v-model="local.model"
            class="form-input"
            placeholder="e.g. Camry"
            @input="emit"
          />
        </div>

        <!-- Year -->
        <div>
          <label class="form-label">Year</label>
          <input
            v-model.number="local.year"
            type="number"
            class="form-input"
            :placeholder="currentYear"
            min="1980"
            :max="currentYear + 1"
            @input="emit"
          />
        </div>

        <!-- Min Price -->
        <div>
          <label class="form-label">Min Price</label>
          <input
            v-model.number="local.minPrice"
            type="number"
            class="form-input"
            placeholder="e.g. 5000"
            min="0"
            step="1000"
            @input="emit"
          />
        </div>

        <!-- Max Price -->
        <div>
          <label class="form-label">Max Price</label>
          <input
            v-model.number="local.maxPrice"
            type="number"
            class="form-input"
            placeholder="e.g. 50000"
            min="0"
            step="1000"
            @input="emit"
          />
        </div>

        <!-- Max Mileage -->
        <div>
          <label class="form-label">Max Mileage</label>
          <select v-model.number="local.maxMileage" class="form-input" @change="emit">
            <option :value="null">Any</option>
            <option v-for="m in mileageOptions" :key="m.value" :value="m.value">{{ m.label }}</option>
          </select>
        </div>

        <!-- Condition -->
        <div>
          <label class="form-label">Condition</label>
          <select v-model="local.condition" class="form-input" @change="emit">
            <option value="">Any</option>
            <option value="New">New</option>
            <option value="Used">Used</option>
            <option value="Certified">Certified Pre-Owned</option>
          </select>
        </div>
      </div>

      <!-- Clear filters -->
      <div v-if="activeFilterCount" class="px-6 pb-5 flex justify-end">
        <button
          class="text-sm text-red-500 hover:text-red-700 font-medium transition-colors"
          @click="clearFilters"
        >
          Clear all filters
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'

const props = defineProps({
  modelValue: { type: Object, default: () => ({}) },
})
const emits = defineEmits(['update:modelValue'])

const open = ref(false)
const isDesktop = ref(window.innerWidth >= 768)

function onResize() { isDesktop.value = window.innerWidth >= 768 }
onMounted(() => window.addEventListener('resize', onResize))
onUnmounted(() => window.removeEventListener('resize', onResize))

const currentYear = new Date().getFullYear()

const local = ref({
  search: props.modelValue.search ?? '',
  make: props.modelValue.make ?? '',
  model: props.modelValue.model ?? '',
  year: props.modelValue.year ?? null,
  minPrice: props.modelValue.minPrice ?? null,
  maxPrice: props.modelValue.maxPrice ?? null,
  maxMileage: props.modelValue.maxMileage ?? null,
  condition: props.modelValue.condition ?? '',
})

const activeFilterCount = computed(() =>
  Object.values(local.value).filter(v => v !== '' && v !== null).length
)

function emit() {
  // Strip empty/null values before emitting
  const clean = Object.fromEntries(
    Object.entries(local.value).filter(([, v]) => v !== '' && v !== null)
  )
  emits('update:modelValue', clean)
}

function clearFilters() {
  local.value = { make: '', model: '', year: null, maxPrice: null, maxMileage: null, condition: '' }
  emits('update:modelValue', {})
}

const priceOptions = [
  { label: 'Under $10,000',  value: 10000 },
  { label: 'Under $20,000',  value: 20000 },
  { label: 'Under $30,000',  value: 30000 },
  { label: 'Under $40,000',  value: 40000 },
  { label: 'Under $50,000',  value: 50000 },
  { label: 'Under $75,000',  value: 75000 },
  { label: 'Under $100,000', value: 100000 },
]

const mileageOptions = [
  { label: 'Under 10,000',  value: 10000 },
  { label: 'Under 25,000',  value: 25000 },
  { label: 'Under 50,000',  value: 50000 },
  { label: 'Under 75,000',  value: 75000 },
  { label: 'Under 100,000', value: 100000 },
  { label: 'Under 150,000', value: 150000 },
]
</script>
