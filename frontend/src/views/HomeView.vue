<!--
  HomeView — landing page.
  Sections: Hero → Featured Inventory → Why Us → Testimonials (if enabled) → Hours
-->
<template>
  <PageLayout>
    <HeroSection />
    <FeaturedInventory />
    <WhyUsSection />
    <TestimonialsSection v-if="siteSettings.reviews_enabled === 'true' && reviews.length" :reviews="reviews" />
    <HoursSection />
  </PageLayout>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import PageLayout          from '../components/layout/PageLayout.vue'
import HeroSection         from '../components/home/HeroSection.vue'
import FeaturedInventory   from '../components/home/FeaturedInventory.vue'
import WhyUsSection        from '../components/home/WhyUsSection.vue'
import TestimonialsSection from '../components/home/TestimonialsSection.vue'
import HoursSection        from '../components/home/HoursSection.vue'
import { siteSettings }    from '../composables/useSiteSettings'
import { usePageMeta }     from '../composables/usePageMeta'
import { getReviews }      from '../api'

usePageMeta({
  description: 'Monaca Auto Sales — quality used cars, trucks, and SUVs at 303 9th Street, Monaca, PA. Serving Beaver County and the greater Pittsburgh area. Financing available.',
  path:        '/',
})

const reviews = ref([])

onMounted(async () => {
  try {
    const res = await getReviews()
    reviews.value = res.data
  } catch {
    // reviews unavailable — section stays hidden
  }
})
</script>
