<!--
  AboutView — About Us page.
  All content pulled from siteSettings (admin-configurable).
-->
<template>
  <PageLayout>
    <!-- Hero banner -->
    <div class="relative bg-dark-800 pt-28 pb-20 overflow-hidden">
      <div class="absolute inset-0 opacity-20">
        <img
          src="https://images.unsplash.com/photo-1560958089-b8a1929cea89?auto=format&fit=crop&w=1920&q=60"
          alt=""
          class="w-full h-full object-cover"
        />
      </div>
      <div class="relative max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <p class="text-primary-400 text-sm font-semibold uppercase tracking-widest mb-3" data-aos="fade-up">Our Story</p>
        <h1 class="text-4xl md:text-5xl font-extrabold text-white" data-aos="fade-up" data-aos-delay="100">
          About {{ siteSettings.businessName }}
        </h1>
        <p v-if="siteSettings.aboutYearFounded" class="text-gray-300 text-lg mt-4 max-w-2xl" data-aos="fade-up" data-aos-delay="200">
          Serving the community with integrity and passion since {{ siteSettings.aboutYearFounded }}.
        </p>
      </div>
    </div>

    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20">

      <!-- Story + photo -->
      <div class="grid grid-cols-1 lg:grid-cols-2 gap-16 items-center">

        <!-- Story text -->
        <div data-aos="fade-right">
          <h2 class="section-title mb-6">Who We Are</h2>

          <template v-if="siteSettings.aboutBlurb">
            <p
              v-for="(para, i) in blurbParagraphs"
              :key="i"
              class="text-gray-600 leading-relaxed mb-5"
            >{{ para }}</p>
          </template>
          <template v-else>
            <p class="text-gray-400 italic leading-relaxed">
              Add your dealership's story in Admin → Settings → About Us Page.
            </p>
          </template>
        </div>

        <!-- Photo -->
        <div data-aos="fade-left">
          <div class="rounded-2xl overflow-hidden shadow-2xl">
            <img
              src="https://images.unsplash.com/photo-1568605117036-5fe5e7bab0b7?auto=format&fit=crop&w=900&q=80"
              alt="Dealership showroom"
              class="w-full h-96 object-cover"
            />
          </div>
        </div>
      </div>

      <!-- Stats row — only shown if at least one stat is filled in -->
      <div v-if="hasStats" class="grid grid-cols-2 md:grid-cols-4 gap-8 mt-20">
        <div
          v-for="(stat, i) in visibleStats"
          :key="stat.label"
          class="text-center"
          data-aos="fade-up"
          :data-aos-delay="i * 100"
        >
          <p class="text-4xl font-extrabold text-primary-600">{{ stat.value }}</p>
          <p class="text-gray-500 text-sm mt-2">{{ stat.label }}</p>
        </div>
      </div>

      <!-- Mission statement — only shown if filled in -->
      <div v-if="siteSettings.aboutMission" class="mt-20 bg-dark-800 rounded-2xl p-10 text-center" data-aos="fade-up">
        <h2 class="text-2xl font-bold text-white mb-4">Our Mission</h2>
        <p class="text-gray-300 text-lg leading-relaxed max-w-3xl mx-auto">
          "{{ siteSettings.aboutMission }}"
        </p>
      </div>

      <!-- CTA -->
      <div class="mt-20 text-center" data-aos="fade-up">
        <h2 class="text-2xl font-bold text-gray-900 mb-4">Ready to Find Your Next Vehicle?</h2>
        <p class="text-gray-500 mb-8 max-w-xl mx-auto">
          Browse our current inventory or reach out — we'd love to help you find the right car at the right price.
        </p>
        <div class="flex flex-wrap justify-center gap-4">
          <RouterLink to="/inventory" class="btn-primary px-8 py-3 text-base">Browse Inventory</RouterLink>
          <RouterLink to="/contact" class="btn-secondary px-8 py-3 text-base">Contact Us</RouterLink>
        </div>
      </div>

    </div>
  </PageLayout>
</template>

<script setup>
import { computed } from 'vue'
import { RouterLink } from 'vue-router'
import PageLayout from '../components/layout/PageLayout.vue'
import { siteSettings } from '../composables/useSiteSettings'

// Split blurb by double newlines (or single) into paragraphs
const blurbParagraphs = computed(() => {
  if (!siteSettings.aboutBlurb) return []
  return siteSettings.aboutBlurb
    .split(/\n\n+/)
    .map(p => p.trim())
    .filter(Boolean)
})

const allStats = computed(() => [
  { label: 'Years in Business', value: siteSettings.aboutStatYears },
  { label: 'Vehicles Sold',     value: siteSettings.aboutStatVehicles },
  { label: '5-Star Reviews',    value: siteSettings.aboutStatReviews },
  { label: 'Team Members',      value: siteSettings.aboutStatTeam },
])

const visibleStats = computed(() => allStats.value.filter(s => s.value))
const hasStats     = computed(() => visibleStats.value.length > 0)
</script>
