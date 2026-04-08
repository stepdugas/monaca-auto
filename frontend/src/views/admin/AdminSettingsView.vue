<template>
  <div class="max-w-3xl mx-auto">

    <!-- Header -->
    <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3 mb-6">
      <h1 class="text-2xl font-bold text-gray-900">Site Settings</h1>
      <button
        v-if="activeTab !== 'password'"
        @click="save"
        :disabled="saving"
        class="self-start sm:self-auto px-6 py-2 bg-blue-600 text-white font-semibold rounded-lg hover:bg-blue-700
               disabled:opacity-50 disabled:cursor-not-allowed transition"
      >{{ saving ? 'Saving...' : 'Save Changes' }}</button>
    </div>

    <!-- Success / Error -->
    <div v-if="successMsg" class="mb-4 p-4 bg-green-50 border border-green-200 rounded-lg text-green-800 text-sm font-medium">✓ {{ successMsg }}</div>
    <div v-if="errorMsg"   class="mb-4 p-4 bg-red-50 border border-red-200 rounded-lg text-red-800 text-sm font-medium">{{ errorMsg }}</div>

    <div v-if="loading" class="text-center py-20 text-gray-400">Loading settings...</div>

    <div v-else>
      <!-- Tab Bar -->
      <div class="flex overflow-x-auto gap-1 mb-6 pb-1 border-b border-gray-200 scrollbar-hide">
        <button
          v-for="tab in tabs"
          :key="tab.key"
          @click="activeTab = tab.key"
          class="px-4 py-2 text-sm font-medium rounded-t-lg whitespace-nowrap transition flex-shrink-0"
          :class="activeTab === tab.key
            ? 'bg-blue-600 text-white'
            : 'text-gray-600 hover:text-gray-900 hover:bg-gray-100'"
        >{{ tab.label }}</button>
      </div>

      <!-- ── BUSINESS INFO ── -->
      <div v-show="activeTab === 'business'" class="space-y-6">
        <section class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
          <h2 class="text-base font-semibold text-gray-900 mb-5 pb-3 border-b border-gray-100">Business Information</h2>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div class="md:col-span-2">
              <label class="block text-sm font-medium text-gray-700 mb-1">Business Name</label>
              <input v-model="form.business_name" type="text" class="field" placeholder="e.g. Monaca Auto Sales" />
            </div>
            <div class="md:col-span-2">
              <label class="block text-sm font-medium text-gray-700 mb-1">Tagline</label>
              <input v-model="form.tagline" type="text" class="field" placeholder="e.g. Quality Cars, Honest Deals" />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Phone Number</label>
              <input v-model="form.phone" type="tel" class="field" placeholder="(555) 123-4567" />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Email Address</label>
              <input v-model="form.email" type="email" class="field" placeholder="contact@yourdealership.com" />
            </div>
            <div class="md:col-span-2">
              <label class="block text-sm font-medium text-gray-700 mb-1">Street Address</label>
              <input v-model="form.address" type="text" class="field" placeholder="123 Main Street" />
            </div>
            <div class="md:col-span-2">
              <label class="block text-sm font-medium text-gray-700 mb-1">City, State ZIP</label>
              <input v-model="form.city_state_zip" type="text" class="field" placeholder="Monaca, PA 15061" />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Facebook URL</label>
              <input v-model="form.facebook_url" type="url" class="field" placeholder="https://facebook.com/yourpage" />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Instagram URL</label>
              <input v-model="form.instagram_url" type="url" class="field" placeholder="https://instagram.com/yourpage" />
            </div>
            <div class="md:col-span-2 border-t border-gray-100 pt-4">
              <label class="block text-sm font-medium text-gray-700 mb-1">Logo <span class="text-gray-400 font-normal">(optional — shown in header & footer)</span></label>
              <div class="flex gap-3 items-start">
                <input v-model="form.logo_url" type="url" class="field flex-1" placeholder="Paste image URL or upload below" />
                <button type="button" @click="$refs.logoFileInput.click()" :disabled="logoUploading"
                  class="px-4 py-2 text-sm border border-gray-300 rounded-lg bg-white hover:bg-gray-50 whitespace-nowrap disabled:opacity-50">
                  {{ logoUploading ? 'Uploading…' : 'Upload' }}
                </button>
                <input ref="logoFileInput" type="file" accept="image/*" class="hidden" @change="uploadLogo" />
              </div>
              <div v-if="form.logo_url" class="mt-3 flex items-center gap-3">
                <img :src="form.logo_url" alt="Logo preview" class="h-12 w-auto rounded border border-gray-200 bg-gray-50 p-1" @error="logoPreviewError = true" />
                <span v-if="logoPreviewError" class="text-xs text-red-500">Image failed to load — check the URL</span>
              </div>
              <p class="text-xs text-gray-400 mt-1">If left blank, the dealership name shows as text instead.</p>
            </div>
          </div>
        </section>

        <section class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
          <h2 class="text-base font-semibold text-gray-900 mb-1 pb-3 border-b border-gray-100">Customer Inquiry Notifications</h2>
          <p class="text-sm text-gray-500 mb-4 mt-3">When a customer submits a contact form or clicks "I'm Interested," this email gets notified.</p>
          <label class="block text-sm font-medium text-gray-700 mb-1">Notification Email</label>
          <input v-model="form.notification_email" type="email" class="field max-w-sm" placeholder="owner@yourdealership.com" />
        </section>
      </div>

      <!-- ── HOME ── -->
      <div v-show="activeTab === 'home'" class="space-y-6">
        <section class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
          <h2 class="text-base font-semibold text-gray-900 mb-1 pb-3 border-b border-gray-100">Background Image</h2>
          <p class="text-sm text-gray-500 mb-5 mt-3">Pick the vibe that fits your dealership. This is the big image customers see first.</p>
          <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 mb-5">
            <div
              v-for="preset in HERO_PRESETS" :key="preset.key"
              @click="form.hero_image = preset.key"
              class="relative cursor-pointer rounded-xl overflow-hidden border-4 transition-all"
              :class="form.hero_image === preset.key ? 'border-blue-500 ring-2 ring-blue-300' : 'border-transparent hover:border-gray-300'"
            >
              <img :src="preset.url" :alt="preset.label" class="w-full h-32 object-cover" />
              <div class="absolute inset-0 bg-gradient-to-t from-black/60 to-transparent"></div>
              <div class="absolute bottom-2 left-3 text-white text-sm font-semibold">{{ preset.label }}</div>
              <div v-if="form.hero_image === preset.key" class="absolute top-2 right-2 bg-blue-500 text-white rounded-full w-6 h-6 flex items-center justify-center text-xs font-bold">✓</div>
            </div>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Custom Image URL <span class="text-gray-400 font-normal">(optional — overrides preset above)</span></label>
            <input v-model="customHeroUrl" @input="form.hero_image = customHeroUrl || 'sports'" type="url" class="field" placeholder="Paste any photo URL to use instead of a preset" />
          </div>
        </section>

        <section class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
          <div class="flex items-center justify-between pb-3 border-b border-gray-100 mb-5">
            <div>
              <h2 class="text-base font-semibold text-gray-900">Customer Reviews</h2>
              <p class="text-sm text-gray-500 mt-0.5">Show a reviews section on your home page.</p>
            </div>
            <input v-model="form.reviews_enabled" type="checkbox" class="w-5 h-5 accent-blue-600 cursor-pointer flex-shrink-0 ml-4" />
          </div>

          <div v-if="form.reviews_enabled" class="space-y-5">
            <!-- Add review form -->
            <div class="bg-gray-50 rounded-lg p-4 space-y-3">
              <p class="text-sm font-semibold text-gray-800">Add a Review</p>
              <div class="grid grid-cols-1 sm:grid-cols-2 gap-3">
                <div>
                  <label class="block text-xs font-medium text-gray-700 mb-1">Customer Name *</label>
                  <input v-model="newReview.reviewerName" type="text" class="field" placeholder="e.g. John Smith" />
                </div>
                <div>
                  <label class="block text-xs font-medium text-gray-700 mb-1">Vehicle Purchased <span class="text-gray-400">(optional)</span></label>
                  <input v-model="newReview.vehiclePurchased" type="text" class="field" placeholder="e.g. 2019 Honda Accord" />
                </div>
              </div>
              <div>
                <label class="block text-xs font-medium text-gray-700 mb-1">Rating</label>
                <div class="flex gap-2">
                  <button v-for="n in 5" :key="n" type="button" @click="newReview.rating = n"
                    class="text-2xl transition" :class="n <= newReview.rating ? 'text-amber-400' : 'text-gray-300 hover:text-amber-300'">★</button>
                </div>
              </div>
              <div>
                <label class="block text-xs font-medium text-gray-700 mb-1">Review Text *</label>
                <textarea v-model="newReview.body" class="field resize-none" rows="3" placeholder="What did the customer say? Write it in their words." />
              </div>
              <button @click="addReview" :disabled="reviewSaving || !newReview.reviewerName || !newReview.body"
                class="px-4 py-2 bg-blue-600 text-white text-sm font-semibold rounded-lg hover:bg-blue-700 disabled:opacity-50 transition">
                {{ reviewSaving ? 'Adding...' : 'Add Review' }}
              </button>
              <p v-if="reviewMsg" class="text-sm text-green-700 font-medium">{{ reviewMsg }}</p>
            </div>

            <!-- Existing reviews -->
            <div v-if="reviews.length" class="space-y-3">
              <p class="text-sm font-semibold text-gray-800">Current Reviews ({{ reviews.length }})</p>
              <div v-for="r in reviews" :key="r.id" class="flex items-start justify-between p-3 bg-gray-50 rounded-lg gap-3">
                <div class="flex-1 min-w-0">
                  <div class="flex items-center gap-2 mb-1">
                    <span class="text-sm font-semibold text-gray-800">{{ r.reviewerName }}</span>
                    <span class="text-amber-400 text-xs">{{ '★'.repeat(r.rating) }}</span>
                  </div>
                  <p v-if="r.vehiclePurchased" class="text-xs text-gray-500 mb-1">{{ r.vehiclePurchased }}</p>
                  <p class="text-xs text-gray-600 line-clamp-2">"{{ r.body }}"</p>
                </div>
                <button @click="deleteReview(r.id)" class="text-red-400 hover:text-red-600 text-xs font-medium flex-shrink-0 transition">Remove</button>
              </div>
            </div>
            <p v-else class="text-sm text-gray-400 italic">No reviews yet. Add your first one above.</p>
          </div>
        </section>
      </div>

      <!-- ── ABOUT US ── -->
      <div v-show="activeTab === 'about'" class="space-y-6">
        <section class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
          <h2 class="text-base font-semibold text-gray-900 mb-1 pb-3 border-b border-gray-100">About Us Page</h2>
          <p class="text-sm text-gray-500 mb-5 mt-3">This content appears on your About Us page. Blank fields won't show up.</p>
          <div class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Your Story</label>
              <textarea v-model="form.about_blurb" class="field resize-none" rows="5"
                placeholder="Tell customers who you are. e.g. Family-owned since 1998, serving the Monaca area with quality used vehicles and honest deals." />
              <p class="text-xs text-gray-400 mt-1">Write it in your own voice — this is what customers will read.</p>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Mission Statement <span class="text-gray-400 font-normal">(optional)</span></label>
              <textarea v-model="form.about_mission" class="field resize-none" rows="2"
                placeholder='e.g. "To provide every customer with an honest, transparent car-buying experience."' />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Year Founded <span class="text-gray-400 font-normal">(optional)</span></label>
              <input v-model="form.about_year_founded" type="text" class="field max-w-xs" placeholder="e.g. 1998" />
            </div>
            <div class="border-t border-gray-100 pt-4">
              <p class="text-sm font-medium text-gray-700 mb-1">Stats <span class="text-gray-400 font-normal">(optional — shown as big numbers on About page)</span></p>
              <div class="grid grid-cols-2 sm:grid-cols-4 gap-3 mt-3">
                <div>
                  <label class="block text-xs font-medium text-gray-600 mb-1">Years in Business</label>
                  <input v-model="form.about_stat_years" type="text" class="field" placeholder="20+" />
                </div>
                <div>
                  <label class="block text-xs font-medium text-gray-600 mb-1">Vehicles Sold</label>
                  <input v-model="form.about_stat_vehicles" type="text" class="field" placeholder="1,000+" />
                </div>
                <div>
                  <label class="block text-xs font-medium text-gray-600 mb-1">5-Star Reviews</label>
                  <input v-model="form.about_stat_reviews" type="text" class="field" placeholder="500+" />
                </div>
                <div>
                  <label class="block text-xs font-medium text-gray-600 mb-1">Team Members</label>
                  <input v-model="form.about_stat_team" type="text" class="field" placeholder="12" />
                </div>
              </div>
            </div>
          </div>
        </section>
      </div>

      <!-- ── CONTACT ── -->
      <div v-show="activeTab === 'contact'" class="space-y-6">
        <section class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
          <h2 class="text-base font-semibold text-gray-900 mb-1 pb-3 border-b border-gray-100">Contact Page</h2>
          <p class="text-sm text-gray-500 mb-5 mt-3">Add a Google Map so customers can easily find you.</p>
          <label class="block text-sm font-medium text-gray-700 mb-1">Google Maps Embed URL <span class="text-gray-400 font-normal">(optional)</span></label>
          <input v-model="form.google_maps_embed_url" type="url" class="field" placeholder="https://www.google.com/maps/embed?pb=..." />
          <p class="text-xs text-gray-400 mt-1">Go to Google Maps → search your address → Share → Embed a map → copy the full URL from the src="" value.</p>
        </section>
      </div>

      <!-- ── FINANCING ── -->
      <div v-show="activeTab === 'financing'" class="space-y-6">
        <section class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
          <div class="flex items-center justify-between pb-3 border-b border-gray-100 mb-5">
            <div>
              <h2 class="text-base font-semibold text-gray-900">Financing Page</h2>
              <p class="text-sm text-gray-500 mt-0.5">Turn on to add a Financing page to your site navigation.</p>
            </div>
            <input v-model="form.page_financing" type="checkbox" class="w-5 h-5 accent-blue-600 cursor-pointer flex-shrink-0 ml-4" />
          </div>
          <div v-if="form.page_financing" class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Your Financing Pitch</label>
              <textarea v-model="form.financing_blurb" class="field resize-none" rows="3"
                placeholder="e.g. We work with over 10 local lenders to get you approved, regardless of credit history. Bad credit, no credit, first-time buyer — we have options for everyone." />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">External Application URL <span class="text-gray-400 font-normal">(optional)</span></label>
              <input v-model="form.financing_apply_url" type="url" class="field" placeholder="https://your-lender.com/apply — leave blank to use the built-in form" />
              <p class="text-xs text-gray-400 mt-1">If you have a link from your lender, paste it here. Otherwise customers fill out a short form that gets emailed directly to you.</p>
            </div>
          </div>
          <p v-else class="text-sm text-gray-400 italic">Toggle on to configure and show the Financing page.</p>
        </section>
      </div>

      <!-- ── SCHEDULE SERVICE ── -->
      <div v-show="activeTab === 'schedule'" class="space-y-6">
        <section class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
          <div class="flex items-center justify-between pb-3 border-b border-gray-100 mb-5">
            <div>
              <h2 class="text-base font-semibold text-gray-900">Schedule Service Page</h2>
              <p class="text-sm text-gray-500 mt-0.5">Turn on to add a Schedule Service page to your site navigation.</p>
            </div>
            <input v-model="form.page_schedule_service" type="checkbox" class="w-5 h-5 accent-blue-600 cursor-pointer flex-shrink-0 ml-4" />
          </div>
          <div v-if="form.page_schedule_service" class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Intro Text</label>
              <textarea v-model="form.schedule_service_blurb" class="field resize-none" rows="2"
                placeholder="e.g. Our service team is ready to help. Book an appointment below or give us a call." />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Calendar Booking URL <span class="text-gray-400 font-normal">(optional)</span></label>
              <input v-model="form.schedule_calendar_url" type="url" class="field" placeholder="https://calendly.com/your-dealership — leave blank to use the built-in form" />
              <p class="text-xs text-gray-400 mt-1">Works with Calendly, Cal.com, Google Calendar, Acuity, or any booking link.</p>
            </div>
            <div class="border-t border-gray-100 pt-4">
              <p class="text-sm font-medium text-gray-700 mb-3">Built-in Form Sections <span class="text-gray-400 font-normal">(shown when no calendar URL is set)</span></p>
              <div class="space-y-3">
                <label class="flex items-center justify-between p-3 bg-gray-50 rounded-lg cursor-pointer hover:bg-gray-100 transition">
                  <div>
                    <p class="text-sm font-medium text-gray-800">Vehicle Information</p>
                    <p class="text-xs text-gray-500">Year, make, model, mileage, VIN</p>
                  </div>
                  <input v-model="form.schedule_show_vehicle_info" type="checkbox" class="w-4 h-4 accent-blue-600 cursor-pointer ml-4" />
                </label>
                <label class="flex items-center justify-between p-3 bg-gray-50 rounded-lg cursor-pointer hover:bg-gray-100 transition">
                  <div>
                    <p class="text-sm font-medium text-gray-800">Appointment Preference</p>
                    <p class="text-xs text-gray-500">Preferred date and time of day</p>
                  </div>
                  <input v-model="form.schedule_show_preferred_time" type="checkbox" class="w-4 h-4 accent-blue-600 cursor-pointer ml-4" />
                </label>
                <label class="flex items-center justify-between p-3 bg-gray-50 rounded-lg cursor-pointer hover:bg-gray-100 transition">
                  <div>
                    <p class="text-sm font-medium text-gray-800">"How did you hear about us?"</p>
                    <p class="text-xs text-gray-500">Optional referral source question</p>
                  </div>
                  <input v-model="form.schedule_show_referral" type="checkbox" class="w-4 h-4 accent-blue-600 cursor-pointer ml-4" />
                </label>
              </div>
            </div>
          </div>
          <p v-else class="text-sm text-gray-400 italic">Toggle on to configure and show the Schedule Service page.</p>
        </section>
      </div>

      <!-- ── STAFF ── -->
      <div v-show="activeTab === 'staff'" class="space-y-6">
        <section class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
          <div class="flex items-center justify-between pb-3 border-b border-gray-100 mb-5">
            <div>
              <h2 class="text-base font-semibold text-gray-900">Meet the Staff Page</h2>
              <p class="text-sm text-gray-500 mt-0.5">Turn on to add a Staff page to your site navigation.</p>
            </div>
            <input v-model="form.page_staff" type="checkbox" class="w-5 h-5 accent-blue-600 cursor-pointer flex-shrink-0 ml-4" />
          </div>
          <RouterLink
            to="/admin/staff"
            class="flex items-center justify-between p-4 bg-blue-50 rounded-lg hover:bg-blue-100 transition group"
          >
            <div class="text-sm text-blue-800">
              <p class="font-semibold">Manage Staff Members</p>
              <p class="mt-0.5 text-blue-600">Add, edit, or remove team profiles →</p>
            </div>
            <svg class="w-5 h-5 text-blue-400 group-hover:text-blue-600 transition" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/>
            </svg>
          </RouterLink>
        </section>
      </div>

      <!-- ── HOURS ── -->
      <div v-show="activeTab === 'hours'" class="space-y-6">
        <section class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
          <h2 class="text-base font-semibold text-gray-900 mb-5 pb-3 border-b border-gray-100">Business Hours</h2>
          <div class="space-y-3">
            <div
              v-for="day in days" :key="day.key"
              class="flex flex-wrap items-center gap-x-3 gap-y-2 p-3 bg-gray-50 rounded-lg"
              :class="{ 'opacity-50': form[day.closedKey] }"
            >
              <span class="w-24 text-sm font-semibold text-gray-700 flex-shrink-0">{{ day.label }}</span>
              <div class="flex items-center gap-2 flex-wrap">
                <input v-model="form[day.openKey]" type="time" class="field w-32" :disabled="form[day.closedKey]" />
                <span class="text-gray-400 text-sm">to</span>
                <input v-model="form[day.closeKey]" type="time" class="field w-32" :disabled="form[day.closedKey]" />
              </div>
              <label class="flex items-center gap-2 ml-auto text-sm text-gray-600 cursor-pointer flex-shrink-0">
                <input v-model="form[day.closedKey]" type="checkbox" class="w-4 h-4 accent-blue-600 cursor-pointer" />
                Closed
              </label>
            </div>
          </div>
        </section>
      </div>

      <!-- ── INTEGRATIONS ── -->
      <div v-show="activeTab === 'integrations'" class="space-y-6">
        <section class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
          <h2 class="text-base font-semibold text-gray-900 mb-1 pb-3 border-b border-gray-100">Integrations</h2>
          <p class="text-sm text-gray-500 mb-6 mt-3">Connect third-party services to your site.</p>
          <div class="space-y-4">
            <div class="p-4 bg-gray-50 rounded-lg">
              <p class="text-sm font-semibold text-gray-800">Facebook Marketplace Feed</p>
              <p class="text-xs text-gray-500 mt-0.5 mb-3">Your available inventory auto-syncs to Facebook Marketplace every 24 hours. Copy this URL and paste it into Facebook Commerce Manager → Catalogs → Add items → Data feed.</p>
              <div class="flex gap-2">
                <input :value="`${API_BASE_URL}/api/feeds/facebook-marketplace`" readonly
                  class="flex-1 px-3 py-2 bg-white border border-gray-300 rounded-lg text-xs text-gray-700 font-mono" />
                <button @click="copyFeedUrl"
                  class="px-3 py-2 bg-gray-800 text-white text-xs font-semibold rounded-lg hover:bg-gray-900 transition whitespace-nowrap">
                  {{ feedCopied ? 'Copied!' : 'Copy URL' }}
                </button>
              </div>
            </div>
            <label class="flex items-center justify-between p-4 bg-gray-50 rounded-lg cursor-pointer hover:bg-gray-100 transition">
              <div>
                <p class="text-sm font-semibold text-gray-800">Show CARFAX Report Links</p>
                <p class="text-xs text-gray-500 mt-0.5">Adds a "View CARFAX Report" button to each listing. No dealer account required — clicking it takes customers to carfax.com where they can purchase the report for that VIN.</p>
              </div>
              <input v-model="form.carfax_enabled" type="checkbox" class="w-5 h-5 accent-blue-600 cursor-pointer flex-shrink-0 ml-4" />
            </label>
          </div>
        </section>
      </div>

      <!-- ── PASSWORD ── -->
      <div v-show="activeTab === 'password'" class="space-y-6">
        <section class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
          <h2 class="text-base font-semibold text-gray-900 mb-1 pb-3 border-b border-gray-100">Password Management</h2>
          <p class="text-sm text-gray-500 mb-6 mt-3">Update your admin password or reset the manager's password.</p>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-8">
            <div>
              <h3 class="text-sm font-semibold text-gray-800 mb-3">Your Admin Password</h3>
              <div class="space-y-3">
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">Current Password</label>
                  <input v-model="pwForm.currentPassword" type="password" class="field" placeholder="••••••••" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">New Password</label>
                  <input v-model="pwForm.newPassword" type="password" class="field" placeholder="Min 6 characters" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">Confirm New Password</label>
                  <input v-model="pwForm.confirmPassword" type="password" class="field" placeholder="••••••••" />
                </div>
                <div v-if="pwMsg.admin" :class="pwMsg.adminOk ? 'text-green-700' : 'text-red-600'" class="text-sm font-medium">{{ pwMsg.admin }}</div>
                <button @click="changeAdminPassword" :disabled="pwSaving.admin"
                  class="px-5 py-2 bg-gray-800 text-white text-sm font-semibold rounded-lg hover:bg-gray-900 disabled:opacity-50 transition">
                  {{ pwSaving.admin ? 'Updating...' : 'Update My Password' }}
                </button>
              </div>
            </div>
            <div>
              <h3 class="text-sm font-semibold text-gray-800 mb-3">Manager Password</h3>
              <p class="text-xs text-gray-500 mb-3">Reset the manager's password anytime — useful if a manager leaves or forgets theirs.</p>
              <div class="space-y-3">
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">New Manager Password</label>
                  <input v-model="pwForm.managerNewPassword" type="password" class="field" placeholder="Min 6 characters" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">Confirm Password</label>
                  <input v-model="pwForm.managerConfirmPassword" type="password" class="field" placeholder="••••••••" />
                </div>
                <div v-if="pwMsg.manager" :class="pwMsg.managerOk ? 'text-green-700' : 'text-red-600'" class="text-sm font-medium">{{ pwMsg.manager }}</div>
                <button @click="changeManagerPassword" :disabled="pwSaving.manager"
                  class="px-5 py-2 bg-gray-800 text-white text-sm font-semibold rounded-lg hover:bg-gray-900 disabled:opacity-50 transition">
                  {{ pwSaving.manager ? 'Updating...' : 'Reset Manager Password' }}
                </button>
              </div>
            </div>
          </div>
        </section>
      </div>

      <!-- Save button (bottom) -->
      <div v-if="activeTab !== 'password'" class="flex justify-end pb-8 mt-4">
        <button @click="save" :disabled="saving"
          class="px-8 py-3 bg-blue-600 text-white font-semibold rounded-lg hover:bg-blue-700 disabled:opacity-50 transition">
          {{ saving ? 'Saving...' : 'Save Changes' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { RouterLink } from 'vue-router'
import { adminGetSettings, adminUpdateSettings, adminChangePassword, adminChangeManagerPassword, getReviews, adminCreateReview, adminDeleteReview } from '../../api/index'
import { HERO_PRESETS, fetchSiteSettings } from '../../composables/useSiteSettings'
import { CLOUDINARY_CLOUD_NAME, CLOUDINARY_UPLOAD_PRESET, API_BASE_URL } from '../../config'

const activeTab = ref('business')
const tabs = [
  { key: 'business',     label: 'Business Info' },
  { key: 'home',         label: 'Home' },
  { key: 'about',        label: 'About Us' },
  { key: 'contact',      label: 'Contact' },
  { key: 'financing',    label: 'Financing' },
  { key: 'schedule',     label: 'Schedule Service' },
  { key: 'staff',        label: 'Staff' },
  { key: 'hours',        label: 'Hours' },
  { key: 'integrations', label: 'Integrations' },
  { key: 'password',     label: 'Password' },
]

const loading    = ref(true)
const saving     = ref(false)
const feedCopied = ref(false)
const successMsg = ref('')
const errorMsg   = ref('')
const customHeroUrl = ref('')

function copyFeedUrl() {
  navigator.clipboard.writeText(`${API_BASE_URL}/api/feeds/facebook-marketplace`)
  feedCopied.value = true
  setTimeout(() => { feedCopied.value = false }, 2000)
}

const days = [
  { label: 'Monday',    key: 'monday',    openKey: 'hours_monday_open',    closeKey: 'hours_monday_close',    closedKey: 'hours_monday_closed' },
  { label: 'Tuesday',   key: 'tuesday',   openKey: 'hours_tuesday_open',   closeKey: 'hours_tuesday_close',   closedKey: 'hours_tuesday_closed' },
  { label: 'Wednesday', key: 'wednesday', openKey: 'hours_wednesday_open', closeKey: 'hours_wednesday_close', closedKey: 'hours_wednesday_closed' },
  { label: 'Thursday',  key: 'thursday',  openKey: 'hours_thursday_open',  closeKey: 'hours_thursday_close',  closedKey: 'hours_thursday_closed' },
  { label: 'Friday',    key: 'friday',    openKey: 'hours_friday_open',    closeKey: 'hours_friday_close',    closedKey: 'hours_friday_closed' },
  { label: 'Saturday',  key: 'saturday',  openKey: 'hours_saturday_open',  closeKey: 'hours_saturday_close',  closedKey: 'hours_saturday_closed' },
  { label: 'Sunday',    key: 'sunday',    openKey: 'hours_sunday_open',    closeKey: 'hours_sunday_close',    closedKey: 'hours_sunday_closed' },
]

const form = reactive({
  business_name: '', tagline: '', phone: '', email: '', address: '', city_state_zip: '',
  facebook_url: '', instagram_url: '', notification_email: '', logo_url: '',
  google_maps_embed_url: '', hero_image: 'sports',
  about_blurb: '', about_mission: '', about_year_founded: '',
  about_stat_years: '', about_stat_vehicles: '', about_stat_reviews: '', about_stat_team: '',
  financing_blurb: '', financing_apply_url: '',
  schedule_service_blurb: '', schedule_calendar_url: '',
  schedule_show_vehicle_info: true, schedule_show_preferred_time: true, schedule_show_referral: false,
  page_staff: false, page_financing: false, page_schedule_service: false,
  carfax_enabled: false, reviews_enabled: false,
  hours_monday_open: '09:00',    hours_monday_close: '18:00',    hours_monday_closed: false,
  hours_tuesday_open: '09:00',   hours_tuesday_close: '18:00',   hours_tuesday_closed: false,
  hours_wednesday_open: '09:00', hours_wednesday_close: '18:00', hours_wednesday_closed: false,
  hours_thursday_open: '09:00',  hours_thursday_close: '18:00',  hours_thursday_closed: false,
  hours_friday_open: '09:00',    hours_friday_close: '18:00',    hours_friday_closed: false,
  hours_saturday_open: '10:00',  hours_saturday_close: '17:00',  hours_saturday_closed: false,
  hours_sunday_open: '',         hours_sunday_close: '',         hours_sunday_closed: true,
})

// ── Reviews ──────────────────────────────────────────────────────────────────
const reviews     = ref([])
const reviewSaving = ref(false)
const reviewMsg    = ref('')
const newReview    = reactive({ reviewerName: '', vehiclePurchased: '', rating: 5, body: '' })

async function loadReviews() {
  try { reviews.value = (await getReviews()).data } catch { /* ignore */ }
}

async function addReview() {
  if (!newReview.reviewerName || !newReview.body) return
  reviewSaving.value = true
  reviewMsg.value = ''
  try {
    await adminCreateReview({ ...newReview })
    newReview.reviewerName = ''
    newReview.vehiclePurchased = ''
    newReview.rating = 5
    newReview.body = ''
    reviewMsg.value = 'Review added!'
    await loadReviews()
    setTimeout(() => { reviewMsg.value = '' }, 3000)
  } finally {
    reviewSaving.value = false
  }
}

async function deleteReview(id) {
  if (!confirm('Remove this review?')) return
  await adminDeleteReview(id)
  await loadReviews()
}

// ── Settings load ─────────────────────────────────────────────────────────────
onMounted(async () => {
  try {
    const res = await adminGetSettings()
    const s = res.data
    const textFields = ['business_name','tagline','phone','email','address',
      'city_state_zip','facebook_url','instagram_url','notification_email',
      'logo_url','google_maps_embed_url','hero_image',
      'about_blurb','about_mission','about_year_founded',
      'about_stat_years','about_stat_vehicles','about_stat_reviews','about_stat_team',
      'financing_blurb','financing_apply_url','schedule_service_blurb','schedule_calendar_url']
    textFields.forEach(k => { if (s[k]) form[k] = s[k] })
    if (s.hero_image && s.hero_image.startsWith('http')) customHeroUrl.value = s.hero_image
    if (s.schedule_show_vehicle_info   !== undefined) form.schedule_show_vehicle_info   = s.schedule_show_vehicle_info   !== 'false'
    if (s.schedule_show_preferred_time !== undefined) form.schedule_show_preferred_time = s.schedule_show_preferred_time !== 'false'
    if (s.schedule_show_referral       !== undefined) form.schedule_show_referral       = s.schedule_show_referral       === 'true'
    if (s.page_staff            !== undefined) form.page_staff            = s.page_staff            === 'true'
    if (s.page_financing        !== undefined) form.page_financing        = s.page_financing        === 'true'
    if (s.page_schedule_service !== undefined) form.page_schedule_service = s.page_schedule_service === 'true'
    if (s.carfax_enabled        !== undefined) form.carfax_enabled        = s.carfax_enabled        === 'true'
    if (s.reviews_enabled       !== undefined) form.reviews_enabled       = s.reviews_enabled       === 'true'
    days.forEach(day => {
      if (s[day.openKey])  form[day.openKey]  = s[day.openKey]
      if (s[day.closeKey]) form[day.closeKey] = s[day.closeKey]
      if (s[day.closedKey] !== undefined) form[day.closedKey] = s[day.closedKey] === 'true'
    })
  } catch { /* new deployment — use defaults */ } finally {
    loading.value = false
  }
  await loadReviews()
})

// ── Save ──────────────────────────────────────────────────────────────────────
async function save() {
  saving.value = true
  successMsg.value = ''
  errorMsg.value = ''
  try {
    const updates = {}
    const textFields = ['business_name','tagline','phone','email','address',
      'city_state_zip','facebook_url','instagram_url','notification_email',
      'logo_url','google_maps_embed_url','hero_image',
      'about_blurb','about_mission','about_year_founded',
      'about_stat_years','about_stat_vehicles','about_stat_reviews','about_stat_team',
      'financing_blurb','financing_apply_url','schedule_service_blurb','schedule_calendar_url']
    textFields.forEach(k => { updates[k] = form[k] })
    updates.schedule_show_vehicle_info   = String(form.schedule_show_vehicle_info)
    updates.schedule_show_preferred_time = String(form.schedule_show_preferred_time)
    updates.schedule_show_referral       = String(form.schedule_show_referral)
    updates.page_staff            = String(form.page_staff)
    updates.page_financing        = String(form.page_financing)
    updates.page_schedule_service = String(form.page_schedule_service)
    updates.carfax_enabled        = String(form.carfax_enabled)
    updates.reviews_enabled       = String(form.reviews_enabled)
    days.forEach(day => {
      updates[day.openKey]   = form[day.openKey]
      updates[day.closeKey]  = form[day.closeKey]
      updates[day.closedKey] = String(form[day.closedKey])
      updates[`hours_${day.key}`] = form[day.closedKey]
        ? 'Closed'
        : `${formatTime(form[day.openKey])} – ${formatTime(form[day.closeKey])}`
    })
    await adminUpdateSettings(updates)
    await fetchSiteSettings()
    successMsg.value = 'Settings saved! Changes are now live on your site.'
    window.scrollTo({ top: 0, behavior: 'smooth' })
  } catch {
    errorMsg.value = 'Failed to save. Please try again.'
  } finally {
    saving.value = false
  }
}

// ── Logo upload ───────────────────────────────────────────────────────────────
const logoUploading    = ref(false)
const logoPreviewError = ref(false)

async function uploadLogo(e) {
  const file = e.target.files?.[0]
  if (!file) return
  logoUploading.value = true
  logoPreviewError.value = false
  try {
    const fd = new FormData()
    fd.append('file', file)
    fd.append('upload_preset', CLOUDINARY_UPLOAD_PRESET)
    const res  = await fetch(`https://api.cloudinary.com/v1_1/${CLOUDINARY_CLOUD_NAME}/image/upload`, { method: 'POST', body: fd })
    const data = await res.json()
    form.logo_url = data.secure_url
  } catch { /* silently fail */ } finally {
    logoUploading.value = false
  }
}

// ── Password management ───────────────────────────────────────────────────────
const pwForm = reactive({ currentPassword: '', newPassword: '', confirmPassword: '', managerNewPassword: '', managerConfirmPassword: '' })
const pwMsg  = reactive({ admin: '', adminOk: false, manager: '', managerOk: false })
const pwSaving = reactive({ admin: false, manager: false })

async function changeAdminPassword() {
  if (pwForm.newPassword !== pwForm.confirmPassword) { pwMsg.admin = 'Passwords do not match.'; pwMsg.adminOk = false; return }
  pwSaving.admin = true
  pwMsg.admin = ''
  try {
    await adminChangePassword(pwForm.currentPassword, pwForm.newPassword)
    pwMsg.admin = 'Password updated successfully!'
    pwMsg.adminOk = true
    pwForm.currentPassword = ''
    pwForm.newPassword = ''
    pwForm.confirmPassword = ''
  } catch (err) {
    pwMsg.admin = err.response?.status === 401 ? 'Current password is incorrect.' : 'Failed to update password.'
    pwMsg.adminOk = false
  } finally { pwSaving.admin = false }
}

async function changeManagerPassword() {
  if (pwForm.managerNewPassword !== pwForm.managerConfirmPassword) { pwMsg.manager = 'Passwords do not match.'; pwMsg.managerOk = false; return }
  pwSaving.manager = true
  pwMsg.manager = ''
  try {
    await adminChangeManagerPassword(pwForm.managerNewPassword)
    pwMsg.manager = 'Manager password updated!'
    pwMsg.managerOk = true
    pwForm.managerNewPassword = ''
    pwForm.managerConfirmPassword = ''
  } catch {
    pwMsg.manager = 'Failed to update password.'
    pwMsg.managerOk = false
  } finally { pwSaving.manager = false }
}

function formatTime(t) {
  if (!t) return ''
  const [h, m] = t.split(':')
  const hour = parseInt(h)
  const ampm = hour >= 12 ? 'PM' : 'AM'
  const h12  = hour % 12 || 12
  return `${h12}:${m} ${ampm}`
}
</script>
