package com.lukasanda.medicalapp.fragments


import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.lukasanda.medicalapp.R
import com.lukasanda.medicalapp.WaitingPatient
import com.lukasanda.medicalapp.utils.SwipeAndDragHelper
import kotlinx.android.synthetic.main.fragment_pacient.view.*
import java.text.SimpleDateFormat
import java.util.*


class MyPacientRecyclerViewAdapter(
	private var mValues: MutableList<WaitingPatient> = mutableListOf(),
	var listener: AdapterInteraction
) : RecyclerView.Adapter<MyPacientRecyclerViewAdapter.ViewHolder>(), SwipeAndDragHelper.ActionCompletionContract {

	var touchHelper: ItemTouchHelper? = null

	override fun onViewMoved(oldPosition: Int, newPosition: Int) {
		val targetPatient = mValues[oldPosition]
		val newPatient = targetPatient.copy()
		mValues.removeAt(oldPosition)
		mValues.add(newPosition, newPatient)
		listener.onModified(mValues)
		notifyItemMoved(oldPosition, newPosition)
	}

	override fun onViewSwiped(position: Int) {
		val item = mValues.removeAt(position)
		listener.onModified(mValues)
		listener.onRemoved(item)
		notifyItemRemoved(position)
	}

	override fun onCreateViewHolder(
		parent: ViewGroup, viewType: Int
	): ViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(
			R.layout.fragment_pacient, parent, false
		)
		return ViewHolder(view)
	}

	override fun onBindViewHolder(
		holder: ViewHolder, position: Int
	) {
		val it = mValues[position]
		var expanded = false
		holder.mView.name.text = "Meno: ${it.patient.meno}"
		holder.mView.komentar.text = it.comment
		holder.mView.apply {
			email.text = "Email: ${it.patient.email}"
			poistovna.text = "Poisťovňa: ${it.patient.poistovna.toString()}"
			krv.text = "Krv: ${it.patient.krvnaSkupina}${it.patient.krvRh}"
			alergie.text = "Alergie:\n${it.patient.alergie?.joinToString(
				separator = "\n"
			)}"
			ochorenia.text = "Ochorenia:\n${it.patient.ochorenia?.joinToString(
				separator = "\n"
			)}"

			val operacieText = StringBuilder()
			operacieText.append("Operacie:\n")
			it.patient.operacie?.forEach {
				operacieText.append(
					"${it.operacia} - ${SimpleDateFormat(
						"dd.MM.yyyy"
					).format(Date(it.datumOperacie!!))}"
				)
				operacieText.append("\n")
			}
			operacie.text = operacieText

			lieky.text = "Lieky:\n${it.patient.lieky?.joinToString(separator = "\n")}"

			setOnClickListener {
				if(expanded){
					detail.visibility = View.GONE
					expanded = false
				} else {
					detail.visibility = View.VISIBLE
					expanded = true
				}
			}
		}


		holder.mView.handle.setOnTouchListener { v, event ->
			if (event.actionMasked == MotionEvent.ACTION_DOWN) {
				touchHelper?.startDrag(holder)
			}
			return@setOnTouchListener false
		}
	}

	fun setItems(items: List<WaitingPatient>) {
		this.mValues = items.toMutableList()
		this.notifyDataSetChanged()
	}

	override fun getItemCount(): Int = mValues.size

	inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

	}

	interface AdapterInteraction {
		fun onModified(patients: List<WaitingPatient>)
		fun onRemoved(patient: WaitingPatient)
	}
}
