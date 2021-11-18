package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    //TODO: Add insert query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg elections: Election)

    //TODO: Add select all election query
    @Query("select * from election_table")
    fun findAll(): LiveData<List<Election>>

    //TODO: Add select single election query
    @Query("select * from election_table where id = :electionId")
    fun findElection(electionId: Long): LiveData<Election>

    //TODO: Add delete query
    @Query("delete from election_table where id = :electionId")
    suspend fun deleteElection(electionId: Long)

    //TODO: Add clear query
    @Query("delete from election_table")
    suspend fun deleteAll()

}