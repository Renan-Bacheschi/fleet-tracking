<script setup lang="ts">
import { useRouter } from 'vue-router'

import AppIcon from './AppIcon.vue'

const router = useRouter()
</script>

<template>
  <header class="topbar">
    <div>
      <p class="topbar__eyebrow">Central de operação</p>
      <h1>Veículos</h1>
    </div>

    <div class="profile">
      <span class="profile__avatar" aria-hidden="true">OP</span>
      <div class="profile__copy">
        <strong>Operador</strong>
        <span>Central de monitoramento</span>
      </div>
      <button class="profile__logout" type="button" aria-label="Sair" title="Sair" @click="router.push('/login')">
        <AppIcon name="logout" />
      </button>
    </div>
  </header>
</template>

<style scoped>
.topbar {
  display: flex;
  min-height: var(--header-height);
  align-items: center;
  justify-content: space-between;
  gap: 1.5rem;
  padding: 0 2rem;
  border-bottom: 1px solid var(--color-border);
  background: rgba(10, 14, 19, 0.82);
  backdrop-filter: blur(14px);
}

.topbar__eyebrow {
  margin: 0 0 0.2rem;
  color: var(--color-text-muted);
  font-size: 0.68rem;
  font-weight: 650;
  letter-spacing: 0.09em;
  text-transform: uppercase;
}

h1 {
  margin: 0;
  color: var(--color-text-strong);
  font-size: 1.28rem;
  font-weight: 640;
  letter-spacing: -0.025em;
}

.profile {
  display: flex;
  align-items: center;
  gap: 0.7rem;
}

.profile__avatar {
  display: grid;
  width: 2.15rem;
  height: 2.15rem;
  place-items: center;
  border: 1px solid rgba(55, 202, 146, 0.25);
  border-radius: 50%;
  background: rgba(55, 202, 146, 0.1);
  color: #a5f4d4;
  font-size: 0.65rem;
  font-weight: 700;
}

.profile__copy {
  display: grid;
  gap: 0.08rem;
}

.profile__copy strong {
  color: var(--color-text-primary);
  font-size: 0.78rem;
  font-weight: 600;
}

.profile__copy span {
  color: var(--color-text-muted);
  font-size: 0.65rem;
}

.profile__logout {
  display: grid;
  width: 2rem;
  height: 2rem;
  margin-left: 0.25rem;
  place-items: center;
  border: 1px solid transparent;
  border-radius: 0.45rem;
  background: transparent;
  color: var(--color-text-muted);
  cursor: pointer;
  transition: border-color 160ms ease, color 160ms ease, background 160ms ease;
}

.profile__logout:hover {
  border-color: var(--color-border-strong);
  background: var(--color-surface-raised);
  color: var(--color-text-primary);
}

@media (max-width: 600px) {
  .topbar {
    min-height: 4.5rem;
    padding: 0 1rem;
  }

  .profile__copy {
    display: none;
  }
}
</style>
