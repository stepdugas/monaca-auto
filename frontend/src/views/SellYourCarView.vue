<!--
  SellYourCarView — Sell Us Your Car scaffold page.
  [SCAFFOLD] Add trade-in appraisal form when ready.
-->
<template>
  <PageLayout>
    <div class="bg-dark-800 pt-28 pb-12">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h1 class="text-4xl font-extrabold text-white" data-aos="fade-up">Sell Us Your Car</h1>
        <p class="text-gray-400 mt-2" data-aos="fade-up" data-aos-delay="100">
          Get a fast, fair offer — no obligation.
        </p>
      </div>
    </div>

    <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-16 text-center" data-aos="fade-up">
      <div class="bg-gray-50 rounded-2xl p-12 border border-gray-200">
        <h2 class="text-2xl font-bold text-gray-900 mb-3">We Buy All Makes & Models</h2>
        <!-- [SELL_CAR_COPY] -->
        <p class="text-gray-600 leading-relaxed mb-8">
          [SELL_CAR_COPY] — e.g. Skip the hassle of private-party selling. Bring your vehicle in for a
          free appraisal and walk out with a check the same day.
        </p>
        <RouterLink to="/contact" class="btn-primary text-base px-10 py-4">
          Get Your Free Offer
        </RouterLink>
      </div>
    </div>
  </PageLayout>
</template>

<script setup>
import { RouterLink } from 'vue-router'
import PageLayout from '../components/layout/PageLayout.vue'
</script>
