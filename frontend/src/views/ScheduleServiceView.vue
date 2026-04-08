<template>
  <PageLayout>

    <!-- Hero -->
    <div class="bg-dark-800 pt-28 pb-14">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h1 class="text-4xl font-extrabold text-white" data-aos="fade-up">
          Schedule a Service Appointment
        </h1>
        <p class="text-gray-300 mt-4 max-w-2xl text-lg leading-relaxed" data-aos="fade-up" data-aos-delay="100">
          {{ siteSettings.scheduleServiceBlurb || 'Our service team is ready to help keep your vehicle running its best. Book an appointment below or give us a call.' }}
        </p>
        <div class="mt-8 flex flex-wrap gap-4" data-aos="fade-up" data-aos-delay="200">
          <a
            v-if="siteSettings.scheduleCalendarUrl"
            :href="siteSettings.scheduleCalendarUrl"
            target="_blank"
            rel="noopener noreferrer"
            class="btn-primary text-base px-8 py-3"
          >
            Book Online
          </a>
          <a v-else href="#schedule-form" class="btn-primary text-base px-8 py-3">
            Request an Appointment
          </a>
          <a :href="'tel:' + siteSettings.phone" class="btn-secondary text-base px-8 py-3">
            Call Us — {{ siteSettings.phone }}
          </a>
        </div>
      </div>
    </div>

    <!-- What to Expect -->
    <section class="py-16 bg-white">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h2 class="text-2xl font-bold text-gray-900 mb-10 text-center" data-aos="fade-up">
          What to Expect
        </h2>
        <div class="grid md:grid-cols-3 gap-8">
          <div
            v-for="step in steps"
            :key="step.title"
            class="text-center p-6 rounded-2xl border border-gray-100 shadow-sm"
            data-aos="fade-up"
          >
            <div class="w-12 h-12 bg-primary-100 rounded-xl flex items-center justify-center mx-auto mb-4">
              <component :is="step.icon" class="w-6 h-6 text-primary-600" />
            </div>
            <h3 class="font-bold text-gray-900 mb-2">{{ step.title }}</h3>
            <p class="text-gray-500 text-sm leading-relaxed">{{ step.description }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- Main content: calendar link OR built-in form -->
    <section id="schedule-form" class="py-16 bg-gray-50">
      <div class="max-w-2xl mx-auto px-4 sm:px-6 lg:px-8">

        <!-- External calendar link CTA -->
        <div v-if="siteSettings.scheduleCalendarUrl" class="text-center" data-aos="fade-up">
          <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-10">
            <h2 class="text-2xl font-bold text-gray-900 mb-3">Ready to Book?</h2>
            <p class="text-gray-500 mb-8 text-sm">Click below to choose your appointment time. It only takes a minute.</p>
            <a
              :href="siteSettings.scheduleCalendarUrl"
              target="_blank"
              rel="noopener noreferrer"
              class="btn-primary text-base px-10 py-4 inline-block"
            >
              Open Booking Calendar
            </a>
            <p class="text-xs text-gray-400 mt-6">Powered by your scheduling provider</p>
          </div>
        </div>

        <!-- Built-in request form -->
        <div v-else data-aos="fade-up">
          <h2 class="text-2xl font-bold text-gray-900 mb-2 text-center">Request an Appointment</h2>
          <p class="text-gray-500 text-center mb-8 text-sm">Fill out the form below and we'll reach out to confirm your time.</p>

          <!-- Success state -->
          <div v-if="submitSuccess" class="bg-green-50 border border-green-200 rounded-2xl p-8 text-center">
            <p class="text-2xl mb-2">✓</p>
            <p class="font-semibold text-green-800">Request Received!</p>
            <p class="text-green-700 text-sm mt-1">We'll contact you shortly to confirm your appointment time.</p>
          </div>

          <!-- Form -->
          <form v-else @submit.prevent="submitRequest" class="bg-white rounded-2xl shadow-sm border border-gray-100 p-8 space-y-6">

            <!-- Customer info -->
            <div class="space-y-4">
              <div>
                <label class="form-label">Full Name *</label>
                <input v-model="serviceForm.name" type="text" class="form-input" placeholder="Jane Smith" required />
              </div>
              <div class="grid sm:grid-cols-2 gap-4">
                <div>
                  <label class="form-label">Email *</label>
                  <input v-model="serviceForm.email" type="email" class="form-input" placeholder="jane@email.com" required />
                </div>
                <div>
                  <label class="form-label">Phone *</label>
                  <input v-model="serviceForm.phone" type="tel" class="form-input" placeholder="(555) 123-4567" required />
                </div>
              </div>
              <div>
                <label class="form-label">Service Type *</label>
                <select v-model="serviceForm.serviceType" class="form-input" required>
                  <option value="">Select a service…</option>
                  <option>Oil Change</option>
                  <option>Tire Rotation / Balance</option>
                  <option>Brake Inspection or Service</option>
                  <option>Battery Check / Replacement</option>
                  <option>AC / Heat Service</option>
                  <option>Engine or Transmission</option>
                  <option>Full Inspection</option>
                  <option>Detailing</option>
                  <option>Other</option>
                </select>
              </div>
              <div v-if="showOther">
                <label class="form-label">Please describe *</label>
                <input v-model="serviceForm.serviceTypeOther" type="text" class="form-input" placeholder="Describe the service needed" required />
              </div>
              <div>
                <label class="form-label">Additional Notes <span class="text-gray-400 font-normal">(optional)</span></label>
                <textarea v-model="serviceForm.description" class="form-input resize-none" rows="3"
                  placeholder="Any other details we should know before your appointment"></textarea>
              </div>
            </div>

            <!-- Vehicle info section -->
            <div v-if="siteSettings.scheduleShowVehicleInfo" class="border-t border-gray-100 pt-5">
              <p class="text-sm font-semibold text-gray-700 border-b border-gray-100 pb-2 mb-4">Your Vehicle</p>
              <div class="space-y-4">
                <div class="grid grid-cols-3 gap-3">
                  <div>
                    <label class="form-label">Year</label>
                    <input v-model="serviceForm.vehicleYear" type="text" class="form-input" placeholder="2019" />
                  </div>
                  <div>
                    <label class="form-label">Make</label>
                    <input v-model="serviceForm.vehicleMake" type="text" class="form-input" placeholder="Toyota" />
                  </div>
                  <div>
                    <label class="form-label">Model</label>
                    <input v-model="serviceForm.vehicleModel" type="text" class="form-input" placeholder="Camry" />
                  </div>
                </div>
                <div>
                  <label class="form-label">Approximate Mileage</label>
                  <input v-model="serviceForm.vehicleMileage" type="text" class="form-input" placeholder="e.g. 65,000" />
                </div>
                <div>
                  <label class="form-label">VIN <span class="text-gray-400 font-normal">(optional — helps us look up service history)</span></label>
                  <input v-model="serviceForm.vehicleVin" type="text" class="form-input" placeholder="e.g. 1HGCM82633A123456" />
                </div>
              </div>
            </div>

            <!-- Appointment preference section -->
            <div v-if="siteSettings.scheduleShowPreferredTime" class="border-t border-gray-100 pt-5">
              <p class="text-sm font-semibold text-gray-700 border-b border-gray-100 pb-2 mb-4">Appointment Preference</p>
              <div class="grid sm:grid-cols-2 gap-4">
                <div>
                  <label class="form-label">Preferred Date</label>
                  <input v-model="serviceForm.preferredDate" type="date" class="form-input" />
                </div>
                <div>
                  <label class="form-label">Preferred Time</label>
                  <select v-model="serviceForm.preferredTime" class="form-input">
                    <option value="">Select…</option>
                    <option>Morning (8am–12pm)</option>
                    <option>Afternoon (12pm–5pm)</option>
                    <option>I'm Flexible</option>
                  </select>
                </div>
              </div>
            </div>

            <!-- Referral source -->
            <div v-if="siteSettings.scheduleShowReferral" class="border-t border-gray-100 pt-5">
              <label class="form-label">How did you hear about us?</label>
              <select v-model="serviceForm.referralSource" class="form-input">
                <option value="">Select…</option>
                <option>Google</option>
                <option>Facebook</option>
                <option>Word of Mouth</option>
                <option>Drive By</option>
                <option>Returning Customer</option>
                <option>Other</option>
              </select>
            </div>

            <p v-if="submitError" class="text-red-500 text-sm">{{ submitError }}</p>

            <button type="submit" :disabled="submitting" class="btn-primary w-full py-3">
              {{ submitting ? 'Submitting…' : 'Submit Request' }}
            </button>
            <p class="text-xs text-gray-400 text-center">
              This submits a request — not a confirmed appointment. We'll contact you to confirm your time.
            </p>
          </form>
        </div>

      </div>
    </section>

  </PageLayout>
</template>

<script setup>
import { ref, computed } from 'vue'
import axios from 'axios'
import PageLayout from '../components/layout/PageLayout.vue'
import { siteSettings } from '../composables/useSiteSettings'
import { API_BASE_URL } from '../config'

// ── Form state ────────────────────────────────────────────────────────────
const serviceForm = ref({
  name: '',
  email: '',
  phone: '',
  serviceType: '',
  serviceTypeOther: '',
  description: '',
  vehicleYear: '',
  vehicleMake: '',
  vehicleModel: '',
  vehicleMileage: '',
  vehicleVin: '',
  preferredDate: '',
  preferredTime: '',
  referralSource: '',
})

const submitting    = ref(false)
const submitSuccess = ref(false)
const submitError   = ref('')

const showOther = computed(() => serviceForm.value.serviceType === 'Other')

async function submitRequest() {
  submitting.value  = true
  submitError.value = ''
  try {
    // Build a flat payload — if "Other" was selected, use the custom text
    const payload = {
      ...serviceForm.value,
      serviceType: showOther.value
        ? serviceForm.value.serviceTypeOther || 'Other'
        : serviceForm.value.serviceType,
    }
    await axios.post(`${API_BASE_URL}/api/schedule-service`, payload)
    submitSuccess.value = true
  } catch {
    submitError.value = 'Failed to submit. Please call us directly or try again.'
  } finally {
    submitting.value = false
  }
}

// ── Static content ────────────────────────────────────────────────────────
const steps = [
  {
    title: 'Book Your Time',
    description: 'Choose a date and time that works for you.',
    icon: CalendarIcon,
  },
  {
    title: 'Drop Off Your Vehicle',
    description: 'Bring your car in at your scheduled time. Our team will check you in quickly.',
    icon: KeyIcon,
  },
  {
    title: 'We Handle the Rest',
    description: "We'll call or text you with updates and when your vehicle is ready.",
    icon: WrenchIcon,
  },
]

const CalendarIcon = {
  template: `<svg fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"/></svg>`,
}
const KeyIcon = {
  template: `<svg fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 7a2 2 0 012 2m4 0a6 6 0 01-7.743 5.743L11 17H9v2H7v2H4a1 1 0 01-1-1v-2.586a1 1 0 01.293-.707l5.964-5.964A6 6 0 1121 9z"/></svg>`,
}
const WrenchIcon = {
  template: `<svg fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z"/><circle cx="12" cy="12" r="3" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/></svg>`,
}
</script>
