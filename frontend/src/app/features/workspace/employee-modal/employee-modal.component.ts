import { Component, EventEmitter, Input, OnInit, Output, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { EmployeeService } from '../../../core/services/employee.service';
import { Employee, EmployeeRequest } from '../../../models/employee.model';
import { COUNTRY_OPTIONS, CURRENCIES, DEPARTMENTS, currencyForCountry } from '../../../core/data/reference-data';
import { ConfirmDialogComponent } from '../../../shared/components/confirm-dialog/confirm-dialog.component';

export type EmployeeModalMode = 'create' | 'edit';

@Component({
  selector: 'app-employee-modal',
  standalone: true,
  imports: [ReactiveFormsModule, ConfirmDialogComponent],
  templateUrl: './employee-modal.component.html'
})
export class EmployeeModalComponent implements OnInit {
  private fb = inject(FormBuilder);
  private api = inject(EmployeeService);

  @Input() mode: EmployeeModalMode = 'create';
  @Input() employee: Employee | null = null;

  @Output() saved = new EventEmitter<Employee>();
  @Output() deleted = new EventEmitter<number>();
  @Output() closed = new EventEmitter<void>();

  readonly departments = DEPARTMENTS;
  readonly countries = COUNTRY_OPTIONS;
  readonly currencies = CURRENCIES;

  saving = signal(false);
  error = signal('');
  confirmingDelete = signal(false);
  deleting = signal(false);

  form = this.fb.nonNullable.group({
    employeeNumber: ['', [Validators.required]],
    firstName: ['', [Validators.required]],
    lastName: ['', [Validators.required]],
    department: ['Engineering', [Validators.required]],
    country: ['India', [Validators.required]],
    currency: ['INR', [Validators.required]],
    annualSalary: [0, [Validators.required, Validators.min(0)]],
    effectiveDate: ['', [Validators.required]]
  });

  ngOnInit(): void {
    if (this.mode === 'edit' && this.employee) {
      const employee = this.employee;
      this.form.patchValue({
        employeeNumber: employee.employeeNumber,
        firstName: employee.firstName,
        lastName: employee.lastName,
        department: employee.department,
        country: employee.country,
        currency: employee.currency,
        annualSalary: employee.annualSalary,
        effectiveDate: employee.effectiveDate
      });
    } else {
      this.form.patchValue({ effectiveDate: this.today() });
    }
  }

  onCountryChange(): void {
    const country = this.form.controls.country.value;
    const matchedCurrency = currencyForCountry(country);
    if (matchedCurrency) {
      this.form.controls.currency.setValue(matchedCurrency);
    }
  }

  save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.saving.set(true);
    this.error.set('');
    const value = this.form.getRawValue();
    const body: EmployeeRequest = {
      employeeNumber: value.employeeNumber,
      firstName: value.firstName,
      lastName: value.lastName,
      department: value.department,
      country: value.country,
      currency: value.currency,
      annualSalary: value.annualSalary,
      effectiveDate: value.effectiveDate
    };

    if (this.mode === 'edit' && this.employee) {
      body.version = this.employee.version;
      this.api.update(this.employee.id, body).subscribe({
        next: updated => {
          this.saving.set(false);
          this.saved.emit(updated);
        },
        error: err => this.handleSaveError(err)
      });
    } else {
      this.api.create(body).subscribe({
        next: created => {
          this.saving.set(false);
          this.saved.emit(created);
        },
        error: err => this.handleSaveError(err)
      });
    }
  }

  requestDelete(): void {
    this.confirmingDelete.set(true);
  }

  cancelDelete(): void {
    this.confirmingDelete.set(false);
  }

  confirmDelete(): void {
    if (!this.employee) return;
    this.deleting.set(true);
    this.api.remove(this.employee.id, this.employee.version).subscribe({
      next: () => {
        this.deleting.set(false);
        this.confirmingDelete.set(false);
        this.deleted.emit(this.employee!.id);
      },
      error: () => {
        this.deleting.set(false);
        this.confirmingDelete.set(false);
        this.error.set('Could not delete this employee. Please try again.');
      }
    });
  }

  private handleSaveError(err: { status?: number }): void {
    this.saving.set(false);
    this.error.set(
      err?.status === 409
        ? 'This record was changed by someone else. Close and reopen it to see the latest version.'
        : 'Could not save this employee. Check the entered data and try again.'
    );
  }

  private today(): string {
    return new Date().toISOString().slice(0, 10);
  }
}
