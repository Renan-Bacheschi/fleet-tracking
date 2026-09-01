<script setup lang="ts">
import AppIcon from './AppIcon.vue'
import BrandLogo from './BrandLogo.vue'

const navigation = [
  { label: 'Visão geral', icon: 'overview' as const },
  { label: 'Veículos', icon: 'vehicle' as const, active: true },
  { label: 'Telemetria', icon: 'telemetry' as const, comingSoon: true },
  { label: 'Geofences', icon: 'geofence' as const, comingSoon: true },
]
</script>

<template>
  <aside class="sidebar">
    <div class="sidebar__brand">
      <BrandLogo />
    </div>

    <nav class="sidebar__nav" aria-label="Navegação principal">
      <p class="sidebar__label">Operação</p>
      <button
        v-for="item in navigation"
        :key="item.label"
        class="nav-item"
        :class="{ 'nav-item--active': item.active }"
        type="button"
        :aria-current="item.active ? 'page' : undefined"
        :disabled="item.comingSoon"
      >
        <AppIcon :name="item.icon" />
        <span>{{ item.label }}</span>
        <span v-if="item.comingSoon" class="nav-item__badge">Em breve</span>
      </button>
    </nav>

    <div class="sidebar__footer">
      <div class="connection-status">
        <span class="connection-status__dot" aria-hidden="true"></span>
        <div>
          <strong>Sistema operacional</strong>
          <span>Serviços disponíveis</span>
        </div>
      </div>
      <span class="sidebar__version">Fleet Tracking · M1.5</span>
    </div>
  </aside>
</template>

<style scoped>
.sidebar {
  position: fixed;
  inset: 0 auto 0 0;
  z-index: 10;
  display: flex;
  width: var(--sidebar-width);
  flex-direction: column;
  border-right: 1px solid var(--color-border);
  background: #0b1016;
}

.sidebar__brand {
  display: flex;
  height: var(--header-height);
  align-items: center;
  padding: 0 1.5rem;
  border-bottom: 1px solid var(--color-border);
}

.sidebar__nav {
  flex: 1;
  padding: 1.75rem 0.85rem;
}

.sidebar__label {
  margin: 0 0 0.6rem;
  padding: 0 0.75rem;
  color: var(--color-text-muted);
  font-size: 0.66rem;
  font-weight: 700;
  letter-spacing: 0.13em;
  text-transform: uppercase;
}

.nav-item {
  position: relative;
  display: flex;
  width: 100%;
  min-height: 2.75rem;
  align-items: center;
  gap: 0.75rem;
  margin-bottom: 0.2rem;
  padding: 0.7rem 0.75rem;
  border: 0;
  border-radius: 0.55rem;
  background: transparent;
  color: var(--color-text-secondary);
  font: inherit;
  font-size: 0.875rem;
  text-align: left;
  cursor: pointer;
  transition: color 160ms ease, background 160ms ease;
}

.nav-item:not(:disabled):hover {
  background: rgba(255, 255, 255, 0.035);
  color: var(--color-text-strong);
}

.nav-item--active {
  background: rgba(55, 202, 146, 0.09);
  color: #d9fff0;
}

.nav-item--active::before {
  position: absolute;
  left: -0.85rem;
  width: 2px;
  height: 1.45rem;
  border-radius: 0 2px 2px 0;
  background: var(--color-accent);
  content: '';
}

.nav-item:disabled {
  cursor: default;
  opacity: 0.58;
}

.nav-item__badge {
  margin-left: auto;
  padding: 0.2rem 0.38rem;
  border: 1px solid var(--color-border-strong);
  border-radius: 0.3rem;
  color: var(--color-text-muted);
  font-size: 0.59rem;
  font-weight: 700;
  letter-spacing: 0.035em;
  text-transform: uppercase;
}

.sidebar__footer {
  padding: 1rem 1.1rem 1.25rem;
}

.connection-status {
  display: flex;
  align-items: flex-start;
  gap: 0.65rem;
  padding: 0.85rem;
  border: 1px solid var(--color-border);
  border-radius: 0.65rem;
  background: var(--color-surface-raised);
}

.connection-status__dot {
  width: 0.45rem;
  height: 0.45rem;
  flex: 0 0 auto;
  margin-top: 0.28rem;
  border-radius: 50%;
  background: var(--color-accent);
  box-shadow: 0 0 0 3px rgba(55, 202, 146, 0.1);
}

.connection-status div {
  display: grid;
  gap: 0.14rem;
}

.connection-status strong {
  color: var(--color-text-primary);
  font-size: 0.72rem;
  font-weight: 600;
}

.connection-status span {
  color: var(--color-text-muted);
  font-size: 0.65rem;
}

.sidebar__version {
  display: block;
  margin-top: 0.8rem;
  color: #555f6d;
  font-size: 0.65rem;
  text-align: center;
}

@media (max-width: 780px) {
  .sidebar {
    position: static;
    width: 100%;
    border-right: 0;
    border-bottom: 1px solid var(--color-border);
  }

  .sidebar__brand {
    height: 4rem;
    padding: 0 1rem;
  }

  .sidebar__nav {
    display: flex;
    gap: 0.35rem;
    overflow-x: auto;
    padding: 0.5rem 1rem 0.75rem;
  }

  .sidebar__label,
  .sidebar__footer {
    display: none;
  }

  .nav-item {
    width: auto;
    min-width: max-content;
    min-height: 2.35rem;
    margin: 0;
    padding: 0.55rem 0.7rem;
  }

  .nav-item--active::before {
    inset: auto 0 -0.75rem;
    width: auto;
    height: 2px;
  }
}
</style>
