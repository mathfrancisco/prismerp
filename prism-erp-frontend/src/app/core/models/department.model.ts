export interface DepartmentDTO {
  id: number;
  name: string;
  code: string;
  managerId: number | null; // Pode ser nulo
  description: string | null; // Pode ser nulo
}
