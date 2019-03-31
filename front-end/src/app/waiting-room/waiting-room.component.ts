import { Component, OnInit } from '@angular/core';
import { AngularFirestore } from '@angular/fire/firestore';

import { Observable } from 'rxjs';
import { async } from '@angular/core/testing';

@Component({
  selector: 'app-waiting-room',
  templateUrl: './waiting-room.component.html',
  styleUrls: ['./waiting-room.component.css']
})
export class WaitingRoomComponent implements OnInit {

  public doctor: Observable<any[]>;
  public patients: Observable<any[]>;
  public day: number;
  public month: number;
  public year: number;
  public pocet: number = 0;

  navysPocet = function() {
    this.pocet += 1;
    return this.pocet;
  }

  constructor(db: AngularFirestore) {
    this.doctor = db.collection('/doctors').valueChanges();
    this.patients = db.collection('/patients').valueChanges();
    const d = new Date();
    this.day = d.getDate();
    this.month = d.getMonth() + 1;
    this.year = d.getFullYear();
   }  

  ngOnInit() {
  }

}
