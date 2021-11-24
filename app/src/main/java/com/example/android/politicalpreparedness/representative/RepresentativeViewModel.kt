package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch

class RepresentativeViewModel : ViewModel() {

    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>> = _representatives

    val line1 = MutableLiveData<String>()
    val line2 = MutableLiveData<String>()
    val city = MutableLiveData<String>()
    val state = MutableLiveData<String>()
    val zip = MutableLiveData<String>()

    fun findRepresentatives(address: Address) {
        viewModelScope.launch {
            val response =
                CivicsApi.retrofitService.getRepresentativesAsync(address.toFormattedString())
                    .await()
            _representatives.value =
                response.offices.flatMap { office -> office.getRepresentatives(response.officials) }
        }
    }

    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */

    //TODO: Create function get address from geo location
    fun setInputAddress(address: Address) {
        line1.value = address.line1
        line2.value = address.line2
        city.value = address.city
        state.value = address.state
        zip.value = address.zip
    }

    //TODO: Create function to get address from individual fields
    fun getInputAddress(): Address? {
        if (line1.value != null && city.value != null && state.value != null && zip.value != null) {
            return Address(line1.value!!, line2.value, city.value!!, state.value!!, zip.value!!)
        }
        return null
    }

}
