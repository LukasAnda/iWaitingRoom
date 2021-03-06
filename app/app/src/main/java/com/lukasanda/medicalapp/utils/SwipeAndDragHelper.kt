package com.lukasanda.medicalapp.utils

import android.graphics.Canvas
import androidx.core.view.ViewCompat.setAlpha
import android.opengl.ETC1.getWidth
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


class SwipeAndDragHelper(private val contract: ActionCompletionContract) : ItemTouchHelper.Callback() {

	override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
		val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
		val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
		return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
	}

	override fun onMove(
		recyclerView: RecyclerView,
		viewHolder: RecyclerView.ViewHolder,
		target: RecyclerView.ViewHolder
	): Boolean {
		contract.onViewMoved(viewHolder.adapterPosition, target.adapterPosition)
		return true
	}

	override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
		contract.onViewSwiped(viewHolder.adapterPosition)
	}

	override fun isLongPressDragEnabled(): Boolean {
		return false
	}

	override fun onChildDraw(
		c: Canvas,
		recyclerView: RecyclerView,
		viewHolder: RecyclerView.ViewHolder,
		dX: Float,
		dY: Float,
		actionState: Int,
		isCurrentlyActive: Boolean
	) {
		if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
			val alpha = 1 - Math.abs(dX) / recyclerView.width
			viewHolder.itemView.alpha = alpha
		}
		super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
	}

	interface ActionCompletionContract {
		fun onViewMoved(oldPosition: Int, newPosition: Int)

		fun onViewSwiped(position: Int)
	}

}