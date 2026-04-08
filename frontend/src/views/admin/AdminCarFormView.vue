<!--
  AdminCarFormView — Add / Edit vehicle form.
  Used for both /admin/cars/new and /admin/cars/:id/edit.
  Photo upload goes to Cloudinary — configure CLOUDINARY_* in config.js.
-->
<template>
  <div class="max-w-3xl">
    <!-- Header -->
    <div class="flex items-center gap-4 mb-8">
      <RouterLink to="/admin/cars" class="text-gray-400 hover:text-gray-600 transition-colors">
        <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"/>
        </svg>
      </RouterLink>
      <h1 class="text-2xl font-bold text-gray-900">{{ isEdit ? 'Edit Vehicle' : 'Add New Vehicle' }}</h1>
    </div>

    <!-- Loading (edit mode) -->
    <div v-if="fetchingCar" class="space-y-4 animate-pulse">
      <div v-for="n in 8" :key="n" class="h-12 bg-gray-200 rounded-xl"></div>
    </div>

    <form v-else @submit.prevent="save" class="space-y-6">

      <!-- Basic info -->
      <div class="bg-white rounded-2xl p-7 shadow-sm border border-gray-100">
        <h2 class="font-semibold text-gray-900 mb-5">Basic Information</h2>
        <div class="grid grid-cols-1 sm:grid-cols-2 gap-5">
          <div>
            <label class="form-label">Year *</label>
            <input v-model.number="form.year" type="number" class="form-input" :placeholder="currentYear" required
              min="1900" :max="currentYear + 2" />
          </div>
          <div>
            <label class="form-label">Make *</label>
            <input v-model="form.make" class="form-input" placeholder="Toyota" required />
          </div>
          <div>
            <label class="form-label">Model *</label>
            <input v-model="form.model" class="form-input" placeholder="Camry" required />
          </div>
          <div>
            <label class="form-label">Trim</label>
            <input v-model="form.trim" class="form-input" placeholder="XLE" />
          </div>
          <div>
            <label class="form-label">Price ($) *</label>
            <input v-model.number="form.price" type="number" class="form-input" placeholder="24995" required min="0" />
          </div>
          <div>
            <label class="form-label">Estimated Value ($)</label>
            <div class="flex gap-2">
              <input v-model.number="form.kbbValue" type="number" class="form-input flex-1" placeholder="Optional — shown as comparison on listing" min="0" />
              <a
                v-if="form.year && form.make && form.model"
                :href="`https://www.nadaguides.com/Cars/${form.year}/${encodeURIComponent(form.make)}/${encodeURIComponent(form.model)}`"
                target="_blank"
                rel="noopener noreferrer"
                class="px-3 py-2 bg-gray-100 hover:bg-gray-200 text-gray-700 text-xs font-semibold rounded-lg border border-gray-300 transition whitespace-nowrap flex items-center"
                title="Look up NADA value"
              >NADA Lookup</a>
            </div>
          </div>
          <div>
            <label class="form-label">Mileage</label>
            <input v-model.number="form.mileage" type="number" class="form-input" placeholder="45000" min="0" />
          </div>
          <div>
            <label class="form-label">Condition *</label>
            <select v-model="form.condition" class="form-input" required>
              <option value="">Select…</option>
              <option>New</option>
              <option>Used</option>
              <option>Certified</option>
            </select>
          </div>
          <div>
            <label class="form-label">Status</label>
            <select v-model="form.status" class="form-input">
              <option value="available">Available</option>
              <option value="pending">Pending</option>
              <option value="sold">Sold</option>
            </select>
          </div>
        </div>
      </div>

      <!-- Technical specs -->
      <div class="bg-white rounded-2xl p-7 shadow-sm border border-gray-100">
        <h2 class="font-semibold text-gray-900 mb-5">Technical Specs</h2>
        <div class="grid grid-cols-1 sm:grid-cols-2 gap-5">
          <div>
            <label class="form-label">
              VIN
              <span v-if="vinLookupStatus" class="ml-2 font-normal text-xs"
                :class="vinLookupStatus === 'ok' ? 'text-emerald-600' : vinLookupStatus === 'loading' ? 'text-gray-400' : 'text-red-500'">
                {{ vinLookupStatus === 'ok' ? '✓ Auto-filled from VIN' : vinLookupStatus === 'loading' ? 'Looking up…' : 'VIN not found' }}
              </span>
            </label>
            <div class="flex gap-2">
              <input
                v-model="form.vin"
                @input="onVinInput"
                class="form-input font-mono uppercase flex-1"
                placeholder="1HGBH41JXMN109186"
                maxlength="17"
              />
              <button
                type="button"
                @click="lookupVin"
                :disabled="form.vin.length !== 17 || vinLookupStatus === 'loading'"
                class="px-3 py-2 text-sm rounded-lg border border-gray-300 bg-white hover:bg-gray-50 disabled:opacity-40 disabled:cursor-not-allowed whitespace-nowrap"
              >
                Auto-fill
              </button>
            </div>
          </div>
          <div>
            <label class="form-label">Transmission</label>
            <select v-model="form.transmission" class="form-input">
              <option value="">Select…</option>
              <option>Automatic</option>
              <option>Manual</option>
              <option>CVT</option>
              <option>DCT</option>
            </select>
          </div>
          <div>
            <label class="form-label">Engine</label>
            <input v-model="form.engine" class="form-input" placeholder="2.5L 4-Cylinder" />
          </div>
          <div>
            <label class="form-label">Drive Train</label>
            <select v-model="form.driveTrain" class="form-input">
              <option value="">Select…</option>
              <option>FWD</option>
              <option>RWD</option>
              <option>AWD</option>
              <option>4WD</option>
            </select>
          </div>
          <div>
            <label class="form-label">Exterior Color</label>
            <input v-model="form.exteriorColor" class="form-input" placeholder="Midnight Black" />
          </div>
          <div>
            <label class="form-label">Interior Color</label>
            <input v-model="form.interiorColor" class="form-input" placeholder="Ivory" />
          </div>
        </div>
      </div>

      <!-- Description + features -->
      <div class="bg-white rounded-2xl p-7 shadow-sm border border-gray-100">
        <h2 class="font-semibold text-gray-900 mb-5">Description & Features</h2>
        <div class="space-y-5">
          <div>
            <label class="form-label">Description</label>
            <textarea v-model="form.description" class="form-input resize-none" rows="4"
              placeholder="Describe the vehicle…"></textarea>
          </div>
          <div>
            <label class="form-label">
              Features (one per line)
            </label>
            <textarea v-model="featuresText" class="form-input resize-none font-mono text-sm" rows="6"
              placeholder="Backup Camera&#10;Heated Seats&#10;Apple CarPlay&#10;Sunroof"></textarea>
            <p class="text-xs text-gray-400 mt-1">Enter one feature per line. Saved as JSON array.</p>
          </div>
        </div>
      </div>

      <!-- Photo upload -->
      <div class="bg-white rounded-2xl p-7 shadow-sm border border-gray-100">
        <h2 class="font-semibold text-gray-900 mb-2">Photos</h2>
        <p class="text-sm text-gray-400 mb-5">
          Photos are uploaded to Cloudinary. Configure
          <code class="bg-gray-100 px-1 rounded text-xs">CLOUDINARY_CLOUD_NAME</code> and
          <code class="bg-gray-100 px-1 rounded text-xs">CLOUDINARY_UPLOAD_PRESET</code> in
          <code class="bg-gray-100 px-1 rounded text-xs">config.js</code>.
        </p>

        <!-- Existing images -->
        <div v-if="form.images && form.images.length" class="flex flex-wrap gap-3 mb-4">
          <div
            v-for="(img, i) in form.images"
            :key="i"
            class="relative w-24 h-20 rounded-lg overflow-hidden group"
          >
            <img :src="img.imageUrl" class="w-full h-full object-cover" />
            <div class="absolute inset-0 bg-black/50 opacity-0 group-hover:opacity-100 transition-opacity
                        flex items-center justify-center gap-1">
              <button
                type="button"
                class="text-white text-xs font-bold px-1.5 py-0.5 rounded bg-primary-500 hover:bg-primary-600"
                @click="setPrimary(i)"
                :title="img.isPrimary ? 'Primary' : 'Set as primary'"
              >
                {{ img.isPrimary ? '★' : '☆' }}
              </button>
              <button
                type="button"
                class="text-white text-xs font-bold px-1.5 py-0.5 rounded bg-red-500 hover:bg-red-600"
                @click="removeImage(i)"
              >
                ✕
              </button>
            </div>
          </div>
        </div>

        <!-- Upload input -->
        <label class="flex flex-col items-center justify-center w-full h-28 border-2 border-dashed
                      border-gray-300 rounded-xl cursor-pointer hover:border-primary-400
                      hover:bg-primary-50 transition-colors">
          <svg class="w-8 h-8 text-gray-400 mb-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
              d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12"/>
          </svg>
          <span class="text-sm text-gray-500">{{ uploading ? 'Uploading…' : 'Click to upload photos' }}</span>
          <input type="file" accept="image/*" multiple class="hidden" :disabled="uploading" @change="uploadPhotos" />
        </label>
      </div>

      <!-- Error / submit -->
      <p v-if="saveError" class="text-red-500 text-sm">{{ saveError }}</p>
      <div class="flex gap-4">
        <button type="submit" class="btn-primary flex-1 py-4" :disabled="saving">
          {{ saving ? 'Saving…' : (isEdit ? 'Save Changes' : 'Add Vehicle') }}
        </button>
        <RouterLink to="/admin/cars" class="btn-outline flex-1 py-4 text-center">
          Cancel
        </RouterLink>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter, RouterLink } from 'vue-router'
import { getCar, createCar, updateCar } from '../../api'
import { CLOUDINARY_CLOUD_NAME, CLOUDINARY_UPLOAD_PRESET } from '../../config'

const route  = useRoute()
const router = useRouter()

const isEdit     = computed(() => !!route.params.id)
const currentYear = new Date().getFullYear()

const form = ref({
  year: '', make: '', model: '', trim: '', price: '', mileage: '',
  condition: '', status: 'available', vin: '', transmission: '',
  engine: '', driveTrain: '', exteriorColor: '', interiorColor: '',
  description: '', images: [],
})
const featuresText    = ref('')
const fetchingCar     = ref(false)
const saving          = ref(false)
const uploading       = ref(false)
const saveError       = ref('')
const vinLookupStatus = ref('')  // '', 'loading', 'ok', 'error'

let vinDebounceTimer = null
function onVinInput() {
  vinLookupStatus.value = ''
  clearTimeout(vinDebounceTimer)
  if (form.value.vin.length === 17) {
    vinDebounceTimer = setTimeout(lookupVin, 600)
  }
}

async function lookupVin() {
  if (form.value.vin.length !== 17) return
  vinLookupStatus.value = 'loading'
  try {
    const vin = form.value.vin.toUpperCase()
    const res  = await fetch(`https://vpic.nhtsa.dot.gov/api/vehicles/decodevinvalues/${vin}?format=json`)
    const data = await res.json()
    const v    = data?.Results?.[0]
    if (!v || v.ErrorCode !== '0') { vinLookupStatus.value = 'error'; return }

    if (v.ModelYear && !form.value.year) form.value.year  = v.ModelYear
    if (v.Make      && !form.value.make) form.value.make  = toTitleCase(v.Make)
    if (v.Model     && !form.value.model) form.value.model = toTitleCase(v.Model)
    if (v.Trim      && !form.value.trim) form.value.trim  = v.Trim
    // Engine displacement + cylinders
    if (v.DisplacementL && !form.value.engine) {
      const liters   = parseFloat(v.DisplacementL).toFixed(1)
      const cylLabel = v.EngineCylinders ? `V${v.EngineCylinders}` : ''
      const fuel     = v.FuelTypePrimary ? v.FuelTypePrimary.split('/')[0].trim() : ''
      form.value.engine = [liters + 'L', cylLabel, fuel].filter(Boolean).join(' ')
    }
    // Drive type
    if (v.DriveType && !form.value.driveTrain) {
      const driveMap = { 'FWD/Front Wheel Drive': 'FWD', 'RWD/Rear Wheel Drive': 'RWD',
        '4WD/4-Wheel Drive/4x4': '4WD', 'AWD/All Wheel Drive': 'AWD' }
      form.value.driveTrain = driveMap[v.DriveType] || v.DriveType
    }

    vinLookupStatus.value = 'ok'
  } catch {
    vinLookupStatus.value = 'error'
  }
}

function toTitleCase(str) {
  return str.toLowerCase().replace(/\b\w/g, c => c.toUpperCase())
}

// Load existing car data in edit mode
onMounted(async () => {
  if (!isEdit.value) return
  fetchingCar.value = true
  try {
    const res = await getCar(route.params.id)
    const car = res.data
    Object.assign(form.value, car)
    // Flatten features array to line-separated text
    const f = car.features
    if (Array.isArray(f)) featuresText.value = f.join('\n')
    else if (typeof f === 'string') {
      try { featuresText.value = JSON.parse(f).join('\n') } catch { featuresText.value = f }
    }
  } finally {
    fetchingCar.value = false
  }
})

// Upload photos to Cloudinary unsigned upload
async function uploadPhotos(e) {
  const files = Array.from(e.target.files)
  if (!files.length) return
  uploading.value = true
  try {
    for (const file of files) {
      const fd = new FormData()
      fd.append('file', file)
      fd.append('upload_preset', CLOUDINARY_UPLOAD_PRESET)
      const res = await fetch(
        `https://api.cloudinary.com/v1_1/${CLOUDINARY_CLOUD_NAME}/image/upload`,
        { method: 'POST', body: fd }
      )
      const data = await res.json()
      form.value.images = [
        ...form.value.images,
        { imageUrl: data.secure_url, isPrimary: form.value.images.length === 0, sortOrder: form.value.images.length },
      ]
    }
  } finally {
    uploading.value = false
    e.target.value = ''
  }
}

function setPrimary(idx) {
  form.value.images = form.value.images.map((img, i) => ({ ...img, isPrimary: i === idx }))
}

function removeImage(idx) {
  form.value.images = form.value.images.filter((_, i) => i !== idx)
  // Ensure at least one primary
  if (form.value.images.length && !form.value.images.some(i => i.isPrimary)) {
    form.value.images[0].isPrimary = true
  }
}

async function save() {
  saving.value    = true
  saveError.value = ''
  try {
    // Convert feature text back to array
    const features = featuresText.value
      .split('\n')
      .map(s => s.trim())
      .filter(Boolean)

    const payload = { ...form.value, features }

    if (isEdit.value) {
      await updateCar(route.params.id, payload)
    } else {
      await createCar(payload)
    }
    router.push({ name: 'AdminCars' })
  } catch (err) {
    saveError.value = err.response?.data?.message ?? 'Failed to save. Please try again.'
  } finally {
    saving.value = false
  }
}
</script>
