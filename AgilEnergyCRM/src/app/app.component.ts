import { Component } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  standalone: false
})
export class AppComponent {
  title = 'StagePFE';
  role: string | null = null;
  showSidebar = true;
  showHeader = true;

  constructor(private router: Router) {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        const userStr = localStorage.getItem('user');
        if (userStr) {
          const user = JSON.parse(userStr);
          this.role = user.role;
          const url = event.urlAfterRedirects;
          this.showSidebar = !(url.includes('/login') || url.includes('/acceuil'));
          this.showHeader = !url.includes('/acceuil');
        } else {
          this.role = null;
          this.showSidebar = false;
          this.showHeader = !event.urlAfterRedirects.includes('/acceuil');
        }
      }
    });    
  }
}
