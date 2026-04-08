import { createRouter, createWebHistory } from 'vue-router'

/**
 * Application routes.
 * Admin routes use a lazy-loaded chunk to keep the main bundle small.
 * Optional scaffold pages are included but only render placeholder content.
 */
const routes = [
  // ── Public pages ────────────────────────────────────────────────────
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/HomeView.vue'),
    meta: { title: 'Home' },
  },
  {
    path: '/inventory',
    name: 'Inventory',
    component: () => import('../views/InventoryView.vue'),
    meta: { title: 'Browse Inventory' },
  },
  {
    path: '/inventory/:id',
    name: 'CarDetail',
    component: () => import('../views/CarDetailView.vue'),
    meta: { title: 'Vehicle Details' },
  },
  {
    path: '/about',
    name: 'About',
    component: () => import('../views/AboutView.vue'),
    meta: { title: 'About Us' },
  },
  {
    path: '/contact',
    name: 'Contact',
    component: () => import('../views/ContactView.vue'),
    meta: { title: 'Contact Us' },
  },
  // ── Optional pages (shown only when toggled on in admin settings) ────
  {
    path: '/staff',
    name: 'Staff',
    component: () => import('../views/StaffView.vue'),
    meta: { title: 'Meet the Staff' },
  },
  {
    path: '/financing',
    name: 'Financing',
    component: () => import('../views/FinancingView.vue'),
    meta: { title: 'Financing' },
  },
  {
    path: '/schedule-service',
    name: 'ScheduleService',
    component: () => import('../views/ScheduleServiceView.vue'),
    meta: { title: 'Schedule Service' },
  },

  // ── Admin panel ──────────────────────────────────────────────────────
  {
    path: '/admin/login',
    name: 'AdminLogin',
    component: () => import('../views/admin/AdminLoginView.vue'),
    meta: { title: 'Admin Login', isAdminPublic: true },
  },
  {
    path: '/admin',
    name: 'AdminDashboard',
    component: () => import('../views/admin/AdminDashboardView.vue'),
    meta: { title: 'Admin Dashboard', requiresAdmin: true },
    redirect: '/admin/home',
    children: [
      {
        path: 'home',
        name: 'AdminHome',
        component: () => import('../views/admin/AdminHomeView.vue'),
        meta: { title: 'Dashboard', requiresAdmin: true },
      },
      {
        path: 'cars',
        name: 'AdminCars',
        component: () => import('../views/admin/AdminCarsView.vue'),
        meta: { title: 'Manage Inventory', requiresAdmin: true },
      },
      {
        path: 'cars/new',
        name: 'AdminCarNew',
        component: () => import('../views/admin/AdminCarFormView.vue'),
        meta: { title: 'Add Vehicle', requiresAdmin: true },
      },
      {
        path: 'cars/:id/edit',
        name: 'AdminCarEdit',
        component: () => import('../views/admin/AdminCarFormView.vue'),
        meta: { title: 'Edit Vehicle', requiresAdmin: true },
      },
      {
        path: 'contacts',
        name: 'AdminContacts',
        component: () => import('../views/admin/AdminContactsView.vue'),
        meta: { title: 'Contact Submissions', requiresAdmin: true },
      },
      {
        path: 'settings',
        name: 'AdminSettings',
        component: () => import('../views/admin/AdminSettingsView.vue'),
        meta: { title: 'Site Settings', requiresAdmin: true },
      },
      {
        path: 'staff',
        name: 'AdminStaff',
        component: () => import('../views/admin/AdminStaffView.vue'),
        meta: { title: 'Manage Staff', requiresAdmin: true },
      },
    ],
  },

  // ── 404 catch-all ────────────────────────────────────────────────────
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('../views/NotFoundView.vue'),
    meta: { title: 'Page Not Found' },
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
  // Scroll to top on every route change
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) return savedPosition
    if (to.hash) return { el: to.hash, behavior: 'smooth' }
    return { top: 0, behavior: 'smooth' }
  },
})

// ── Navigation guard: protect admin routes ──────────────────────────────
router.beforeEach((to, from, next) => {
  // Update document title
  const { DEALERSHIP_NAME } = /* will be tree-shaken */ { DEALERSHIP_NAME: '' }
  if (to.meta.title) {
    document.title = to.meta.title
  }

  // Admin auth check
  if (to.meta.requiresAdmin) {
    const isLoggedIn = localStorage.getItem('admin_token')
    if (!isLoggedIn) {
      return next({ name: 'AdminLogin', query: { redirect: to.fullPath } })
    }
  }

  next()
})

export default router
