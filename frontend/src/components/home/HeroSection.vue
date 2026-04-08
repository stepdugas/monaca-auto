<!--
  HeroSection — full-viewport dark hero with background image, headline,
  and CTA buttons. Background image uses Unsplash placeholder.
-->
<template>
  <section class="relative min-h-screen flex items-center justify-center overflow-hidden">

    <!-- Background image — set by admin via hero image picker -->
    <div class="absolute inset-0 z-0">
      <img
        :src="siteSettings.heroUrl"
        alt="Dealership"
        class="w-full h-full object-cover object-center"
      />
      <!-- Dark gradient overlay for text legibility -->
      <div class="absolute inset-0 bg-gradient-to-r from-dark-900/90 via-dark-900/70 to-dark-900/40"></div>
    </div>

    <!-- Hero content -->
    <div class="relative z-10 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 w-full pt-20">
      <div class="max-w-2xl">
        <!-- Eyebrow text -->
        <p
          class="text-primary-400 font-semibold text-sm uppercase tracking-widest mb-4"
          data-aos="fade-up"
          data-aos-delay="0"
        >
          Welcome to {{ siteSettings.businessName }}
        </p>

        <!-- Main headline — [TAGLINE] placeholder -->
        <h1
          class="text-4xl sm:text-5xl lg:text-6xl font-extrabold text-white leading-tight mb-6"
          data-aos="fade-up"
          data-aos-delay="100"
        >
          {{ siteSettings.tagline }}
        </h1>

        <!-- Sub-copy -->
        <p
          class="text-gray-300 text-lg sm:text-xl leading-relaxed mb-10"
          data-aos="fade-up"
          data-aos-delay="200"
        >
          Quality pre-owned and new vehicles. Transparent pricing, no-pressure sales.
          <!-- [HERO_SUBTEXT] — customise per client -->
        </p>

        <!-- CTA buttons -->
        <div
          class="flex flex-wrap gap-4"
          data-aos="fade-up"
          data-aos-delay="300"
        >
          <RouterLink to="/inventory" class="btn-primary text-base px-8 py-4">
            Browse Inventory
          </RouterLink>
          <RouterLink to="/contact" class="btn-outline text-base px-8 py-4 border-white text-white hover:bg-white hover:text-dark-900">
            Contact Us
          </RouterLink>
        </div>

        <!-- Quick contact bar -->
        <div
          class="flex flex-wrap items-center gap-6 mt-12 text-sm text-gray-300"
          data-aos="fade-up"
          data-aos-delay="400"
        >
          <a :href="'tel:' + siteSettings.phone" class="flex items-center gap-2 hover:text-white transition-colors">
            <svg class="w-4 h-4 text-primary-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z"/>
            </svg>
            {{ siteSettings.phone }}
          </a>
          <span class="flex items-center gap-2">
            <svg class="w-4 h-4 text-primary-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"/>
            </svg>
            Open Today: {{ todayHours }}
          </span>
        </div>
      </div>
    </div>

    <!-- Scroll indicator -->
    <div class="absolute bottom-8 left-1/2 -translate-x-1/2 z-10 animate-bounce">
      <svg class="w-6 h-6 text-white/50" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"/>
      </svg>
    </div>
  </section>
</template>

<script setup>
import { computed } from 'vue'
import { RouterLink } from 'vue-router'
import { siteSettings } from '../../composables/useSiteSettings'

// Show today's hours in the hero quick-contact bar
const todayHours = computed(() => {
  const days = ['sunday','monday','tuesday','wednesday','thursday','friday','saturday']
  const today = days[new Date().getDay()]
  return siteSettings.hours[today] || 'Call for hours'
})
</script>
