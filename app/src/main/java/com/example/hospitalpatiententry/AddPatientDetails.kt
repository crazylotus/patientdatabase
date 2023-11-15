package com.example.hospitalpatiententry

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.hospitalpatiententry.databinding.ActivityAddPatientDetailsBinding
import com.example.hospitalpatiententry.roomDB.DataModal
import com.example.hospitalpatiententry.roomDB.LocalDataBase
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnCameraMoveListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import kotlinx.coroutines.runBlocking


class AddPatientDetails : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding : ActivityAddPatientDetailsBinding
    private lateinit var mPatientViewModel: PatientViewModel
    var gender = ""


    var latitude =0.0
    var longitude =0.0
    //google map
    lateinit var mapFragment: SupportMapFragment
    lateinit var googleMap: GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPatientDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        val database = (application as ApplicationContainer).database


        //google map integration
        mapFragment = supportFragmentManager.findFragmentById(R.id.search_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mPatientViewModel =  ViewModelProvider(this).get(PatientViewModel::class.java)

        binding.radioMale.setOnClickListener {
            gender = "Male"
            closeKeyBoard()
        }
        binding.radioFemale.setOnClickListener {
            gender = "Female"
            closeKeyBoard()
        }

        binding.btnRegister.setOnClickListener {
            if(binding.editTextName.text.equals("") || !binding.editTextName.text.isNotEmpty()){
                Toast.makeText(this,"Enter the name",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(binding.editTextAge.text.equals("") || !binding.editTextAge.text.isNotEmpty()){
                Toast.makeText(this,"Enter the age",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(gender.equals("")){
                Toast.makeText(this,"Enter the gender",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(latitude.equals(0.0)){
                Toast.makeText(this,"Select the location",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.e("Location","saved location $latitude $longitude")

            //patient data
            val data = DataModal()
            data.name = binding.editTextName.text.toString()
            data.age = binding.editTextAge.text.toString().toInt()
            data.gender = gender
            data.latitude =latitude.toString()
            data.longitude =longitude.toString()
            runBlocking {
                data.patientId = generateUniqueKey(database)
            }

            //patient date insert function
            mPatientViewModel.addUser(data)
            Toast.makeText(this, "Successfully added!", Toast.LENGTH_LONG).show()
           onBackPressed()
        }



        binding.btnLocation.setOnClickListener {
            binding.map.visibility = View.VISIBLE
            closeKeyBoard()
        }

        binding.close.setOnClickListener {
            latitude = 0.0
            longitude = 0.0
            binding.map.visibility = View.GONE
        }

        binding.btnSelectLocation.setOnClickListener {
            binding.map.visibility = View.GONE
        }

    }

    private fun closeKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    //Generate the Unique id
    suspend fun generateUniqueKey(database: LocalDataBase): String {
            val maxId = mPatientViewModel.getUniqueId(database) ?: 0
            val nextId = maxId + 1
            return "HRS${String.format("%03d", nextId)}"

    }

    //map theme
    fun isDarkTheme(resources: Resources): Boolean {
        return resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }

    //Map function
    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.uiSettings.isMapToolbarEnabled = false

        if (isDarkTheme(resources)) {
            googleMap!!.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this,
                    R.raw.map_style_dark
                )
            )
        } else {
            googleMap!!.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
        }

        //using camera position to get the target location latitude longitude
        googleMap.setOnCameraMoveListener(OnCameraMoveListener {
            val target: LatLng = googleMap.getCameraPosition().target
            latitude = target.latitude
            longitude = target.longitude
        })
    }
}