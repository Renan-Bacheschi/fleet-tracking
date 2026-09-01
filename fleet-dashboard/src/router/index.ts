import { createRouter, createWebHistory } from 'vue-router'

import LoginView from '@/views/LoginView.vue'
import VehiclesView from '@/views/VehiclesView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'vehicles',
      component: VehiclesView,
      meta: { title: 'Veículos' },
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
      meta: { title: 'Acesso' },
    },
    {
      path: '/:pathMatch(.*)*',
      redirect: '/',
    },
  ],
  scrollBehavior: () => ({ top: 0 }),
})

router.afterEach((to) => {
  document.title = `${String(to.meta.title ?? 'Central')} | Fleet Tracking`
})

export default router
