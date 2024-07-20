import { HttpInterceptorFn } from '@angular/common/http';
import {AuthService} from "../services/auth.service";
import {inject} from "@angular/core";
import {MemoryCache} from "@angular-devkit/build-angular/src/tools/esbuild/cache";
import {error} from "@angular/compiler-cli/src/transformers/util";
import {catchError, throwError} from "rxjs";
import {Route, Router} from "@angular/router";


export const appHttpInterceptorInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router : Router = inject(Router)
  if (!req.url.includes("/auth/login")){
    console.log(authService.accessToken)

    let request = req.clone({
      headers : req.headers.set('Authorization','Bearer '+authService.accessToken)
    })
    return next(request).pipe(catchError(err=> {
      console.log('error stqtus: '+err.status)
      if (err.status == 401){
        authService.logout();
      }
      return throwError(err.message)
    }));;
  } else {
    return next(req)
  }

};
