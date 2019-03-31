import { Injectable } from '@angular/core';

import { AngularFirestore } from '@angular/fire/firestore';
import { Doctor } from './doctor.model';

@Injectable({
  providedIn: 'root'
})
export class DoktorService {

  constructor(private firestore: AngularFirestore) { }
  getDoctor() {
    return this.firestore.collection("doctors").snapshotChanges();
  }
}
