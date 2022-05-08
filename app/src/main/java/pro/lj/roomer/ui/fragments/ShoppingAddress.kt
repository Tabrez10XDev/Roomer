package pro.lj.roomer.ui.fragments

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import pro.lj.roomer.R
import pro.lj.roomer.databinding.ShoppingAddressBinding


class ShoppingAddress : Fragment(R.layout.shopping_address) {
    private var _binding: ShoppingAddressBinding? = null
    private val binding get() = _binding!!
    lateinit var locationManager: LocationManager

    var locationByGps : Location ?= null
    var locationByNetwork : Location ?= null
    private val COUNTRIES = arrayOf(
            "Belgium", "France", "Italy", "Germany", "Spain"
    )

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = ShoppingAddressBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter: ArrayAdapter<String> = ArrayAdapter(requireContext(),
                android.R.layout.simple_dropdown_item_1line, COUNTRIES)
        binding.outlinedExposedDropdownEditable.setAdapter(adapter)
        binding.btnLocation.setOnClickListener {
            fetchLocation()
        }
    }


    @SuppressLint("MissingPermission")
    fun fetchLocation(){
        locationManager = requireActivity().getSystemService(android.content.Context.LOCATION_SERVICE) as LocationManager
        val hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        val networkLocationListener: LocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                Log.d("baby", location.latitude.toString() + "\n" + location.longitude + "\n")

                locationByNetwork= location
                return
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {
                Toast.makeText(requireContext(), "Check your connection",Toast.LENGTH_SHORT).show()
                return
            }
        }

        if (hasNetwork) {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    5000,
                    0F,
                    networkLocationListener
            )
        }
    }

}