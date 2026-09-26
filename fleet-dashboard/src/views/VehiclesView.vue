<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'

import AppIcon from '@/components/AppIcon.vue'
import AppSidebar from '@/components/AppSidebar.vue'
import DashboardHeader from '@/components/DashboardHeader.vue'
import MetricCard from '@/components/MetricCard.vue'
import VehicleTable from '@/components/VehicleTable.vue'
import { vehicleService } from '@/services/vehicleService'
import type { Vehicle } from '@/types/vehicle'

const vehicles = ref<Vehicle[]>([])
const searchTerm = ref('')
const isLoading = ref(true)
const errorMessage = ref('')
const router = useRouter()

const filteredVehicles = computed(() => {
  const term = searchTerm.value.trim().toLocaleLowerCase('pt-BR')

  if (!term) {
    return vehicles.value
  }

  return vehicles.value.filter((vehicle) =>
    [vehicle.licensePlate, vehicle.fleetCode, vehicle.brand, vehicle.model].some((value) =>
      value.toLocaleLowerCase('pt-BR').includes(term),
    ),
  )
})

const activeVehicles = computed(() => vehicles.value.filter(({ status }) => status === 'ACTIVE').length)
const maintenanceVehicles = computed(
  () => vehicles.value.filter(({ status }) => status === 'MAINTENANCE').length,
)
const inactiveVehicles = computed(() => vehicles.value.filter(({ status }) => status === 'INACTIVE').length)

async function loadVehicles() {
  isLoading.value = true
  errorMessage.value = ''

  try {
    vehicles.value = await vehicleService.getAll()
  } catch {
    errorMessage.value = 'Não foi possível carregar os veículos. Verifique sua conexão e tente novamente.'
  } finally {
    isLoading.value = false
  }
}

function openVehicle(id: string) {
  router.push({ name: 'vehicle-detail', params: { id } })
}

onMounted(loadVehicles)
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
          <MetricCard
            label="Veículos ativos"
            :value="isLoading || errorMessage ? '—' : String(activeVehicles)"
            tone="active"
          />
          <MetricCard
            label="Em manutenção"
            :value="isLoading || errorMessage ? '—' : String(maintenanceVehicles)"
            tone="maintenance"
          />
          <MetricCard
            label="Inativos"
            :value="isLoading || errorMessage ? '—' : String(inactiveVehicles)"
            tone="inactive"
          />
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
              <input
                v-model="searchTerm"
                type="search"
                placeholder="Buscar veículo"
                aria-label="Buscar por placa, código da frota, marca ou modelo"
              />
            </label>
          </div>

          <div v-if="isLoading" class="feedback-state" role="status">
            Carregando veículos…
          </div>
          <div v-else-if="errorMessage" class="feedback-state feedback-state--error" role="alert">
            <p>{{ errorMessage }}</p>
            <button type="button" @click="loadVehicles">Tentar novamente</button>
          </div>
          <VehicleTable v-else :vehicles="filteredVehicles" @select="openVehicle" />
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
}

.feedback-state {
  display: grid;
  min-height: 13rem;
  place-items: center;
  padding: 2rem;
  border: 1px solid var(--color-border);
  border-radius: 0.75rem;
  background: var(--color-surface);
  color: var(--color-text-secondary);
  font-size: 0.8rem;
  text-align: center;
}

.feedback-state--error {
  gap: 0.9rem;
}

.feedback-state p {
  margin: 0;
}

.feedback-state button,
.back-button {
  padding: 0.6rem 0.85rem;
  border: 1px solid rgba(55, 202, 146, 0.4);
  border-radius: 0.45rem;
  background: rgba(55, 202, 146, 0.1);
  color: #d9fff0;
  cursor: pointer;
  font-size: 0.75rem;
  font-weight: 650;
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
