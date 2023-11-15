package com.example.hospitalpatiententry

import androidx.lifecycle.LiveData
import com.example.hospitalpatiententry.roomDB.DataModal
import com.example.hospitalpatiententry.roomDB.LocalDataBase
import com.example.hospitalpatiententry.roomDB.PatientDAO

class PatientRepository(private val userDao: PatientDAO) {
    val readAllData: LiveData<List<DataModal>> = userDao.getAllData()

    suspend fun addUser(user: DataModal) {
        userDao.insert(user)
    }

    suspend fun getUniqueId(dataBase: LocalDataBase) : Int? {
       return userDao.getMaxId()
    }



   /* suspend fun deleteUser(user: DataModal) {
        userDao.deleteUser(user)
    }*/


}