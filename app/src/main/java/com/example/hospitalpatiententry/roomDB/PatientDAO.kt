package com.example.hospitalpatiententry.roomDB

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PatientDAO {

    // insert data into DB
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(dataModal: DataModal ) : Long

    // get all data from DB
    @Query("SELECT * FROM DataModal")
    fun getAllData(): LiveData<List<DataModal>>

    //get the last unique id
    @Query("SELECT MAX(CAST(SUBSTR(patient_id, 4) AS INTEGER)) FROM DataModal")
    fun getMaxId(): Int?
}