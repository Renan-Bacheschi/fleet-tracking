<script setup lang="ts">
import { nextTick, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'

import BrandLogo from '@/components/BrandLogo.vue'

const router = useRouter()
const form = reactive({
  email: '',
  password: '',
})
const errors = reactive({
  email: '',
  password: '',
})
const emailInput = ref<HTMLInputElement | null>(null)
const passwordInput = ref<HTMLInputElement | null>(null)

async function submit() {
  errors.email = form.email.trim() ? '' : 'Informe seu e-mail.'
  errors.password = form.password ? '' : 'Informe sua senha.'

  if (errors.email || errors.password) {
    await nextTick()
    ;(errors.email ? emailInput.value : passwordInput.value)?.focus()
    return
  }

  await router.push('/')
}
</script>

<template>
  <main class="login-page">
    <section class="login-panel">
      <div class="login-panel__content">
        <BrandLogo />

        <div class="login-copy">
          <p class="login-copy__eyebrow">Central de monitoramento</p>
          <h1>Acesse a central de operação</h1>
          <p>Entre para acompanhar a frota e os indicadores operacionais em um único lugar.</p>
        </div>

        <form novalidate @submit.prevent="submit">
          <div class="field">
            <label for="email">E-mail</label>
            <input
              id="email"
              ref="emailInput"
              v-model="form.email"
              type="email"
              name="email"
              autocomplete="email"
              placeholder="operador@empresa.com"
              :aria-invalid="Boolean(errors.email)"
              :aria-describedby="errors.email ? 'email-error' : undefined"
              @input="errors.email = ''"
            />
            <span v-if="errors.email" id="email-error" class="field__error" role="alert">{{ errors.email }}</span>
          </div>

          <div class="field">
            <div class="field__label-row">
              <label for="password">Senha</label>
              <span>Ambiente seguro</span>
            </div>
            <input
              id="password"
              ref="passwordInput"
              v-model="form.password"
              type="password"
              name="password"
              autocomplete="current-password"
              placeholder="Digite sua senha"
              :aria-invalid="Boolean(errors.password)"
              :aria-describedby="errors.password ? 'password-error' : undefined"
              @input="errors.password = ''"
            />
            <span v-if="errors.password" id="password-error" class="field__error" role="alert">{{ errors.password }}</span>
          </div>

          <button class="submit-button" type="submit">
            <span>Entrar</span>
            <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
              <path d="m9 18 6-6-6-6" />
            </svg>
          </button>
        </form>

        <p class="login-panel__footnote">Acesso exclusivo para operadores autorizados.</p>
      </div>
    </section>

    <section class="operations-panel" aria-hidden="true">
      <div class="operations-panel__grid"></div>
      <div class="operations-panel__glow"></div>
      <div class="operations-panel__content">
        <div class="operations-panel__topline">
          <span>Monitoramento em tempo real</span>
          <span class="live-indicator"><i></i> Operacional</span>
        </div>

        <div class="map-card">
          <div class="map-card__header">
            <div>
              <span>Visão da frota</span>
              <strong>Área operacional</strong>
            </div>
            <span class="map-card__time">Agora</span>
          </div>
          <div class="map-visual">
            <svg viewBox="0 0 620 340" preserveAspectRatio="xMidYMid slice">
              <path class="map-line map-line--soft" d="M-20 90C75 85 88 173 171 168s102-85 183-56 76 123 172 89 87-93 130-96" />
              <path class="map-line" d="M-10 262c72-18 90-79 167-73s87 68 153 48 58-97 125-112 109 27 200 16" />
              <path class="map-line map-line--soft" d="M85-20c5 81 37 110 18 179s-72 102-45 201M431-20c-22 75 11 109 6 172s-56 132-40 208" />
              <path class="route" d="M54 265c68-26 81-74 150-64s83 52 141 29 49-79 108-96 77-3 112-3" />
            </svg>
            <span class="map-pin map-pin--one"><i></i><b>FT-024</b></span>
            <span class="map-pin map-pin--two"><i></i><b>FT-081</b></span>
            <span class="map-pin map-pin--three"><i></i></span>
            <div class="map-card__legend">
              <span><i></i> Rota monitorada</span>
              <strong>12 veículos</strong>
            </div>
          </div>
        </div>

        <p class="operations-panel__message">Controle preciso para decisões mais rápidas.</p>
      </div>
    </section>
  </main>
</template>

<style scoped>
.login-page {
  display: grid;
  min-height: 100vh;
  grid-template-columns: minmax(30rem, 0.9fr) minmax(34rem, 1.1fr);
  background: var(--color-canvas);
}

.login-panel {
  position: relative;
  z-index: 2;
  display: grid;
  min-height: 100vh;
  place-items: center;
  padding: 3rem;
  border-right: 1px solid var(--color-border);
  background: #0a0e13;
}

.login-panel__content {
  width: min(100%, 23.5rem);
}

.login-copy {
  margin: 4.75rem 0 2.2rem;
}

.login-copy__eyebrow {
  margin: 0 0 0.75rem !important;
  color: var(--color-accent) !important;
  font-size: 0.68rem !important;
  font-weight: 700;
  letter-spacing: 0.12em;
  text-transform: uppercase;
}

.login-copy h1 {
  max-width: 22rem;
  margin: 0;
  color: var(--color-text-strong);
  font-size: clamp(2rem, 3.2vw, 2.55rem);
  font-weight: 610;
  letter-spacing: -0.045em;
  line-height: 1.12;
}

.login-copy p {
  margin: 1rem 0 0;
  color: var(--color-text-secondary);
  font-size: 0.86rem;
  line-height: 1.7;
}

form {
  display: grid;
  gap: 1.15rem;
}

.field {
  display: grid;
  gap: 0.5rem;
}

.field label {
  color: var(--color-text-primary);
  font-size: 0.75rem;
  font-weight: 580;
}

.field__label-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.field__label-row span {
  color: var(--color-text-muted);
  font-size: 0.65rem;
}

.field input {
  width: 100%;
  height: 3rem;
  padding: 0 0.9rem;
  border: 1px solid var(--color-border-strong);
  border-radius: 0.55rem;
  outline: 0;
  background: #10161e;
  color: var(--color-text-strong);
  font: inherit;
  font-size: 0.82rem;
  transition: border-color 160ms ease, box-shadow 160ms ease, background 160ms ease;
}

.field input::placeholder {
  color: #556171;
}

.field input:hover {
  border-color: #394452;
}

.field input:focus {
  border-color: rgba(55, 202, 146, 0.7);
  background: #111922;
  box-shadow: 0 0 0 3px rgba(55, 202, 146, 0.08);
}

.field input[aria-invalid='true'] {
  border-color: rgba(238, 105, 105, 0.75);
}

.field__error {
  color: #f18f8f;
  font-size: 0.68rem;
}

.submit-button {
  display: flex;
  width: 100%;
  height: 3rem;
  align-items: center;
  justify-content: center;
  gap: 0.45rem;
  margin-top: 0.3rem;
  border: 1px solid #51dca6;
  border-radius: 0.55rem;
  background: var(--color-accent);
  color: #062017;
  font: inherit;
  font-size: 0.8rem;
  font-weight: 700;
  cursor: pointer;
  box-shadow: 0 0.6rem 1.8rem rgba(55, 202, 146, 0.1);
  transition: transform 160ms ease, background 160ms ease, box-shadow 160ms ease;
}

.submit-button svg {
  width: 1rem;
  height: 1rem;
  stroke: currentColor;
  stroke-linecap: round;
  stroke-linejoin: round;
  stroke-width: 2;
  transition: transform 160ms ease;
}

.submit-button:hover {
  background: #4bdca5;
  box-shadow: 0 0.75rem 2rem rgba(55, 202, 146, 0.16);
  transform: translateY(-1px);
}

.submit-button:hover svg {
  transform: translateX(2px);
}

.login-panel__footnote {
  margin: 2.2rem 0 0;
  color: var(--color-text-muted);
  font-size: 0.66rem;
  text-align: center;
}

.operations-panel {
  position: relative;
  min-height: 100vh;
  overflow: hidden;
  background: #0c1219;
}

.operations-panel__grid {
  position: absolute;
  inset: 0;
  background-image: linear-gradient(rgba(255, 255, 255, 0.018) 1px, transparent 1px), linear-gradient(90deg, rgba(255, 255, 255, 0.018) 1px, transparent 1px);
  background-size: 42px 42px;
  mask-image: linear-gradient(to bottom, rgba(0, 0, 0, 0.8), transparent 88%);
}

.operations-panel__glow {
  position: absolute;
  top: 14%;
  left: 40%;
  width: 28rem;
  height: 28rem;
  border-radius: 50%;
  background: rgba(39, 180, 131, 0.08);
  filter: blur(110px);
}

.operations-panel__content {
  position: relative;
  z-index: 1;
  display: flex;
  min-height: 100%;
  flex-direction: column;
  padding: 2rem clamp(2rem, 5vw, 4.5rem) 2.5rem;
}

.operations-panel__topline {
  display: flex;
  align-items: center;
  justify-content: space-between;
  color: var(--color-text-muted);
  font-size: 0.65rem;
  font-weight: 600;
  letter-spacing: 0.07em;
  text-transform: uppercase;
}

.live-indicator {
  display: flex;
  align-items: center;
  gap: 0.45rem;
  color: #7a9189;
}

.live-indicator i {
  width: 0.38rem;
  height: 0.38rem;
  border-radius: 50%;
  background: var(--color-accent);
  box-shadow: 0 0 0 4px rgba(55, 202, 146, 0.08);
}

.map-card {
  width: min(100%, 42rem);
  margin: auto;
  overflow: hidden;
  border: 1px solid rgba(150, 170, 191, 0.15);
  border-radius: 0.85rem;
  background: rgba(13, 20, 28, 0.75);
  box-shadow: 0 2.5rem 6rem rgba(0, 0, 0, 0.28);
  backdrop-filter: blur(12px);
}

.map-card__header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1rem 1.2rem;
  border-bottom: 1px solid var(--color-border);
}

.map-card__header div {
  display: grid;
  gap: 0.15rem;
}

.map-card__header span {
  color: var(--color-text-muted);
  font-size: 0.62rem;
  letter-spacing: 0.06em;
  text-transform: uppercase;
}

.map-card__header strong {
  color: var(--color-text-primary);
  font-size: 0.78rem;
  font-weight: 580;
}

.map-card__time {
  padding: 0.25rem 0.45rem;
  border: 1px solid var(--color-border-strong);
  border-radius: 0.3rem;
}

.map-visual {
  position: relative;
  aspect-ratio: 1.7;
  overflow: hidden;
  background: radial-gradient(circle at 75% 28%, rgba(55, 202, 146, 0.06), transparent 28%), #0c131b;
}

.map-visual > svg {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
}

.map-line {
  fill: none;
  stroke: #27333f;
  stroke-width: 8;
}

.map-line--soft {
  stroke: #202a34;
  stroke-width: 5;
}

.route {
  fill: none;
  stroke: var(--color-accent);
  stroke-dasharray: 7 8;
  stroke-linecap: round;
  stroke-width: 2;
}

.map-pin {
  position: absolute;
  display: flex;
  align-items: center;
  gap: 0.45rem;
}

.map-pin i {
  width: 0.62rem;
  height: 0.62rem;
  border: 2px solid #0c131b;
  border-radius: 50%;
  background: var(--color-accent);
  box-shadow: 0 0 0 4px rgba(55, 202, 146, 0.16), 0 0 16px rgba(55, 202, 146, 0.5);
}

.map-pin b {
  padding: 0.3rem 0.4rem;
  border: 1px solid rgba(55, 202, 146, 0.17);
  border-radius: 0.3rem;
  background: rgba(8, 15, 20, 0.88);
  color: #a9dcca;
  font-size: 0.58rem;
  font-weight: 650;
  letter-spacing: 0.04em;
}

.map-pin--one {
  top: 53%;
  left: 29%;
}

.map-pin--two {
  top: 37%;
  left: 70%;
}

.map-pin--three {
  top: 68%;
  left: 52%;
  opacity: 0.65;
}

.map-card__legend {
  position: absolute;
  right: 1rem;
  bottom: 1rem;
  left: 1rem;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0.65rem 0.75rem;
  border: 1px solid var(--color-border);
  border-radius: 0.4rem;
  background: rgba(9, 14, 20, 0.88);
  color: var(--color-text-muted);
  font-size: 0.61rem;
  backdrop-filter: blur(8px);
}

.map-card__legend span {
  display: flex;
  align-items: center;
  gap: 0.45rem;
}

.map-card__legend i {
  width: 1.5rem;
  border-top: 1px dashed var(--color-accent);
}

.map-card__legend strong {
  color: var(--color-text-secondary);
  font-size: 0.63rem;
  font-weight: 580;
}

.operations-panel__message {
  margin: 0;
  color: #717d8b;
  font-size: 0.75rem;
  letter-spacing: 0.02em;
  text-align: center;
}

@media (max-width: 980px) {
  .login-page {
    grid-template-columns: 1fr;
  }

  .operations-panel {
    display: none;
  }

  .login-panel {
    border-right: 0;
  }
}

@media (max-width: 540px) {
  .login-panel {
    align-items: start;
    padding: 2rem 1.25rem;
  }

  .login-panel__content {
    padding-top: 0.5rem;
  }

  .login-copy {
    margin-top: 4rem;
  }
}
</style>
