/**
 * usePageMeta — sets <title>, <meta name="description">, Open Graph tags,
 * Twitter Card tags, canonical URL, and robots directive for every page.
 *
 * Usage (call at the top of each view's <script setup>):
 *
 *   usePageMeta(() => ({
 *     title:       'Browse Inventory',          // appended "| Monaca Auto Sales"
 *     description: 'Shop our used car lot…',
 *     path:        '/inventory',                // used for canonical + og:url
 *     ogImage:     'https://…',                 // optional, falls back to default
 *     noindex:     false,                       // true for admin/404 pages
 *   }))
 *
 * Pass a GETTER FUNCTION so reactive dependencies (siteSettings, car ref, etc.)
 * are automatically tracked — meta re-runs whenever they change.
 */

import { watchEffect, onUnmounted } from 'vue'
import { siteSettings } from './useSiteSettings'
import { SITE_URL } from '../config'

// Default OG image — dealership lot hero
const DEFAULT_OG_IMAGE =
  'https://images.unsplash.com/photo-1550355291-bbee04a92027?auto=format&fit=crop&w=1200&q=80'

// ── DOM helpers ────────────────────────────────────────────────────────────────

function setTag(selector, attrKey, attrVal, content) {
  if (!content) return
  let el = document.querySelector(selector)
  if (!el) {
    el = document.createElement('meta')
    el.setAttribute(attrKey, attrVal)
    document.head.appendChild(el)
  }
  el.setAttribute('content', String(content))
}

function setName(name, content)     { setTag(`meta[name="${name}"]`,     'name',     name, content) }
function setProperty(prop, content) { setTag(`meta[property="${prop}"]`, 'property', prop, content) }

function setCanonical(url) {
  let el = document.querySelector('link[rel="canonical"]')
  if (!el) {
    el = document.createElement('link')
    el.setAttribute('rel', 'canonical')
    document.head.appendChild(el)
  }
  el.setAttribute('href', url)
}

// ── Main export ────────────────────────────────────────────────────────────────

export function usePageMeta(getOptions) {
  const stop = watchEffect(() => {
    const opts = typeof getOptions === 'function' ? getOptions() : getOptions

    const {
      title,
      description = '',
      ogTitle,
      ogDescription,
      ogImage = DEFAULT_OG_IMAGE,
      ogType = 'website',
      path,
      noindex = false,
    } = opts

    const name      = siteSettings.businessName || 'Monaca Auto Sales'
    const fullTitle = title ? `${title} | ${name}` : name

    // ── <title> ──────────────────────────────────────────────────────────────
    document.title = fullTitle

    // ── Meta description ─────────────────────────────────────────────────────
    if (description) setName('description', description)

    // ── Robots ───────────────────────────────────────────────────────────────
    setName('robots', noindex ? 'noindex, nofollow' : 'index, follow')

    // ── Canonical ────────────────────────────────────────────────────────────
    if (path !== undefined && SITE_URL && !SITE_URL.includes('[')) {
      setCanonical(`${SITE_URL}${path}`)
      setProperty('og:url', `${SITE_URL}${path}`)
    }

    // ── Open Graph ───────────────────────────────────────────────────────────
    setProperty('og:type',        ogType)
    setProperty('og:title',       ogTitle || fullTitle)
    setProperty('og:description', ogDescription || description)
    setProperty('og:image',       ogImage)
    setProperty('og:site_name',   name)

    // ── Twitter Card ─────────────────────────────────────────────────────────
    setName('twitter:card',        'summary_large_image')
    setName('twitter:title',       ogTitle || fullTitle)
    setName('twitter:description', ogDescription || description)
    setName('twitter:image',       ogImage)
  })

  onUnmounted(stop)
}
