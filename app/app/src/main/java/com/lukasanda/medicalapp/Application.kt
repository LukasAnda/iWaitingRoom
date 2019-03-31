package com.lukasanda.medicalapp

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings


class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        val settings = FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build()
        FirebaseFirestore.getInstance().firestoreSettings = settings
    }
}