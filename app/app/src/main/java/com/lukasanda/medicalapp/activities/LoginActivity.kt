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
import com.lukasanda.medicalapp.Doktor
import com.lukasanda.medicalapp.Patient
import com.lukasanda.medicalapp.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var googleSignInClient: GoogleSignInClient
    lateinit var db: FirebaseFirestore

    var isDoctor = false
    var isPatient = false

    companion object {
        const val RC_SIGN_IN = 9001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .requestProfile()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()

        db = FirebaseFirestore.getInstance()

        login_doctor.setOnClickListener {
            isDoctor = true
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent,
	            RC_SIGN_IN)
        }

        login_patient.setOnClickListener {
            isPatient = true
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent,
	            RC_SIGN_IN)
        }


        register.setOnClickListener {


            startActivity(Intent(this@LoginActivity, RegisterDoctorActivity::class.java))
        }
    }

	override fun onResume() {
		super.onResume()
		auth.signOut()
		googleSignInClient.signOut()
	}

	public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RegisterDoctorActivity.RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e)
                userData(null)
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
                    userData(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                    userData(null)
                }

            }
    }

    private fun userData(user: FirebaseUser?) {
	    if(user == null){
		    Log.d("TAG", "User is null")
	    } else {
		    Log.d("TAG", "User is not null")
	    }
        user?.let {

            if (isDoctor) {
                Log.d("TAG", it.uid)
                db.collection("doctors").document(it.uid).get().addOnSuccessListener {
                    val doktor = it?.toObject(Doktor::class.java)

                    doktor?.let {
                        Log.d("TAG", "Prihladeny ako doktor")
                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        intent.putExtra("DOKTOR", it)
                        startActivity(intent)
                    }

                    auth.signOut()
                    googleSignInClient.signOut()
                }
                return
            }

            if (isPatient) {
                Log.d("TAG", it.uid)
                db.collection("patients").document(it.uid).get().addOnSuccessListener {
                    val patient = it?.toObject(Patient::class.java)

                    patient?.let {
                        Log.d("TAG", "Prihladeny ako pacient")
                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        intent.putExtra("PACIENT", it)
                        startActivity(intent)
                    }

                    auth.signOut()
                    googleSignInClient.signOut()
                }

                isDoctor = false
                isPatient = false
                return
            }
        }
    }
}
