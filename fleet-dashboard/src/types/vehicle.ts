export type VehicleType = 'TRUCK' | 'VAN' | 'CAR' | 'MOTORCYCLE'

export type VehicleStatus = 'ACTIVE' | 'INACTIVE' | 'MAINTENANCE'

export interface Vehicle {
  id: string
  licensePlate: string
  fleetCode: string
  brand: string
  model: string
  modelYear: number
  type: VehicleType
  status: VehicleStatus
  createdAt: string
  updatedAt: string
}
