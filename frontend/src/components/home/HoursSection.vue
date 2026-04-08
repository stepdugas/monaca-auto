<!--
  HoursSection — business hours + address strip, displayed on homepage.
-->
<template>
  <section class="py-16 bg-primary-600">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="grid grid-cols-1 lg:grid-cols-3 gap-10 text-white">

        <!-- Hours -->
        <div data-aos="fade-up" data-aos-delay="0">
          <h3 class="text-lg font-bold uppercase tracking-wider mb-5 opacity-80">Business Hours</h3>
          <dl class="space-y-2 text-sm">
            <div v-for="(row, i) in hoursRows" :key="i" class="flex justify-between gap-6">
              <dt class="opacity-70">{{ row.label }}</dt>
              <dd class="font-medium">{{ row.value }}</dd>
            </div>
          </dl>
        </div>

        <!-- Location -->
        <div data-aos="fade-up" data-aos-delay="100">
          <h3 class="text-lg font-bold uppercase tracking-wider mb-5 opacity-80">Find Us</h3>
          <address class="not-italic text-sm leading-loose opacity-90">
            {{ siteSettings.address }}<br />
            {{ siteSettings.cityStateZip }}
          </address>
          <a
            :href="'https://www.google.com/maps/search/?api=1&query=' + encodeURIComponent(siteSettings.address + ' ' + siteSettings.cityStateZip)"
            target="_blank"
            rel="noopener noreferrer"
            class="inline-block mt-4 text-sm underline underline-offset-2 hover:opacity-75 transition-opacity"
          >
            Get Directions →
          </a>
        </div>

        <!-- Contact CTA -->
        <div data-aos="fade-up" data-aos-delay="200" class="flex flex-col justify-center">
          <h3 class="text-lg font-bold uppercase tracking-wider mb-5 opacity-80">Get In Touch</h3>
          <a
            :href="'tel:' + siteSettings.phone"
            class="text-3xl font-extrabold tracking-tight hover:opacity-75 transition-opacity"
          >
            {{ siteSettings.phone }}
          </a>
          <p class="text-sm mt-2 opacity-70">Call or text anytime during business hours</p>
          <RouterLink
            to="/contact"
            class="mt-6 inline-flex items-center gap-2 bg-white text-primary-600
                   font-semibold text-sm px-6 py-3 rounded-lg hover:bg-primary-50 transition-colors w-fit"
          >
            Send a Message
          </RouterLink>
        </div>

      </div>
    </div>
  </section>
</template>

<script setup>
import { computed } from 'vue'
import { RouterLink } from 'vue-router'
import { siteSettings } from '../../composables/useSiteSettings'

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
