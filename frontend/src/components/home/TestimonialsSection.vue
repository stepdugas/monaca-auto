<!--
  TestimonialsSection — customer review cards.
  [TESTIMONIALS] — replace with real reviews per client.
-->
<template>
  <section class="py-20 bg-dark-800">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">

      <div class="text-center mb-14" data-aos="fade-up">
        <h2 class="section-title text-white">What Our Customers Say</h2>
        <p class="section-subtitle mx-auto text-gray-400">
          Don't take our word for it — hear it from our happy drivers.
        </p>
      </div>

      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
        <div
          v-for="(t, i) in testimonials"
          :key="t.name"
          class="bg-dark-700 rounded-2xl p-8 flex flex-col gap-5 border border-white/5
                 hover:border-primary-500/30 transition-colors duration-300"
          data-aos="fade-up"
          :data-aos-delay="i * 100"
        >
          <!-- Stars -->
          <div class="flex gap-1">
            <svg
              v-for="s in 5"
              :key="s"
              class="w-5 h-5"
              :class="s <= t.rating ? 'text-amber-400' : 'text-gray-600'"
              fill="currentColor"
              viewBox="0 0 24 24"
            >
              <path d="M11.049 2.927c.3-.921 1.603-.921 1.902 0l1.519 4.674a1 1 0 00.95.69h4.915c.969 0 1.371 1.24.588 1.81l-3.976 2.888a1 1 0 00-.363 1.118l1.518 4.674c.3.922-.755 1.688-1.538 1.118l-3.976-2.888a1 1 0 00-1.176 0l-3.976 2.888c-.783.57-1.838-.197-1.538-1.118l1.518-4.674a1 1 0 00-.363-1.118l-3.976-2.888c-.784-.57-.38-1.81.588-1.81h4.914a1 1 0 00.951-.69l1.519-4.674z"/>
            </svg>
          </div>

          <!-- Quote -->
          <p class="text-gray-300 text-sm leading-relaxed flex-1">
            "{{ t.body }}"
          </p>

          <!-- Reviewer -->
          <div class="flex items-center gap-3 border-t border-white/10 pt-5">
            <!-- Avatar circle with initials -->
            <div class="w-10 h-10 rounded-full bg-primary-500/20 flex items-center justify-center flex-shrink-0">
              <span class="text-primary-400 font-bold text-sm">{{ initials(t.name) }}</span>
            </div>
            <div>
              <p class="text-white font-semibold text-sm">{{ t.name }}</p>
              <p class="text-gray-500 text-xs">{{ t.vehicle }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
// [TESTIMONIALS] — replace with real customer reviews
const testimonials = [
  {
    name: '[CUSTOMER_NAME_1]',
    vehicle: '[VEHICLE_PURCHASED_1]',
    rating: 5,
    body: '[TESTIMONIAL_BODY_1] — e.g. "Absolutely the best car-buying experience I have ever had. The team was honest, helpful, and made the whole process effortless."',
  },
  {
    name: '[CUSTOMER_NAME_2]',
    vehicle: '[VEHICLE_PURCHASED_2]',
    rating: 5,
    body: '[TESTIMONIAL_BODY_2] — e.g. "I was nervous about financing but they found me a great rate the same day. Drove off the lot in my dream car!"',
  },
  {
    name: '[CUSTOMER_NAME_3]',
    vehicle: '[VEHICLE_PURCHASED_3]',
    rating: 5,
    body: '[TESTIMONIAL_BODY_3] — e.g. "No pressure, no gimmicks. Just straightforward service. I have already recommended them to three friends."',
  },
]

function initials(name) {
  if (!name || name.startsWith('[')) return '?'
  return name.split(' ').map(n => n[0]).join('').slice(0, 2).toUpperCase()
}
</script>
