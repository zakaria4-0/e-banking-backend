import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {jwtDecode} from "jwt-decode";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  isAuthenticated : Boolean=false;
  roles : any;
  username! : any;
  accessToken! : any

  constructor(private http:HttpClient, private router: Router) { }

  public login(username: string, password : string){
    let options = {
      headers : new HttpHeaders().set("Content-type", "application/x-www-form-urlencoded")
    }
    let params = new HttpParams().set("username", username).set("password",password);
    return this.http.post("http://localhost:8085/auth/login",params, options)
  }

  loadProfile(value: any) {
    this.isAuthenticated=true;
    this.accessToken = value['access-token'];
    let jwtDecoded:any = jwtDecode(this.accessToken)
    this.username=jwtDecoded.sub;
    this.roles = jwtDecoded.scope;
    window.localStorage.setItem('access-token', this.accessToken);
  }

  logout() {
    this.isAuthenticated=false;
    this.roles = undefined;
    this.username=undefined;
    this.accessToken=undefined;
    window.localStorage.clear();
    this.router.navigateByUrl('/login')
  }

  uploadTokenFromLocalStorage() {
    let token = window.localStorage.getItem('access-token');
    this.loadProfile({'access-token': token});
    this.router.navigateByUrl('/admin')

  }
}
