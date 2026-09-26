<script setup lang="ts">
import type { Vehicle } from '@/types/vehicle'

defineProps<{
  vehicles: Vehicle[]
}>()

const emit = defineEmits<{
  select: [id: string]
}>()
</script>

<template>
  <div class="table-shell">
    <div class="table-scroll">
      <table>
        <thead>
          <tr>
            <th>Placa</th>
            <th>Código da frota</th>
            <th>Veículo</th>
            <th>Tipo</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="vehicle in vehicles"
            :key="vehicle.id"
            class="vehicle-row"
            tabindex="0"
            @click="emit('select', vehicle.id)"
            @keydown.enter="emit('select', vehicle.id)"
            @keydown.space.prevent="emit('select', vehicle.id)"
          >
            <td>{{ vehicle.licensePlate }}</td>
            <td>{{ vehicle.fleetCode }}</td>
            <td>{{ vehicle.brand }} {{ vehicle.model }}</td>
            <td>{{ vehicle.type }}</td>
            <td>
              <span class="status" :class="`status--${vehicle.status.toLowerCase()}`">
                {{ vehicle.status }}
              </span>
            </td>
          </tr>
          <tr v-if="vehicles.length === 0">
            <td class="empty-cell" colspan="5">
              <div class="empty-state">
                <span class="empty-state__icon" aria-hidden="true">
                  <svg viewBox="0 0 32 32" fill="none">
                    <path d="M7 21v-8.3c0-1 .6-1.9 1.6-2.2l2.5-.8L13 7h6l1.9 2.7 2.5.8c1 .3 1.6 1.2 1.6 2.2V21" />
                    <path d="M7 16h18M11.5 16l1.1-3.5h6.8l1.1 3.5M9 21h14" />
                    <circle cx="11" cy="22.5" r="1.5" />
                    <circle cx="21" cy="22.5" r="1.5" />
                  </svg>
                </span>
                <strong>Nenhum veículo encontrado.</strong>
                <p>Os veículos cadastrados pela API aparecerão aqui.</p>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<style scoped>
.table-shell {
  overflow: hidden;
  border: 1px solid var(--color-border);
  border-radius: 0.75rem;
  background: var(--color-surface);
}

.table-scroll {
  overflow-x: auto;
}

table {
  width: 100%;
  min-width: 700px;
  border-collapse: collapse;
}

th {
  height: 3rem;
  padding: 0 1.15rem;
  border-bottom: 1px solid var(--color-border);
  background: rgba(255, 255, 255, 0.012);
  color: var(--color-text-muted);
  font-size: 0.65rem;
  font-weight: 680;
  letter-spacing: 0.075em;
  text-align: left;
  text-transform: uppercase;
}

td {
  padding: 0.9rem 1.15rem;
  border-bottom: 1px solid var(--color-border);
  color: var(--color-text-primary);
  font-size: 0.8rem;
}

tbody tr:last-child td {
  border-bottom: 0;
}

.vehicle-row {
  cursor: pointer;
  transition: background 160ms ease;
}

.vehicle-row:hover,
.vehicle-row:focus {
  background: rgba(55, 202, 146, 0.055);
  outline: 0;
}

.vehicle-row:focus-visible {
  outline: 2px solid rgba(55, 202, 146, 0.7);
  outline-offset: -2px;
}

.status {
  display: inline-flex;
  align-items: center;
  padding: 0.28rem 0.48rem;
  border-radius: 999px;
  font-size: 0.66rem;
  font-weight: 680;
  letter-spacing: 0.04em;
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

.empty-cell {
  height: 19rem;
  padding: 2rem;
}

.empty-state {
  display: flex;
  align-items: center;
  flex-direction: column;
  justify-content: center;
  text-align: center;
}

.empty-state__icon {
  display: grid;
  width: 3.5rem;
  height: 3.5rem;
  margin-bottom: 1rem;
  place-items: center;
  border: 1px solid var(--color-border-strong);
  border-radius: 0.9rem;
  background: var(--color-surface-raised);
  color: #667283;
}

.empty-state__icon svg {
  width: 2rem;
  height: 2rem;
  stroke: currentColor;
  stroke-linecap: round;
  stroke-linejoin: round;
  stroke-width: 1.4;
}

.empty-state strong {
  color: var(--color-text-primary);
  font-size: 0.85rem;
  font-weight: 590;
}

.empty-state p {
  max-width: 25rem;
  margin: 0.4rem 0 0;
  color: var(--color-text-muted);
  font-size: 0.75rem;
  line-height: 1.6;
}
</style>
