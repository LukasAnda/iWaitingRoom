package com.lukasanda.medicalapp.fragments


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.lukasanda.medicalapp.Cakaren
import com.lukasanda.medicalapp.Doktor
import com.lukasanda.medicalapp.HodinaACas
import com.lukasanda.medicalapp.R
import kotlinx.android.synthetic.main.fragment_doktor.view.*


/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyDoktorRecyclerViewAdapter(
	private var mValues: List<Doktor> = emptyList()
) : RecyclerView.Adapter<MyDoktorRecyclerViewAdapter.ViewHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_doktor, parent, false)
		return ViewHolder(view)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val item = mValues[position]
		var expanded = false
		holder.mView.apply {
			name.text = "Meno: ${item.meno}"
			email.text = "Email: ${item.email}"
			poistovna.text = "Poisťovne: ${item.poistovne?.joinToString(separator = ", ")}"
			cas.text = "Čas: ${(item.cakaren?.size ?: 0 * (item.adminCas ?: 0))}min"
			ludia.text = "Počet ľudí pred vami: ${item.cakaren?.size.toString()}"


			item.priemernyDen?.let {
				setUpChart(normal, it, "Priemerný deň")
			}

			item.chorobnyDen?.let {
				setUpChart(epidemic, it, "Chrípkový deň")
			}

			setOnClickListener {
				if (expanded) {
					expanded = false
					graph_layout.visibility = View.GONE
				} else {
					expanded = true
					graph_layout.visibility = View.VISIBLE
				}
			}

		}

	}

	fun getTime(cakaren: List<Cakaren>) {
		var time = cakaren
	}

	fun setUpChart(normal: LineChart, casy: List<HodinaACas>, title: String) {
		val normalValues = mutableListOf<Entry>()
		casy.forEach {
			normalValues.add(Entry(it.hodina.toFloat(), it.cas.toFloat()))
		}

		val set1 = LineDataSet(normalValues, title)

		set1.mode = LineDataSet.Mode.CUBIC_BEZIER
		set1.cubicIntensity = 0.2f
		set1.setDrawFilled(true)
		set1.setDrawCircles(false)
		set1.lineWidth = 1.8f
		set1.circleRadius = 4f
		set1.setCircleColor(Color.WHITE)
		set1.highLightColor = Color.rgb(244, 117, 117)
		set1.color = Color.WHITE
		set1.fillColor = normal.context.getColor(R.color.colorPrimary)
		set1.fillAlpha = 100
		set1.setDrawHorizontalHighlightIndicator(false)
		set1.fillFormatter = IFillFormatter { dataSet, dataProvider -> normal.getAxisLeft().getAxisMinimum() }

		// create a data object with the data sets
		val data = LineData(set1)
		data.setValueTextSize(9f)
		data.setDrawValues(false)

		normal.setDrawGridBackground(false)

		normal.axisLeft.setDrawGridLines(false)
		normal.axisLeft.setDrawGridLinesBehindData(false)

		normal.axisRight.setDrawGridLines(false)
		normal.axisRight.setDrawGridLinesBehindData(false)

		normal.xAxis.setDrawGridLines(false)
		normal.xAxis.setDrawGridLinesBehindData(false)
		normal.xAxis.textColor = normal.context.getColor(android.R.color.transparent)

		normal.description.isEnabled = false

		// set data
		normal.setData(data)
	}

	fun setItems(list: List<Doktor>) {
		mValues = list
		notifyDataSetChanged()
	}

	override fun getItemCount(): Int = mValues.size

	inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

	}
}
