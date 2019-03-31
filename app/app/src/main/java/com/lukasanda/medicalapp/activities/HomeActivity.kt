package com.lukasanda.medicalapp.activities

import android.animation.Animator
import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.lukasanda.medicalapp.Doktor
import com.lukasanda.medicalapp.Patient
import com.lukasanda.medicalapp.R
import com.lukasanda.medicalapp.fragments.DoktorFragment
import com.lukasanda.medicalapp.fragments.PacientListFragment
import com.lukasanda.medicalapp.fragments.PlaceFragment
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.text.SimpleDateFormat
import java.util.*


class HomeActivity : AppCompatActivity() {
	private lateinit var db: FirebaseFirestore
	private var pacient: Patient? = null
	private var doktor: Doktor? = null
	private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_home)

		db = FirebaseFirestore.getInstance()

		intent?.let {
			pacient = it.getParcelableExtra("PACIENT")
			doktor = it.getParcelableExtra("DOKTOR")
		}


		setSupportActionBar(toolbar)
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

		// Set up the ViewPager with the sections adapter.
		container.adapter = mSectionsPagerAdapter

		container.addOnPageChangeListener(
			TabLayout.TabLayoutOnPageChangeListener(
				tabs
			)
		)
		tabs.addOnTabSelectedListener(
			TabLayout.ViewPagerOnTabSelectedListener(
				container
			)
		)

		try {
			val fabDrawable = resources.getDrawable(R.drawable.ic_profile)
			val newFabIcon = fabDrawable.constantState!!.newDrawable()
			newFabIcon.mutate().setColorFilter(
				Color.WHITE, PorterDuff.Mode.MULTIPLY
			)
			fab.setImageDrawable(newFabIcon)
		} catch (e: Exception) {
			e.printStackTrace()
		}

		fab.setOnClickListener { view ->
			if (profile_layout.layoutParams.height == 0) {
				val anim = ObjectAnimator.ofInt(0, 400)
				anim.addUpdateListener {
					val newParams = profile_layout.layoutParams
					newParams.height = dpToPx(it.animatedValue as Int)
					profile_layout.layoutParams = newParams
				}
				anim.duration = 500

				anim.addListener(object : Animator.AnimatorListener {
					override fun onAnimationRepeat(animation: Animator?) {
					}

					override fun onAnimationEnd(
						animation: Animator?, isReverse: Boolean
					) {
						super.onAnimationEnd(
							animation, isReverse
						)
						profile_text.visibility = View.VISIBLE
					}

					override fun onAnimationEnd(animation: Animator?) {
					}

					override fun onAnimationCancel(animation: Animator?) {
					}

					override fun onAnimationStart(
						animation: Animator?, isReverse: Boolean
					) {
						super.onAnimationStart(
							animation, isReverse
						)
					}

					override fun onAnimationStart(animation: Animator?) {

					}
				})
				anim.start()

			} else {
				val anim = ObjectAnimator.ofInt(400, 0)
				anim.addUpdateListener {
					val newParams = profile_layout.layoutParams
					newParams.height = dpToPx(it.animatedValue as Int)
					profile_layout.layoutParams = newParams
				}
				profile_text.visibility = View.GONE
				anim.duration = 500
				anim.start()
			}
		}

		pacient?.let {
			name.text = "Meno: ${it.meno}"
			email.text = "Email: ${it.email}"
			poistovna.text = "Poisťovňa: ${it.poistovna.toString()}"
			krv.text = "Krv: ${it.krvnaSkupina}${it.krvRh}"
			alergie.text = "Alergie:\n${it.alergie?.joinToString(
				separator = "\n"
			)}"
			ochorenia.text = "Ochorenia:\n${it.ochorenia?.joinToString(
				separator = "\n"
			)}"

			val operacieText = StringBuilder()
			operacieText.append("Operacie:\n")
			it.operacie?.forEach {
				operacieText.append(
					"${it.operacia} - ${SimpleDateFormat(
						"dd.MM.yyyy"
					).format(Date(it.datumOperacie!!))}"
				)
				operacieText.append("\n")
			}
			operacie.text = operacieText

			lieky.text = "Lieky:\n${it.lieky?.joinToString(separator = "\n")}"
		}

	}

	fun dpToPx(dp: Int): Int {
		val displayMetrics = resources.displayMetrics
		return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
	}


	/**
	 * A [FragmentPagerAdapter] that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

		override fun getItem(position: Int): Fragment {
			return if (position == 2) {
				if (pacient != null) {
					PlaceFragment.newInstance(
						pacient!!
					)
				} else {
					PacientListFragment.newInstance(doktor!!)
				}
			} else if (position == 1) {
				DoktorFragment.newInstance()
			} else {
				PlaceholderFragment.newInstance(
					position + 1
				)
			}
		}

		override fun getCount(): Int {
			// Show 3 total pages.
			return 3
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	class PlaceholderFragment : Fragment() {

		override fun onCreateView(
			inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
		): View? {
			val rootView = inflater.inflate(
				R.layout.fragment_home, container, false
			)
			return rootView
		}

		companion object {
			/**
			 * The fragment argument representing the section number for this
			 * fragment.
			 */
			private val ARG_SECTION_NUMBER = "section_number"

			/**
			 * Returns a new instance of this fragment for the given section
			 * number.
			 */
			fun newInstance(sectionNumber: Int): PlaceholderFragment {
				val fragment = PlaceholderFragment()
				val args = Bundle()
				args.putInt(
					ARG_SECTION_NUMBER, sectionNumber
				)
				fragment.arguments = args
				return fragment
			}
		}
	}
}
