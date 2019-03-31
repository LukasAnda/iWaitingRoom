package com.lukasanda.medicalapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.lukasanda.medicalapp.*
import kotlinx.android.synthetic.main.activity_register_doctor.*

class RegisterDoctorActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var googleSignInClient: GoogleSignInClient
    lateinit var db: FirebaseFirestore

    var isDoctor = false
    var isPatient = false

    val doctor = Doktor()
    val patient = Patient()

    companion object {
        const val RC_SIGN_IN = 9001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_doctor)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .requestProfile()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()

        db = FirebaseFirestore.getInstance()

        register.setOnClickListener {
            isDoctor = true
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent,
	            RC_SIGN_IN)
        }

        registerpatient.setOnClickListener {
            isPatient = true
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent,
	            RC_SIGN_IN)
        }
    }

    private fun registerDoctor() {
        doctor.vysetrenieCas = 1200
        doctor.adminCas = 300
        val otvaracieHodiny = mutableListOf<OtvaraciaHodina>()
        otvaracieHodiny.add(OtvaraciaHodina(
	        "PO",
	        "8:00 - 16:00"))
        otvaracieHodiny.add(OtvaraciaHodina(
	        "UT",
	        "8:00 - 16:00"))
        otvaracieHodiny.add(OtvaraciaHodina(
	        "ST",
	        "8:00 - 16:00"))
        otvaracieHodiny.add(OtvaraciaHodina(
	        "ŠT",
	        "8:00 - 16:00"))
        otvaracieHodiny.add(OtvaraciaHodina(
	        "PI",
	        "8:00 - 16:00"))
        doctor.otvaracieHodiny = otvaracieHodiny
        doctor.poistovne = listOf(23,21,31,43)
        doctor.specifikacia = "Vseobecny lekar"

        val beznyDen = mutableListOf<HodinaACas>()
        beznyDen.add(HodinaACas(5, 0))
        beznyDen.add(HodinaACas(11, 7))
        beznyDen.add(HodinaACas(17, 13))
        beznyDen.add(HodinaACas(25, 19))
        beznyDen.add(HodinaACas(33, 23))
        beznyDen.add(HodinaACas(42, 27))
        beznyDen.add(HodinaACas(52, 30))
        beznyDen.add(HodinaACas(63, 31))
        beznyDen.add(HodinaACas(77, 30))
        beznyDen.add(HodinaACas(92, 29))
        beznyDen.add(HodinaACas(113, 26))
        beznyDen.add(HodinaACas(134, 23))
        beznyDen.add(HodinaACas(162, 18))
        beznyDen.add(HodinaACas(205, 8))
        beznyDen.add(HodinaACas(300, 3))
	    doctor.priemernyDen = beznyDen

	    val chorobnyDen = mutableListOf<HodinaACas>()
	    chorobnyDen.add(HodinaACas(3, 0))
	    chorobnyDen.add(HodinaACas(6, 7))
	    chorobnyDen.add(HodinaACas(10, 13))
	    chorobnyDen.add(HodinaACas(13, 19))
	    chorobnyDen.add(HodinaACas(16, 25))
	    chorobnyDen.add(HodinaACas(20, 31))
	    chorobnyDen.add(HodinaACas(24, 36))
	    chorobnyDen.add(HodinaACas(28, 42))
	    chorobnyDen.add(HodinaACas(32, 48))
	    chorobnyDen.add(HodinaACas(37, 53))
	    chorobnyDen.add(HodinaACas(41, 58))
	    chorobnyDen.add(HodinaACas(46, 63))
	    chorobnyDen.add(HodinaACas(50, 68))
	    chorobnyDen.add(HodinaACas(56, 72))
	    chorobnyDen.add(HodinaACas(60, 77))
	    chorobnyDen.add(HodinaACas(65, 81))
	    chorobnyDen.add(HodinaACas(71, 84))
	    chorobnyDen.add(HodinaACas(79, 86))
	    chorobnyDen.add(HodinaACas(87, 87))
	    chorobnyDen.add(HodinaACas(95, 89))
	    chorobnyDen.add(HodinaACas(104, 90))
	    chorobnyDen.add(HodinaACas(114, 89))
	    chorobnyDen.add(HodinaACas(124, 88))
	    chorobnyDen.add(HodinaACas(140, 82))
	    chorobnyDen.add(HodinaACas(157, 75))
	    chorobnyDen.add(HodinaACas(175, 68))
	    chorobnyDen.add(HodinaACas(194, 59))
	    chorobnyDen.add(HodinaACas(224, 47))
	    chorobnyDen.add(HodinaACas(275, 27))
	    chorobnyDen.add(HodinaACas(374, 11))
	    doctor.chorobnyDen = chorobnyDen

        db.collection("doctors").document(doctor.id!!).set(doctor).addOnCompleteListener {
            Log.d("TAG", "Inserted doctor")
            FirebaseAuth.getInstance().signOut()
            googleSignInClient.signOut()
        }.addOnFailureListener {
            Log.d("TAG", "FAILED TO INSERT DOCTOR")
            FirebaseAuth.getInstance().signOut()
            googleSignInClient.signOut()
        }
        isDoctor = false
    }

    private fun registerPatient() {
        patient.krvnaSkupina = "A"
        patient.lieky = listOf("Asmanex", "Livostat", "Ibalgin")
        patient.krvRh = "+"
        patient.alergie = listOf("Palina", "Artemisia", "Roztoce")
        patient.ochorenia = listOf("TBC", "AIDS")
        patient.poistovna = 21
        val operacie = mutableListOf<Operacia>()
        operacie.add(Operacia(
	        1553955834L,
	        "Slepe crevo"))
        operacie.add(Operacia(
	        1553915834L,
	        "Zlomena noha"))
        operacie.add(Operacia(
	        1552955834L,
	        "Zlomena ruka"))
        operacie.add(Operacia(
	        1553945834L,
	        "Krcne mandle"))
        patient.operacie = operacie



        db.collection("patients").document(patient.id!!).set(patient).addOnCompleteListener {
            Log.d("TAG", "Inserted patient")
            FirebaseAuth.getInstance().signOut()
            googleSignInClient.signOut()
        }.addOnFailureListener {
            Log.d("TAG", "FAILED TO INSERT PATIENT")
            FirebaseAuth.getInstance().signOut()
            googleSignInClient.signOut()
        }
        isPatient = false
    }

    fun zistiPoistovne(poistovne: String): List<Int> {
        val poistovneList = mutableListOf<Int>()
        val novePoistovne = poistovne.split(",")
        novePoistovne.forEach {
            poistovneList.add(it.trim().toInt())
        }
        return poistovneList
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e)
                setDoctorData(null)
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithCredential:success")
                    val user = auth.currentUser
                    setDoctorData(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                    setDoctorData(null)
                }

            }
    }

    private fun setDoctorData(user: FirebaseUser?) {
        user?.let {

            if (isDoctor) {
                Log.d("TAG", it.uid)
                doctor.meno = it.displayName
                doctor.email = it.email
                doctor.id = it.uid
                registerDoctor()
                return
            }

            if (isPatient) {
                Log.d("TAG", it.uid)
                patient.meno = it.displayName
                patient.email = it.email
                patient.id = it.uid
                registerPatient()
                return
            }
        }
    }
}
