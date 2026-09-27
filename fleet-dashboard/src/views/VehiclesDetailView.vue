<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'

import AppSidebar from '@/components/AppSidebar.vue'
import DashboardHeader from '@/components/DashboardHeader.vue'
import { vehicleService } from '@/services/vehicleService'
import type { Vehicle } from '@/types/vehicle'

const route = useRoute()
const router = useRouter()
const vehicle = ref<Vehicle | null>(null)
const isLoading = ref(true)
const errorMessage = ref('')

const statusLabel = computed(() => {
  const labels = {
    ACTIVE: 'Ativo',
    MAINTENANCE: 'Em manutenção',
    INACTIVE: 'Inativo',
  }

  return vehicle.value ? labels[vehicle.value.status] : ''
})

const typeLabel = computed(() => {
  const labels = {
    TRUCK: 'Caminhão',
    VAN: 'Van',
    CAR: 'Carro',
    MOTORCYCLE: 'Motocicleta',
  }

  return vehicle.value ? labels[vehicle.value.type] : ''
})

const createdAt = computed(() => formatDate(vehicle.value?.createdAt))
const updatedAt = computed(() => formatDate(vehicle.value?.updatedAt))

function formatDate(value: string | undefined) {
  if (!value) return '—'

  return new Intl.DateTimeFormat('pt-BR', {
    dateStyle: 'medium',
    timeStyle: 'short',
  }).format(new Date(value))
}

async function loadVehicle() {
  isLoading.value = true
  errorMessage.value = ''

  try {
    vehicle.value = await vehicleService.getById(String(route.params.id))
  } catch {
    errorMessage.value = 'Não foi possível carregar os dados deste veículo. Tente novamente.'
  } finally {
    isLoading.value = false
  }
}

function goBack() {
  router.push({ name: 'vehicles' })
}

onMounted(loadVehicle)
</script>

<template>
  <div class="dashboard-shell">
    <AppSidebar />

    <div class="dashboard-main">
      <DashboardHeader />

      <main class="dashboard-content">
        <button class="back-button" type="button" @click="goBack">Voltar para a lista</button>

        <div v-if="isLoading" class="feedback-state" role="status">Carregando veículo…</div>

        <div v-else-if="errorMessage" class="feedback-state feedback-state--error" role="alert">
          <p>{{ errorMessage }}</p>
          <div class="feedback-state__actions">
            <button type="button" @click="loadVehicle">Tentar novamente</button>
            <button class="secondary-button" type="button" @click="goBack">Voltar para a lista</button>
          </div>
        </div>

        <template v-else-if="vehicle">
          <div class="page-intro">
            <div>
              <p class="eyebrow">Veículo</p>
              <h2>{{ vehicle.brand }} {{ vehicle.model }}</h2>
              <p>{{ vehicle.licensePlate }} · {{ vehicle.fleetCode }}</p>
            </div>
            <span class="status" :class="`status--${vehicle.status.toLowerCase()}`">
              {{ statusLabel }}
            </span>
          </div>

          <section class="details-card" aria-labelledby="vehicle-details-heading">
            <h3 id="vehicle-details-heading">Dados do veículo</h3>
            <dl class="details-grid">
              <div>
                <dt>Placa</dt>
                <dd>{{ vehicle.licensePlate }}</dd>
              </div>
              <div>
                <dt>Código da frota</dt>
                <dd>{{ vehicle.fleetCode }}</dd>
              </div>
              <div>
                <dt>Marca</dt>
                <dd>{{ vehicle.brand }}</dd>
              </div>
              <div>
                <dt>Modelo</dt>
                <dd>{{ vehicle.model }}</dd>
              </div>
              <div>
                <dt>Ano do modelo</dt>
                <dd>{{ vehicle.modelYear }}</dd>
              </div>
              <div>
                <dt>Tipo</dt>
                <dd>{{ typeLabel }}</dd>
              </div>
              <div>
                <dt>Status</dt>
                <dd>{{ statusLabel }}</dd>
              </div>
              <div>
                <dt>Identificador</dt>
                <dd class="details-grid__id">{{ vehicle.id }}</dd>
              </div>
              <div>
                <dt>Cadastrado em</dt>
                <dd>{{ createdAt }}</dd>
              </div>
              <div>
                <dt>Atualizado em</dt>
                <dd>{{ updatedAt }}</dd>
              </div>
            </dl>
          </section>
        </template>
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

.back-button,
.feedback-state button {
  padding: 0.6rem 0.85rem;
  border: 1px solid rgba(55, 202, 146, 0.4);
  border-radius: 0.45rem;
  background: rgba(55, 202, 146, 0.1);
  color: #d9fff0;
  cursor: pointer;
  font-size: 0.75rem;
  font-weight: 650;
}

.page-intro {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  gap: 1rem;
  margin: 1.5rem 0;
}

.eyebrow {
  margin: 0 0 0.4rem;
  color: var(--color-accent);
  font-size: 0.67rem;
  font-weight: 700;
  letter-spacing: 0.1em;
  text-transform: uppercase;
}

.page-intro h2 {
  margin: 0;
  color: var(--color-text-strong);
  font-size: 1.4rem;
  font-weight: 620;
  letter-spacing: -0.02em;
}

.page-intro > div > p:last-child {
  margin: 0.4rem 0 0;
  color: var(--color-text-muted);
  font-size: 0.78rem;
}

.details-card {
  padding: 1.4rem;
  border: 1px solid var(--color-border);
  border-radius: 0.75rem;
  background: var(--color-surface);
}

.details-card h3 {
  margin: 0 0 1.2rem;
  color: var(--color-text-strong);
  font-size: 0.9rem;
  font-weight: 610;
}

.details-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 1rem;
  margin: 0;
}

.details-grid > div {
  min-width: 0;
  padding: 0.85rem;
  border: 1px solid var(--color-border);
  border-radius: 0.55rem;
  background: var(--color-surface-raised);
}

dt {
  color: var(--color-text-muted);
  font-size: 0.66rem;
  font-weight: 680;
  letter-spacing: 0.07em;
  text-transform: uppercase;
}

dd {
  margin: 0.45rem 0 0;
  color: var(--color-text-primary);
  font-size: 0.82rem;
}

.details-grid__id {
  overflow-wrap: anywhere;
}

.status {
  display: inline-flex;
  align-items: center;
  padding: 0.32rem 0.55rem;
  border-radius: 999px;
  font-size: 0.7rem;
  font-weight: 680;
}

.status--active {
  background: rgba(55, 202, 146, 0.11);
  color: var(--color-accent);
}

.status--maintenance {
  background: rgba(216, 166, 87, 0.12);
  color: var(--color-warning);
}

.status--inactive {
  background: rgba(118, 131, 149, 0.14);
  color: #9ba7b5;
}

.feedback-state {
  display: grid;
  min-height: 16rem;
  place-items: center;
  padding: 2rem;
  border: 1px solid var(--color-border);
  border-radius: 0.75rem;
  background: var(--color-surface);
  color: var(--color-text-secondary);
  font-size: 0.8rem;
  text-align: center;
}

.feedback-state--error,
.feedback-state__actions {
  gap: 0.9rem;
}

.feedback-state p {
  margin: 0;
}

.feedback-state__actions {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
}

.secondary-button {
  border-color: var(--color-border-strong) !important;
  background: transparent !important;
  color: var(--color-text-primary) !important;
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
  .page-intro {
    align-items: flex-start;
    flex-direction: column;
  }

  .details-grid {
    grid-template-columns: 1fr;
  }
}
</style>
