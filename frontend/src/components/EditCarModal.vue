<!--
  EditCarModal — modal for editing a car's details.
  Emits 'close' and 'save' events.
-->
<template>
  <!-- Modal backdrop -->
  <Teleport to="body">
    <Transition name="fade">
      <div v-if="open" class="fixed inset-0 bg-black bg-opacity-50 z-40 flex items-center justify-center p-4" @click="$emit('close')">
        <!-- Modal body -->
        <div
          class="bg-white rounded-2xl shadow-xl max-w-2xl w-full max-h-[90vh] overflow-y-auto z-50"
          @click.stop
        >
          <!-- Header -->
          <div class="sticky top-0 bg-white border-b border-gray-200 px-6 py-4 flex items-center justify-between">
            <h2 class="text-2xl font-bold text-gray-900">Edit Vehicle</h2>
            <button
              @click="$emit('close')"
              class="p-2 hover:bg-gray-100 rounded-lg transition-colors"
            >
              <svg class="w-6 h-6 text-gray-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>

          <!-- Form -->
          <form @submit.prevent="saveCar" class="p-6 space-y-6">

            <!-- Year, Make, Model row -->
            <div class="grid grid-cols-3 gap-4">
              <div>
                <label class="form-label">Year *</label>
                <input
                  v-model.number="formData.year"
                  type="number"
                  class="form-input"
                  placeholder="2023"
                  min="1980"
                  :max="new Date().getFullYear() + 1"
                  required
                />
              </div>
              <div>
                <label class="form-label">Make *</label>
                <input
                  v-model="formData.make"
                  class="form-input"
                  placeholder="Toyota"
                  required
                />
              </div>
              <div>
                <label class="form-label">Model *</label>
                <input
                  v-model="formData.model"
                  class="form-input"
                  placeholder="Camry"
                  required
                />
              </div>
            </div>

            <!-- Trim and Price -->
            <div class="grid grid-cols-2 gap-4">
              <div>
                <label class="form-label">Trim</label>
                <input
                  v-model="formData.trim"
                  class="form-input"
                  placeholder="SE, LE, XLE, etc."
                />
              </div>
              <div>
                <label class="form-label">Price *</label>
                <input
                  v-model.number="formData.price"
                  type="number"
                  class="form-input"
                  placeholder="25000"
                  min="0"
                  step="100"
                  required
                />
              </div>
            </div>

            <!-- Mileage and Condition -->
            <div class="grid grid-cols-2 gap-4">
              <div>
                <label class="form-label">Mileage (miles)</label>
                <input
                  v-model.number="formData.mileage"
                  type="number"
                  class="form-input"
                  placeholder="50000"
                  min="0"
                />
              </div>
              <div>
                <label class="form-label">Condition</label>
                <select v-model="formData.condition" class="form-input">
                  <option value="">Select condition</option>
                  <option value="New">New</option>
                  <option value="Used">Used</option>
                  <option value="Certified">Certified Pre-Owned</option>
                </select>
              </div>
            </div>

            <!-- VIN -->
            <div>
              <label class="form-label">VIN</label>
              <input
                v-model="formData.vin"
                class="form-input"
                placeholder="17-character VIN"
                maxlength="17"
              />
            </div>

            <!-- Transmission, Engine, Drive Train -->
            <div class="grid grid-cols-3 gap-4">
              <div>
                <label class="form-label">Transmission</label>
                <select v-model="formData.transmission" class="form-input">
                  <option value="">Select</option>
                  <option value="Manual">Manual</option>
                  <option value="Automatic">Automatic</option>
                  <option value="CVT">CVT</option>
                </select>
              </div>
              <div>
                <label class="form-label">Engine</label>
                <input v-model="formData.engine" class="form-input" placeholder="2.5L 4-cyl" />
              </div>
              <div>
                <label class="form-label">Drive Train</label>
                <select v-model="formData.driveTrain" class="form-input">
                  <option value="">Select</option>
                  <option value="FWD">Front-Wheel Drive</option>
                  <option value="RWD">Rear-Wheel Drive</option>
                  <option value="AWD">All-Wheel Drive</option>
                  <option value="4WD">4-Wheel Drive</option>
                </select>
              </div>
            </div>

            <!-- Colors -->
            <div class="grid grid-cols-2 gap-4">
              <div>
                <label class="form-label">Exterior Color</label>
                <input v-model="formData.exteriorColor" class="form-input" placeholder="Black Pearl" />
              </div>
              <div>
                <label class="form-label">Interior Color</label>
                <input v-model="formData.interiorColor" class="form-input" placeholder="Tan Leather" />
              </div>
            </div>

            <!-- Status -->
            <div>
              <label class="form-label">Status</label>
              <select v-model="formData.status" class="form-input">
                <option value="available">Available</option>
                <option value="pending">Pending</option>
                <option value="sold">Sold</option>
              </select>
            </div>

            <!-- Description -->
            <div>
              <label class="form-label">Description</label>
              <textarea
                v-model="formData.description"
                class="form-input resize-none"
                rows="4"
                placeholder="Detailed description of the vehicle..."
              ></textarea>
            </div>

            <!-- Features (JSON array as comma-separated list) -->
            <div>
              <label class="form-label">Features (comma-separated)</label>
              <textarea
                v-model="featuresText"
                class="form-input resize-none"
                rows="3"
                placeholder="Backup Camera, Sunroof, Leather Seats, Apple CarPlay, Android Auto"
              ></textarea>
              <p class="text-xs text-gray-500 mt-1">Enter features separated by commas</p>
            </div>

            <!-- Error message -->
            <div v-if="error" class="bg-red-50 border border-red-200 rounded-lg p-3">
              <p class="text-sm text-red-700">{{ error }}</p>
            </div>

            <!-- Success message -->
            <div v-if="success" class="bg-emerald-50 border border-emerald-200 rounded-lg p-3">
              <p class="text-sm text-emerald-700">✓ Vehicle updated successfully!</p>
            </div>

            <!-- Buttons -->
            <div class="flex gap-3 justify-end border-t border-gray-200 pt-6">
              <button
                type="button"
                @click="$emit('close')"
                class="px-6 py-2 border border-gray-300 rounded-lg font-medium text-gray-700 hover:bg-gray-50 transition-colors"
              >
                Cancel
              </button>
              <button
                type="submit"
                :disabled="saving"
                class="px-6 py-2 bg-primary-600 hover:bg-primary-700 text-white font-medium rounded-lg transition-colors disabled:opacity-50 disabled:cursor-not-allowed flex items-center gap-2"
              >
                <span v-if="saving" class="inline-block animate-spin">⟳</span>
                {{ saving ? 'Saving…' : 'Save Changes' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>

<script setup>
import { ref, computed, watch, Teleport, Transition } from 'vue'
import { updateCar } from '../api'

const props = defineProps({
  open: Boolean,
  car: Object,
})

const emits = defineEmits(['close', 'save'])

const formData = ref({
  year: null,
  make: '',
  model: '',
  trim: '',
  price: null,
  mileage: null,
  vin: '',
  condition: '',
  transmission: '',
  engine: '',
  driveTrain: '',
  exteriorColor: '',
  interiorColor: '',
  status: 'available',
  description: '',
  features: [],
})

const featuresText = ref('')
const saving = ref(false)
const error = ref('')
const success = ref(false)

// Populate form when car prop changes
watch(
  () => props.car,
  (newCar) => {
    if (newCar) {
      formData.value = {
        year: newCar.year,
        make: newCar.make,
        model: newCar.model,
        trim: newCar.trim || '',
        price: newCar.price,
        mileage: newCar.mileage,
        vin: newCar.vin || '',
        condition: newCar.condition || '',
        transmission: newCar.transmission || '',
        engine: newCar.engine || '',
        driveTrain: newCar.driveTrain || '',
        exteriorColor: newCar.exteriorColor || '',
        interiorColor: newCar.interiorColor || '',
        status: newCar.status || 'available',
        description: newCar.description || '',
        features: Array.isArray(newCar.features) ? newCar.features : [],
      }
      // Convert features array to comma-separated text
      featuresText.value = formData.value.features.join(', ')
      error.value = ''
      success.value = false
    }
  },
  { deep: true }
)

async function saveCar() {
  if (!props.car?.id) return

  saving.value = true
  error.value = ''
  success.value = false

  try {
    // Convert features text back to array
    formData.value.features = featuresText.value
      .split(',')
      .map((f) => f.trim())
      .filter((f) => f)

    const res = await updateCar(props.car.id, formData.value)
    success.value = true
    setTimeout(() => {
      emits('save', res.data)
      emits('close')
    }, 1000)
  } catch (err) {
    error.value = err.response?.data?.message || 'Failed to save vehicle. Please try again.'
  } finally {
    saving.value = false
  }
}
</script>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
