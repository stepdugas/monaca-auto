/**
 * Scheduled function — pings the Render backend every 10 minutes
 * to prevent it from spinning down on the free tier.
 *
 * Schedule: every 10 minutes
 * Docs: https://docs.netlify.com/functions/scheduled-functions/
 */
const BACKEND_URL = 'https://monaca-auto-sales.onrender.com/actuator/health'

export const handler = async () => {
  try {
    const res = await fetch(BACKEND_URL)
    console.log(`keep-alive: ${res.status} ${res.statusText}`)
  } catch (err) {
    console.error(`keep-alive failed: ${err.message}`)
  }
}

export const config = {
  schedule: '*/10 * * * *',
}
