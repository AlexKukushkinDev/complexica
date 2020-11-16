import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginFormComponent } from './shared/components';
import { AuthGuardService } from './shared/services';
import { HomeComponent } from './pages/home/home.component';
import { ForecastComponent } from './pages/forecast/forecast.component';
import { ItinerariesComponent } from './pages/itineraries/itineraries.component';
import { DxDataGridModule, DxFormModule, DxButtonModule } from 'devextreme-angular';

const routes: Routes = [
  {
    path: 'itineraries',
    component: ItinerariesComponent,
    canActivate: [ AuthGuardService ]
  },
  {
    path: 'forecast',
    component: ForecastComponent,
    canActivate: [ AuthGuardService ]
  },
  {
    path: 'home',
    component: HomeComponent,
    canActivate: [ AuthGuardService ]
  },
  {
    path: 'login-form',
    component: LoginFormComponent,
    canActivate: [ AuthGuardService ]
  },
  {
    path: '**',
    redirectTo: 'home',
    canActivate: [ AuthGuardService ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes), DxDataGridModule, DxFormModule, DxButtonModule],
  providers: [AuthGuardService],
  exports: [RouterModule],
  declarations: [HomeComponent, ForecastComponent, ItinerariesComponent]
})
export class AppRoutingModule { }
