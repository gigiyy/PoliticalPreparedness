package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApi
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
            _voterInfo.value = response
        }
        viewModelScope.launch {
            val election = dataSource.findElection(electionId.toLong())
            _saveButtonState.value = election != null
        }
    }
}