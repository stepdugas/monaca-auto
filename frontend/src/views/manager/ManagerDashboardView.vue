<!--
  ManagerDashboardView — Manager panel with tabs for:
  - Inventory (add / edit / delete vehicles)
  - Contact submissions
  - Store settings (hours, contact info)
-->
<template>
  <div class="min-h-screen bg-gray-50">

    <!-- ── Top nav ──────────────────────────────────────────────────── -->
    <header class="bg-dark-800 border-b border-white/10 px-6 py-4 flex items-center justify-between">
      <div class="flex items-center gap-4">
        <h1 class="text-white font-bold">Manager Portal</h1>
        <nav class="flex gap-1 ml-4">
          <button
            v-for="tab in tabs"
            :key="tab.key"
            @click="activeTab = tab.key"
            :class="[
              'px-4 py-1.5 rounded-lg text-sm font-medium transition-colors',
              activeTab === tab.key
                ? 'bg-primary-600 text-white'
                : 'text-gray-400 hover:text-white hover:bg-white/10'
            ]"
          >{{ tab.label }}</button>
        </nav>
      </div>
      <button @click="logout" class="text-gray-400 hover:text-white text-sm transition-colors">
        Sign Out
      </button>
    </header>

    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">

      <!-- ── Inventory tab ─────────────────────────────────────────── -->
      <div v-if="activeTab === 'inventory'">
        <div class="flex items-center justify-between mb-6">
          <div>
            <h2 class="text-xl font-bold text-gray-900">Inventory</h2>
            <p class="text-gray-500 text-sm mt-0.5">{{ cars.length }} vehicles loaded</p>
          </div>
          <button @click="openAddCar" class="btn-primary text-sm py-2 px-5">+ Add Vehicle</button>
        </div>

        <div v-if="invLoading" class="space-y-3">
          <div v-for="n in 5" :key="n" class="h-14 bg-gray-200 rounded-xl animate-pulse"></div>
        </div>

        <div v-else-if="cars.length" class="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
          <div class="overflow-x-auto">
            <table class="w-full text-sm">
              <thead class="bg-gray-50 text-xs uppercase tracking-wide text-gray-500">
                <tr>
                  <th class="text-left px-5 py-3">Vehicle</th>
                  <th class="text-left px-5 py-3">Year</th>
                  <th class="text-left px-5 py-3">Price</th>
                  <th class="text-left px-5 py-3">Status</th>
                  <th class="px-5 py-3"></th>
                </tr>
              </thead>
              <tbody class="divide-y divide-gray-100">
                <tr v-for="car in cars" :key="car.id" class="hover:bg-gray-50 transition-colors">
                  <td class="px-5 py-3 font-medium text-gray-900">{{ car.make }} {{ car.model }}</td>
                  <td class="px-5 py-3 text-gray-500">{{ car.year }}</td>
                  <td class="px-5 py-3 font-semibold text-gray-900">
                    {{ car.price ? '$' + Number(car.price).toLocaleString() : '—' }}
                  </td>
                  <td class="px-5 py-3">
                    <span :class="statusClass(car.status)" class="px-2 py-1 rounded-full text-xs font-medium">
                      {{ car.status }}
                    </span>
                  </td>
                  <td class="px-5 py-3 text-right space-x-2">
                    <button @click="openEditCar(car)" class="text-primary-600 hover:underline text-sm">Edit</button>
                    <button @click="confirmDelete(car)" class="text-red-500 hover:underline text-sm">Delete</button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <p v-else class="text-gray-400 text-center py-16">No vehicles found.</p>
      </div>

      <!-- ── Contacts tab ──────────────────────────────────────────── -->
      <div v-else-if="activeTab === 'contacts'">
        <h2 class="text-xl font-bold text-gray-900 mb-6">Contact Submissions</h2>

        <div v-if="contLoading" class="space-y-3">
          <div v-for="n in 4" :key="n" class="h-20 bg-gray-200 rounded-xl animate-pulse"></div>
        </div>

        <div v-else-if="contacts.length" class="space-y-4">
          <div v-for="c in contacts" :key="c.id"
               class="bg-white rounded-2xl border border-gray-100 shadow-sm p-5">
            <div class="flex items-start justify-between mb-2">
              <div>
                <p class="font-semibold text-gray-900">{{ c.name }}</p>
                <p class="text-sm text-gray-500">{{ c.email }}
                  <span v-if="c.phone"> · {{ c.phone }}</span>
                </p>
              </div>
              <p class="text-xs text-gray-400">{{ formatDate(c.createdAt) }}</p>
            </div>
            <p class="text-gray-700 text-sm leading-relaxed">{{ c.message }}</p>
            <p v-if="c.carId" class="text-xs text-primary-600 mt-2">Re: Vehicle #{{ c.carId }}</p>
          </div>
        </div>

        <p v-else class="text-gray-400 text-center py-16">No contact submissions yet.</p>
      </div>

      <!-- ── Settings tab ──────────────────────────────────────────── -->
      <div v-else-if="activeTab === 'settings'">
        <h2 class="text-xl font-bold text-gray-900 mb-2">Store Settings</h2>
        <p class="text-gray-500 text-sm mb-6">Update hours, contact info, and other store details.</p>

        <div v-if="settLoading" class="space-y-3">
          <div v-for="n in 8" :key="n" class="h-12 bg-gray-200 rounded-xl animate-pulse"></div>
        </div>

        <form v-else @submit.prevent="saveSettings" class="bg-white rounded-2xl border border-gray-100 shadow-sm p-6 space-y-4">
          <div v-for="(value, key) in settings" :key="key">
            <label class="form-label text-xs uppercase tracking-wide text-gray-500">{{ formatKey(key) }}</label>
            <input v-model="settings[key]" class="form-input" :placeholder="key" />
          </div>

          <p v-if="settSuccess" class="text-green-600 text-sm">Settings saved successfully.</p>
          <p v-if="settError" class="text-red-500 text-sm">{{ settError }}</p>

          <button type="submit" class="btn-primary" :disabled="settSaving">
            {{ settSaving ? 'Saving…' : 'Save Settings' }}
          </button>
        </form>
      </div>

    </main>

    <!-- ── Car form modal ────────────────────────────────────────────── -->
    <div v-if="carModal.open"
         class="fixed inset-0 bg-black/60 flex items-center justify-center z-50 p-4">
      <div class="bg-white rounded-2xl shadow-2xl w-full max-w-2xl max-h-[90vh] overflow-y-auto p-6">
        <div class="flex items-center justify-between mb-6">
          <h3 class="text-lg font-bold text-gray-900">{{ carModal.isEdit ? 'Edit Vehicle' : 'Add Vehicle' }}</h3>
          <button @click="carModal.open = false" class="text-gray-400 hover:text-gray-600 text-2xl leading-none">&times;</button>
        </div>

        <form @submit.prevent="submitCar" class="space-y-4">
          <div class="grid sm:grid-cols-2 gap-4">
            <div>
              <label class="form-label">Make *</label>
              <input v-model="carForm.make" class="form-input" required />
            </div>
            <div>
              <label class="form-label">Model *</label>
              <input v-model="carForm.model" class="form-input" required />
            </div>
            <div>
              <label class="form-label">Year *</label>
              <input v-model.number="carForm.year" type="number" min="1900" max="2100" class="form-input" required />
            </div>
            <div>
              <label class="form-label">Trim</label>
              <input v-model="carForm.trim" class="form-input" />
            </div>
            <div>
              <label class="form-label">Price ($)</label>
              <input v-model.number="carForm.price" type="number" min="0" class="form-input" />
            </div>
            <div>
              <label class="form-label">Mileage</label>
              <input v-model.number="carForm.mileage" type="number" min="0" class="form-input" />
            </div>
            <div>
              <label class="form-label">VIN</label>
              <input v-model="carForm.vin" class="form-input" maxlength="17" />
            </div>
            <div>
              <label class="form-label">Condition</label>
              <select v-model="carForm.condition" class="form-input">
                <option value="Used">Used</option>
                <option value="New">New</option>
                <option value="Certified">Certified</option>
              </select>
            </div>
            <div>
              <label class="form-label">Status</label>
              <select v-model="carForm.status" class="form-input">
                <option value="available">Available</option>
                <option value="pending">Pending</option>
                <option value="sold">Sold</option>
              </select>
            </div>
            <div>
              <label class="form-label">Transmission</label>
              <input v-model="carForm.transmission" class="form-input" placeholder="Automatic" />
            </div>
            <div>
              <label class="form-label">Exterior Color</label>
              <input v-model="carForm.exteriorColor" class="form-input" />
            </div>
            <div>
              <label class="form-label">Interior Color</label>
              <input v-model="carForm.interiorColor" class="form-input" />
            </div>
          </div>
          <div>
            <label class="form-label">Description</label>
            <textarea v-model="carForm.description" class="form-input" rows="3"></textarea>
          </div>

          <p v-if="carModal.error" class="text-red-500 text-sm">{{ carModal.error }}</p>

          <div class="flex gap-3 pt-2">
            <button type="submit" class="btn-primary flex-1" :disabled="carModal.saving">
              {{ carModal.saving ? 'Saving…' : (carModal.isEdit ? 'Save Changes' : 'Add Vehicle') }}
            </button>
            <button type="button" @click="carModal.open = false" class="btn-secondary flex-1">Cancel</button>
          </div>
        </form>
      </div>
    </div>

    <!-- ── Delete confirm ────────────────────────────────────────────── -->
    <div v-if="deleteTarget"
         class="fixed inset-0 bg-black/60 flex items-center justify-center z-50 p-4">
      <div class="bg-white rounded-2xl shadow-2xl p-6 max-w-sm w-full text-center">
        <p class="font-semibold text-gray-900 mb-2">Delete vehicle?</p>
        <p class="text-gray-500 text-sm mb-6">{{ deleteTarget.year }} {{ deleteTarget.make }} {{ deleteTarget.model }} — this cannot be undone.</p>
        <div class="flex gap-3">
          <button @click="deleteCar" class="btn-primary flex-1 bg-red-600 hover:bg-red-700">Delete</button>
          <button @click="deleteTarget = null" class="btn-secondary flex-1">Cancel</button>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import {
  managerGetInventory,
  managerGetContacts,
  managerGetSettings,
  managerUpdateSettings,
  managerCreateCar,
  managerUpdateCar,
  managerDeleteCar,
} from '../../api'

const router = useRouter()

const tabs = [
  { key: 'inventory', label: 'Inventory' },
  { key: 'contacts',  label: 'Contacts'  },
  { key: 'settings',  label: 'Settings'  },
]
const activeTab = ref('inventory')

// ── Inventory ────────────────────────────────────────────────────────────
const cars      = ref([])
const invLoading = ref(false)

async function fetchCars() {
  invLoading.value = true
  try {
    const res = await managerGetInventory({ size: 200, status: 'available' })
    const data = res.data
    cars.value = data.content ?? (Array.isArray(data) ? data : [])
  } catch { cars.value = [] }
  finally { invLoading.value = false }
}

const carForm = ref(emptyCarForm())
const carModal = ref({ open: false, isEdit: false, saving: false, error: '' })
const deleteTarget = ref(null)

function emptyCarForm() {
  return { make: '', model: '', year: new Date().getFullYear(), trim: '', price: null,
           mileage: null, vin: '', condition: 'Used', status: 'available',
           transmission: '', exteriorColor: '', interiorColor: '', description: '' }
}

function openAddCar() {
  carForm.value = emptyCarForm()
  carModal.value = { open: true, isEdit: false, saving: false, error: '' }
}

function openEditCar(car) {
  carForm.value = { ...car }
  carModal.value = { open: true, isEdit: true, id: car.id, saving: false, error: '' }
}

async function submitCar() {
  carModal.value.saving = true
  carModal.value.error  = ''
  try {
    if (carModal.value.isEdit) {
      await managerUpdateCar(carModal.value.id, carForm.value)
    } else {
      await managerCreateCar(carForm.value)
    }
    carModal.value.open = false
    await fetchCars()
  } catch (err) {
    carModal.value.error = err.response?.data?.message || 'Save failed.'
  } finally {
    carModal.value.saving = false
  }
}

function confirmDelete(car) { deleteTarget.value = car }

async function deleteCar() {
  try {
    await managerDeleteCar(deleteTarget.value.id)
    deleteTarget.value = null
    await fetchCars()
  } catch { /* silently ignore */ }
}

function statusClass(s) {
  return {
    available: 'bg-green-100 text-green-700',
    pending:   'bg-yellow-100 text-yellow-700',
    sold:      'bg-gray-100 text-gray-500',
  }[s] || 'bg-gray-100 text-gray-500'
}

// ── Contacts ─────────────────────────────────────────────────────────────
const contacts    = ref([])
const contLoading = ref(false)

async function fetchContacts() {
  contLoading.value = true
  try {
    const res = await managerGetContacts()
    contacts.value = res.data
  } catch { contacts.value = [] }
  finally { contLoading.value = false }
}

function formatDate(iso) {
  if (!iso) return ''
  return new Date(iso).toLocaleString('en-US', { month: 'short', day: 'numeric', year: 'numeric', hour: '2-digit', minute: '2-digit' })
}

// ── Settings ─────────────────────────────────────────────────────────────
const settings    = ref({})
const settLoading = ref(false)
const settSaving  = ref(false)
const settSuccess = ref(false)
const settError   = ref('')

async function fetchSettings() {
  settLoading.value = true
  try {
    const res = await managerGetSettings()
    settings.value = res.data
  } catch { settings.value = {} }
  finally { settLoading.value = false }
}

async function saveSettings() {
  settSaving.value  = true
  settSuccess.value = false
  settError.value   = ''
  try {
    await managerUpdateSettings(settings.value)
    settSuccess.value = true
    setTimeout(() => { settSuccess.value = false }, 3000)
  } catch (err) {
    settError.value = err.response?.data?.message || 'Save failed.'
  } finally {
    settSaving.value = false
  }
}

function formatKey(key) {
  return key.replace(/_/g, ' ')
}

// ── Auth ──────────────────────────────────────────────────────────────────
function logout() {
  localStorage.removeItem('manager_token')
  router.push({ name: 'ManagerLogin' })
}

// ── Init ──────────────────────────────────────────────────────────────────
onMounted(() => {
  fetchCars()
  fetchContacts()
  fetchSettings()
})
</script>
