package com.example.hospitalpatiententry

import android.app.Application
import androidx.room.Room
import com.example.hospitalpatiententry.roomDB.LocalDataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ApplicationContainer : Application() {

    val database by lazy {
        Room.databaseBuilder(this, LocalDataBase::class.java, "Patient_Details")
            .fallbackToDestructiveMigration()
            .build()
    }
}