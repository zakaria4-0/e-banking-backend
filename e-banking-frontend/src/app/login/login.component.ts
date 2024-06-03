import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {AuthService} from "../services/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit{
  formLogin!: FormGroup;
  constructor(private  fb : FormBuilder, private authService : AuthService, private router : Router) {
  }
  ngOnInit(): void {
    this.formLogin=this.fb.group({
      username : this.fb.control(""),
      password : this.fb.control("")
    })
  }

  handelLogin() {
    let username = this.formLogin.value.username;
    let password = this.formLogin.value.password;
    this.authService.login(username,password).subscribe(
      {
        next : value => {
          this.authService.loadProfile(value)
          this.router.navigateByUrl("/admin")
        },
        error : err => {
          console.log(err)
        }
      }
    )
  }
}
