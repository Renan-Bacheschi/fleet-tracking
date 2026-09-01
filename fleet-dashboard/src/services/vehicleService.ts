import { apiRequest } from './api'
import type { Vehicle } from '@/types/vehicle'

const VEHICLES_PATH = '/api/v1/vehicles'

export const vehicleService = {
  getAll(): Promise<Vehicle[]> {
    return apiRequest<Vehicle[]>(VEHICLES_PATH)
  },
}
