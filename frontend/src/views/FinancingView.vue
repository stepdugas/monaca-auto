<template>
  <PageLayout>

    <!-- Hero -->
    <div class="bg-dark-800 pt-28 pb-14">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h1 class="text-4xl font-extrabold text-white" data-aos="fade-up">
          Flexible Financing for Every Driver
        </h1>
        <p class="text-gray-300 mt-4 max-w-2xl text-lg leading-relaxed" data-aos="fade-up" data-aos-delay="100">
          {{ siteSettings.financingBlurb || 'We work with lenders to find the best rate for your situation — all credit types welcome.' }}
        </p>
        <div class="mt-8 flex flex-wrap gap-4" data-aos="fade-up" data-aos-delay="200">
          <a
            v-if="siteSettings.financingApplyUrl"
            :href="siteSettings.financingApplyUrl"
            target="_blank"
            rel="noopener noreferrer"
            class="btn-primary text-base px-8 py-3"
          >
            Apply Now
          </a>
          <a v-else href="#apply" class="btn-primary text-base px-8 py-3">
            Start Your Application
          </a>
          <a :href="'tel:' + siteSettings.phone" class="btn-secondary text-base px-8 py-3">
            Call Us — {{ siteSettings.phone }}
          </a>
        </div>
      </div>
    </div>

    <!-- Why finance with us -->
    <section class="py-16 bg-white">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h2 class="text-2xl font-bold text-gray-900 mb-10 text-center" data-aos="fade-up">
          Why Finance With Us?
        </h2>
        <div class="grid md:grid-cols-3 gap-8">
          <div
            v-for="benefit in benefits"
            :key="benefit.title"
            class="text-center p-6 rounded-2xl border border-gray-100 shadow-sm"
            data-aos="fade-up"
          >
            <div class="w-12 h-12 bg-primary-100 rounded-xl flex items-center justify-center mx-auto mb-4">
              <component :is="benefit.icon" class="w-6 h-6 text-primary-600" />
            </div>
            <h3 class="font-bold text-gray-900 mb-2">{{ benefit.title }}</h3>
            <p class="text-gray-500 text-sm leading-relaxed">{{ benefit.description }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- Payment calculator -->
    <section class="py-16 bg-gray-50">
      <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
        <h2 class="text-2xl font-bold text-gray-900 mb-2 text-center" data-aos="fade-up">
          Payment Estimator
        </h2>
        <p class="text-gray-500 text-center mb-10 text-sm" data-aos="fade-up" data-aos-delay="50">
          Estimate your monthly payment. Actual rates depend on credit and lender approval.
        </p>
        <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-8" data-aos="fade-up">
          <div class="grid sm:grid-cols-2 gap-6 mb-6">
            <div>
              <label class="form-label">Vehicle Price ($)</label>
              <input v-model.number="calc.price" type="number" min="0" step="500" class="form-input" placeholder="25000" />
            </div>
            <div>
              <label class="form-label">Down Payment ($)</label>
              <input v-model.number="calc.down" type="number" min="0" step="500" class="form-input" placeholder="3000" />
            </div>
            <div>
              <label class="form-label">Annual Interest Rate (%)</label>
              <input v-model.number="calc.rate" type="number" min="0" max="30" step="0.1" class="form-input" placeholder="6.9" />
            </div>
            <div>
              <label class="form-label">Loan Term</label>
              <select v-model.number="calc.term" class="form-input">
                <option :value="24">24 months (2 yr)</option>
                <option :value="36">36 months (3 yr)</option>
                <option :value="48">48 months (4 yr)</option>
                <option :value="60">60 months (5 yr)</option>
                <option :value="72">72 months (6 yr)</option>
                <option :value="84">84 months (7 yr)</option>
              </select>
            </div>
          </div>
          <div class="bg-primary-50 rounded-xl p-6 text-center border border-primary-100">
            <p class="text-sm text-gray-500 mb-1">Estimated Monthly Payment</p>
            <p class="text-4xl font-extrabold text-primary-700">
              {{ monthlyPayment !== null ? '$' + monthlyPayment.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 }) : '—' }}
            </p>
            <p class="text-xs text-gray-400 mt-2">
              Loan amount: {{ loanAmount > 0 ? '$' + loanAmount.toLocaleString() : '—' }}
              &nbsp;|&nbsp;
              Total paid: {{ totalPaid > 0 ? '$' + totalPaid.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 }) : '—' }}
            </p>
          </div>
          <p class="text-xs text-gray-400 mt-4 text-center">
            This calculator is for estimation purposes only. Taxes, fees, and final rates vary.
          </p>
        </div>
      </div>
    </section>

    <!-- Understanding your loan (FAQ) -->
    <section class="py-16 bg-white">
      <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
        <h2 class="text-2xl font-bold text-gray-900 mb-10 text-center" data-aos="fade-up">
          Understanding Your Loan
        </h2>
        <div class="space-y-4">
          <details
            v-for="item in loanTerms"
            :key="item.term"
            class="bg-gray-50 rounded-2xl border border-gray-100 overflow-hidden"
            data-aos="fade-up"
          >
            <summary class="cursor-pointer px-6 py-4 font-semibold text-gray-900 flex items-center justify-between select-none hover:bg-gray-100 transition-colors">
              {{ item.term }}
              <svg class="w-4 h-4 text-gray-400 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"/>
              </svg>
            </summary>
            <div class="px-6 pb-5 text-gray-600 text-sm leading-relaxed border-t border-gray-200 pt-4">
              {{ item.explanation }}
            </div>
          </details>
        </div>
      </div>
    </section>

    <!-- Apply section — external link OR built-in form -->
    <section id="apply" class="py-16 bg-gray-50">
      <div class="max-w-2xl mx-auto px-4 sm:px-6 lg:px-8">

        <!-- External link CTA -->
        <div v-if="siteSettings.financingApplyUrl" class="text-center" data-aos="fade-up">
          <h2 class="text-3xl font-extrabold text-gray-900 mb-4">Ready to Apply?</h2>
          <p class="text-gray-500 mb-8">Click below to start your application. It only takes a few minutes.</p>
          <a
            :href="siteSettings.financingApplyUrl"
            target="_blank"
            rel="noopener noreferrer"
            class="btn-primary text-base px-10 py-4"
          >
            Start Your Application
          </a>
        </div>

        <!-- Built-in application form -->
        <div v-else data-aos="fade-up">
          <h2 class="text-2xl font-bold text-gray-900 mb-2 text-center">Start Your Application</h2>
          <p class="text-gray-500 text-center mb-8 text-sm">Fill out the short form below and we'll be in touch to discuss your options.</p>

          <div v-if="submitSuccess" class="bg-green-50 border border-green-200 rounded-2xl p-8 text-center">
            <p class="text-2xl mb-2">✓</p>
            <p class="font-semibold text-green-800">Application Received!</p>
            <p class="text-green-700 text-sm mt-1">We'll reach out to you shortly to go over your options.</p>
          </div>

          <form v-else @submit.prevent="submitApplication" class="bg-white rounded-2xl shadow-sm border border-gray-100 p-8 space-y-5">
            <div class="grid sm:grid-cols-2 gap-5">
              <div class="sm:col-span-2">
                <label class="form-label">Full Name *</label>
                <input v-model="appForm.name" type="text" class="form-input" placeholder="Jane Smith" required />
              </div>
              <div>
                <label class="form-label">Email *</label>
                <input v-model="appForm.email" type="email" class="form-input" placeholder="jane@email.com" required />
              </div>
              <div>
                <label class="form-label">Phone</label>
                <input v-model="appForm.phone" type="tel" class="form-input" placeholder="(555) 123-4567" />
              </div>
              <div>
                <label class="form-label">Employment Status</label>
                <select v-model="appForm.employment" class="form-input">
                  <option value="">Select…</option>
                  <option>Employed Full-Time</option>
                  <option>Employed Part-Time</option>
                  <option>Self-Employed</option>
                  <option>Retired</option>
                  <option>Other</option>
                </select>
              </div>
              <div>
                <label class="form-label">Approximate Monthly Income</label>
                <select v-model="appForm.monthlyIncome" class="form-input">
                  <option value="">Prefer not to say</option>
                  <option>Under $1,500</option>
                  <option>$1,500 – $2,500</option>
                  <option>$2,500 – $4,000</option>
                  <option>$4,000 – $6,000</option>
                  <option>Over $6,000</option>
                </select>
              </div>
              <div class="sm:col-span-2">
                <label class="form-label">Additional Notes <span class="text-gray-400 font-normal">(optional)</span></label>
                <textarea v-model="appForm.notes" class="form-input resize-none" rows="3"
                  placeholder="Any details about the vehicle you're interested in, trade-in, credit situation, etc."></textarea>
              </div>
            </div>
            <p v-if="submitError" class="text-red-500 text-sm">{{ submitError }}</p>
            <button type="submit" :disabled="submitting" class="btn-primary w-full py-3">
              {{ submitting ? 'Submitting…' : 'Submit Application' }}
            </button>
            <p class="text-xs text-gray-400 text-center">
              This is not a credit check. We'll contact you to discuss next steps.
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

// ── Payment calculator ───────────────────────────────────────────────────
const calc = ref({ price: 25000, down: 3000, rate: 6.9, term: 60 })

const loanAmount = computed(() => Math.max(0, (calc.value.price || 0) - (calc.value.down || 0)))

const monthlyPayment = computed(() => {
  const P = loanAmount.value
  const r = (calc.value.rate || 0) / 100 / 12
  const n = calc.value.term || 60
  if (P <= 0) return 0
  if (r === 0) return P / n
  return (P * r * Math.pow(1 + r, n)) / (Math.pow(1 + r, n) - 1)
})

const totalPaid = computed(() =>
  monthlyPayment.value > 0 ? monthlyPayment.value * calc.value.term : 0
)

// ── Built-in application form ────────────────────────────────────────────
const appForm = ref({ name: '', email: '', phone: '', employment: '', monthlyIncome: '', notes: '' })
const submitting   = ref(false)
const submitSuccess = ref(false)
const submitError   = ref('')

async function submitApplication() {
  submitting.value  = true
  submitError.value = ''
  try {
    await axios.post(`${API_BASE_URL}/api/financing-application`, appForm.value)
    submitSuccess.value = true
  } catch {
    submitError.value = 'Failed to submit. Please call us directly or try again.'
  } finally {
    submitting.value = false
  }
}

// ── Static content ───────────────────────────────────────────────────────
const benefits = [
  {
    title: 'All Credit Situations',
    description: 'First-time buyer, rebuilding credit, or excellent score — we have options for every situation.',
    icon: CheckIcon,
  },
  {
    title: 'Quick Pre-Approval',
    description: 'Get a decision fast. No long waits or confusing paperwork.',
    icon: ClockIcon,
  },
  {
    title: 'Competitive Rates',
    description: 'We work with lenders on your behalf to find the lowest rate available.',
    icon: DollarIcon,
  },
]

const loanTerms = [
  {
    term: 'APR (Annual Percentage Rate)',
    explanation: 'APR is the yearly cost of borrowing, including interest and fees. A lower APR means you pay less over the life of the loan.',
  },
  {
    term: 'Down Payment',
    explanation: 'The amount you pay upfront. A larger down payment reduces your loan amount, lowers your monthly payment, and can help you qualify for better rates.',
  },
  {
    term: 'Loan Term',
    explanation: 'The length of your loan in months. Shorter terms mean higher monthly payments but less total interest. Longer terms lower your payment but increase total cost.',
  },
  {
    term: 'Credit Score Impact',
    explanation: 'Your credit score is one of the biggest factors in your rate. Scores above 700 typically qualify for the best rates. We also work with buyers rebuilding credit.',
  },
  {
    term: 'Pre-Approval vs. Pre-Qualification',
    explanation: 'Pre-qualification is a quick estimate. Pre-approval involves a credit check and gives you a firmer commitment from a lender — stronger when shopping for a car.',
  },
]

const CheckIcon = {
  template: `<svg fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"/></svg>`,
}
const ClockIcon = {
  template: `<svg fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"/></svg>`,
}
const DollarIcon = {
  template: `<svg fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/></svg>`,
}
</script>
