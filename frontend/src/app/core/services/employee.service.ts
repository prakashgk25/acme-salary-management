import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  Employee,
  EmployeePage,
  EmployeeQuery,
  EmployeeRequest
} from '../../models/employee.model';

@Injectable({ providedIn: 'root' })
export class EmployeeService {

  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/api/employees`;

  list(query: EmployeeQuery): Observable<EmployeePage> {
    let params = new HttpParams()
      .set('page', query.page)
      .set('size', query.size);

    if (query.search) {
      params = params.set('search', query.search);
    }

    if (query.country) {
      params = params.set('country', query.country);
    }

    if (query.department) {
      params = params.set('department', query.department);
    }

    if (query.currency) {
      params = params.set('currency', query.currency);
    }

    return this.http.get<EmployeePage>(this.url, { params });
  }

  get(id: number): Observable<Employee> {
    return this.http.get<Employee>(`${this.url}/${id}`);
  }

  create(body: EmployeeRequest): Observable<Employee> {
    return this.http.post<Employee>(this.url, body);
  }

  update(id: number, body: EmployeeRequest): Observable<Employee> {
    return this.http.put<Employee>(`${this.url}/${id}`, body);
  }

  remove(id: number, version: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`, {
      headers: {
        'If-Match-Version': version.toString()
      }
    });
  }
}