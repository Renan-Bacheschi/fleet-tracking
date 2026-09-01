<script setup lang="ts">
import { ref } from 'vue'

import AppIcon from '@/components/AppIcon.vue'
import AppSidebar from '@/components/AppSidebar.vue'
import DashboardHeader from '@/components/DashboardHeader.vue'
import MetricCard from '@/components/MetricCard.vue'
import VehicleTable from '@/components/VehicleTable.vue'
import type { Vehicle } from '@/types/vehicle'

const vehicles = ref<Vehicle[]>([])
</script>

<template>
  <div class="dashboard-shell">
    <AppSidebar />

    <div class="dashboard-main">
      <DashboardHeader />

      <main class="dashboard-content">
        <div class="page-intro">
          <div>
            <h2>Gestão da frota</h2>
            <p>Acompanhe a disponibilidade e os dados dos veículos cadastrados.</p>
          </div>
          <span class="page-intro__date">Dados da operação</span>
        </div>

        <section class="metrics-grid" aria-label="Indicadores da frota">
          <MetricCard label="Veículos ativos" value="—" tone="active" />
          <MetricCard label="Em manutenção" value="—" tone="maintenance" />
          <MetricCard label="Inativos" value="—" tone="inactive" />
        </section>

        <section class="vehicles-section" aria-labelledby="vehicles-heading">
          <div class="vehicles-section__header">
            <div>
              <h2 id="vehicles-heading">Lista de veículos</h2>
              <p>Veículos cadastrados e seus estados operacionais.</p>
            </div>
            <label class="search-field">
              <span class="sr-only">Buscar veículo</span>
              <AppIcon name="search" />
              <input type="search" placeholder="Buscar veículo" disabled />
            </label>
          </div>

          <VehicleTable :vehicles="vehicles" />
        </section>
      </main>
    </div>
  </div>
</template>

<style scoped>
.dashboard-shell {
  min-height: 100vh;
  background: var(--color-canvas);
}

.dashboard-main {
  min-width: 0;
  margin-left: var(--sidebar-width);
}

.dashboard-content {
  width: min(100%, 90rem);
  margin: 0 auto;
  padding: 2rem;
}

.page-intro {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  gap: 1rem;
  margin-bottom: 1.5rem;
}

.page-intro h2,
.vehicles-section h2 {
  margin: 0;
  color: var(--color-text-strong);
  font-size: 0.95rem;
  font-weight: 610;
  letter-spacing: -0.015em;
}

.page-intro p,
.vehicles-section p {
  margin: 0.38rem 0 0;
  color: var(--color-text-muted);
  font-size: 0.73rem;
}

.page-intro__date {
  padding: 0.38rem 0.55rem;
  border: 1px solid var(--color-border);
  border-radius: 0.35rem;
  color: var(--color-text-muted);
  font-size: 0.65rem;
}

.metrics-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 1rem;
}

.vehicles-section {
  margin-top: 2rem;
}

.vehicles-section__header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
  margin-bottom: 0.85rem;
}

.search-field {
  display: flex;
  width: 13rem;
  height: 2.35rem;
  align-items: center;
  gap: 0.55rem;
  padding: 0 0.75rem;
  border: 1px solid var(--color-border);
  border-radius: 0.45rem;
  background: var(--color-surface);
  color: var(--color-text-muted);
  opacity: 0.7;
}

.search-field input {
  min-width: 0;
  flex: 1;
  border: 0;
  outline: 0;
  background: transparent;
  color: var(--color-text-primary);
  font: inherit;
  font-size: 0.72rem;
}

.search-field input::placeholder {
  color: var(--color-text-muted);
}

@media (max-width: 900px) {
  .metrics-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 780px) {
  .dashboard-main {
    margin-left: 0;
  }

  .dashboard-content {
    padding: 1.25rem 1rem 2rem;
  }
}

@media (max-width: 600px) {
  .page-intro__date {
    display: none;
  }

  .vehicles-section__header {
    align-items: stretch;
    flex-direction: column;
  }

  .search-field {
    width: 100%;
  }
}
</style>
