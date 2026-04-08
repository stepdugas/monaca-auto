<template>
  <header
    class="fixed top-0 left-0 right-0 z-50 transition-all duration-300"
    :class="scrolled ? 'bg-dark-900/95 backdrop-blur-sm shadow-lg' : 'bg-transparent'"
  >
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="flex items-center justify-between h-16 md:h-20">

        <!-- Logo / Dealership Name -->
        <RouterLink to="/" class="flex items-center gap-3 flex-shrink-0">
          <img
            v-if="siteSettings.logoUrl"
            :src="siteSettings.logoUrl"
            :alt="siteSettings.businessName + ' logo'"
            class="h-10 w-auto"
          />
          <span v-else class="text-white font-bold text-xl tracking-tight">
            {{ siteSettings.businessName }}
          </span>
        </RouterLink>

        <!-- Desktop nav -->
        <nav class="hidden md:flex items-center gap-1">
          <RouterLink
            v-for="link in navLinks"
            :key="link.to"
            :to="link.to"
            class="px-4 py-2 text-sm font-medium text-gray-300 hover:text-white rounded-lg
                   hover:bg-white/10 transition-colors duration-150"
            active-class="text-white bg-white/10"
          >
            {{ link.label }}
          </RouterLink>

          <!-- Primary CTA -->
          <RouterLink to="/inventory" class="btn-primary ml-4 text-sm py-2 px-5">
            Browse Inventory
          </RouterLink>
        </nav>

        <!-- Mobile menu toggle -->
        <button
          class="md:hidden p-2 text-gray-300 hover:text-white transition-colors"
          @click="mobileOpen = !mobileOpen"
          aria-label="Toggle menu"
        >
          <svg v-if="!mobileOpen" class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16"/>
          </svg>
          <svg v-else class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
          </svg>
        </button>
      </div>
    </div>

    <!-- Mobile nav drawer -->
    <Transition name="slide-down">
      <div
        v-if="mobileOpen"
        class="md:hidden bg-dark-900/98 backdrop-blur-sm border-t border-white/10 px-4 pb-6 pt-2"
      >
        <nav class="flex flex-col gap-1">
          <RouterLink
            v-for="link in navLinks"
            :key="link.to"
            :to="link.to"
            class="px-4 py-3 text-sm font-medium text-gray-300 hover:text-white
                   rounded-lg hover:bg-white/10 transition-colors"
            active-class="text-white bg-white/10"
            @click="mobileOpen = false"
          >
            {{ link.label }}
          </RouterLink>
          <RouterLink
            to="/inventory"
            class="btn-primary mt-3 text-center text-sm"
            @click="mobileOpen = false"
          >
            Browse Inventory
          </RouterLink>
        </nav>
      </div>
    </Transition>
  </header>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { RouterLink } from 'vue-router'
import { DEALERSHIP_NAME } from '../../config'
import { siteSettings } from '../../composables/useSiteSettings'

const scrolled = ref(false)
const mobileOpen = ref(false)

const navLinks = computed(() => {
  const links = [
    { label: 'Home',      to: '/' },
    { label: 'Inventory', to: '/inventory' },
    { label: 'About Us',  to: '/about' },
  ]
  if (siteSettings.showFinancing)       links.push({ label: 'Financing',         to: '/financing' })
  if (siteSettings.showStaff)           links.push({ label: 'Meet the Staff',     to: '/staff' })
  if (siteSettings.showScheduleService) links.push({ label: 'Schedule Service',   to: '/schedule-service' })
  links.push({ label: 'Contact', to: '/contact' })
  return links
})

function handleScroll() {
  scrolled.value = window.scrollY > 20
}

onMounted(() => window.addEventListener('scroll', handleScroll, { passive: true }))
onUnmounted(() => window.removeEventListener('scroll', handleScroll))
</script>

<style scoped>
.slide-down-enter-active,
.slide-down-leave-active {
  transition: all 0.25s ease;
}
.slide-down-enter-from,
.slide-down-leave-to {
  opacity: 0;
  transform: translateY(-8px);
}
</style>
