package com.lukasanda.medicalapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.google.firebase.firestore.FirebaseFirestore
import com.lukasanda.medicalapp.Cakaren
import com.lukasanda.medicalapp.Doktor
import com.lukasanda.medicalapp.Patient
import com.lukasanda.medicalapp.R
import kotlinx.android.synthetic.main.fragment_place.view.*
import kotlin.random.Random

class PlaceFragment : Fragment() {

	private lateinit var codeScanner: CodeScanner
	private lateinit var db: FirebaseFirestore
	private var pacient: Patient? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		db = FirebaseFirestore.getInstance()
		arguments?.let {
			pacient = it.getParcelable("PACIENT")
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View? {
		// Inflate the layout for this fragment
		val v = inflater.inflate(
			R.layout.fragment_place, container, false
		)
		codeScanner = CodeScanner(requireActivity(), v.scanner_view)
		codeScanner.decodeCallback = DecodeCallback {
			activity?.runOnUiThread {
				Toast.makeText(
					activity, it.text, Toast.LENGTH_LONG
				).show()
				Log.d("SCANNER", it.text)
				pridajDoCakarneAZobraz(it.text)
				v.scan.visibility = View.VISIBLE
				v.scanner_view.visibility = View.INVISIBLE
			}
		}

		v.scan.setOnClickListener {
			v.scan.visibility = View.INVISIBLE
			v.scanner_view.visibility = View.VISIBLE
			codeScanner.startPreview()
		}

		v.send.setOnClickListener {
			pridajKomentar(v.comment.text.toString())
		}
		return v
	}

	override fun onViewCreated(
		view: View, savedInstanceState: Bundle?
	) {
		pacient?.let {
			if (it.cakaren != null && it.cakaren!!.isNotEmpty()) {
				updateData(it.cakaren!!)
				view.scan.visibility = View.GONE
			}
		}
	}

	@SuppressLint("SetTextI18n")
	fun pridajDoCakarneAZobraz(id: String) {
		db.collection("doctors").document(id).get().addOnSuccessListener {
			val doktor = it.toObject(Doktor::class.java)
			doktor?.let { mojDoktor ->
				pacient?.let {
					val rand = Random(123).nextBoolean()
					if (rand) {
						mojDoktor.cakaren?.add(
							Cakaren(
								it.id, "Vysetrenie", System.currentTimeMillis(), "", mojDoktor.cakaren?.size ?: 0
							)
						)
					} else {
						mojDoktor.cakaren?.add(
							Cakaren(
								it.id, "ADMIN", System.currentTimeMillis(), "", mojDoktor.cakaren?.size ?: 0
							)
						)
					}
					db.collection("doctors").document(id).set(
						mojDoktor
					).addOnSuccessListener {
						Log.d(
							"TAG", "Added person to cakaren of ${mojDoktor.email}"
						)
					}
					it.cakaren = mojDoktor.id
					db.collection("patients").document(it.id!!).set(it).addOnSuccessListener {
						Log.d(
							"TAG", "Added doctor id to personal waiting"
						)
					}
				}
			}
		}

		updateData(id)
	}

	fun pridajKomentar(komentar: String) {
		db.collection("doctors").document(pacient?.cakaren ?: "").get().addOnSuccessListener {
			val doktor = it.toObject(Doktor::class.java)
			doktor?.let { mojDoktor ->
				pacient?.let { patient ->
					val novaCakaren = mojDoktor.cakaren
					novaCakaren?.forEach {
						if (it.id == patient.id) {
							it.komentar = komentar
						}
					}
					mojDoktor.cakaren = novaCakaren
					db.collection("doctors").document(mojDoktor.id ?: "").set(
						mojDoktor
					).addOnSuccessListener {
						Log.d(
							"TAG", "Added person to cakaren of ${mojDoktor.email}"
						)
					}
				}
			}
		}
	}

	fun updateData(id: String) {
		db.collection("doctors").document(id).addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
			documentSnapshot?.let {
				val mojDoktor = it.toObject(Doktor::class.java)
				mojDoktor?.let {

					if (amIinQueue(it)) {

						view?.apply {
							doctor_name.text = it.meno
							doctor_email.text = it.email
							doctor_spec.text = it.specifikacia
							scan.visibility = View.INVISIBLE
							doctor_layout.visibility = View.VISIBLE
							waiting_layout.visibility = View.VISIBLE
							comment_layout.visibility = View.VISIBLE
						}

						val time = getApproxTime(it) / 60f
						val ppl = getPplWaiting(it)
						if (ppl > 0) {
							view?.est_time?.text = "Približné čakanie: ${time}min"
							view?.est_ppl?.text = "Ľudia pred vami: ${ppl}"
						} else {
							view?.est_time?.text = "Ste na rade!"
							view?.est_ppl?.text = ""
						}
						view?.scan?.visibility = View.INVISIBLE
					} else {
						view?.apply {
							doctor_layout.visibility = View.GONE
							waiting_layout.visibility = View.GONE
							comment_layout.visibility = View.GONE
							scan.visibility = View.VISIBLE
						}
					}
				}
			}
		}
	}

	fun amIinQueue(doktor: Doktor): Boolean {
		var foundMyself = false
		doktor.cakaren?.forEach {
			if (it.id == pacient?.id) {
				foundMyself = true
			}
		}
		return foundMyself
	}

	fun getPplWaiting(doktor: Doktor): Int {
		var ppl = 0
		val ludiaVCakarni = doktor.cakaren
		if (ludiaVCakarni == null || ludiaVCakarni.isEmpty()) return 0

		for (c in ludiaVCakarni.sortedBy { it.position }) {
			if (c.id == pacient!!.id) break

			ppl++
		}

		return ppl
	}

	fun getApproxTime(doktor: Doktor): Int {
		val ludiaVCakarni = doktor.cakaren
		if (ludiaVCakarni == null || ludiaVCakarni.isEmpty()) return 0

		var time = 0

		for (c in ludiaVCakarni.sortedBy { it.position }) {
			if (c.id == pacient!!.id) break
			if (c.typ == "ADMIN") {
				time += doktor.adminCas!!
			} else {
				time += doktor.vysetrenieCas!!
			}
		}
		return time
	}


	companion object {

		@JvmStatic
		fun newInstance(pacient: Patient) = PlaceFragment().apply {
			arguments = Bundle().apply {
				putParcelable("PACIENT", pacient)
			}
		}
	}
}
