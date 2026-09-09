import { Injectable, computed, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';


export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  success: boolean;
  message: string;
}

const STORAGE_KEY = 'acme_salary_user';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);

  private readonly currentUserId = signal<string | null>(localStorage.getItem(STORAGE_KEY));

  readonly isAuthenticated = computed(() => this.currentUserId() !== null);
  readonly userId = computed(() => this.currentUserId() ?? 'HRadmin');

  login(request: LoginRequest): Observable<LoginResponse> {
    
    const body = { username: request.username, password: request.password };
    return this.http.post<LoginResponse>(`${environment.apiUrl}/api/auth/login`, body).pipe(
      tap(response => {
        if (response.success) {
          localStorage.setItem(STORAGE_KEY, request.username);
          this.currentUserId.set(request.username);
        }
      })
    );
  }

  logout(): void {
    localStorage.removeItem(STORAGE_KEY);
    this.currentUserId.set(null);
  }
}
