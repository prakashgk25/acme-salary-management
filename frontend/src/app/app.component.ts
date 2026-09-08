import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpParams } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

interface Employee {
  id: number;
  version: number;
  employeeNumber: string;
  firstName: string;
  lastName: string;
  department: string;
  country: string;
  currency: string;
  annualSalary: number;
  normalizedUsdSalary: number;
  effectiveDate: string;
}

interface Page {
  content: Employee[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

interface LoginResponse {
  success: boolean;
  message: string;
}

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="login-page" *ngIf="!loggedIn">
      <div class="login-card">
        <div class="login-brand">ACME</div>
        <h1>Salary Manager</h1>
        <p class="login-subtitle">HR Administration Portal</p>

        <form (ngSubmit)="login()">
          <label>
            User ID
            <input
              type="text"
              [(ngModel)]="loginUserId"
              name="userId"
              placeholder="Enter user ID"
              autocomplete="username"
              required
            />
          </label>

          <label>
            Password
            <input
              type="password"
              [(ngModel)]="loginPassword"
              name="password"
              placeholder="Enter password"
              autocomplete="current-password"
              required
            />
          </label>

          <p class="login-error" *ngIf="loginError">{{ loginError }}</p>

          <button
            type="submit"
            class="login-button"
            [disabled]="loggingIn"
          >
            {{ loggingIn ? 'Signing in...' : 'Sign in' }}
          </button>
        </form>
      </div>
    </div>

    <div class="shell" *ngIf="loggedIn">
      <header>
        <div>
          <h1>ACME Salary Manager</h1>
          <p>HR compensation workspace · 10,000 employees</p>
        </div>

        <div class="header-actions">
          <span class="signed-in">HRadmin</span>
          <button (click)="load()">Refresh</button>
          <button class="logout" (click)="logout()">Logout</button>
        </div>
      </header>

      <section class="cards">
        <div>
          <span>Employees</span>
          <strong>{{ analytics.employeeCount }}</strong>
        </div>

        <div>
          <span>Average salary (USD)</span>
          <strong>{{ analytics.averageUsdSalary | number:'1.2-2' }}</strong>
        </div>

        <div>
          <span>Total payroll (USD)</span>
          <strong>{{ analytics.totalUsdPayroll | number:'1.2-2' }}</strong>
        </div>
      </section>

      <section class="panel filters">
        <input
          [(ngModel)]="search"
          (keyup.enter)="load()"
          placeholder="Search employee / name / ID"
        />

        <select [(ngModel)]="department">
          <option value="">All departments</option>
          <option *ngFor="let d of departments" [value]="d">{{ d }}</option>
        </select>

        <select [(ngModel)]="country">
          <option value="">All countries</option>
          <option *ngFor="let c of countries" [value]="c">{{ c }}</option>
        </select>

        <select [(ngModel)]="currency">
          <option value="">All currencies</option>
          <option *ngFor="let c of currencies" [value]="c">{{ c }}</option>
        </select>

        <button (click)="load()">Search</button>
      </section>

      <section class="panel">
        <div class="table-head">
          <h2>Salary records</h2>
          <button (click)="openNew()">+ Add employee</button>
        </div>

        <div class="table-wrap">
          <table>
            <thead>
              <tr>
                <th>Employee</th>
                <th>Department</th>
                <th>Country</th>
                <th>Salary</th>
                <th>USD normalized</th>
                <th>Effective</th>
                <th></th>
              </tr>
            </thead>

            <tbody>
              <tr *ngFor="let e of page.content">
                <td>
                  <b>{{ e.firstName }} {{ e.lastName }}</b>
                  <small>{{ e.employeeNumber }}</small>
                </td>
                <td>{{ e.department }}</td>
                <td>{{ e.country }}</td>
                <td>{{ e.annualSalary | number:'1.2-2' }} {{ e.currency }}</td>
                <td>{{ e.normalizedUsdSalary | number:'1.2-2' }} USD</td>
                <td>{{ e.effectiveDate }}</td>
                <td>
                  <button class="link" (click)="edit(e)">Edit</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="pager">
          <button [disabled]="page.number === 0" (click)="go(page.number - 1)">‹</button>
          <span>Page {{ page.number + 1 }} of {{ page.totalPages }}</span>
          <button
            [disabled]="page.number + 1 >= page.totalPages"
            (click)="go(page.number + 1)"
          >›</button>
        </div>
      </section>

      <div class="modal" *ngIf="form">
        <div class="dialog">
          <h2>{{ form.id ? 'Edit' : 'Add' }} employee</h2>

          <div class="grid">
            <label>
              Employee no.
              <input [(ngModel)]="form.employeeNumber" />
            </label>

            <label>
              First name
              <input [(ngModel)]="form.firstName" />
            </label>

            <label>
              Last name
              <input [(ngModel)]="form.lastName" />
            </label>

            <label>
              Department
              <select [(ngModel)]="form.department">
                <option *ngFor="let d of departments" [value]="d">{{ d }}</option>
              </select>
            </label>

            <label>
              Country
              <select [(ngModel)]="form.country">
                <option *ngFor="let c of countries" [value]="c">{{ c }}</option>
              </select>
            </label>

            <label>
              Currency
              <select [(ngModel)]="form.currency">
                <option *ngFor="let c of currencies" [value]="c">{{ c }}</option>
              </select>
            </label>

            <label>
              Annual salary
              <input type="number" [(ngModel)]="form.annualSalary" />
            </label>

            <label>
              Effective date
              <input type="date" [(ngModel)]="form.effectiveDate" />
            </label>
          </div>

          <p class="error" *ngIf="error">{{ error }}</p>

          <footer>
            <button (click)="form = null">Cancel</button>
            <button class="primary" (click)="save()">Save</button>
          </footer>
        </div>
      </div>
    </div>
  `
})
export class AppComponent {
  private api = 'http://localhost:8080/api';

  loggedIn = false;
  loginUserId = '';
  loginPassword = '';
  loginError = '';
  loggingIn = false;

  page: Page = {
    content: [],
    totalElements: 0,
    totalPages: 0,
    number: 0,
    size: 25
  };

  analytics = {
    employeeCount: 0,
    averageUsdSalary: 0,
    totalUsdPayroll: 0
  };

  search = '';
  department = '';
  country = '';
  currency = '';
  form: any = null;
  error = '';

  departments = [
    'Engineering',
    'Developement',
    'Finance',
    'HR',
    'Sales',
    'Operations',
    'Product'
  ];

  countries = [
    'India',
    'USA',
    'UK',
    'Germany',
    'Singapore',
    'Australia'
  ];

  currencies = [
    'USD',
    'EUR',
    'GBP',
    'INR',
    'SGD',
    'AUD'
  ];

  constructor(private http: HttpClient) {
    if (sessionStorage.getItem('hrAuthenticated') === 'true') {
      this.loggedIn = true;
      this.loadDashboard();
    }
  }

  login() {
    this.loginError = '';

    if (!this.loginUserId || !this.loginPassword) {
      this.loginError = 'Please enter user ID and password.';
      return;
    }

    this.loggingIn = true;

    this.http.post<LoginResponse>(
      this.api + '/auth/login',
      {
        userId: this.loginUserId,
        password: this.loginPassword
      }
    ).subscribe({
      next: response => {
        this.loggingIn = false;

        if (response.success) {
          sessionStorage.setItem('hrAuthenticated', 'true');
          this.loggedIn = true;
          this.loadDashboard();
        } else {
          this.loginError = response.message;
        }
      },
      error: error => {
        this.loggingIn = false;
        this.loginError =
          error?.error?.message || 'Invalid user ID or password.';
      }
    });
  }

  logout() {
    sessionStorage.removeItem('hrAuthenticated');
    this.loggedIn = false;
    this.loginUserId = '';
    this.loginPassword = '';
    this.loginError = '';
    this.form = null;
  }

  loadDashboard() {
    this.load();

    this.http
      .get<any>(this.api + '/analytics/summary')
      .subscribe({
        next: response => this.analytics = response
      });
  }

  load() {
    let params = new HttpParams()
      .set('page', this.page.number)
      .set('size', 25);

    if (this.search) {
      params = params.set('search', this.search);
    }

    if (this.department) {
      params = params.set('department', this.department);
    }

    if (this.country) {
      params = params.set('country', this.country);
    }

    if (this.currency) {
      params = params.set('currency', this.currency);
    }

    this.http
      .get<Page>(this.api + '/employees', { params })
      .subscribe({
        next: response => this.page = response
      });
  }

  go(n: number) {
    this.page.number = n;
    this.load();
  }

  openNew() {
    this.error = '';

    this.form = {
      employeeNumber: '',
      firstName: '',
      lastName: '',
      department: 'Engineering',
      country: 'India',
      currency: 'INR',
      annualSalary: 50000,
      effectiveDate: new Date().toISOString().slice(0, 10),
      version: 0
    };
  }

  edit(employee: Employee) {
    this.error = '';
    this.form = { ...employee };
  }

  save() {
    this.error = '';

    const body = { ...this.form };
    delete body.id;
    delete body.normalizedUsdSalary;

    const request = this.form.id
      ? this.http.put<Employee>(
          this.api + '/employees/' + this.form.id,
          body
        )
      : this.http.post<Employee>(
          this.api + '/employees',
          body
        );

    request.subscribe({
      next: () => {
        this.form = null;
        this.load();

        this.http
          .get<any>(this.api + '/analytics/summary')
          .subscribe({
            next: response => this.analytics = response
          });
      },
      error: error => {
        this.error =
          error?.error?.error ||
          error?.error?.message ||
          'Unable to save record.';
      }
    });
  }
}
