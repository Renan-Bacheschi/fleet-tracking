<script setup lang="ts">
defineProps<{
  name: 'overview' | 'vehicle' | 'telemetry' | 'geofence' | 'search' | 'chevron' | 'logout'
}>()
</script>

<template>
  <svg v-if="name === 'overview'" viewBox="0 0 24 24" fill="none" aria-hidden="true">
    <rect x="3" y="3" width="7" height="7" rx="1.5" />
    <rect x="14" y="3" width="7" height="7" rx="1.5" />
    <rect x="3" y="14" width="7" height="7" rx="1.5" />
    <rect x="14" y="14" width="7" height="7" rx="1.5" />
  </svg>
  <svg v-else-if="name === 'vehicle'" viewBox="0 0 24 24" fill="none" aria-hidden="true">
    <path d="M4 16V8.8c0-.8.5-1.5 1.3-1.8l2.1-.7L9 4h6l1.6 2.3 2.1.7c.8.3 1.3 1 1.3 1.8V16" />
    <path d="M4 12h16M8 12l1-3h6l1 3M6 16h12" />
    <circle cx="7" cy="17.5" r="1.5" />
    <circle cx="17" cy="17.5" r="1.5" />
  </svg>
  <svg v-else-if="name === 'telemetry'" viewBox="0 0 24 24" fill="none" aria-hidden="true">
    <path d="M4 17.5a9 9 0 0 1 16 0M7.5 15a5.2 5.2 0 0 1 9 0" />
    <path d="m12 14.5 3.5-5" />
    <circle cx="12" cy="17" r="1.5" />
  </svg>
  <svg v-else-if="name === 'geofence'" viewBox="0 0 24 24" fill="none" aria-hidden="true">
    <path d="M12 21s7-5.6 7-12a7 7 0 1 0-14 0c0 6.4 7 12 7 12Z" />
    <circle cx="12" cy="9" r="2.5" />
  </svg>
  <svg v-else-if="name === 'search'" viewBox="0 0 24 24" fill="none" aria-hidden="true">
    <circle cx="11" cy="11" r="6.5" />
    <path d="m16 16 4 4" />
  </svg>
  <svg v-else-if="name === 'chevron'" viewBox="0 0 24 24" fill="none" aria-hidden="true">
    <path d="m9 18 6-6-6-6" />
  </svg>
  <svg v-else viewBox="0 0 24 24" fill="none" aria-hidden="true">
    <path d="M10 5H6a2 2 0 0 0-2 2v10a2 2 0 0 0 2 2h4M14 8l4 4-4 4M18 12H9" />
  </svg>
</template>

<style scoped>
svg {
  width: 1.15rem;
  height: 1.15rem;
  flex: 0 0 auto;
  stroke: currentColor;
  stroke-width: 1.7;
  stroke-linecap: round;
  stroke-linejoin: round;
}
</style>
