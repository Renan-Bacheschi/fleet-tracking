<script setup lang="ts">
withDefaults(
  defineProps<{
    compact?: boolean
  }>(),
  {
    compact: false,
  },
)
</script>

<template>
  <div class="brand" :class="{ 'brand--compact': compact }" aria-label="Fleet Tracking">
    <span class="brand__mark" aria-hidden="true">
      <svg viewBox="0 0 32 32" fill="none">
        <path d="M16 4.5 26 10v12L16 27.5 6 22V10L16 4.5Z" stroke="currentColor" stroke-width="1.7" />
        <path d="m10.5 19 3.7-6 3.1 4 4.2-6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
        <circle cx="10.5" cy="19" r="1.5" fill="currentColor" />
        <circle cx="21.5" cy="11" r="1.5" fill="currentColor" />
      </svg>
    </span>
    <span v-if="!compact" class="brand__name">Fleet Tracking</span>
  </div>
</template>

<style scoped>
.brand {
  display: inline-flex;
  align-items: center;
  gap: 0.75rem;
  color: var(--color-text-strong);
}

.brand__mark {
  display: grid;
  width: 2.25rem;
  height: 2.25rem;
  flex: 0 0 auto;
  place-items: center;
  border: 1px solid rgba(78, 223, 167, 0.34);
  border-radius: 0.65rem;
  background: rgba(55, 202, 146, 0.1);
  color: var(--color-accent);
  box-shadow: inset 0 0 18px rgba(55, 202, 146, 0.06);
}

.brand__mark svg {
  width: 1.55rem;
  height: 1.55rem;
}

.brand__name {
  font-size: 1rem;
  font-weight: 660;
  letter-spacing: -0.02em;
}

.brand--compact .brand__mark {
  width: 2rem;
  height: 2rem;
}
</style>
