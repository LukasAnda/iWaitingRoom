import { Component, OnInit } from '@angular/core';

import { Observable } from 'rxjs';
import { AngularFirestore } from '@angular/fire/firestore';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {

  public settings: Observable<any[]>;

  constructor(db: AngularFirestore) { 
    this.settings = db.collection('/doctors').valueChanges();
  }

  ngOnInit() {
  }

}
