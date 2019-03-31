import { Component, OnInit } from '@angular/core';

import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

import { Observable } from 'rxjs';
import { AngularFirestore } from '@angular/fire/firestore';

@Component({
  selector: 'app-pacient-info',
  templateUrl: './pacient-info.component.html',
  styleUrls: ['./pacient-info.component.css']
})
export class PacientInfoComponent implements OnInit {

  public patient: Observable<any>;
  public items: Observable<any[]>;

  public patientId: string;
  
  constructor(
    db: AngularFirestore,
    private route: ActivatedRoute,
    private location: Location
  ) { 
    this.items = db.collection('/patients').valueChanges();
  }

  ngOnInit() {
    this.patientId = this.route.snapshot.params['id'];
  }
}
