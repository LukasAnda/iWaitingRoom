import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './navbar/navbar.component';

import { AngularFireModule } from '@angular/fire';
import { AngularFireDatabaseModule } from '@angular/fire/database';
import { environment } from '../environments/environment';
import { PacientInfoComponent } from './pacient-info/pacient-info.component';
import { DoktorComponent } from './doktor/doktor.component';
import { AngularFirestore } from '@angular/fire/firestore';
import { PacientsTableComponent } from './pacients-table/pacients-table.component';
import { SettingsComponent } from './settings/settings.component';
import { WaitingRoomComponent } from './waiting-room/waiting-room.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    PacientInfoComponent,
    DoktorComponent,
    PacientsTableComponent,
    SettingsComponent,
    WaitingRoomComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    AngularFireModule.initializeApp(environment.firebaseConfig),
    AngularFireDatabaseModule
  ],
  providers: [AngularFirestore],
  bootstrap: [AppComponent]
})
export class AppModule { }
