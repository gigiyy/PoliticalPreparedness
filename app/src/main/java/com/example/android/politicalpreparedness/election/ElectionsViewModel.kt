package com.example.android.politicalpreparedness.election

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.launch

class ElectionsViewModel(electionDao: ElectionDao) : ViewModel() {

    private val database = electionDao

    private val _upcoming = MutableLiveData<List<Election>>()
    val upcoming = _upcoming

    val saved = database.findAll()

    private val _navigateToVoterInfo = MutableLiveData<Election>()
    val navigateToVoterInfo = _navigateToVoterInfo

    init {
        loadElections()
    }

    private fun loadElections() {
        viewModelScope.launch {
            val electionResponse = CivicsApi.retrofitService.getElectionsAsync().await()
            _upcoming.value = electionResponse.elections
        }
    }

    fun navigateToVoterInfoOf(election: Election) {
        _navigateToVoterInfo.value = election
    }

    fun doneNavigationOfVoterInfo() {
        _navigateToVoterInfo.value = null
    }
}