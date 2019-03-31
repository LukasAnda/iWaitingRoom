package com.lukasanda.medicalapp

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Doktor(
    var meno: String? = "",
    var email: String? = "",
    var id: String? = "",
    var specifikacia: String? = "",
    var poistovne: List<Int>? = emptyList(),
    var otvaracieHodiny: List<OtvaraciaHodina>? = emptyList(),
    var cakaren: MutableList<Cakaren>? = mutableListOf(),
    var adminCas: Int? = 0,
    var vysetrenieCas: Int? = 0,
	var priemernyDen: List<HodinaACas>? = emptyList(),
	var chorobnyDen: List<HodinaACas>? = emptyList()
) : Parcelable

@Parcelize
data class OtvaraciaHodina(
    var den: String? = "",
    var cas: String? = ""
) : Parcelable

@Parcelize
data class HodinaACas(
	var hodina: Int = 0,
	var cas: Int = 0
): Parcelable

@Parcelize
data class Cakaren(
    var id: String? = "",
    var typ: String? = "",
    var cas: Long? = 0L,
	var komentar: String? = "",
	var position: Int = 0
) : Parcelable

@Parcelize
data class Patient(
    var meno: String? = "",
    var email: String? = "",
    var id: String? = "",
    var poistovna: Int? = 0,
    var krvnaSkupina: String? = "",
    var krvRh: String? = "",
    var alergie: List<String>? = emptyList(),
    var ochorenia: List<String>? = emptyList(),
    var operacie: List<Operacia>? = emptyList(),
    var lieky: List<String>? = emptyList(),
    var cakaren: String? = ""
) : Parcelable

data class WaitingPatient(
	var from: Long,
	var patient: Patient,
	var type: String,
	var comment: String,
	var position: Int
)

@Parcelize
data class Operacia(
    var datumOperacie: Long? = 0L,
    var operacia: String? = ""
) : Parcelable
