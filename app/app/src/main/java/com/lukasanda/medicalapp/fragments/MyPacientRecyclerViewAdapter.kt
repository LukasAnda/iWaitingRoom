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
		val item = mValues[position]
		holder.mView.name.text = item.patient.meno
		holder.mView.poistovna.text = item.comment
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
