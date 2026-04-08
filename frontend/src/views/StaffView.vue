<!--
  StaffView — Public "Meet the Team" page.
  Fetches live staff data from /api/public/staff.
-->
<template>
  <PageLayout>
    <!-- Hero banner -->
    <div class="bg-dark-800 pt-28 pb-12">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h1 class="text-4xl font-extrabold text-white" data-aos="fade-up">Meet the Team</h1>
        <p class="text-gray-400 mt-2" data-aos="fade-up" data-aos-delay="100">
          The friendly faces behind {{ DEALERSHIP_NAME }}.
        </p>
      </div>
    </div>

    <!-- Staff grid -->
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16">

      <!-- Loading skeleton -->
      <div v-if="loading" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-8">
        <div v-for="n in 6" :key="n" class="bg-white rounded-2xl p-6 border border-gray-100 animate-pulse">
          <div class="w-24 h-24 rounded-full bg-gray-200 mx-auto mb-4"></div>
          <div class="h-4 bg-gray-200 rounded w-3/4 mx-auto mb-2"></div>
          <div class="h-3 bg-gray-100 rounded w-1/2 mx-auto"></div>
        </div>
      </div>

      <!-- Staff cards -->
      <div
        v-else-if="staff.length"
        class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-8"
      >
        <div
          v-for="(member, i) in staff"
          :key="member.id"
          class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6 text-center"
          data-aos="fade-up"
          :data-aos-delay="i * 80"
        >
          <!-- Photo or placeholder avatar -->
          <div class="w-24 h-24 mx-auto mb-4 rounded-full bg-gray-100 overflow-hidden flex items-center justify-center border border-gray-200">
            <img
              v-if="member.photoUrl"
              :src="member.photoUrl"
              :alt="member.name"
              class="w-full h-full object-cover"
            />
            <svg
              v-else
              class="w-12 h-12 text-gray-400"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="1.5"
                d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"
              />
            </svg>
          </div>

          <!-- Name -->
          <h3 class="font-bold text-gray-900 text-lg">{{ member.name }}</h3>

          <!-- Title -->
          <p v-if="member.title" class="text-primary-600 text-sm font-medium mt-0.5">
            {{ member.title }}
          </p>

          <!-- Bio -->
          <p v-if="member.bio" class="text-gray-500 text-sm mt-3 leading-relaxed">
            {{ member.bio }}
          </p>

          <!-- Contact links -->
          <div v-if="member.phone || member.email" class="mt-4 flex flex-col items-center gap-1.5">
            <a
              v-if="member.phone"
              :href="`tel:${member.phone}`"
              class="inline-flex items-center gap-1.5 text-sm text-gray-600 hover:text-primary-600 transition-colors"
            >
              <!-- Phone icon -->
              <svg class="w-4 h-4 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z"/>
              </svg>
              {{ member.phone }}
            </a>
            <a
              v-if="member.email"
              :href="`mailto:${member.email}`"
              class="inline-flex items-center gap-1.5 text-sm text-gray-600 hover:text-primary-600 transition-colors"
            >
              <!-- Mail icon -->
              <svg class="w-4 h-4 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"/>
              </svg>
              {{ member.email }}
            </a>
          </div>
        </div>
      </div>

      <!-- Empty state -->
      <div v-else class="text-center py-24 text-gray-400">
        <svg class="w-16 h-16 mx-auto mb-4 text-gray-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"
            d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0z"/>
        </svg>
        <p class="text-xl font-semibold text-gray-700">Coming Soon</p>
        <p class="mt-2 text-gray-500">We're getting our team page ready. Check back soon!</p>
      </div>
    </div>
  </PageLayout>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import PageLayout from '../components/layout/PageLayout.vue'
import { DEALERSHIP_NAME, API_BASE_URL } from '../config'

const staff   = ref([])
const loading = ref(true)

async function fetchStaff() {
  try {
    const res = await axios.get(`${API_BASE_URL}/api/public/staff`)
    staff.value = res.data
  } catch (err) {
    console.error('Failed to load staff:', err)
  } finally {
    loading.value = false
  }
}

onMounted(fetchStaff)
</script>
