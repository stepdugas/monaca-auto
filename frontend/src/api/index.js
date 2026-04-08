/**
 * API service layer — all backend calls go through here.
 * Base URL is configured in config.js / .env
 */
import axios from 'axios'
import { API_BASE_URL } from '../config'

const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: { 'Content-Type': 'application/json' },
})

// ── Request interceptor: attach admin token if present ──────────────────
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('admin_token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// ── Response interceptor: redirect to login on 401 ─────────────────────
api.interceptors.response.use(
  (res) => res,
  (err) => {
    if (err.response?.status === 401) {
      localStorage.removeItem('admin_token')
      window.location.href = '/admin/login'
    }
    return Promise.reject(err)
  },
)

// ── Manager API instance (uses manager_token) ───────────────────────────
const managerApi = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: { 'Content-Type': 'application/json' },
})

managerApi.interceptors.request.use((config) => {
  const token = localStorage.getItem('manager_token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

managerApi.interceptors.response.use(
  (res) => res,
  (err) => {
    if (err.response?.status === 401) {
      localStorage.removeItem('manager_token')
      window.location.href = '/manager/login'
    }
    return Promise.reject(err)
  },
)

// ── Inventory endpoints ─────────────────────────────────────────────────

/**
 * Fetch paginated/filtered inventory.
 * @param {Object} filters - { make, model, year, minPrice, maxPrice, maxMileage, page, size }
 */
export const getInventory = (filters = {}) =>
  api.get('/api/inventory', { params: filters })

/**
 * Fetch a single vehicle by id.
 * @param {number|string} id
 */
export const getCar = (id) => api.get(`/api/inventory/${id}`)

/**
 * Create a new vehicle (admin only).
 * @param {Object} carData
 */
export const createCar = (carData) => api.post('/api/inventory', carData)

/**
 * Update an existing vehicle (admin only).
 * @param {number|string} id
 * @param {Object} carData
 */
export const updateCar = (id, carData) => api.put(`/api/inventory/${id}`, carData)

/**
 * Delete a vehicle (admin only).
 * @param {number|string} id
 */
export const deleteCar = (id) => api.delete(`/api/inventory/${id}`)

/**
 * Refresh KBB valuation for a specific vehicle (admin only).
 * @param {number|string} id
 */
export const refreshKBBValue = (id) => api.post(`/api/inventory/${id}/refresh-kbb`)

// ── Contact endpoint ────────────────────────────────────────────────────

/**
 * Submit a contact form.
 * @param {{ name, email, phone, message, carId? }} payload
 */
export const submitContact = (payload) => api.post('/api/contact', payload)

// ── Admin settings endpoints ────────────────────────────────────────

/** Get all dealership config settings (admin). */
export const adminGetSettings = () => api.get('/api/manager/settings')

/** Bulk-update dealership config settings (admin). */
export const adminUpdateSettings = (updates) => api.put('/api/manager/settings', updates)

/** Admin dashboard stats — inventory counts + recent contacts. */
export const adminGetDashboard = () => api.get('/api/admin/dashboard')

// ── Admin auth endpoint ─────────────────────────────────────────────────

/**
 * Admin login — returns a JWT token on success.
 * @param {{ username, password }} credentials
 */
export const adminLogin = (credentials) => api.post('/api/admin/login', credentials)

/** Admin changes their own password. Requires currentPassword + newPassword. */
export const adminChangePassword = (currentPassword, newPassword) =>
  api.post('/api/admin/change-password', { currentPassword, newPassword })

/** Admin resets the manager password. Only requires newPassword. */
export const adminChangeManagerPassword = (newPassword) =>
  api.post('/api/admin/change-manager-password', { newPassword })

// ── Contact submissions (admin) ─────────────────────────────────────────

/** Fetch all contact submissions (admin only). */
export const getContactSubmissions = () => api.get('/api/admin/contacts')

// ── Reviews ─────────────────────────────────────────────────────────────────

/** Fetch all reviews (public). */
export const getReviews = () => api.get('/api/public/reviews')

/** Admin: add a review. */
export const adminCreateReview = (review) => api.post('/api/admin/reviews', review)

/** Admin: delete a review by id. */
export const adminDeleteReview = (id) => api.delete(`/api/admin/reviews/${id}`)

// ── Manager auth + endpoints ─────────────────────────────────────────────

/** Manager login — returns a JWT with MANAGER role. */
export const managerLogin = (credentials) => managerApi.post('/api/manager/login', credentials)

/** Fetch all contact submissions (manager). */
export const managerGetContacts = () => managerApi.get('/api/manager/contacts')

/** Get all dealership config settings. */
export const managerGetSettings = () => managerApi.get('/api/manager/settings')

/** Bulk-update dealership config settings. */
export const managerUpdateSettings = (updates) => managerApi.put('/api/manager/settings', updates)

/** Update a single config setting by key. */
export const managerUpdateSetting = (key, value) =>
  managerApi.put(`/api/manager/settings/${key}`, { value })

// Manager re-uses main inventory endpoints — uses managerApi so token is sent
export const managerGetInventory = (filters = {}) =>
  managerApi.get('/api/inventory', { params: filters })

export const managerCreateCar = (carData) => managerApi.post('/api/inventory', carData)

export const managerUpdateCar = (id, carData) => managerApi.put(`/api/inventory/${id}`, carData)

export const managerDeleteCar = (id) => managerApi.delete(`/api/inventory/${id}`)

export default api
