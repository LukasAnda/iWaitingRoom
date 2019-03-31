import { Component, OnInit } from '@angular/core';

import { pacienti } from '../../assets/data/pacients';
import { Pacient } from '../pacient.model';

import { Observable } from 'rxjs';
import { AngularFirestore } from '@angular/fire/firestore';

@Component({
  selector: 'app-pacients-table',
  templateUrl: './pacients-table.component.html',
  styleUrls: ['./pacients-table.component.css']
})
export class PacientsTableComponent implements OnInit {

  public data: Observable<any[]>;
  public pacienti: Observable<any[]>;

  constructor(db: AngularFirestore) { 
    this.pacienti = db.collection('/patients').valueChanges();
  }
  ngOnInit() {
    // this.data.subscribe((res) => {
    //   this.pacienti.push(res);
    //   console.log(res);
    // });
    // console.log("toto su pacienti");
    // console.log(this.pacienti);
  }

}
