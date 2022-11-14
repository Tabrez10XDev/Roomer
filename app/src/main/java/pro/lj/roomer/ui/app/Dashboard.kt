package pro.lj.roomer.ui.app

import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import pro.lj.roomer.R
import pro.lj.roomer.api.MapboxAPI
import pro.lj.roomer.databinding.DashboardBinding
import pro.lj.roomer.repositories.MainRepository
import pro.lj.roomer.viewmodel.MainVIewModelProviderFactory
import pro.lj.roomer.viewmodel.MainViewModel

@AndroidEntryPoint
class Dashboard : AppCompatActivity() {
    private lateinit var binding: DashboardBinding
    var doubleBackToExitPressedOnce: Boolean ?= null
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = MainRepository()
        val viewModelProviderFactory = MainVIewModelProviderFactory(repository)
        viewModel = ViewModelProvider(this,viewModelProviderFactory).get(MainViewModel::class.java)
        binding = DashboardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.navView.setItemSelected(R.id.home)
        binding.navView.setOnItemSelectedListener {
            when(it){
                R.id.home ->{
                    findNavController(R.id.dashboardFragment).popBackStack(R.id.dashboard_nav, true)
                    findNavController(R.id.dashboardFragment).navigate(it)

                }else->{
                findNavController(R.id.dashboardFragment).popBackStack(R.id.dashboard_nav, true)
                findNavController(R.id.dashboardFragment).navigate(it)

            }
            }
        }

//        binding.bottomNavBar.setupWithNavController(findNavController(R.id.fragment2))
//
//        binding.bottomNavBar.itemIconTintList = null

        isLocationPermissionGranted()

    }


    override fun onBackPressed() {
        super.onBackPressed()
        if(doubleBackToExitPressedOnce == true){ this?.finish() }

        doubleBackToExitPressedOnce = true
        binding.navView.setItemSelected(R.id.home)
        findNavController(R.id.dashboardFragment).popBackStack(R.id.dashboard_nav, true)
        findNavController(R.id.dashboardFragment).navigate(R.id.home)

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }

    private fun isLocationPermissionGranted(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    143
            )
            false
        } else {
            true
        }
    }

}