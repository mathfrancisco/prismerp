
export interface UserDTO { // Use UserDTO para consistência com o backend
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  role: Role; // Use o enum Role aqui
}

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

export enum Role { // Defina o enum Role
  ADMIN = 'ADMIN',
  USER = 'USER',
  MANAGER = 'MANAGER',
  ACCOUNTANT = 'ACCOUNTANT',
  SALES_REPRESENTATIVE = 'SALES_REPRESENTATIVE'
}
