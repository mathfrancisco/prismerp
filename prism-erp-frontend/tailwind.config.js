/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      colors: {
        primary: '#2563eb',
        secondary: '#4f46e5',
        success: '#16a34a',
        warning: '#eab308',
        danger: '#dc2626',
      },
    },
  },
  plugins: [],
}

