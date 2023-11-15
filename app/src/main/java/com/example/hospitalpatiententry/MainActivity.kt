package com.example.hospitalpatiententry


import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.hospitalpatiententry.databinding.ActivityMainBinding
import com.example.hospitalpatiententry.roomDB.DataModal
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(),MainAdapter.OnItemClickListerner, OnMapReadyCallback {

    lateinit var binding : ActivityMainBinding
    lateinit var adapter : MainAdapter
    private lateinit var mPatientViewModel: PatientViewModel
    lateinit var googleMap: GoogleMap
    var latitude =0.0
    var longitude =0.0
    lateinit var mapFragment: SupportMapFragment
    var patientList : ArrayList<DataModal> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Add patient details
        binding.tvAdd.setOnClickListener {
            startActivity(Intent(this,AddPatientDetails::class.java))
        }

        mPatientViewModel =  ViewModelProvider(this).get(PatientViewModel::class.java)

        adapter = MainAdapter(this,this)

        var layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

        binding.rvPatient.layoutManager = layoutManager
        binding.rvPatient.adapter = adapter
        binding.rvPatient.setHasFixedSize(true)
        mPatientViewModel = ViewModelProvider(this).get(PatientViewModel::class.java)
        mPatientViewModel.readAllData.observe(this, Observer { user ->

            for(patient in user)
                patientList.add(patient)

            binding.tvNoData.visibility = if(patientList.size>0) View.GONE else View.VISIBLE

            adapter.setMovieList(user)
        })

        binding.close.setOnClickListener {
            binding.map.visibility=View.GONE
            latitude =0.0
            longitude =0.0
            val latLong: LatLng
            latLong = LatLng(latitude, longitude)
            val cameraPosition = CameraPosition.Builder()
                .target(latLong).zoom(0f).tilt(0f).build()
            googleMap.moveCamera(
                CameraUpdateFactory
                    .newCameraPosition(cameraPosition)
            )
            googleMap.clear()
        }

        binding.goLocation.setOnClickListener {
            markLocation()
        }

        //Google map integration
        mapFragment = supportFragmentManager.findFragmentById(R.id.search_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }
    fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }
    fun mapDialog(latitude: Double = 9.9246, longitude : Double = 78.1386 ){

        binding.map.visibility= View.VISIBLE
        binding.goLocation.visibility= View.VISIBLE
        this.longitude= longitude
        this.latitude = latitude

        markLocation()
    }

    //Mark location in google map
    fun markLocation(){
        binding.goLocation.visibility= View.GONE
        googleMap.uiSettings.isZoomControlsEnabled = false
        val latLong: LatLng
        latLong = LatLng(latitude, longitude)
        Log.e("location---->","$latitude $longitude")

        val cameraPosition = CameraPosition.Builder()
            .target(latLong).zoom(16.5f).tilt(0f).build()

        // if we need to show the current location on map, we need to enable location permission
        /*if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return@setOnClickListener
        }*/
        //googleMap.isMyLocationEnabled = true
        googleMap.uiSettings.isMyLocationButtonEnabled = false
        googleMap.moveCamera(
            CameraUpdateFactory
                .newCameraPosition(cameraPosition)
        )

        val dropmarker: MarkerOptions
        dropmarker = MarkerOptions()
        dropmarker.position(latLong)
        dropmarker.icon(
            bitmapDescriptorFromVector(
                this,
                R.drawable.baseline_location_on_24
            )
        )
        dropmarker.alpha(.6f)
        googleMap.addMarker(dropmarker)
    }
    override fun onProductClicked(position: Int) {
        mapDialog(patientList[position].latitude!!.toDouble(),patientList[position].longitude!!.toDouble())
    }

    fun isDarkTheme(resources: Resources): Boolean {
        return resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }

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

    }
}