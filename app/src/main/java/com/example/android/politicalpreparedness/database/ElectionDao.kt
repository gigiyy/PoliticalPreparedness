package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg elections: Election)

    @Query("select * from election_table")
    fun findAll(): LiveData<List<Election>>

    @Query("select * from election_table where id = :electionId")
    suspend fun findElection(electionId: Long): Election?

    @Query("delete from election_table where id = :electionId")
    suspend fun deleteElection(electionId: Long)

    @Query("delete from election_table")
    suspend fun deleteAll()

}