package com.example.android.politicalpreparedness.network.jsonadapter

import com.example.android.politicalpreparedness.network.models.Division
import com.squareup.moshi.*
import java.text.SimpleDateFormat
import java.util.*

class ElectionAdapter {
    @FromJson
    fun divisionFromJson (ocdDivisionId: String): Division {
        val countryDelimiter = "country:"
        val stateDelimiter = "state:"
        val country = ocdDivisionId.substringAfter(countryDelimiter,"")
                .substringBefore("/")
        val state = ocdDivisionId.substringAfter(stateDelimiter,"")
                .substringBefore("/")
        return Division(ocdDivisionId, country, state)
    }

    @ToJson
    fun divisionToJson (division: Division): String {
        return division.id
    }
}


class DateJsonAdapter: JsonAdapter<Date>() {

    private val dateFormat = "yyyy-MM-dd"
    private val sdFormat = SimpleDateFormat(dateFormat, Locale.JAPAN)

    @Synchronized
    @Throws(Exception::class)
    override fun fromJson(reader: JsonReader): Date  {
        val string = reader.nextString()
        return sdFormat.parse(string)
    }

    @Synchronized
    @Throws(Exception::class)
    override fun toJson(writer: JsonWriter, value: Date?) {
        writer.value(sdFormat.format(value))
    }

}