export interface AddressDTO {
  street: string;
  number: string;
  complement: string;
  neighborhood: string;
  city: string;
  state: string;
  zipCode: string;
}

export interface CompanyDTO {
  id: number;
  name: string;
  documentNumber: string;
  email: string;
  phone: string;
  address: AddressDTO;
  active: boolean;
}
