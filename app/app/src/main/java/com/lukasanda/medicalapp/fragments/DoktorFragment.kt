package com.lukasanda.medicalapp.fragments

import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.lukasanda.medicalapp.Doktor
import com.lukasanda.medicalapp.Patient
import com.lukasanda.medicalapp.R

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [DoktorFragment.OnListFragmentInteractionListener] interface.
 */
class DoktorFragment : Fragment() {


	private lateinit var db: FirebaseFirestore

	private lateinit var adapter: MyDoktorRecyclerViewAdapter


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		adapter = MyDoktorRecyclerViewAdapter()

		db = FirebaseFirestore.getInstance()
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View? {
		val view = inflater.inflate(R.layout.fragment_doktor_list, container, false)

		// Set the adapter
		if (view is RecyclerView) {
			with(view) {
				layoutManager = LinearLayoutManager(context)
				adapter = this@DoktorFragment.adapter
			}
		}
		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		db.collection("doctors").addSnapshotListener { querySnapshot, firebaseFirestoreException ->
			val listOfDocs = querySnapshot?.documents
			val expectedSize = listOfDocs?.size ?: 0

			val listOfDoktors = mutableListOf<Doktor>()

			listOfDocs?.forEach {
				val newDoktor = it.toObject(Doktor::class.java)
				newDoktor?.let { it1 -> listOfDoktors.add(it1) }
				if(listOfDoktors.size == expectedSize){
					adapter.setItems(listOfDoktors)
				}
			}
		}
	}

	companion object {

		@JvmStatic
		fun newInstance() = DoktorFragment().apply {
			arguments = Bundle().apply {
			}
		}
	}
}
