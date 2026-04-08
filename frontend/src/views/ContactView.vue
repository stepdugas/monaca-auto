<!--
  ContactView — contact page with form, hours, address, and Google Maps embed.
-->
<template>
  <PageLayout>
    <!-- Header banner -->
    <div class="bg-dark-800 pt-28 pb-12">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h1 class="text-4xl font-extrabold text-white" data-aos="fade-up">Contact Us</h1>
        <p class="text-gray-400 mt-2" data-aos="fade-up" data-aos-delay="100">
          We'd love to hear from you — reach out any time.
        </p>
      </div>
    </div>

    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16">
      <div class="grid grid-cols-1 lg:grid-cols-5 gap-12">

        <!-- Contact form (3/5 cols) -->
        <div class="lg:col-span-3" data-aos="fade-right">
          <h2 class="text-2xl font-bold text-gray-900 mb-6">Send Us a Message</h2>

          <form @submit.prevent="submit" class="space-y-5">
            <div class="grid grid-cols-1 sm:grid-cols-2 gap-5">
              <div>
                <label class="form-label">First Name *</label>
                <input v-model="form.firstName" class="form-input" placeholder="Jane" required />
              </div>
              <div>
                <label class="form-label">Last Name *</label>
                <input v-model="form.lastName" class="form-input" placeholder="Smith" required />
              </div>
            </div>

            <div>
              <label class="form-label">Email Address *</label>
              <input v-model="form.email" type="email" class="form-input" placeholder="jane@example.com" required />
            </div>

            <div>
              <label class="form-label">Phone Number</label>
              <input v-model="form.phone" type="tel" class="form-input" placeholder="(555) 000-0000" />
            </div>

            <div>
              <label class="form-label">Subject</label>
              <select v-model="form.subject" class="form-input">
                <option value="">Select a subject…</option>
                <option>General Inquiry</option>
                <option>Vehicle Inquiry</option>
                <option>Financing</option>
                <option>Service / Repair</option>
                <option>Sell Us Your Car</option>
                <option>Other</option>
              </select>
            </div>

            <div>
              <label class="form-label">Message *</label>
              <textarea
                v-model="form.message"
                class="form-input resize-none"
                rows="5"
                placeholder="How can we help you?"
                required
              ></textarea>
            </div>

            <!-- Trade-in toggle -->
            <label class="flex items-center gap-3 cursor-pointer select-none">
              <input v-model="form.hasTradeIn" type="checkbox" class="w-4 h-4 accent-primary-600 cursor-pointer" />
              <span class="text-sm font-medium text-gray-700">I have a vehicle I'd like to trade in</span>
            </label>

            <!-- Trade-in fields -->
            <div v-if="form.hasTradeIn" class="bg-gray-50 rounded-xl p-5 space-y-4 border border-gray-200">
              <p class="text-sm font-semibold text-gray-700">Trade-In Vehicle</p>
              <div class="grid grid-cols-3 gap-3">
                <div>
                  <label class="form-label">Year</label>
                  <input v-model="form.tradeYear" class="form-input" placeholder="2018" />
                </div>
                <div>
                  <label class="form-label">Make</label>
                  <input v-model="form.tradeMake" class="form-input" placeholder="Ford" />
                </div>
                <div>
                  <label class="form-label">Model</label>
                  <input v-model="form.tradeModel" class="form-input" placeholder="F-150" />
                </div>
              </div>
              <div class="grid grid-cols-2 gap-3">
                <div>
                  <label class="form-label">Mileage</label>
                  <input v-model="form.tradeMileage" class="form-input" placeholder="e.g. 78,000" />
                </div>
                <div>
                  <label class="form-label">Condition</label>
                  <select v-model="form.tradeCondition" class="form-input">
                    <option value="">Select…</option>
                    <option>Excellent</option>
                    <option>Good</option>
                    <option>Fair</option>
                    <option>Poor</option>
                  </select>
                </div>
              </div>
            </div>

            <button type="submit" class="btn-primary w-full text-base py-4" :disabled="submitting">
              {{ submitting ? 'Sending…' : 'Send Message' }}
            </button>

            <p v-if="success" class="text-emerald-600 font-medium text-center">
              ✓ Message received! We'll reply within 1 business day.
            </p>
            <p v-if="errorMsg" class="text-red-500 text-sm text-center">{{ errorMsg }}</p>
          </form>
        </div>

        <!-- Info sidebar (2/5 cols) -->
        <div class="lg:col-span-2 space-y-8" data-aos="fade-left">

          <!-- Contact details card -->
          <div class="bg-gray-50 rounded-2xl p-7 space-y-5">
            <div class="flex items-start gap-4">
              <div class="w-10 h-10 rounded-xl bg-primary-100 flex items-center justify-center flex-shrink-0">
                <svg class="w-5 h-5 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z"/>
                </svg>
              </div>
              <div>
                <p class="text-sm text-gray-500 font-medium">Phone</p>
                <a :href="'tel:' + siteSettings.phone" class="text-lg font-bold text-gray-900 hover:text-primary-600 transition-colors">
                  {{ siteSettings.phone }}
                </a>
              </div>
            </div>

            <div class="flex items-start gap-4">
              <div class="w-10 h-10 rounded-xl bg-primary-100 flex items-center justify-center flex-shrink-0">
                <svg class="w-5 h-5 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"/>
                </svg>
              </div>
              <div>
                <p class="text-sm text-gray-500 font-medium">Email</p>
                <a :href="'mailto:' + siteSettings.email" class="text-gray-900 font-bold hover:text-primary-600 transition-colors">
                  {{ siteSettings.email }}
                </a>
              </div>
            </div>

            <div class="flex items-start gap-4">
              <div class="w-10 h-10 rounded-xl bg-primary-100 flex items-center justify-center flex-shrink-0">
                <svg class="w-5 h-5 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"/>
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"/>
                </svg>
              </div>
              <div>
                <p class="text-sm text-gray-500 font-medium">Address</p>
                <p class="text-gray-900 font-bold">{{ siteSettings.address }}<br />{{ siteSettings.cityStateZip }}</p>
              </div>
            </div>
          </div>

          <!-- Hours card -->
          <div class="bg-gray-50 rounded-2xl p-7">
            <h3 class="font-bold text-gray-900 mb-4">Business Hours</h3>
            <dl class="space-y-2 text-sm">
              <div v-for="(row, i) in hoursRows" :key="i" class="flex justify-between">
                <dt class="text-gray-500">{{ row.label }}</dt>
                <dd class="font-medium text-gray-900">{{ row.value }}</dd>
              </div>
            </dl>
          </div>

          <!-- Google Maps embed -->
          <div class="rounded-2xl overflow-hidden shadow-md" data-aos="fade-up">
            <!-- [GOOGLE_MAPS_EMBED_URL] — paste your embed iframe src from Google Maps -->
            <div
              v-if="siteSettings.googleMapsEmbedUrl"
              class="w-full h-56"
            >
              <iframe
                :src="siteSettings.googleMapsEmbedUrl"
                width="100%"
                height="100%"
                style="border:0"
                allowfullscreen
                loading="lazy"
                referrerpolicy="no-referrer-when-downgrade"
                title="Dealership location map"
              ></iframe>
            </div>
            <div
              v-else
              class="w-full h-56 bg-gray-200 flex items-center justify-center text-gray-400 text-sm"
            >
              Map — set Google Maps Embed URL in Admin → Settings
            </div>
          </div>
        </div>
      </div>
    </div>
  </PageLayout>
</template>

<script setup>
import { ref, computed } from 'vue'
import PageLayout from '../components/layout/PageLayout.vue'
import { submitContact } from '../api'
import { siteSettings } from '../composables/useSiteSettings'

const form = ref({
  firstName: '', lastName: '', email: '', phone: '', subject: '', message: '',
  hasTradeIn: false,
  tradeYear: '', tradeMake: '', tradeModel: '', tradeMileage: '', tradeCondition: '',
})
const submitting = ref(false)
const success    = ref(false)
const errorMsg   = ref('')

async function submit() {
  submitting.value = true
  success.value    = false
  errorMsg.value   = ''
  try {
    let message = `[${form.value.subject || 'General'}] ${form.value.message}`
    if (form.value.hasTradeIn) {
      message += `\n\nTrade-In: ${form.value.tradeYear} ${form.value.tradeMake} ${form.value.tradeModel}`.trim()
      if (form.value.tradeMileage)   message += ` | Mileage: ${form.value.tradeMileage}`
      if (form.value.tradeCondition) message += ` | Condition: ${form.value.tradeCondition}`
    }
    await submitContact({
      name:    `${form.value.firstName} ${form.value.lastName}`.trim(),
      email:   form.value.email,
      phone:   form.value.phone,
      message,
    })
    success.value = true
    form.value = {
      firstName: '', lastName: '', email: '', phone: '', subject: '', message: '',
      hasTradeIn: false,
      tradeYear: '', tradeMake: '', tradeModel: '', tradeMileage: '', tradeCondition: '',
    }
  } catch {
    errorMsg.value = 'Something went wrong. Please call us directly or try again.'
  } finally {
    submitting.value = false
  }
}

const hoursRows = computed(() => [
  { label: 'Monday',    value: siteSettings.hours.monday },
  { label: 'Tuesday',   value: siteSettings.hours.tuesday },
  { label: 'Wednesday', value: siteSettings.hours.wednesday },
  { label: 'Thursday',  value: siteSettings.hours.thursday },
  { label: 'Friday',    value: siteSettings.hours.friday },
  { label: 'Saturday',  value: siteSettings.hours.saturday },
  { label: 'Sunday',    value: siteSettings.hours.sunday },
])
</script>
