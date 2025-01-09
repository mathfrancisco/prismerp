
export interface UserDTO {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  role: Role;
  password?: string;
}

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}


export enum Role {
  ADMIN = 'ADMIN',
  USER = 'USER',
  MANAGER = 'MANAGER',
  ACCOUNTANT = 'ACCOUNTANT',
  SALES_REPRESENTATIVE = 'SALES_REPRESENTATIVE'
}

export interface AuthenticationRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
  firstName: string;
  lastName: string;
}

export interface AuthenticationResponse {
  token: string;
  user: UserDTO;
}

export interface ForgotPasswordRequest {
  email: string;
}

export interface ResetPasswordRequest {
  token: string;
  newPassword: string;
}



