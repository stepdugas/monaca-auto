<!--
  HomeView — landing page.
  Sections: Hero → Featured Inventory → Why Us → Testimonials → Hours
-->
<template>
  <PageLayout>
    <HeroSection />
    <FeaturedInventory />
    <WhyUsSection />
    <TestimonialsSection />
    <HoursSection />
  </PageLayout>
</template>

<script setup>
import PageLayout        from '../components/layout/PageLayout.vue'
import HeroSection       from '../components/home/HeroSection.vue'
import FeaturedInventory from '../components/home/FeaturedInventory.vue'
import WhyUsSection      from '../components/home/WhyUsSection.vue'
import TestimonialsSection from '../components/home/TestimonialsSection.vue'
import HoursSection      from '../components/home/HoursSection.vue'
</script>
