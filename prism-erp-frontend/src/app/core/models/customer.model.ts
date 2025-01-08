// customer.model.ts
export interface Address {
  street: string;
  number: string;
  complement?: string;
  zipCode: string;
  city: string;
  state: string;
  country: string;
}

export interface CustomerDTO {
  id?: number;
  name: string;
  documentNumber: string;
  email?: string;
  phone?: string;
  address: Address;
  active: boolean;
}
