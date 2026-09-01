<script setup lang="ts">
defineProps<{
  label: string
  value: string
  tone: 'active' | 'maintenance' | 'inactive'
}>()
</script>

<template>
  <article class="metric-card" :class="`metric-card--${tone}`">
    <div class="metric-card__heading">
      <span class="metric-card__indicator" aria-hidden="true"></span>
      <span>{{ label }}</span>
    </div>
    <strong>{{ value }}</strong>
    <span class="metric-card__caption">Aguardando dados da frota</span>
  </article>
</template>

<style scoped>
.metric-card {
  position: relative;
  min-height: 8.5rem;
  overflow: hidden;
  padding: 1.2rem 1.25rem;
  border: 1px solid var(--color-border);
  border-radius: 0.75rem;
  background: linear-gradient(145deg, rgba(20, 27, 36, 0.96), rgba(15, 21, 28, 0.96));
}

.metric-card::after {
  position: absolute;
  inset: auto -1.5rem -2rem auto;
  width: 6rem;
  height: 6rem;
  border-radius: 50%;
  background: currentColor;
  content: '';
  filter: blur(42px);
  opacity: 0.06;
}

.metric-card__heading {
  display: flex;
  align-items: center;
  gap: 0.55rem;
  color: var(--color-text-secondary);
  font-size: 0.78rem;
  font-weight: 550;
}

.metric-card__indicator {
  width: 0.45rem;
  height: 0.45rem;
  border-radius: 50%;
  background: currentColor;
  box-shadow: 0 0 0 3px color-mix(in srgb, currentColor 10%, transparent);
}

.metric-card strong {
  display: block;
  margin-top: 0.9rem;
  color: var(--color-text-strong);
  font-size: 1.75rem;
  font-weight: 580;
  line-height: 1;
}

.metric-card__caption {
  display: block;
  margin-top: 0.65rem;
  color: var(--color-text-muted);
  font-size: 0.67rem;
}

.metric-card--active {
  color: var(--color-accent);
}

.metric-card--maintenance {
  color: var(--color-warning);
}

.metric-card--inactive {
  color: #768395;
}
</style>
