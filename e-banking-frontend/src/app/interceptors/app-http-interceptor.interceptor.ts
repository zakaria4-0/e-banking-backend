import { HttpInterceptorFn } from '@angular/common/http';
import {AuthService} from "../services/auth.service";


export const appHttpInterceptorInterceptor: HttpInterceptorFn = (req, next) => {
  let request = req.clone().headers.set('Authorization','Bearer ')
  return next(req);
};
