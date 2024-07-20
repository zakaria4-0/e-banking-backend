import {CanActivateFn, Route, Router} from '@angular/router';
import {AuthService} from "../services/auth.service";
import {inject} from "@angular/core";

export const authorizationGuard: CanActivateFn = (route, state) => {
  const authService : AuthService = inject(AuthService)
  const router: Router = inject(Router);
  if (authService.roles.includes('ADMIN')){
    return true
  } else {
    router.navigateByUrl('/admin/unauthorized')
    return false
  }
};
