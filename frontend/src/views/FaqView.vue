<!--
  FaqView — FAQ accordion scaffold page.
  [SCAFFOLD] Replace placeholder Q&A with real content.
-->
<template>
  <PageLayout>
    <div class="bg-dark-800 pt-28 pb-12">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h1 class="text-4xl font-extrabold text-white" data-aos="fade-up">Frequently Asked Questions</h1>
      </div>
    </div>

    <div class="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-16 space-y-4">
      <div
        v-for="(faq, i) in faqs"
        :key="i"
        class="border border-gray-200 rounded-xl overflow-hidden"
        data-aos="fade-up"
        :data-aos-delay="i * 60"
      >
        <button
          class="w-full flex items-center justify-between px-6 py-4 text-left font-semibold text-gray-900
                 hover:bg-gray-50 transition-colors"
          @click="open === i ? open = null : open = i"
        >
          {{ faq.q }}
          <svg class="w-5 h-5 text-gray-400 flex-shrink-0 transition-transform" :class="open === i ? 'rotate-180' : ''"
            fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"/>
          </svg>
        </button>
        <div v-show="open === i" class="px-6 pb-5 text-gray-600 text-sm leading-relaxed border-t border-gray-100">
          {{ faq.a }}
        </div>
      </div>
    </div>
  </PageLayout>
</template>

<script setup>
import { ref } from 'vue'
import PageLayout from '../components/layout/PageLayout.vue'

const open = ref(null)

// [FAQ_ITEMS] — replace with real Q&A
const faqs = [
  { q: '[FAQ_QUESTION_1]', a: '[FAQ_ANSWER_1]' },
  { q: '[FAQ_QUESTION_2]', a: '[FAQ_ANSWER_2]' },
  { q: '[FAQ_QUESTION_3]', a: '[FAQ_ANSWER_3]' },
  { q: '[FAQ_QUESTION_4]', a: '[FAQ_ANSWER_4]' },
  { q: '[FAQ_QUESTION_5]', a: '[FAQ_ANSWER_5]' },
]
</script>
