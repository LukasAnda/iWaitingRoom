package com.lukasanda.medicalapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.lukasanda.medicalapp.*
import com.lukasanda.medicalapp.utils.SwipeAndDragHelper


/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [PacientListFragment.OnListFragmentInteractionListener] interface.
 */
class PacientListFragment : Fragment(), MyPacientRecyclerViewAdapter.AdapterInteraction {


	private var doktor: Doktor? = null
	private lateinit var db: FirebaseFirestore
	private lateinit var adapter: MyPacientRecyclerViewAdapter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		db = FirebaseFirestore.getInstance()

		arguments?.let {
			doktor = it.getParcelable("DOKTOR")
		}
		adapter = MyPacientRecyclerViewAdapter(listener = this)
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View? {
		val view = inflater.inflate(
			R.layout.fragment_pacient_list, container, false
		)

		// Set the adapter
		if (view is RecyclerView) {
			with(view) {
				layoutManager = LinearLayoutManager(context)
				adapter = this@PacientListFragment.adapter
			}
			val swipeAndDragHelper = SwipeAndDragHelper(adapter)
			val touchHelper = ItemTouchHelper(swipeAndDragHelper)
			adapter.touchHelper = touchHelper
			touchHelper.attachToRecyclerView(view)
		}
		return view
	}


	override fun onViewCreated(
		view: View, savedInstanceState: Bundle?
	) {
		super.onViewCreated(view, savedInstanceState)
		doktor?.id?.let {
			db.collection("doctors").document(it).addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
				documentSnapshot?.let {
					val aktualnyDoktor = it.toObject(Doktor::class.java)
					aktualnyDoktor?.let { aktualnyDoktor ->
						val listOfPatients = mutableListOf<WaitingPatient>()
						val expectedSize = aktualnyDoktor.cakaren?.size ?: 0
						Log.d("TAG", "Expected size: $expectedSize")
						if (aktualnyDoktor.cakaren == null || aktualnyDoktor.cakaren!!.isEmpty()) {
							adapter.setItems(
								listOfPatients
							)
							return@addSnapshotListener
						}
						aktualnyDoktor.cakaren?.forEach { cakaren ->
							db.collection("patients").document(cakaren.id!!).get().addOnSuccessListener {
								val aktualnyPacient = it.toObject(
									Patient::class.java
								)
								Log.d("TAG", aktualnyPacient.toString())

								aktualnyPacient?.let {
									listOfPatients.add(
										WaitingPatient(
											cakaren.cas!!, it, cakaren.typ!!, cakaren.komentar!!, cakaren.position
										)
									)
									if (listOfPatients.size == expectedSize) {
										adapter.setItems(listOfPatients.sortedBy { it.position })
									}
								}
							}
						}
					}
				}
			}
		}
	}

	override fun onModified(patients: List<WaitingPatient>) {
		val novaCakaren = mutableListOf<Cakaren>()
		for (i in 0 until patients.size) {
			val patient = patients[i]
			novaCakaren.add(Cakaren(patient.patient.id, patient.type, patient.from, patient.comment, i))
		}
		doktor?.cakaren = novaCakaren

		db.collection("doctors").document(doktor!!.id!!).set(doktor!!)
	}

	override fun onRemoved(patient: WaitingPatient) {
		val newPatient = patient.patient
		newPatient.cakaren = ""
		db.collection("patients").document(newPatient.id!!).set(newPatient)
	}

	companion object {

		@JvmStatic
		fun newInstance(doktor: Doktor) = PacientListFragment().apply {
			arguments = Bundle().apply {
				putParcelable("DOKTOR", doktor)
			}
		}
	}
}
