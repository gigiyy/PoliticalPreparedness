package com.example.android.politicalpreparedness.network.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Division(
    val id: String,
    val country: String,
    val state: String
) : Parcelable {
    fun getAddress(): String {
        val addressState = if (state.isEmpty()) "ca" else state
        val addressCountry = if (country.isEmpty()) "us" else country
        return "%s, %s".format(addressState, addressCountry)
    }
}