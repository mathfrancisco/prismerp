export enum EmploymentStatus {
  ACTIVE = 'ACTIVE',
  ON_LEAVE = 'ON_LEAVE',
  SUSPENDED = 'SUSPENDED',
  TERMINATED = 'TERMINATED'
}

export interface EmployeeDTO {
  id: number;
  employeeNumber: string;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  joinDate: string; // Formato de data como string (yyyy-MM-dd)
  status: EmploymentStatus;
  departmentId: number;
  salary: number;
}
