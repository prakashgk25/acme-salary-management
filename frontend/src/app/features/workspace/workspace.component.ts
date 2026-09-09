import { Component, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { EmployeeService } from '../../core/services/employee.service';
import { AnalyticsService } from '../../core/services/analytics.service';
import { ToastService } from '../../shared/services/toast.service';
import { AnalyticsSummary, Employee, EmployeePage } from '../../models/employee.model';
import { DEPARTMENTS, COUNTRY_OPTIONS, CURRENCIES } from '../../core/data/reference-data';
import { LoadingComponent } from '../../shared/components/loading/loading.component';
import { EmptyStateComponent } from '../../shared/components/empty-state/empty-state.component';
import { EmployeeModalComponent, EmployeeModalMode } from './employee-modal/employee-modal.component';

const EMPTY_PAGE: EmployeePage = {
  content: [],
  totalPages: 0,
  totalElements: 0,
  size: 25,
  number: 0,
  first: true,
  last: true,
  numberOfElements: 0,
  empty: true
};

@Component({
  selector: 'app-workspace',
  standalone: true,
  imports: [CommonModule, FormsModule, LoadingComponent, EmptyStateComponent, EmployeeModalComponent],
  templateUrl: './workspace.component.html'
})
export class WorkspaceComponent {
  private auth = inject(AuthService);
  private employeeService = inject(EmployeeService);
  private analyticsService = inject(AnalyticsService);
  private toast = inject(ToastService);
  private router = inject(Router);

  readonly departments = DEPARTMENTS;
  readonly countries = COUNTRY_OPTIONS;
  readonly currencies = CURRENCIES;
  readonly userId = this.auth.userId;

  // filter draft state (bound to the filter bar inputs)
  searchTerm = signal('');
  department = signal('');
  country = signal('');
  currency = signal('');

  pageSize = signal(25);
  pageData = signal<EmployeePage>(EMPTY_PAGE);
  loading = signal(true);
  loadError = signal('');

  analytics = signal<AnalyticsSummary | null>(null);
  analyticsLoading = signal(true);

  refreshing = signal(false);

  modalOpen = signal(false);
  modalMode = signal<EmployeeModalMode>('create');
  modalEmployee = signal<Employee | null>(null);

  readonly employees = computed(() => this.pageData().content);
  readonly hasFilters = computed(() =>
    this.searchTerm().trim() !== '' || this.department() !== '' || this.country() !== '' || this.currency() !== ''
  );

  readonly pageNumbers = computed(() => {
    const page = this.pageData();
    const windowSize = 5;
    const start = Math.max(0, Math.min(page.number - 2, page.totalPages - windowSize));
    const end = Math.min(page.totalPages, start + windowSize);
    return Array.from({ length: Math.max(0, end - Math.max(0, start)) }, (_, i) => Math.max(0, start) + i);
  });

  constructor() {
    this.loadAnalytics();
    this.loadEmployees(0);
  }

  loadAnalytics(): void {
    this.analyticsLoading.set(true);
    this.analyticsService.summary().subscribe({
      next: summary => {
        this.analytics.set(summary);
        this.analyticsLoading.set(false);
      },
      error: () => {
        this.analyticsLoading.set(false);
      }
    });
  }

  loadEmployees(page: number): void {
    this.loading.set(true);
    this.loadError.set('');
    this.employeeService
      .list({
        page,
        size: this.pageSize(),
        search: this.searchTerm().trim() || undefined,
        department: this.department() || undefined,
        country: this.country() || undefined,
        currency: this.currency() || undefined
      })
      .subscribe({
        next: page => {
          this.pageData.set(page);
          this.loading.set(false);
        },
        error: () => {
          this.loadError.set('Unable to load salary records. Check that the backend is running.');
          this.loading.set(false);
        }
      });
  }

  search(): void {
    this.loadEmployees(0);
  }

  resetFilters(): void {
    this.searchTerm.set('');
    this.department.set('');
    this.country.set('');
    this.currency.set('');
    this.loadEmployees(0);
  }

  goToPage(page: number): void {
    if (page < 0 || page >= this.pageData().totalPages) return;
    this.loadEmployees(page);
  }

  changePageSize(size: number): void {
    this.pageSize.set(size);
    this.loadEmployees(0);
  }

  refresh(): void {
    this.refreshing.set(true);
    this.loadAnalytics();
    this.employeeService
      .list({
        page: this.pageData().number,
        size: this.pageSize(),
        search: this.searchTerm().trim() || undefined,
        department: this.department() || undefined,
        country: this.country() || undefined,
        currency: this.currency() || undefined
      })
      .subscribe({
        next: page => {
          this.pageData.set(page);
          this.refreshing.set(false);
          this.toast.success('Salary records refreshed.');
        },
        error: () => {
          this.refreshing.set(false);
          this.toast.error('Could not refresh records.');
        }
      });
  }

  openAddModal(): void {
    this.modalMode.set('create');
    this.modalEmployee.set(null);
    this.modalOpen.set(true);
  }

  openEditModal(employee: Employee): void {
    this.modalMode.set('edit');
    this.modalEmployee.set(employee);
    this.modalOpen.set(true);
  }

  closeModal(): void {
    this.modalOpen.set(false);
  }

  onSaved(employee: Employee): void {
    const wasCreate = this.modalMode() === 'create';
    this.modalOpen.set(false);
    this.loadEmployees(wasCreate ? 0 : this.pageData().number);
    this.loadAnalytics();
    this.toast.success(wasCreate ? `${employee.firstName} ${employee.lastName} was added.` : `${employee.firstName} ${employee.lastName} was updated.`);
  }

  onDeleted(id: number): void {
    this.modalOpen.set(false);
    const isLastOnPage = this.pageData().numberOfElements === 1 && this.pageData().number > 0;
    this.loadEmployees(isLastOnPage ? this.pageData().number - 1 : this.pageData().number);
    this.loadAnalytics();
    this.toast.success('Employee deleted.');
  }

  initials(employee: Employee): string {
    const first = employee.firstName?.charAt(0) ?? '';
    const last = employee.lastName?.charAt(0) ?? '';
    return (first + last).toUpperCase() || '—';
  }

  avatarClass(employee: Employee): string {
    const palette = ['chip-1', 'chip-2', 'chip-3', 'chip-4', 'chip-5', 'chip-6'];
    const source = employee.employeeNumber || employee.firstName || '';
    let hash = 0;
    for (let i = 0; i < source.length; i++) hash = (hash * 31 + source.charCodeAt(i)) >>> 0;
    return palette[hash % palette.length];
  }

  logout(): void {
    this.auth.logout();
    this.router.navigateByUrl('/login');
  }
}
