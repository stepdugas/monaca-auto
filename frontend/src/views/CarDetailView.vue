<!--
  CarDetailView — full vehicle detail page.
  Shows: photo gallery, specs table, features, VIN, and contact CTA.
-->
<template>
  <PageLayout>
    <div class="pt-20 bg-dark-800 min-h-[30vh]">
      <!-- breadcrumb -->
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 pt-8 pb-4">
        <nav class="text-sm text-gray-400 flex items-center gap-2">
          <RouterLink to="/" class="hover:text-white transition-colors">Home</RouterLink>
          <span>/</span>
          <RouterLink to="/inventory" class="hover:text-white transition-colors">Inventory</RouterLink>
          <span>/</span>
          <span class="text-gray-200">{{ car ? `${car.year} ${car.make} ${car.model}` : '…' }}</span>
        </nav>
      </div>
    </div>

    <!-- Loading state -->
    <div v-if="loading" class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-14 animate-pulse">
      <div class="h-72 bg-gray-200 rounded-2xl mb-8"></div>
      <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <div class="lg:col-span-2 space-y-4">
          <div class="h-6 bg-gray-200 rounded w-1/2"></div>
          <div class="h-4 bg-gray-200 rounded w-1/3"></div>
          <div class="h-32 bg-gray-200 rounded mt-6"></div>
        </div>
        <div class="h-64 bg-gray-200 rounded-2xl"></div>
      </div>
    </div>

    <!-- Error / not found -->
    <div v-else-if="error" class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-24 text-center text-gray-500">
      <p class="text-xl font-medium">Vehicle not found.</p>
      <RouterLink to="/inventory" class="btn-primary mt-6 inline-flex">← Back to Inventory</RouterLink>
    </div>

    <!-- Main content -->
    <div v-else-if="car" class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10">
      <div class="grid grid-cols-1 lg:grid-cols-3 gap-10">

        <!-- Left: gallery + details -->
        <div class="lg:col-span-2 space-y-8">

          <!-- Photo gallery -->
          <div data-aos="fade-up">
            <!-- Main image -->
            <div class="rounded-2xl overflow-hidden bg-gray-100 h-64 sm:h-96">
              <img
                :src="activeImage"
                :alt="`${car.year} ${car.make} ${car.model}`"
                class="w-full h-full object-cover object-center"
              />
            </div>

            <!-- Thumbnails -->
            <div v-if="images.length > 1" class="flex gap-3 mt-3 overflow-x-auto pb-1">
              <button
                v-for="(img, i) in images"
                :key="i"
                class="flex-shrink-0 w-20 h-16 rounded-lg overflow-hidden ring-2 transition-all"
                :class="activeIdx === i ? 'ring-primary-500' : 'ring-transparent opacity-60 hover:opacity-100'"
                @click="activeIdx = i"
              >
                <img :src="img.imageUrl" class="w-full h-full object-cover" loading="lazy" />
              </button>
            </div>
          </div>

          <!-- Title + price -->
          <div data-aos="fade-up">
            <div class="flex flex-wrap items-start justify-between gap-4">
              <div>
                <h1 class="text-3xl font-extrabold text-gray-900">
                  {{ car.year }} {{ car.make }} {{ car.model }}
                </h1>
                <p v-if="car.trim" class="text-gray-500 mt-1">{{ car.trim }}</p>
              </div>
              <div class="text-right">
                <p class="text-3xl font-extrabold text-primary-600">{{ formatPrice(car.price) }}</p>
                <p v-if="car.status" class="text-sm text-gray-400 mt-1 capitalize">{{ car.status }}</p>
              </div>
            </div>
          </div>

          <!-- Estimated Value Display & Comparison -->
          <div v-if="car.kbbValue" class="bg-gradient-to-r from-emerald-50 to-teal-50 border border-emerald-200 rounded-2xl p-6" data-aos="fade-up">
            <p class="text-sm font-medium text-emerald-700 uppercase tracking-wide mb-1">Estimated Value</p>
            <p class="text-2xl font-extrabold text-emerald-600">{{ formatPrice(car.kbbValue) }}</p>
            <div v-if="priceComparison" class="mt-3 flex items-center gap-2">
              <span class="text-sm font-medium" :class="priceComparison.isBelowKBB ? 'text-emerald-600' : 'text-amber-600'">
                {{ priceComparison.label }}
              </span>
              <span class="text-sm font-bold" :class="priceComparison.isBelowKBB ? 'text-emerald-600' : 'text-amber-600'">
                {{ formatPrice(Math.abs(priceComparison.difference)) }}
              </span>
            </div>
          </div>

          <!-- Specs table -->
          <div data-aos="fade-up">
            <h2 class="text-xl font-bold text-gray-900 mb-4">Vehicle Details</h2>
            <div class="grid grid-cols-2 sm:grid-cols-3 gap-4">
              <div v-for="spec in specs" :key="spec.label" class="bg-gray-50 rounded-xl p-4">
                <p class="text-xs text-gray-400 uppercase tracking-wide font-medium">{{ spec.label }}</p>
                <p class="text-gray-900 font-semibold mt-1">{{ spec.value || '—' }}</p>
              </div>
            </div>
          </div>

          <!-- Description -->
          <div v-if="car.description" data-aos="fade-up">
            <h2 class="text-xl font-bold text-gray-900 mb-3">About This Vehicle</h2>
            <p class="text-gray-600 leading-relaxed whitespace-pre-line">{{ car.description }}</p>
          </div>

          <!-- Features list -->
          <div v-if="features.length" data-aos="fade-up">
            <h2 class="text-xl font-bold text-gray-900 mb-4">Features & Options</h2>
            <ul class="grid grid-cols-1 sm:grid-cols-2 gap-2">
              <li
                v-for="f in features"
                :key="f"
                class="flex items-center gap-2 text-sm text-gray-700"
              >
                <svg class="w-4 h-4 text-primary-500 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"/>
                </svg>
                {{ f }}
              </li>
            </ul>
          </div>
        </div>

        <!-- Right: sticky contact sidebar -->
        <div class="lg:sticky lg:top-24 self-start" data-aos="fade-left">
          <div class="bg-white rounded-2xl shadow-lg border border-gray-100 p-7">
            <h2 class="text-xl font-bold text-gray-900 mb-1">Interested in this vehicle?</h2>
            <p class="text-sm text-gray-500 mb-6">
              Fill out the form and we'll get back to you within a few hours.
            </p>

            <!-- Quick contact form -->
            <form @submit.prevent="submitInquiry" class="space-y-4">
              <div>
                <label class="form-label">Your Name *</label>
                <input v-model="form.name" class="form-input" placeholder="Jane Smith" required />
              </div>
              <div>
                <label class="form-label">Email *</label>
                <input v-model="form.email" type="email" class="form-input" placeholder="jane@example.com" required />
              </div>
              <div>
                <label class="form-label">Phone</label>
                <input v-model="form.phone" type="tel" class="form-input" placeholder="(555) 000-0000" />
              </div>
              <div>
                <label class="form-label">Message</label>
                <textarea
                  v-model="form.message"
                  class="form-input resize-none"
                  rows="3"
                  placeholder="I'm interested in this vehicle…"
                ></textarea>
              </div>

              <!-- Trade-in toggle -->
              <label class="flex items-center gap-3 cursor-pointer select-none">
                <input v-model="form.hasTradeIn" type="checkbox" class="w-4 h-4 accent-primary-600 cursor-pointer" />
                <span class="text-sm font-medium text-gray-700">I have a vehicle to trade in</span>
              </label>

              <!-- Trade-in fields -->
              <div v-if="form.hasTradeIn" class="bg-gray-50 rounded-xl p-4 space-y-3 border border-gray-200">
                <p class="text-xs font-semibold text-gray-600">Trade-In Vehicle</p>
                <div class="grid grid-cols-3 gap-2">
                  <div>
                    <label class="form-label">Year</label>
                    <input v-model="form.tradeYear" class="form-input" placeholder="2018" />
                  </div>
                  <div>
                    <label class="form-label">Make</label>
                    <input v-model="form.tradeMake" class="form-input" placeholder="Ford" />
                  </div>
                  <div>
                    <label class="form-label">Model</label>
                    <input v-model="form.tradeModel" class="form-input" placeholder="F-150" />
                  </div>
                </div>
                <div class="grid grid-cols-2 gap-2">
                  <div>
                    <label class="form-label">Mileage</label>
                    <input v-model="form.tradeMileage" class="form-input" placeholder="78,000" />
                  </div>
                  <div>
                    <label class="form-label">Condition</label>
                    <select v-model="form.tradeCondition" class="form-input">
                      <option value="">Select…</option>
                      <option>Excellent</option>
                      <option>Good</option>
                      <option>Fair</option>
                      <option>Poor</option>
                    </select>
                  </div>
                </div>
              </div>

              <button
                type="submit"
                class="btn-primary w-full"
                :disabled="submitting"
              >
                {{ submitting ? 'Sending…' : 'Contact About This Car' }}
              </button>

              <p v-if="submitSuccess" class="text-sm text-emerald-600 font-medium text-center">
                ✓ Message sent! We'll be in touch soon.
              </p>
              <p v-if="submitError" class="text-sm text-red-500 text-center">
                Something went wrong. Please call us directly.
              </p>
            </form>

            <!-- Direct phone CTA -->
            <div class="mt-6 pt-6 border-t border-gray-100 text-center">
              <p class="text-xs text-gray-400 mb-2">Or call us directly</p>
              <a :href="'tel:' + siteSettings.phone" class="text-xl font-extrabold text-primary-600 hover:underline">
                {{ siteSettings.phone }}
              </a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </PageLayout>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, RouterLink } from 'vue-router'
import PageLayout from '../components/layout/PageLayout.vue'
import { getCar, submitContact } from '../api'
import { siteSettings } from '../composables/useSiteSettings'

const route  = useRoute()
const car    = ref(null)
const loading = ref(true)
const error  = ref(false)
const isAdmin = ref(false)

const activeIdx = ref(0)


const images = computed(() => {
  const imgs = car.value?.images ?? []
  // Sort so primary comes first
  return [...imgs].sort((a, b) => (b.isPrimary ? 1 : 0) - (a.isPrimary ? 1 : 0) || a.sortOrder - b.sortOrder)
})

const activeImage = computed(() => {
  if (images.value.length > 0) return images.value[activeIdx.value].imageUrl
  return 'https://images.unsplash.com/photo-1583121274602-3e2820c69888?auto=format&fit=crop&w=1200&q=80'
})

const specs = computed(() => {
  if (!car.value) return []
  return [
    { label: 'Year',         value: car.value.year },
    { label: 'Make',         value: car.value.make },
    { label: 'Model',        value: car.value.model },
    { label: 'Trim',         value: car.value.trim },
    { label: 'Mileage',      value: car.value.mileage ? `${Number(car.value.mileage).toLocaleString()} mi` : null },
    { label: 'Condition',    value: car.value.condition },
    { label: 'Transmission', value: car.value.transmission },
    { label: 'Engine',       value: car.value.engine },
    { label: 'Drive Train',  value: car.value.driveTrain },
    { label: 'Ext. Color',   value: car.value.exteriorColor },
    { label: 'Int. Color',   value: car.value.interiorColor },
    { label: 'VIN',          value: car.value.vin },
  ]
})

const features = computed(() => {
  const f = car.value?.features
  if (!f) return []
  if (Array.isArray(f)) return f
  try { return JSON.parse(f) } catch { return [] }
})

const priceComparison = computed(() => {
  if (!car.value?.price || !car.value?.kbbValue) return null
  const difference = car.value.kbbValue - car.value.price
  const isBelowKBB = difference > 0
  return {
    difference,
    isBelowKBB,
    label: isBelowKBB ? `✓ ${Math.round((difference / car.value.kbbValue) * 100)}% below estimated value` : `Above estimated value by ${Math.round((Math.abs(difference) / car.value.kbbValue) * 100)}%`
  }
})

onMounted(async () => {
  try {
    const res = await getCar(route.params.id)
    car.value = res.data
    isAdmin.value = !!localStorage.getItem('admin_token')
  } catch {
    error.value = true
  } finally {
    loading.value = false
  }
})

function formatDate(dateString) {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' })
}

// Contact form
const form = ref({
  name: '', email: '', phone: '', message: '',
  hasTradeIn: false,
  tradeYear: '', tradeMake: '', tradeModel: '', tradeMileage: '', tradeCondition: '',
})
const submitting   = ref(false)
const submitSuccess = ref(false)
const submitError   = ref(false)

async function submitInquiry() {
  submitting.value   = true
  submitSuccess.value = false
  submitError.value   = false
  try {
    let message = form.value.message || `I'm interested in this vehicle.`
    if (form.value.hasTradeIn) {
      message += `\n\nTrade-In: ${form.value.tradeYear} ${form.value.tradeMake} ${form.value.tradeModel}`.trim()
      if (form.value.tradeMileage)   message += ` | Mileage: ${form.value.tradeMileage}`
      if (form.value.tradeCondition) message += ` | Condition: ${form.value.tradeCondition}`
    }
    await submitContact({ name: form.value.name, email: form.value.email, phone: form.value.phone, message, carId: car.value?.id })
    submitSuccess.value = true
    form.value = {
      name: '', email: '', phone: '', message: '',
      hasTradeIn: false,
      tradeYear: '', tradeMake: '', tradeModel: '', tradeMileage: '', tradeCondition: '',
    }
  } catch {
    submitError.value = true
  } finally {
    submitting.value = false
  }
}

function formatPrice(price) {
  if (price == null) return 'Call for Price'
  return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD', maximumFractionDigits: 0 }).format(price)
}
</script>
