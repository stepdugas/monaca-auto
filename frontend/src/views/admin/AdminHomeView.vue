<template>
  <div>
    <div class="flex items-center justify-between mb-8">
      <div>
        <h1 class="text-2xl font-bold text-gray-900">Dashboard</h1>
        <p class="text-sm text-gray-500 mt-1">Welcome back — here's a quick look at your site.</p>
      </div>
      <RouterLink
        to="/admin/cars/new"
        class="px-4 py-2 bg-blue-600 text-white text-sm font-semibold rounded-lg hover:bg-blue-700 transition"
      >
        + Add Vehicle
      </RouterLink>
    </div>

    <!-- Stats cards -->
    <div v-if="loading" class="grid grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
      <div v-for="i in 4" :key="i" class="bg-white rounded-xl border border-gray-200 p-5 animate-pulse">
        <div class="h-4 bg-gray-200 rounded w-2/3 mb-3"></div>
        <div class="h-8 bg-gray-200 rounded w-1/3"></div>
      </div>
    </div>

    <div v-else class="grid grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
      <RouterLink
        to="/admin/cars"
        class="bg-white rounded-xl border border-gray-200 p-5 hover:border-blue-300 hover:shadow-sm transition group"
      >
        <p class="text-xs font-semibold text-gray-500 uppercase tracking-wider mb-2">Available</p>
        <p class="text-3xl font-extrabold text-green-600">{{ stats.carsAvailable ?? '—' }}</p>
        <p class="text-xs text-gray-400 mt-1 group-hover:text-blue-500 transition">View inventory →</p>
      </RouterLink>

      <RouterLink
        to="/admin/cars"
        class="bg-white rounded-xl border border-gray-200 p-5 hover:border-blue-300 hover:shadow-sm transition group"
      >
        <p class="text-xs font-semibold text-gray-500 uppercase tracking-wider mb-2">Pending / Reserved</p>
        <p class="text-3xl font-extrabold text-yellow-500">{{ stats.carsPending ?? '—' }}</p>
        <p class="text-xs text-gray-400 mt-1 group-hover:text-blue-500 transition">View inventory →</p>
      </RouterLink>

      <RouterLink
        to="/admin/cars"
        class="bg-white rounded-xl border border-gray-200 p-5 hover:border-blue-300 hover:shadow-sm transition group"
      >
        <p class="text-xs font-semibold text-gray-500 uppercase tracking-wider mb-2">Sold</p>
        <p class="text-3xl font-extrabold text-gray-400">{{ stats.carsSold ?? '—' }}</p>
        <p class="text-xs text-gray-400 mt-1 group-hover:text-blue-500 transition">View inventory →</p>
      </RouterLink>

      <RouterLink
        to="/admin/contacts"
        class="bg-white rounded-xl border border-gray-200 p-5 hover:border-blue-300 hover:shadow-sm transition group"
      >
        <p class="text-xs font-semibold text-gray-500 uppercase tracking-wider mb-2">Total Leads</p>
        <p class="text-3xl font-extrabold text-blue-600">{{ stats.totalContacts ?? '—' }}</p>
        <p class="text-xs text-gray-400 mt-1 group-hover:text-blue-500 transition">View all →</p>
      </RouterLink>
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">

      <!-- Recent contacts -->
      <div class="bg-white rounded-xl border border-gray-200 p-6">
        <div class="flex items-center justify-between mb-4">
          <h2 class="font-semibold text-gray-900">Recent Leads</h2>
          <RouterLink to="/admin/contacts" class="text-xs text-blue-600 hover:underline">View all</RouterLink>
        </div>

        <div v-if="loading" class="space-y-3">
          <div v-for="i in 3" :key="i" class="h-14 bg-gray-100 rounded-lg animate-pulse"></div>
        </div>

        <div v-else-if="!stats.recentContacts?.length" class="text-sm text-gray-400 py-6 text-center">
          No leads yet — they'll appear here when customers reach out.
        </div>

        <ul v-else class="divide-y divide-gray-100">
          <li
            v-for="contact in stats.recentContacts"
            :key="contact.id"
            class="py-3 flex items-start justify-between gap-3"
          >
            <div class="min-w-0">
              <p class="text-sm font-medium text-gray-900 truncate">{{ contact.name }}</p>
              <p class="text-xs text-gray-500 truncate">{{ contact.email }}</p>
              <p v-if="contact.message" class="text-xs text-gray-400 mt-0.5 truncate">{{ contact.message.slice(0, 60) }}{{ contact.message.length > 60 ? '…' : '' }}</p>
            </div>
            <span class="text-xs text-gray-400 whitespace-nowrap flex-shrink-0 pt-0.5">{{ formatDate(contact.createdAt) }}</span>
          </li>
        </ul>
      </div>

      <!-- Quick links -->
      <div class="bg-white rounded-xl border border-gray-200 p-6">
        <h2 class="font-semibold text-gray-900 mb-4">Quick Actions</h2>
        <div class="space-y-2">
          <RouterLink
            v-for="action in quickActions"
            :key="action.to"
            :to="action.to"
            class="flex items-center gap-3 p-3 rounded-lg hover:bg-gray-50 transition group"
          >
            <div class="w-8 h-8 rounded-lg bg-blue-50 flex items-center justify-center flex-shrink-0 group-hover:bg-blue-100 transition">
              <component :is="action.icon" class="w-4 h-4 text-blue-600" />
            </div>
            <div>
              <p class="text-sm font-medium text-gray-800">{{ action.label }}</p>
              <p class="text-xs text-gray-400">{{ action.description }}</p>
            </div>
            <svg class="w-4 h-4 text-gray-300 ml-auto group-hover:text-blue-400 transition" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/>
            </svg>
          </RouterLink>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, h } from 'vue'
import { RouterLink } from 'vue-router'
import { adminGetDashboard } from '../../api/index'

const loading = ref(true)
const stats   = ref({})

onMounted(async () => {
  try {
    const res = await adminGetDashboard()
    stats.value = res.data
  } catch {
    // backend offline — show empty state
  } finally {
    loading.value = false
  }
})

function formatDate(iso) {
  if (!iso) return ''
  return new Date(iso).toLocaleDateString('en-US', { month: 'short', day: 'numeric' })
}

// ── Quick action icons ─────────────────────────────────────────────────
const PlusIcon    = { render: () => h('svg', { fill: 'none', stroke: 'currentColor', viewBox: '0 0 24 24' }, [h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke-width': '2', d: 'M12 4v16m8-8H4' })]) }
const CarIcon     = { render: () => h('svg', { fill: 'none', stroke: 'currentColor', viewBox: '0 0 24 24' }, [h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke-width': '2', d: 'M9 17a2 2 0 11-4 0 2 2 0 014 0zM19 17a2 2 0 11-4 0 2 2 0 014 0zM13 16V6a1 1 0 00-1-1H4a1 1 0 00-1 1v10l2 1h8zM13 16l2 1h4l2-1v-5l-3.5-5.5H13v5z' })]) }
const MailIcon    = { render: () => h('svg', { fill: 'none', stroke: 'currentColor', viewBox: '0 0 24 24' }, [h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke-width': '2', d: 'M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z' })]) }
const SettingsIcon = { render: () => h('svg', { fill: 'none', stroke: 'currentColor', viewBox: '0 0 24 24' }, [h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke-width': '2', d: 'M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z' }), h('circle', { cx: '12', cy: '12', r: '3', 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke-width': '2' })]) }
const quickActions = [
  { label: 'Add a Vehicle',    description: 'List a new car in your inventory',          to: '/admin/cars/new',  icon: PlusIcon },
  { label: 'Manage Inventory', description: 'Edit, mark sold, or remove listings',       to: '/admin/cars',      icon: CarIcon },
  { label: 'View Leads',       description: 'See all customer contact submissions',      to: '/admin/contacts',  icon: MailIcon },
  { label: 'Site Settings',    description: 'Update hours, logo, pages, and more',       to: '/admin/settings',  icon: SettingsIcon },
]
</script>
