import { Injectable } from '@angular/core';

import { AngularFirestore } from '@angular/fire/firestore';
import { Pacient } from 'src/app/pacient.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PacientService {
  constructor(private firestore: AngularFirestore) { }
  getPacient() {
    return this.firestore.collection("doctors").snapshotChanges();
  }
  
}