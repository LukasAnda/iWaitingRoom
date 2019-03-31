import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { PacientsTableComponent } from './pacients-table/pacients-table.component';
import { SettingsComponent } from './settings/settings.component';
import { PacientInfoComponent } from './pacient-info/pacient-info.component';
import { WaitingRoomComponent } from './waiting-room/waiting-room.component';

const routes: Routes = [
  {path: 'home', component: WaitingRoomComponent},
  {path: 'nastavenia', component: SettingsComponent},
  {path: 'pacient/:id', component: PacientInfoComponent},
  {path: 'pacient', component: PacientInfoComponent},
  {path: 'pacienti', component: PacientsTableComponent},
  { path: '', redirectTo: '/home', pathMatch: 'full'},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
