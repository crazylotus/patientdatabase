package com.example.hospitalpatiententry.roomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [DataModal::class], version = 1, exportSchema = false)
abstract class LocalDataBase : RoomDatabase() {
    abstract fun PatientDAO(): PatientDAO

    companion object {
        @Volatile
        private var INSTANCE: LocalDataBase? = null

        fun getDatabase(context: Context): LocalDataBase{
            val tempInstance = INSTANCE

            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalDataBase::class.java,
                    "DataModal"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}