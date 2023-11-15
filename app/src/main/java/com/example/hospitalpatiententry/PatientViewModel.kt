package com.example.hospitalpatiententry

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.hospitalpatiententry.roomDB.DataModal
import com.example.hospitalpatiententry.roomDB.LocalDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PatientViewModel (application: Application): AndroidViewModel(application) {
    val readAllData: LiveData<List<DataModal>>
    private val repository: PatientRepository

    init {
        val userDao = LocalDataBase.getDatabase(application).PatientDAO()
        repository= PatientRepository(userDao)
        readAllData = repository.readAllData
    }

    fun addUser(user: DataModal) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user)
        }
    }

   suspend fun getUniqueId(dataBase: LocalDataBase) : Int? {
        return withContext(Dispatchers.IO) {
            repository.getUniqueId(dataBase)
        }
    }


  /*  fun deleteUser(user: DataModal) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteUser(user)
        }
    }*/

   /* fun deleteAllUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllUsers()
        }
    }*/
}