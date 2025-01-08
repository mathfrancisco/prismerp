import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page, UserDTO, Role } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly API_URL = 'http://localhost:8080/api/v1/users';

  constructor(private http: HttpClient) {}

  getUsers(page: number = 0, size: number = 10): Observable<Page<UserDTO>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<Page<UserDTO>>(this.API_URL, { params });
  }

  getUserById(id: number): Observable<UserDTO> {
    return this.http.get<UserDTO>(`${this.API_URL}/${id}`);
  }

  createUser(userDTO: UserDTO): Observable<UserDTO> {
    return this.http.post<UserDTO>(this.API_URL, userDTO);
  }

  updateUser(id: number, userDTO: UserDTO): Observable<UserDTO> {
    return this.http.put<UserDTO>(`${this.API_URL}/${id}`, userDTO);
  }

  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }

  getActiveUsers(): Observable<UserDTO[]> {
    return this.http.get<UserDTO[]>(`${this.API_URL}/active`);
  }

  searchUsers(searchTerm: string): Observable<UserDTO[]> {
    const params = new HttpParams().set('searchTerm', searchTerm);
    return this.http.get<UserDTO[]>(`${this.API_URL}/search`, { params });
  }

  getUsersByRole(role: string): Observable<UserDTO[]> { // Use o enum Role aqui
    return this.http.get<UserDTO[]>(`${this.API_URL}/role/${role}`);
  }


  getUsersByRoles(roles: Role[]): Observable<UserDTO[]> {
    const params = new HttpParams().set('roles', roles.join(','));
    return this.http.get<UserDTO[]>(`${this.API_URL}/roles`, { params });
  }

  getUsersByEmailPattern(emailPattern: string): Observable<UserDTO[]> {
    const params = new HttpParams().set('emailPattern', emailPattern);
    return this.http.get<UserDTO[]>(`${this.API_URL}/email-pattern`, { params });
  }
}
