<!--
  WhyUsSection — 3-column icon/text grid highlighting dealership selling points.
  All copy is placeholder — customise per client.
-->
<template>
  <section class="py-20 bg-white">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">

      <div class="text-center mb-14" data-aos="fade-up">
        <h2 class="section-title">Why Choose Us?</h2>
        <p class="section-subtitle mx-auto">
          <!-- [WHY_US_SUBTITLE] -->
          We make buying a car simple, transparent, and stress-free.
        </p>
      </div>

      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-8">
        <div
          v-for="(item, i) in reasons"
          :key="item.title"
          class="text-center group"
          data-aos="fade-up"
          :data-aos-delay="i * 100"
        >
          <!-- Icon circle -->
          <div class="w-16 h-16 rounded-2xl bg-primary-50 flex items-center justify-center mx-auto mb-5
                      group-hover:bg-primary-500 transition-colors duration-300">
            <component :is="item.icon" class="w-8 h-8 text-primary-500 group-hover:text-white transition-colors duration-300" />
          </div>
          <h3 class="text-lg font-bold text-gray-900 mb-2">{{ item.title }}</h3>
          <p class="text-sm text-gray-500 leading-relaxed">{{ item.description }}</p>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { h } from 'vue'

// Inline SVG icon components
const IconShield = {
  render: () => h('svg', { fill: 'none', stroke: 'currentColor', viewBox: '0 0 24 24', class: 'w-8 h-8' }, [
    h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke-width': '2',
      d: 'M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z' })
  ])
}
const IconTag = {
  render: () => h('svg', { fill: 'none', stroke: 'currentColor', viewBox: '0 0 24 24', class: 'w-8 h-8' }, [
    h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke-width': '2',
      d: 'M7 7h.01M7 3h5c.512 0 1.024.195 1.414.586l7 7a2 2 0 010 2.828l-7 7a2 2 0 01-2.828 0l-7-7A1.994 1.994 0 013 12V7a4 4 0 014-4z' })
  ])
}
const IconStar = {
  render: () => h('svg', { fill: 'none', stroke: 'currentColor', viewBox: '0 0 24 24', class: 'w-8 h-8' }, [
    h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke-width': '2',
      d: 'M11.049 2.927c.3-.921 1.603-.921 1.902 0l1.519 4.674a1 1 0 00.95.69h4.915c.969 0 1.371 1.24.588 1.81l-3.976 2.888a1 1 0 00-.363 1.118l1.518 4.674c.3.922-.755 1.688-1.538 1.118l-3.976-2.888a1 1 0 00-1.176 0l-3.976 2.888c-.783.57-1.838-.197-1.538-1.118l1.518-4.674a1 1 0 00-.363-1.118l-3.976-2.888c-.784-.57-.38-1.81.588-1.81h4.914a1 1 0 00.951-.69l1.519-4.674z' })
  ])
}
const IconCash = {
  render: () => h('svg', { fill: 'none', stroke: 'currentColor', viewBox: '0 0 24 24', class: 'w-8 h-8' }, [
    h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke-width': '2',
      d: 'M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z' })
  ])
}

// [WHY_US_REASONS] — customise per client
const reasons = [
  {
    icon: IconShield,
    title: 'Certified Quality',
    description: 'Every vehicle undergoes a rigorous multi-point inspection before hitting our lot.',
  },
  {
    icon: IconTag,
    title: 'Transparent Pricing',
    description: 'No hidden fees. The price you see is the price you pay — guaranteed.',
  },
  {
    icon: IconStar,
    title: '5-Star Service',
    description: 'Rated #1 in customer satisfaction by our local community for 5 years running.',
  },
  {
    icon: IconCash,
    title: 'Flexible Financing',
    description: 'We work with all credit situations to get you behind the wheel today.',
  },
]
</script>
