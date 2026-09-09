# ACME Salary Manager — Frontend

Angular 22 standalone-component app for the ACME Salary Manager HR portal.

## Run it

```bash
npm install
npm start
```

This starts the dev server on http://localhost:4200 and expects the backend
API at http://localhost:8080 (configured in `src/environments/environment.ts`).


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
