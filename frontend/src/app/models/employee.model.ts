export interface Employee {
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

export interface EmployeePage {
  content: Employee[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
  numberOfElements: number;
  empty: boolean;
}

export interface EmployeeRequest {
  employeeNumber: string;
  firstName: string;
  lastName: string;
  department: string;
  country: string;
  currency: string;
  annualSalary: number;
  effectiveDate: string;
  version?: number;
}

export interface EmployeeQuery {
  page: number;
  size: number;
  search?: string;
  country?: string;
  department?: string;
  currency?: string;
}

export interface AnalyticsSummary {
  employeeCount: number;
  averageUsdSalary: number;
  totalUsdPayroll: number;
}

export interface CountryOption {
  country: string;
  currency: string;
}
