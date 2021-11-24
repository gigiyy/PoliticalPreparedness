package com.example.android.politicalpreparedness.election

import android.view.View
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.AdministrationBody
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.launch

class VoterInfoViewModel(private val dataSource: ElectionDao) : ViewModel() {

    //TODO: Add live data to hold voter info

    //TODO: Add var and methods to populate voter info
    private val _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo: LiveData<VoterInfoResponse> = _voterInfo

    //TODO: Add var and methods to support loading URLs
    val administrationBody: LiveData<AdministrationBody> = Transformations.map(_voterInfo) {
        it?.state?.first()?.electionAdministrationBody
    }

    private val _infoLoaded = MutableLiveData<Int>(View.INVISIBLE)
    val infoLoaded: LiveData<Int> = _infoLoaded

    val hasAddress: LiveData<Int> = Transformations.map(administrationBody) {
        if (administrationBody.value?.correspondenceAddress != null) View.VISIBLE else View.INVISIBLE
    }

    private val _targetUrl = MutableLiveData<String>()
    val targetUrl: LiveData<String> = _targetUrl

    fun setTargetUrl(url: String) {
        _targetUrl.value = url
    }

    fun doneNavigation() {
        _targetUrl.value = null
    }

    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status
    private val _saveButtonState = MutableLiveData<Boolean>()
    val saveButtonState: LiveData<Boolean> = _saveButtonState

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

    fun saveButtonClicked(election: Election) {
        _saveButtonState.value?.let { state ->
            viewModelScope.launch {
                if (state) {
                    dataSource.deleteElection(election.id.toLong())
                    _saveButtonState.value = false
                } else {
                    dataSource.insertAll(election)
                    _saveButtonState.value = true
                }
            }
        }
    }

    fun loadVoterInfo(electionId: Int, division: Division) {
        viewModelScope.launch {
            val response = CivicsApi.retrofitService.getVoterInfoAsync(
                division.getAddress(),
                electionId.toLong()
            ).await()
            _infoLoaded.value = View.VISIBLE
            _voterInfo.value = response
        }
        viewModelScope.launch {
            val election = dataSource.findElection(electionId.toLong())
            _saveButtonState.value = election != null
        }
    }
}