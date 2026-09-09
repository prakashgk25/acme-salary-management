import { HttpInterceptorFn } from '@angular/common/http';

// The API is session-less (login just validates credentials), so there is no
// bearer token to attach. This interceptor is kept as the seam to add one
// (e.g. reading a token from AuthService) if the backend adopts token auth.
export const authInterceptor: HttpInterceptorFn = (req, next) => next(req);
