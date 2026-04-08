<!--
  ManagerLoginView — Manager login page.
  Uses /api/manager/login and stores token as manager_token.
-->
<template>
  <div class="min-h-screen bg-dark-900 flex items-center justify-center px-4">
    <div class="w-full max-w-md">

      <div class="text-center mb-8">
        <h1 class="text-2xl font-bold text-white">{{ DEALERSHIP_NAME }}</h1>
        <p class="text-gray-400 text-sm mt-1">Manager Portal</p>
      </div>

      <div class="bg-dark-700 rounded-2xl p-8 shadow-2xl border border-white/10">
        <h2 class="text-xl font-bold text-white mb-6">Sign In</h2>

        <form @submit.prevent="login" class="space-y-5">
          <div>
            <label class="form-label text-gray-300">Username</label>
            <input
              v-model="form.username"
              class="form-input bg-dark-600 border-white/10 text-white placeholder-gray-500"
              placeholder="manager"
              autocomplete="username"
              required
            />
          </div>
          <div>
            <label class="form-label text-gray-300">Password</label>
            <input
              v-model="form.password"
              type="password"
              class="form-input bg-dark-600 border-white/10 text-white placeholder-gray-500"
              placeholder="••••••••"
              autocomplete="current-password"
              required
            />
          </div>

          <p v-if="errorMsg" class="text-red-400 text-sm">{{ errorMsg }}</p>

          <button type="submit" class="btn-primary w-full" :disabled="loading">
            {{ loading ? 'Signing in…' : 'Sign In' }}
          </button>
        </form>
      </div>

      <p class="text-center text-gray-600 text-xs mt-6">
        <RouterLink to="/" class="hover:text-gray-400 transition-colors">← Back to website</RouterLink>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter, RouterLink } from 'vue-router'
import { managerLogin } from '../../api'
import { DEALERSHIP_NAME } from '../../config'

const router   = useRouter()
const form     = ref({ username: '', password: '' })
const loading  = ref(false)
const errorMsg = ref('')

async function login() {
  loading.value  = true
  errorMsg.value = ''
  try {
    const res = await managerLogin(form.value)
    localStorage.setItem('manager_token', res.data.token)
    router.push({ name: 'ManagerDashboard' })
  } catch (err) {
    errorMsg.value = err.response?.status === 401
      ? 'Invalid username or password.'
      : 'Login failed. Please try again.'
  } finally {
    loading.value = false
  }
}
</script>
