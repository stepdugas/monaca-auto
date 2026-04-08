<template>
  <div class="max-w-3xl mx-auto">
    <div class="flex items-center justify-between mb-8">
      <h1 class="text-2xl font-bold text-gray-900">Site Settings</h1>
      <button
        @click="save"
        :disabled="saving"
        class="px-6 py-2 bg-blue-600 text-white font-semibold rounded-lg hover:bg-blue-700
               disabled:opacity-50 disabled:cursor-not-allowed transition"
      >
        {{ saving ? 'Saving...' : 'Save Changes' }}
      </button>
    </div>

    <!-- Success / Error banner -->
    <div v-if="successMsg" class="mb-6 p-4 bg-green-50 border border-green-200 rounded-lg text-green-800 text-sm font-medium">
      ✓ {{ successMsg }}
    </div>
    <div v-if="errorMsg" class="mb-6 p-4 bg-red-50 border border-red-200 rounded-lg text-red-800 text-sm font-medium">
      {{ errorMsg }}
    </div>

    <div v-if="loading" class="text-center py-20 text-gray-400">Loading settings...</div>

    <div v-else class="space-y-8">

      <!-- BUSINESS INFO -->
      <section class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
        <h2 class="text-base font-semibold text-gray-900 mb-5 pb-3 border-b border-gray-100">
          Business Information
        </h2>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div class="md:col-span-2">
            <label class="block text-sm font-medium text-gray-700 mb-1">Business Name</label>
            <input v-model="form.business_name" type="text" class="field" placeholder="e.g. Dugas Auto Sales" />
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
            <input v-model="form.city_state_zip" type="text" class="field" placeholder="Akron, OH 44301" />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Facebook URL</label>
            <input v-model="form.facebook_url" type="url" class="field" placeholder="https://facebook.com/yourpage" />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Instagram URL</label>
            <input v-model="form.instagram_url" type="url" class="field" placeholder="https://instagram.com/yourpage" />
          </div>

          <!-- Logo -->
          <div class="md:col-span-2 border-t border-gray-100 pt-4">
            <label class="block text-sm font-medium text-gray-700 mb-1">Logo URL <span class="text-gray-400 font-normal">(optional — shown in header & footer)</span></label>
            <div class="flex gap-3 items-start">
              <input v-model="form.logo_url" type="url" class="field flex-1" placeholder="https://res.cloudinary.com/... or any image URL" />
              <button
                type="button"
                @click="$refs.logoFileInput.click()"
                :disabled="logoUploading"
                class="px-4 py-2 text-sm border border-gray-300 rounded-lg bg-white hover:bg-gray-50 whitespace-nowrap disabled:opacity-50"
              >
                {{ logoUploading ? 'Uploading…' : 'Upload Image' }}
              </button>
              <input ref="logoFileInput" type="file" accept="image/*" class="hidden" @change="uploadLogo" />
            </div>
            <div v-if="form.logo_url" class="mt-3 flex items-center gap-3">
              <img :src="form.logo_url" alt="Logo preview" class="h-12 w-auto rounded border border-gray-200 bg-gray-50 p-1" @error="logoPreviewError = true" />
              <span v-if="!logoPreviewError" class="text-xs text-gray-400">Preview</span>
              <span v-else class="text-xs text-red-500">Image failed to load — check the URL</span>
            </div>
            <p class="text-xs text-gray-400 mt-1">Paste a URL or upload via Cloudinary. If left blank, the dealership name is shown as text instead.</p>
          </div>

          <!-- Google Maps embed URL -->
          <div class="md:col-span-2">
            <label class="block text-sm font-medium text-gray-700 mb-1">Google Maps Embed URL <span class="text-gray-400 font-normal">(optional — shows a map on the Contact page)</span></label>
            <input v-model="form.google_maps_embed_url" type="url" class="field" placeholder="https://www.google.com/maps/embed?pb=..." />
            <p class="text-xs text-gray-400 mt-1">
              Get this from Google Maps → Share → Embed a map → copy only the <em>src="…"</em> value.
            </p>
          </div>
        </div>
      </section>

      <!-- CUSTOMER INQUIRY EMAIL -->
      <section class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
        <h2 class="text-base font-semibold text-gray-900 mb-1 pb-3 border-b border-gray-100">
          Customer Inquiry Notifications
        </h2>
        <p class="text-sm text-gray-500 mb-4 mt-3">
          When a customer clicks "I'm Interested" on a vehicle, this email address will receive the notification.
        </p>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Notification Email</label>
          <input v-model="form.notification_email" type="email" class="field max-w-sm" placeholder="owner@yourdealership.com" />
        </div>
      </section>

      <!-- BUSINESS HOURS -->
      <section class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
        <h2 class="text-base font-semibold text-gray-900 mb-5 pb-3 border-b border-gray-100">
          Business Hours
        </h2>
        <div class="space-y-3">
          <div
            v-for="day in days"
            :key="day.key"
            class="flex flex-wrap items-center gap-3 p-3 bg-gray-50 rounded-lg"
            :class="{ 'opacity-50': form[day.closedKey] }"
          >
            <span class="w-24 text-sm font-semibold text-gray-700">{{ day.label }}</span>
            <input
              v-model="form[day.openKey]"
              type="time"
              class="field w-32"
              :disabled="form[day.closedKey]"
            />
            <span class="text-gray-400 text-sm">to</span>
            <input
              v-model="form[day.closeKey]"
              type="time"
              class="field w-32"
              :disabled="form[day.closedKey]"
            />
            <label class="flex items-center gap-2 ml-auto text-sm text-gray-600 cursor-pointer">
              <input
                v-model="form[day.closedKey]"
                type="checkbox"
                class="w-4 h-4 accent-blue-600 cursor-pointer"
              />
              Closed
            </label>
          </div>
        </div>
      </section>

      <!-- HERO IMAGE PICKER -->
      <section class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
        <h2 class="text-base font-semibold text-gray-900 mb-1 pb-3 border-b border-gray-100">
          Homepage Background Image
        </h2>
        <p class="text-sm text-gray-500 mb-5 mt-3">
          Pick the vibe that fits your dealership best. This is the big image customers see first.
        </p>
        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 mb-5">
          <div
            v-for="preset in HERO_PRESETS"
            :key="preset.key"
            @click="form.hero_image = preset.key"
            class="relative cursor-pointer rounded-xl overflow-hidden border-4 transition-all"
            :class="form.hero_image === preset.key
              ? 'border-blue-500 ring-2 ring-blue-300'
              : 'border-transparent hover:border-gray-300'"
          >
            <img :src="preset.url" :alt="preset.label" class="w-full h-32 object-cover" />
            <div class="absolute inset-0 bg-gradient-to-t from-black/60 to-transparent"></div>
            <div class="absolute bottom-2 left-3 text-white text-sm font-semibold">{{ preset.label }}</div>
            <div
              v-if="form.hero_image === preset.key"
              class="absolute top-2 right-2 bg-blue-500 text-white rounded-full w-6 h-6 flex items-center justify-center text-xs font-bold"
            >
              ✓
            </div>
          </div>
        </div>
        <div class="mt-4">
          <label class="block text-sm font-medium text-gray-700 mb-1">
            Custom Background URL <span class="text-gray-400 font-normal">(optional — overrides the preset selection above)</span>
          </label>
          <input
            v-model="customHeroUrl"
            @input="form.hero_image = customHeroUrl || 'sports'"
            type="url"
            class="field"
            placeholder="https://your-image-url.com/photo.jpg — paste any photo URL to use instead"
          />
          <p class="text-xs text-gray-400 mt-1">Paste a Cloudinary URL, Unsplash link, or any direct image URL. Leave blank to use the preset selection.</p>
        </div>
      </section>

      <!-- ABOUT US CONTENT -->
      <section class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
        <h2 class="text-base font-semibold text-gray-900 mb-1 pb-3 border-b border-gray-100">
          About Us Page
        </h2>
        <p class="text-sm text-gray-500 mb-5 mt-3">
          This content appears on your About Us page. Fill in what applies — blank fields won't show up.
        </p>
        <div class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Your Story</label>
            <textarea
              v-model="form.about_blurb"
              class="field resize-none"
              rows="5"
              placeholder="Tell customers who you are. e.g. Family-owned since 1998, serving the Akron area with quality used vehicles and honest deals. What started as a small lot has grown into one of the region's most trusted dealerships."
            ></textarea>
            <p class="text-xs text-gray-400 mt-1">This is the main paragraph customers will read. Write it in your own voice.</p>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Mission Statement <span class="text-gray-400 font-normal">(optional)</span></label>
            <textarea
              v-model="form.about_mission"
              class="field resize-none"
              rows="2"
              placeholder='e.g. "To provide every customer with an honest, transparent, and enjoyable car-buying experience."'
            ></textarea>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Year Founded <span class="text-gray-400 font-normal">(optional)</span></label>
            <input v-model="form.about_year_founded" type="text" class="field max-w-xs" placeholder="e.g. 1998" />
          </div>
          <div class="border-t border-gray-100 pt-4">
            <p class="text-sm font-medium text-gray-700 mb-1">Stats / Social Proof <span class="text-gray-400 font-normal">(optional — leave blank to hide)</span></p>
            <p class="text-xs text-gray-400 mb-3">These appear as big numbers on the About Us page. Only filled-in stats will show.</p>
            <div class="grid grid-cols-2 sm:grid-cols-4 gap-3">
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

      <!-- FINANCING PAGE SETTINGS -->
      <section class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
        <h2 class="text-base font-semibold text-gray-900 mb-1 pb-3 border-b border-gray-100">
          Financing Page
        </h2>
        <p class="text-sm text-gray-500 mb-5 mt-3">
          Shown on your Financing page. The blurb is your pitch to customers — tell them what makes your financing great.
        </p>
        <div class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Your Financing Pitch</label>
            <textarea
              v-model="form.financing_blurb"
              class="field resize-none"
              rows="3"
              placeholder="e.g. We work with over 10 local lenders to get you approved, regardless of credit history. Bad credit, no credit, first-time buyer — we have options for everyone."
            ></textarea>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">External Application URL <span class="text-gray-400 font-normal">(optional)</span></label>
            <input
              v-model="form.financing_apply_url"
              type="url"
              class="field"
              placeholder="https://your-lender.com/apply — leave blank to use the built-in application form"
            />
            <p class="text-xs text-gray-400 mt-1">If you have a link from your lender or a Google Form, paste it here. Otherwise customers will fill out a short form that gets emailed directly to you.</p>
          </div>
        </div>
      </section>

      <!-- SCHEDULE SERVICE SETTINGS -->
      <section class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
        <h2 class="text-base font-semibold text-gray-900 mb-1 pb-3 border-b border-gray-100">
          Schedule Service Page
        </h2>
        <p class="text-sm text-gray-500 mb-5 mt-3">
          Configure your service scheduling page. Paste a calendar link if you use Calendly, Cal.com, Google Calendar booking, or similar — otherwise customers will fill out a built-in request form.
        </p>
        <div class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Intro Text</label>
            <textarea
              v-model="form.schedule_service_blurb"
              class="field resize-none"
              rows="2"
              placeholder="e.g. Our service team is ready to help. Book an appointment below or give us a call."
            ></textarea>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Calendar Booking URL <span class="text-gray-400 font-normal">(optional)</span></label>
            <input
              v-model="form.schedule_calendar_url"
              type="url"
              class="field"
              placeholder="https://calendly.com/your-dealership — leave blank to use the built-in request form"
            />
            <p class="text-xs text-gray-400 mt-1">Works with Calendly, Cal.com, Google Calendar booking, Acuity, Square Appointments, or any bookable link.</p>
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
      </section>

      <!-- ACTIVE PAGES -->
      <section class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
        <h2 class="text-base font-semibold text-gray-900 mb-1 pb-3 border-b border-gray-100">
          Active Pages
        </h2>
        <p class="text-sm text-gray-500 mb-6 mt-3">
          Turn pages on or off. Only enabled pages will appear in the navigation menu.
          Home, Inventory, About Us, and Contact are always visible.
        </p>
        <div class="space-y-4">
          <label class="flex items-center justify-between p-4 bg-gray-50 rounded-lg cursor-pointer hover:bg-gray-100 transition">
            <div>
              <p class="text-sm font-semibold text-gray-800">Meet the Staff</p>
              <p class="text-xs text-gray-500 mt-0.5">Showcase your team with names, titles, photos, and bios</p>
            </div>
            <input v-model="form.page_staff" type="checkbox" class="w-5 h-5 accent-blue-600 cursor-pointer flex-shrink-0 ml-4" />
          </label>
          <label class="flex items-center justify-between p-4 bg-gray-50 rounded-lg cursor-pointer hover:bg-gray-100 transition">
            <div>
              <p class="text-sm font-semibold text-gray-800">Financing</p>
              <p class="text-xs text-gray-500 mt-0.5">Information about financing options available at your dealership</p>
            </div>
            <input v-model="form.page_financing" type="checkbox" class="w-5 h-5 accent-blue-600 cursor-pointer flex-shrink-0 ml-4" />
          </label>
          <label class="flex items-center justify-between p-4 bg-gray-50 rounded-lg cursor-pointer hover:bg-gray-100 transition">
            <div>
              <p class="text-sm font-semibold text-gray-800">Schedule Service</p>
              <p class="text-xs text-gray-500 mt-0.5">Let customers book service appointments online</p>
            </div>
            <input v-model="form.page_schedule_service" type="checkbox" class="w-5 h-5 accent-blue-600 cursor-pointer flex-shrink-0 ml-4" />
          </label>
        </div>
      </section>

      <!-- INTEGRATIONS -->
      <section class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
        <h2 class="text-base font-semibold text-gray-900 mb-1 pb-3 border-b border-gray-100">
          Integrations
        </h2>
        <p class="text-sm text-gray-500 mb-6 mt-3">
          Connect third-party services to your listings.
        </p>
        <div class="space-y-4">
          <label class="flex items-center justify-between p-4 bg-gray-50 rounded-lg cursor-pointer hover:bg-gray-100 transition">
            <div>
              <p class="text-sm font-semibold text-gray-800">Show CARFAX Report Links</p>
              <p class="text-xs text-gray-500 mt-0.5">Displays a "View CARFAX Report" button on each vehicle listing page</p>
            </div>
            <input v-model="form.carfax_enabled" type="checkbox" class="w-5 h-5 accent-blue-600 cursor-pointer flex-shrink-0 ml-4" />
          </label>
        </div>
      </section>

      <!-- PASSWORD MANAGEMENT -->
      <section class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
        <h2 class="text-base font-semibold text-gray-900 mb-1 pb-3 border-b border-gray-100">
          Password Management
        </h2>
        <p class="text-sm text-gray-500 mb-6 mt-3">
          Update your admin password or reset the manager's password.
        </p>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-8">

          <!-- Change admin password -->
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
              <div v-if="pwMsg.admin" :class="pwMsg.adminOk ? 'text-green-700' : 'text-red-600'" class="text-sm font-medium">
                {{ pwMsg.admin }}
              </div>
              <button
                @click="changeAdminPassword"
                :disabled="pwSaving.admin"
                class="px-5 py-2 bg-gray-800 text-white text-sm font-semibold rounded-lg hover:bg-gray-900
                       disabled:opacity-50 disabled:cursor-not-allowed transition"
              >
                {{ pwSaving.admin ? 'Updating...' : 'Update My Password' }}
              </button>
            </div>
          </div>

          <!-- Reset manager password -->
          <div>
            <h3 class="text-sm font-semibold text-gray-800 mb-3">Manager Password</h3>
            <p class="text-xs text-gray-500 mb-3">
              You can reset the manager's password anytime — useful if a manager leaves or forgets theirs.
            </p>
            <div class="space-y-3">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">New Manager Password</label>
                <input v-model="pwForm.managerNewPassword" type="password" class="field" placeholder="Min 6 characters" />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">Confirm Password</label>
                <input v-model="pwForm.managerConfirmPassword" type="password" class="field" placeholder="••••••••" />
              </div>
              <div v-if="pwMsg.manager" :class="pwMsg.managerOk ? 'text-green-700' : 'text-red-600'" class="text-sm font-medium">
                {{ pwMsg.manager }}
              </div>
              <button
                @click="changeManagerPassword"
                :disabled="pwSaving.manager"
                class="px-5 py-2 bg-gray-800 text-white text-sm font-semibold rounded-lg hover:bg-gray-900
                       disabled:opacity-50 disabled:cursor-not-allowed transition"
              >
                {{ pwSaving.manager ? 'Updating...' : 'Reset Manager Password' }}
              </button>
            </div>
          </div>

        </div>
      </section>

      <!-- Save button (bottom) -->
      <div class="flex justify-end pb-8">
        <button
          @click="save"
          :disabled="saving"
          class="px-8 py-3 bg-blue-600 text-white font-semibold rounded-lg hover:bg-blue-700
                 disabled:opacity-50 disabled:cursor-not-allowed transition"
        >
          {{ saving ? 'Saving...' : 'Save Changes' }}
        </button>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { adminGetSettings, adminUpdateSettings, adminChangePassword, adminChangeManagerPassword } from '../../api/index'
import { HERO_PRESETS, fetchSiteSettings } from '../../composables/useSiteSettings'
import { CLOUDINARY_CLOUD_NAME, CLOUDINARY_UPLOAD_PRESET } from '../../config'

const loading       = ref(true)
const saving        = ref(false)
const customHeroUrl = ref('')
const successMsg = ref('')
const errorMsg   = ref('')

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
  business_name: '',
  tagline: '',
  phone: '',
  email: '',
  address: '',
  city_state_zip: '',
  facebook_url: '',
  instagram_url: '',
  notification_email: '',
  logo_url: '',
  google_maps_embed_url: '',
  hero_image: 'sports',
  // about us
  about_blurb: '',
  about_mission: '',
  about_year_founded: '',
  about_stat_years: '',
  about_stat_vehicles: '',
  about_stat_reviews: '',
  about_stat_team: '',
  // financing
  financing_blurb: '',
  financing_apply_url: '',
  // schedule service
  schedule_service_blurb: '',
  schedule_calendar_url: '',
  schedule_show_vehicle_info: true,
  schedule_show_preferred_time: true,
  schedule_show_referral: false,
  // page toggles
  page_staff: false,
  page_financing: false,
  page_schedule_service: false,
  // integrations
  carfax_enabled: false,
  // hours
  hours_monday_open: '09:00',    hours_monday_close: '18:00',    hours_monday_closed: false,
  hours_tuesday_open: '09:00',   hours_tuesday_close: '18:00',   hours_tuesday_closed: false,
  hours_wednesday_open: '09:00', hours_wednesday_close: '18:00', hours_wednesday_closed: false,
  hours_thursday_open: '09:00',  hours_thursday_close: '18:00',  hours_thursday_closed: false,
  hours_friday_open: '09:00',    hours_friday_close: '18:00',    hours_friday_closed: false,
  hours_saturday_open: '10:00',  hours_saturday_close: '17:00',  hours_saturday_closed: false,
  hours_sunday_open: '',         hours_sunday_close: '',         hours_sunday_closed: true,
})

onMounted(async () => {
  try {
    const res = await adminGetSettings()
    const s = res.data
    // Populate text fields
    const textFields = ['business_name','tagline','phone','email','address',
                        'city_state_zip','facebook_url','instagram_url','notification_email',
                        'logo_url','google_maps_embed_url','hero_image',
                        'about_blurb','about_mission','about_year_founded',
                        'about_stat_years','about_stat_vehicles','about_stat_reviews','about_stat_team',
                        'financing_blurb','financing_apply_url',
                        'schedule_service_blurb','schedule_calendar_url']
    textFields.forEach(k => { if (s[k]) form[k] = s[k] })
    // If hero_image is a URL (custom), populate the custom field
    if (s.hero_image && s.hero_image.startsWith('http')) customHeroUrl.value = s.hero_image

    // Populate schedule service toggles
    if (s.schedule_show_vehicle_info   !== undefined) form.schedule_show_vehicle_info   = s.schedule_show_vehicle_info   !== 'false'
    if (s.schedule_show_preferred_time !== undefined) form.schedule_show_preferred_time = s.schedule_show_preferred_time !== 'false'
    if (s.schedule_show_referral       !== undefined) form.schedule_show_referral       = s.schedule_show_referral       === 'true'

    // Populate page toggles
    if (s.page_staff            !== undefined) form.page_staff            = s.page_staff            === 'true'
    if (s.page_financing        !== undefined) form.page_financing        = s.page_financing        === 'true'
    if (s.page_schedule_service !== undefined) form.page_schedule_service = s.page_schedule_service === 'true'
    if (s.carfax_enabled        !== undefined) form.carfax_enabled        = s.carfax_enabled        === 'true'

    // Populate hours
    days.forEach(day => {
      if (s[day.openKey])  form[day.openKey]  = s[day.openKey]
      if (s[day.closeKey]) form[day.closeKey] = s[day.closeKey]
      if (s[day.closedKey] !== undefined) form[day.closedKey] = s[day.closedKey] === 'true'
    })
  } catch {
    // New deployment — form stays at defaults
  } finally {
    loading.value = false
  }
})

async function save() {
  saving.value = true
  successMsg.value = ''
  errorMsg.value = ''

  try {
    // Build flat updates object for the backend
    const updates = {}
    const textFields = ['business_name','tagline','phone','email','address',
                        'city_state_zip','facebook_url','instagram_url','notification_email',
                        'logo_url','google_maps_embed_url','hero_image',
                        'about_blurb','about_mission','about_year_founded',
                        'about_stat_years','about_stat_vehicles','about_stat_reviews','about_stat_team',
                        'financing_blurb','financing_apply_url',
                        'schedule_service_blurb','schedule_calendar_url']
    textFields.forEach(k => { updates[k] = form[k] })

    // Schedule service toggles
    updates.schedule_show_vehicle_info   = String(form.schedule_show_vehicle_info)
    updates.schedule_show_preferred_time = String(form.schedule_show_preferred_time)
    updates.schedule_show_referral       = String(form.schedule_show_referral)

    // Page toggles
    updates.page_staff            = String(form.page_staff)
    updates.page_financing        = String(form.page_financing)
    updates.page_schedule_service = String(form.page_schedule_service)
    updates.carfax_enabled        = String(form.carfax_enabled)

    // Build hours strings and save raw fields
    days.forEach(day => {
      updates[day.openKey]   = form[day.openKey]
      updates[day.closeKey]  = form[day.closeKey]
      updates[day.closedKey] = String(form[day.closedKey])

      // Human-readable hours string for the public settings endpoint
      updates[`hours_${day.key}`] = form[day.closedKey]
        ? 'Closed'
        : `${formatTime(form[day.openKey])} – ${formatTime(form[day.closeKey])}`
    })

    await adminUpdateSettings(updates)

    // Refresh the global site settings so the live site updates immediately
    await fetchSiteSettings()

    successMsg.value = 'Settings saved! Changes are now live on your site.'
    window.scrollTo({ top: 0, behavior: 'smooth' })
  } catch {
    errorMsg.value = 'Failed to save. Make sure the backend is running and try again.'
  } finally {
    saving.value = false
  }
}

// ── Logo upload ──────────────────────────────────────────────────────────
const logoUploading  = ref(false)
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
    const res  = await fetch(
      `https://api.cloudinary.com/v1_1/${CLOUDINARY_CLOUD_NAME}/image/upload`,
      { method: 'POST', body: fd }
    )
    const data = await res.json()
    form.logo_url = data.secure_url
  } catch {
    // silently fail — user can paste URL manually
  } finally {
    logoUploading.value = false
  }
}

// ── Password management ──────────────────────────────────────────────────

const pwForm = reactive({
  currentPassword: '',
  newPassword: '',
  confirmPassword: '',
  managerNewPassword: '',
  managerConfirmPassword: '',
})

const pwSaving = reactive({ admin: false, manager: false })
const pwMsg    = reactive({ admin: '', adminOk: false, manager: '', managerOk: false })

async function changeAdminPassword() {
  pwMsg.admin = ''
  if (pwForm.newPassword !== pwForm.confirmPassword) {
    pwMsg.admin = 'New passwords do not match.'; pwMsg.adminOk = false; return
  }
  if (pwForm.newPassword.length < 6) {
    pwMsg.admin = 'Password must be at least 6 characters.'; pwMsg.adminOk = false; return
  }
  pwSaving.admin = true
  try {
    await adminChangePassword(pwForm.currentPassword, pwForm.newPassword)
    pwMsg.admin = 'Password updated successfully!'; pwMsg.adminOk = true
    pwForm.currentPassword = ''; pwForm.newPassword = ''; pwForm.confirmPassword = ''
  } catch (e) {
    pwMsg.admin = e.response?.data?.error || 'Failed to update password.'; pwMsg.adminOk = false
  } finally {
    pwSaving.admin = false
  }
}

async function changeManagerPassword() {
  pwMsg.manager = ''
  if (pwForm.managerNewPassword !== pwForm.managerConfirmPassword) {
    pwMsg.manager = 'Passwords do not match.'; pwMsg.managerOk = false; return
  }
  if (pwForm.managerNewPassword.length < 6) {
    pwMsg.manager = 'Password must be at least 6 characters.'; pwMsg.managerOk = false; return
  }
  pwSaving.manager = true
  try {
    await adminChangeManagerPassword(pwForm.managerNewPassword)
    pwMsg.manager = 'Manager password updated!'; pwMsg.managerOk = true
    pwForm.managerNewPassword = ''; pwForm.managerConfirmPassword = ''
  } catch (e) {
    pwMsg.manager = e.response?.data?.error || 'Failed to update manager password.'; pwMsg.managerOk = false
  } finally {
    pwSaving.manager = false
  }
}

function formatTime(t) {
  if (!t) return ''
  const [h, m] = t.split(':')
  const hour = parseInt(h)
  const ampm = hour >= 12 ? 'PM' : 'AM'
  const displayHour = hour % 12 || 12
  return `${displayHour}:${m} ${ampm}`
}
</script>

<style scoped>
.field {
  @apply w-full px-3 py-2 border border-gray-300 rounded-lg text-sm
         focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent
         disabled:bg-gray-100 disabled:cursor-not-allowed transition;
}
</style>
