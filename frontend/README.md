# ACME Salary Manager — Frontend

Angular 22 standalone-component app for the ACME Salary Manager HR portal.

## Run it

```bash
npm install
npm start
```

This starts the dev server on http://localhost:4200 and expects the backend
API at http://localhost:8080 (configured in `src/environments/environment.ts`).

Sign in with the credentials your backend accepts (e.g. `HRadmin` / `HRadmin`).

## What's here

- **Login** (`/login`) — posts to `POST /api/auth/login`.
- **Workspace** (`/`) — the main screen: payroll/headcount/average-salary
  stats, a filterable + searchable salary records table
  (`GET /api/employees`), and modals to add, edit, and delete employees
  (`POST` / `PUT` / `DELETE /api/employees`).
  - **Reset filters** clears search/department/country/currency and reloads.
  - **Refresh** re-fetches both the analytics summary and the current page
    of employees.
  - The edit modal has a **Delete employee** action with a confirmation step.

## Build

```bash
npm run build            # production build, output in dist/
```

## Project layout

```
src/app/
  core/                 auth, employee, analytics services + guard + reference data
  features/
    auth/login/          login screen
    workspace/            main dashboard + table + employee add/edit modal
  shared/                 toast notifications, loading skeleton, empty state, confirm dialog
```
