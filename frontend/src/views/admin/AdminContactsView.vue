<!--
  AdminContactsView — View and manage contact form submissions.
-->
<template>
  <div>
    <div class="flex items-center justify-between mb-6">
      <div>
        <h1 class="text-2xl font-bold text-gray-900">Contact Submissions</h1>
        <p class="text-gray-500 text-sm mt-1">{{ contacts.length }} submission{{ contacts.length !== 1 ? 's' : '' }}</p>
      </div>
    </div>

    <!-- Loading -->
    <div v-if="loading" class="space-y-3">
      <div v-for="n in 5" :key="n" class="h-20 bg-gray-200 rounded-xl animate-pulse"></div>
    </div>

    <!-- Empty -->
    <div v-else-if="!contacts.length" class="text-center py-20 text-gray-400">
      <p class="text-lg font-medium">No submissions yet.</p>
    </div>

    <!-- Table -->
    <div v-else class="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
      <div class="overflow-x-auto">
        <table class="w-full text-sm">
          <thead class="bg-gray-50 text-xs uppercase tracking-wide text-gray-500">
            <tr>
              <th class="text-left px-5 py-3">Name</th>
              <th class="text-left px-5 py-3">Email</th>
              <th class="text-left px-5 py-3">Phone</th>
              <th class="text-left px-5 py-3">Vehicle</th>
              <th class="text-left px-5 py-3">Date</th>
              <th class="px-5 py-3"></th>
            </tr>
          </thead>
          <tbody class="divide-y divide-gray-100">
            <template v-for="c in contacts" :key="c.id">
              <tr
                class="hover:bg-gray-50 transition-colors cursor-pointer"
                @click="expanded === c.id ? expanded = null : expanded = c.id"
              >
                <td class="px-5 py-3 font-medium text-gray-900">{{ c.name }}</td>
                <td class="px-5 py-3">
                  <a :href="'mailto:' + c.email" class="text-primary-600 hover:underline" @click.stop>
                    {{ c.email }}
                  </a>
                </td>
                <td class="px-5 py-3 text-gray-500">{{ c.phone || '—' }}</td>
                <td class="px-5 py-3 text-gray-500 text-xs">{{ c.carId ? `#${c.carId}` : '—' }}</td>
                <td class="px-5 py-3 text-gray-400 text-xs">{{ formatDate(c.createdAt) }}</td>
                <td class="px-5 py-3 text-gray-400 text-xs">
                  <svg class="w-4 h-4 transition-transform" :class="expanded === c.id ? 'rotate-180' : ''"
                    fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"/>
                  </svg>
                </td>
              </tr>
              <!-- Expanded message row -->
              <tr v-if="expanded === c.id" class="bg-primary-50">
                <td colspan="6" class="px-5 py-4 text-sm text-gray-700">
                  <strong class="block mb-1 text-xs uppercase tracking-wide text-gray-400">Message</strong>
                  {{ c.message }}
                </td>
              </tr>
            </template>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getContactSubmissions } from '../../api'

const contacts = ref([])
const loading  = ref(true)
const expanded = ref(null)

onMounted(async () => {
  try {
    const res = await getContactSubmissions()
    contacts.value = res.data
  } finally {
    loading.value = false
  }
})

function formatDate(dt) {
  if (!dt) return '—'
  return new Date(dt).toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' })
}
</script>
