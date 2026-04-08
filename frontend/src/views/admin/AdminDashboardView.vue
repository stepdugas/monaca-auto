<!--
  AdminDashboardView — shell layout for all admin sub-pages.
  Contains the sidebar nav and renders child routes via <RouterView>.
-->
<template>
  <div class="min-h-screen bg-gray-100 flex flex-col md:flex-row">

    <!-- Sidebar -->
    <aside
      class="w-full md:w-64 bg-dark-900 text-white flex flex-col flex-shrink-0"
      :class="sidebarOpen ? 'block' : 'hidden md:flex'"
    >
      <!-- Sidebar header -->
      <div class="flex items-center justify-between px-6 py-5 border-b border-white/10">
        <RouterLink to="/" class="font-bold text-lg">{{ siteSettings.businessName }}</RouterLink>
        <button class="md:hidden text-gray-400 hover:text-white" @click="sidebarOpen = false">
          <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
          </svg>
        </button>
      </div>

      <!-- Nav links -->
      <nav class="flex-1 px-4 py-6 space-y-1">
        <RouterLink
          v-for="link in navLinks"
          :key="link.to"
          :to="link.to"
          class="flex items-center gap-3 px-4 py-2.5 rounded-lg text-sm font-medium
                 text-gray-300 hover:text-white hover:bg-white/10 transition-colors"
          active-class="bg-primary-600 text-white"
        >
          <component :is="link.icon" class="w-5 h-5" />
          {{ link.label }}
        </RouterLink>
      </nav>

      <!-- Sign out -->
      <div class="px-4 pb-6 border-t border-white/10 pt-4">
        <button
          class="flex items-center gap-3 px-4 py-2.5 w-full rounded-lg text-sm font-medium
                 text-gray-400 hover:text-white hover:bg-red-500/20 transition-colors"
          @click="signOut"
        >
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
              d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"/>
          </svg>
          Sign Out
        </button>
      </div>
    </aside>

    <!-- Main area -->
    <div class="flex-1 flex flex-col min-w-0">
      <!-- Mobile topbar -->
      <header class="md:hidden bg-dark-900 text-white flex items-center justify-between px-4 py-3">
        <button @click="sidebarOpen = true" class="p-2">
          <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16"/>
          </svg>
        </button>
        <span class="font-semibold">Admin Panel</span>
        <div class="w-10"></div>
      </header>

      <!-- Page content -->
      <main class="flex-1 p-6 overflow-auto">
        <RouterView v-slot="{ Component }">
          <Transition name="admin-fade" mode="out-in">
            <component :is="Component" :key="$route.path" />
          </Transition>
        </RouterView>
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, h } from 'vue'
import { RouterLink, RouterView, useRouter, useRoute } from 'vue-router'
import { siteSettings } from '../../composables/useSiteSettings'

const route = useRoute()

const router     = useRouter()
const sidebarOpen = ref(false)

// Simple SVG icon wrappers for sidebar nav
const CarIcon = { render: () => h('svg', { fill: 'none', stroke: 'currentColor', viewBox: '0 0 24 24' }, [
  h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke-width': '2',
    d: 'M9 17a2 2 0 11-4 0 2 2 0 014 0zM19 17a2 2 0 11-4 0 2 2 0 014 0z'}),
  h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke-width': '2',
    d: 'M13 16V6a1 1 0 00-1-1H4a1 1 0 00-1 1v10l2 1h8zM13 16l2 1h4l2-1v-5l-3.5-5.5H13v5z'}),
]) }

const MailIcon = { render: () => h('svg', { fill: 'none', stroke: 'currentColor', viewBox: '0 0 24 24' }, [
  h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke-width': '2',
    d: 'M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z'}),
]) }

const SettingsIcon = { render: () => h('svg', { fill: 'none', stroke: 'currentColor', viewBox: '0 0 24 24' }, [
  h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke-width': '2',
    d: 'M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z'}),
  h('circle', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke-width': '2', cx: '12', cy: '12', r: '3' }),
]) }

const HomeIcon = { render: () => h('svg', { fill: 'none', stroke: 'currentColor', viewBox: '0 0 24 24' }, [
  h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke-width': '2',
    d: 'M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6'}),
]) }

const navLinks = [
  { label: 'Dashboard', to: '/admin/home',     icon: HomeIcon },
  { label: 'Inventory', to: '/admin/cars',     icon: CarIcon },
  { label: 'Contacts',  to: '/admin/contacts', icon: MailIcon },
  { label: 'Settings',  to: '/admin/settings', icon: SettingsIcon },
]

function signOut() {
  localStorage.removeItem('admin_token')
  router.push({ name: 'AdminLogin' })
}
</script>

<style scoped>
.admin-fade-enter-active,
.admin-fade-leave-active {
  transition: opacity 0.18s ease, transform 0.18s ease;
}
.admin-fade-enter-from {
  opacity: 0;
  transform: translateY(6px);
}
.admin-fade-leave-to {
  opacity: 0;
  transform: translateY(-4px);
}
</style>
