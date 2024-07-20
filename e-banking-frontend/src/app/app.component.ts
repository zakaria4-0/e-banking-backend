import {Component, OnInit} from '@angular/core';
import {AuthService} from "./services/auth.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit{
  title = 'e-banking-frontend';
  constructor(private authService : AuthService) {
  }

  ngOnInit(): void {
    this.authService.uploadTokenFromLocalStorage();
  }
}
