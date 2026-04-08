<!--
  AdminLoginView — Admin login page.
  Default credentials are set in application.properties — change before going to production.
-->
<template>
  <div class="min-h-screen bg-dark-800 flex items-center justify-center px-4">
    <div class="w-full max-w-md">

      <!-- Brand header -->
      <div class="text-center mb-8">
        <div class="w-12 h-12 rounded-xl bg-primary-600 flex items-center justify-center mx-auto mb-4">
          <svg class="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
              d="M9 17a2 2 0 11-4 0 2 2 0 014 0zM19 17a2 2 0 11-4 0 2 2 0 014 0zM13 16V6a1 1 0 00-1-1H4a1 1 0 00-1 1v10l2 1h8zM13 16l2 1h4l2-1v-5l-3.5-5.5H13v5z"/>
          </svg>
        </div>
        <h1 class="text-xl font-bold text-white">{{ siteSettings.businessName || 'Your Dealership' }}</h1>
        <p class="text-white/40 text-sm mt-1">Admin Dashboard</p>
      </div>

      <div class="bg-dark-700 rounded-2xl p-8 shadow-2xl border border-white/10">
        <h2 class="text-lg font-semibold text-white mb-6">Sign In</h2>

        <form @submit.prevent="login" class="space-y-5">
          <div>
            <label class="block text-sm font-medium text-white/60 mb-1.5">Username</label>
            <input
              v-model="form.username"
              class="w-full px-4 py-3 bg-dark-600 border border-white/10 rounded-xl text-white placeholder-white/30
                     focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent transition text-sm"
              placeholder="Enter username"
              autocomplete="username"
              required
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-white/60 mb-1.5">Password</label>
            <input
              v-model="form.password"
              type="password"
              class="w-full px-4 py-3 bg-dark-600 border border-white/10 rounded-xl text-white placeholder-white/30
                     focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent transition text-sm"
              placeholder="••••••••"
              autocomplete="current-password"
              required
            />
          </div>

          <p v-if="errorMsg" class="text-red-400 text-sm bg-red-500/10 border border-red-500/20 rounded-lg px-3 py-2">
            {{ errorMsg }}
          </p>

          <button
            type="submit"
            class="w-full py-3 bg-primary-600 hover:bg-primary-700 text-white font-semibold rounded-xl
                   transition focus:outline-none focus:ring-2 focus:ring-primary-400 focus:ring-offset-2 focus:ring-offset-dark-700"
            :disabled="loading"
          >
            {{ loading ? 'Signing in…' : 'Sign In' }}
          </button>
        </form>
      </div>

      <p class="text-center text-white/20 text-xs mt-6">
        <RouterLink to="/" class="hover:text-white/50 transition-colors">← Back to website</RouterLink>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter, RouterLink } from 'vue-router'
import { adminLogin } from '../../api'
import { siteSettings } from '../../composables/useSiteSettings'

const router   = useRouter()
const form     = ref({ username: '', password: '' })
const loading  = ref(false)
const errorMsg = ref('')

async function login() {
  loading.value  = true
  errorMsg.value = ''
  try {
    const res = await adminLogin(form.value)
    localStorage.setItem('admin_token', res.data.token)
    router.push({ name: 'AdminHome' })
  } catch (err) {
    errorMsg.value = err.response?.status === 401
      ? 'Invalid username or password.'
      : 'Login failed. Please try again.'
  } finally {
    loading.value = false
  }
}
</script>
