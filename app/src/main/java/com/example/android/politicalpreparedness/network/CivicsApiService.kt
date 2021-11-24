package com.example.android.politicalpreparedness.network

import com.example.android.politicalpreparedness.network.jsonadapter.DateJsonAdapter
import com.example.android.politicalpreparedness.network.jsonadapter.ElectionAdapter
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

private const val BASE_URL = "https://www.googleapis.com/civicinfo/v2/"
private const val ELECTIONS_URL = "elections"
private const val VOTERINFO_URL = "voterinfo"
private const val REPRESENTATIVES_URL = "representatives"

private val moshi = Moshi.Builder()
    .add(ElectionAdapter())
    .add(Date::class.java, DateJsonAdapter())
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .client(CivicsHttpClient.getClient())
    .baseUrl(BASE_URL)
    .build()

/**
 *  Documentation for the Google Civics API Service can be found at https://developers.google.com/civic-information/docs/v2
 */

interface CivicsApiService {
    @GET(ELECTIONS_URL)
    fun getElectionsAsync(): Deferred<ElectionResponse>

    @GET(VOTERINFO_URL)
    fun getVoterInfoAsync(
        @Query("address") address: String,
        @Query("electionId") electionId: Long
    ): Deferred<VoterInfoResponse>

    @GET(REPRESENTATIVES_URL)
    fun getRepresentativesAsync(@Query("address") address: String): Deferred<RepresentativeResponse>
}

object CivicsApi {
    val retrofitService: CivicsApiService by lazy {
        retrofit.create(CivicsApiService::class.java)
    }
}