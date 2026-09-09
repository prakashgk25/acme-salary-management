import { CountryOption } from '../../models/employee.model';

export const DEPARTMENTS: string[] = [
  'Engineering',
  'Finance',
  'HR',
  'Sales',
  'Marketing',
  'Operations',
  'Legal',
  'IT',
  'Product',
  'Customer Success'
];

export const COUNTRY_OPTIONS: CountryOption[] = [
  { country: 'India', currency: 'INR' },
  { country: 'USA', currency: 'USD' },
  { country: 'UK', currency: 'GBP' },
  { country: 'Germany', currency: 'EUR' },
  { country: 'France', currency: 'EUR' },
  { country: 'Canada', currency: 'CAD' },
  { country: 'Australia', currency: 'AUD' },
  { country: 'Japan', currency: 'JPY' },
  { country: 'Singapore', currency: 'SGD' },
  { country: 'Brazil', currency: 'BRL' },
  { country: 'UAE', currency: 'AED' },
  { country: 'China', currency: 'CNY' }
];

export const CURRENCIES: string[] = Array.from(
  new Set(COUNTRY_OPTIONS.map(option => option.currency))
).sort();

export function currencyForCountry(country: string): string | undefined {
  return COUNTRY_OPTIONS.find(option => option.country === country)?.currency;
}
