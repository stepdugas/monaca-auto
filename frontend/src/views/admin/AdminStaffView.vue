<!--
  AdminStaffView — Manage staff members (add, edit, delete).
  Photos upload to Cloudinary unsigned.
-->
<template>
  <div>
    <!-- Page header -->
    <div class="flex items-center justify-between mb-6">
      <div>
        <h1 class="text-2xl font-bold text-gray-900">Staff Members</h1>
        <p class="text-gray-500 text-sm mt-1">{{ staff.length }} member{{ staff.length !== 1 ? 's' : '' }}</p>
      </div>
      <button
        v-if="!showForm"
        class="btn-primary text-sm py-2 px-5"
        @click="openAdd"
      >
        + Add Staff Member
      </button>
    </div>

    <!-- Add / Edit form panel -->
    <div v-if="showForm" class="bg-white rounded-2xl shadow-sm border border-gray-100 p-7 mb-8">
      <h2 class="text-lg font-semibold text-gray-900 mb-6">
        {{ editingId ? 'Edit Staff Member' : 'Add Staff Member' }}
      </h2>

      <form @submit.prevent="save" class="space-y-5">
        <!-- Photo upload -->
        <div>
          <label class="form-label">Photo</label>
          <div class="flex items-center gap-5">
            <!-- Preview / placeholder -->
            <div class="w-20 h-20 rounded-full bg-gray-100 flex-shrink-0 overflow-hidden flex items-center justify-center border border-gray-200">
              <img
                v-if="form.photoUrl"
                :src="form.photoUrl"
                alt="Staff photo preview"
                class="w-full h-full object-cover"
              />
              <!-- Placeholder avatar SVG -->
              <svg v-else class="w-10 h-10 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"
                  d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/>
              </svg>
            </div>

            <div>
              <label class="cursor-pointer inline-flex items-center gap-2 px-4 py-2 border border-gray-300 rounded-lg text-sm font-medium text-gray-700 hover:bg-gray-50 transition-colors">
                <svg class="w-4 h-4 text-gray-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-8l-4-4m0 0L8 8m4-4v12"/>
                </svg>
                {{ uploading ? 'Uploading…' : 'Upload Photo' }}
                <input
                  type="file"
                  accept="image/*"
                  class="hidden"
                  :disabled="uploading"
                  @change="uploadPhoto"
                />
              </label>
              <p v-if="form.photoUrl" class="text-xs text-gray-400 mt-1">
                <button type="button" class="text-red-500 hover:text-red-700" @click="form.photoUrl = ''">Remove photo</button>
              </p>
              <p class="text-xs text-gray-400 mt-1">JPG, PNG — uploaded to Cloudinary</p>
            </div>
          </div>
        </div>

        <!-- Name + Title row -->
        <div class="grid grid-cols-1 sm:grid-cols-2 gap-5">
          <div>
            <label class="form-label">Name <span class="text-red-500">*</span></label>
            <input v-model="form.name" class="form-input" placeholder="Jane Smith" required />
          </div>
          <div>
            <label class="form-label">Title / Role</label>
            <input v-model="form.title" class="form-input" placeholder="Sales Manager" />
          </div>
        </div>

        <!-- Email + Phone row -->
        <div class="grid grid-cols-1 sm:grid-cols-2 gap-5">
          <div>
            <label class="form-label">Email</label>
            <input v-model="form.email" type="email" class="form-input" placeholder="jane@example.com" />
          </div>
          <div>
            <label class="form-label">Phone</label>
            <input v-model="form.phone" type="tel" class="form-input" placeholder="(555) 555-1234" />
          </div>
        </div>

        <!-- Bio -->
        <div>
          <label class="form-label">Bio</label>
          <textarea
            v-model="form.bio"
            rows="4"
            class="form-input resize-none"
            placeholder="A short bio about this team member…"
          ></textarea>
        </div>

        <!-- Sort order -->
        <div class="max-w-xs">
          <label class="form-label">Sort Order</label>
          <input v-model.number="form.sortOrder" type="number" min="0" class="form-input" placeholder="0" />
          <p class="text-xs text-gray-400 mt-1">Lower numbers appear first</p>
        </div>

        <!-- Form actions -->
        <div class="flex items-center gap-3 pt-2">
          <button
            type="submit"
            class="btn-primary text-sm py-2 px-6"
            :disabled="saving"
          >
            {{ saving ? 'Saving…' : (editingId ? 'Save Changes' : 'Add Member') }}
          </button>
          <button
            type="button"
            class="text-sm font-medium text-gray-500 hover:text-gray-700 transition-colors"
            @click="cancelForm"
          >
            Cancel
          </button>
        </div>
      </form>
    </div>

    <!-- Loading skeleton -->
    <div v-if="loading" class="space-y-3">
      <div v-for="n in 4" :key="n" class="h-20 bg-gray-200 rounded-xl animate-pulse"></div>
    </div>

    <!-- Staff cards grid -->
    <div v-else-if="staff.length" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-5">
      <div
        v-for="member in staff"
        :key="member.id"
        class="bg-white rounded-2xl shadow-sm border border-gray-100 p-5 flex items-start gap-4"
      >
        <!-- Avatar -->
        <div class="w-14 h-14 rounded-full bg-gray-100 flex-shrink-0 overflow-hidden flex items-center justify-center border border-gray-200">
          <img
            v-if="member.photoUrl"
            :src="member.photoUrl"
            :alt="member.name"
            class="w-full h-full object-cover"
          />
          <svg v-else class="w-7 h-7 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"
              d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/>
          </svg>
        </div>

        <!-- Info -->
        <div class="flex-1 min-w-0">
          <p class="font-semibold text-gray-900 truncate">{{ member.name }}</p>
          <p v-if="member.title" class="text-sm text-primary-600 truncate">{{ member.title }}</p>
          <p v-if="member.email" class="text-xs text-gray-400 truncate mt-0.5">{{ member.email }}</p>
        </div>

        <!-- Actions -->
        <div class="flex flex-col items-end gap-2 flex-shrink-0">
          <button
            class="text-primary-600 hover:text-primary-800 font-medium text-xs"
            @click="openEdit(member)"
          >
            Edit
          </button>
          <button
            class="text-red-500 hover:text-red-700 font-medium text-xs"
            @click="confirmDelete(member)"
          >
            Delete
          </button>
        </div>
      </div>
    </div>

    <!-- Empty state -->
    <div v-else-if="!showForm" class="text-center py-20 text-gray-400">
      <svg class="w-14 h-14 mx-auto mb-4 text-gray-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"
          d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0z"/>
      </svg>
      <p class="text-lg font-medium">No staff members yet.</p>
      <button class="btn-primary mt-4 text-sm" @click="openAdd">Add First Staff Member</button>
    </div>

    <!-- Delete confirmation modal -->
    <Teleport to="body">
      <div
        v-if="deleteTarget"
        class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4"
      >
        <div class="bg-white rounded-2xl p-8 max-w-sm w-full shadow-2xl">
          <h3 class="text-lg font-bold text-gray-900 mb-2">Remove Staff Member?</h3>
          <p class="text-gray-500 text-sm">
            Are you sure you want to remove <strong>{{ deleteTarget.name }}</strong>? This cannot be undone.
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
              {{ deleting ? 'Removing…' : 'Remove' }}
            </button>
          </div>
        </div>
      </div>
    </Teleport>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../../api/index'
import { CLOUDINARY_CLOUD_NAME, CLOUDINARY_UPLOAD_PRESET } from '../../config'

const staff       = ref([])
const loading     = ref(true)
const showForm    = ref(false)
const editingId   = ref(null)
const saving      = ref(false)
const uploading   = ref(false)
const deleteTarget = ref(null)
const deleting    = ref(false)

const emptyForm = () => ({
  name: '',
  title: '',
  email: '',
  phone: '',
  bio: '',
  photoUrl: '',
  sortOrder: 0,
})

const form = ref(emptyForm())

async function fetchStaff() {
  loading.value = true
  try {
    const res = await api.get('/api/admin/staff')
    staff.value = res.data
  } finally {
    loading.value = false
  }
}

function openAdd() {
  editingId.value = null
  form.value = emptyForm()
  showForm.value = true
}

function openEdit(member) {
  editingId.value = member.id
  form.value = {
    name: member.name ?? '',
    title: member.title ?? '',
    email: member.email ?? '',
    phone: member.phone ?? '',
    bio: member.bio ?? '',
    photoUrl: member.photoUrl ?? '',
    sortOrder: member.sortOrder ?? 0,
  }
  showForm.value = true
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

function cancelForm() {
  showForm.value = false
  editingId.value = null
  form.value = emptyForm()
}

async function save() {
  saving.value = true
  try {
    if (editingId.value) {
      const res = await api.put(`/api/admin/staff/${editingId.value}`, form.value)
      const idx = staff.value.findIndex(m => m.id === editingId.value)
      if (idx !== -1) staff.value[idx] = res.data
    } else {
      const res = await api.post('/api/admin/staff', form.value)
      staff.value.push(res.data)
    }
    cancelForm()
  } finally {
    saving.value = false
  }
}

async function uploadPhoto(e) {
  const file = e.target.files[0]
  if (!file) return
  uploading.value = true
  try {
    const fd = new FormData()
    fd.append('file', file)
    fd.append('upload_preset', CLOUDINARY_UPLOAD_PRESET)
    const res = await fetch(
      `https://api.cloudinary.com/v1_1/${CLOUDINARY_CLOUD_NAME}/image/upload`,
      { method: 'POST', body: fd }
    )
    const data = await res.json()
    form.value.photoUrl = data.secure_url
  } finally {
    uploading.value = false
    e.target.value = ''
  }
}

function confirmDelete(member) {
  deleteTarget.value = member
}

async function doDelete() {
  deleting.value = true
  try {
    await api.delete(`/api/admin/staff/${deleteTarget.value.id}`)
    staff.value = staff.value.filter(m => m.id !== deleteTarget.value.id)
    deleteTarget.value = null
  } finally {
    deleting.value = false
  }
}

onMounted(fetchStaff)
</script>
