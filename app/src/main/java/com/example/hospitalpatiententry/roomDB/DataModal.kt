package com.example.hospitalpatiententry.roomDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "DataModal")
class DataModal{

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ic")
    var id: Long = 0

    @ColumnInfo(name = "name")
    var name: String =""

    @ColumnInfo(name = "patient_id")
    var patientId: String =""

    @ColumnInfo(name = "age")
    var age: Int =0

    @ColumnInfo(name = "gender")
    var gender: String =""

    @ColumnInfo(name = "latitude")
    var latitude : String? = null

    @ColumnInfo(name = "longitude")
    var longitude : String? = null

}