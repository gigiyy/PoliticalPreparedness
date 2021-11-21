package com.example.android.politicalpreparedness.election

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel(electionDao: ElectionDao) : ViewModel() {

    private val database = electionDao

    //TODO: Create live data val for upcoming elections
    private val _upcoming = MutableLiveData<List<Election>>()
    val upcoming = _upcoming

    //TODO: Create live data val for saved elections
    val saved = database.findAll()

    private val _navigateToVoterInfo = MutableLiveData<Election>()
    val navigateToVoterInfo = _navigateToVoterInfo

    init {
        loadElections()
    }

    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database
    fun loadElections() {
        viewModelScope.launch {
            val electionResponse = CivicsApi.retrofitService.getElectionsAsync().await()
            _upcoming.value = electionResponse.elections
        }
    }

    //TODO: Create functions to navigate to saved or upcoming election voter info
    fun navigateToVoterInfoOf(election: Election) {
        _navigateToVoterInfo.value = election
    }

    fun doneNavigationOfVoterInfo() {
        _navigateToVoterInfo.value = null
    }

    companion object {
        private const val TAG = "ElectionsViewModel"
    }
}