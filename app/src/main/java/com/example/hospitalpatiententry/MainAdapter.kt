package com.example.hospitalpatiententry

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.hospitalpatiententry.databinding.PatientDetailsBinding
import com.example.hospitalpatiententry.roomDB.DataModal
import java.util.Locale


class MainAdapter(context : Context,listener : OnItemClickListerner) : RecyclerView.Adapter<MainAdapter.ViewHolder>()  {

    lateinit var context : Context
    lateinit var listener: OnItemClickListerner
    init {
        this.context = context
        this.listener = listener
    }

    private var patientList = ArrayList<DataModal>()
    fun setMovieList(patientList : List<DataModal>){
        this.patientList = patientList as ArrayList<DataModal>
        notifyDataSetChanged()
    }

    class ViewHolder(val binding : PatientDetailsBinding) : RecyclerView.ViewHolder(binding.root)  {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            PatientDetailsBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun getItemCount(): Int {
        return patientList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val patient = patientList[position]
        holder.binding.textViewName.text = "Name : "+patient.name
        holder.binding.textViewPatientId.text = "Id : "+patient.patientId.toString()
        holder.binding.textViewGender.text = "Gender : "+patient.gender
        holder.binding.textViewAge.text = "Age : "+patient.age.toString()
        holder.binding.textViewAddress.text = "Location :" + getAddress(patient.latitude!!.toDouble(),patient.longitude!!.toDouble())

        if (patient.gender.equals("male",ignoreCase = true))
            holder.binding.tvProfile.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.man))
        else
            holder.binding.tvProfile.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.woman))

        holder.binding.textViewMap.setOnClickListener { listener.onProductClicked(position) }

    }

    fun getAddress(latitude : Double,longitude : Double) : String{
        val geocoder: Geocoder
        val addresses: List<Address>?
        geocoder = Geocoder(context, Locale.getDefault())

        addresses = geocoder.getFromLocation(
            latitude,
            longitude,
            1
        ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5


        val address =
            addresses!![0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        val city = addresses!![0].locality
        val state = addresses!![0].adminArea
        val country = addresses!![0].countryName
        val postalCode = addresses!![0].postalCode
        val knownName = addresses!![0].featureName
        return "$knownName, $city"
    }

    interface OnItemClickListerner
    {
        fun onProductClicked(position: Int)
    }

}