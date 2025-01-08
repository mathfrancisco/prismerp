// features/users/services/user.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page } from '../models/user.model';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly API_URL = 'http://localhost:8080/api/v1/users';

  constructor(private http: HttpClient) {}

  getUsers(page: number = 0, size: number = 10): Observable<Page<User>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<Page<User>>(this.API_URL, { params });
  }

  getUserById(id: number): Observable<User> {
    return this.http.get<User>(`${this.API_URL}/${id}`);
  }

  updateUser(id: number, userData: Partial<User>): Observable<User> {
    return this.http.put<User>(`${this.API_URL}/${id}`, userData);
  }

  getActiveUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.API_URL}/active`);
  }

  searchUsers(searchTerm: string): Observable<User[]> {
    return this.http.get<User[]>(`${this.API_URL}/search`, {
      params: new HttpParams().set('searchTerm', searchTerm)
    });
  }

  getUsersByRole(role: string): Observable<User[]> {
    return this.http.get<User[]>(`${this.API_URL}/role/${role}`);
  }

  getUsersByRoles(roles: string[]): Observable<User[]> {
    return this.http.get<User[]>(`${this.API_URL}/roles`, {
      params: new HttpParams().set('roles', roles.join(','))
    });
  }
}
