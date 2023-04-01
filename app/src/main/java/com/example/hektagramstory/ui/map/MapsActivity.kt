package com.example.hektagramstory.ui.map

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.hektagramstory.R
import com.example.hektagramstory.data.Result
import com.example.hektagramstory.databinding.ActivityMapsBinding
import com.example.hektagramstory.ui.ViewModelFactory
import com.example.hektagramstory.utils.SharedPreferencesManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private val boundsBuilder = LatLngBounds.Builder()
    private lateinit var mMap: GoogleMap
    private var _binding: ActivityMapsBinding? = null
    private val binding get() = _binding
    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val actionBar = supportActionBar
        actionBar?.title = resources.getString(R.string.story_location)

        sharedPreferencesManager = SharedPreferencesManager(this)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this@MapsActivity)
        val viewModel: MapViewModel by viewModels {
            factory
        }
        val token = sharedPreferencesManager.getUser()
        if (token != null) {
            viewModel.getAllStoriesLocation("Bearer $token", 1).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            Toast.makeText(
                                this,
                                resources.getString(R.string.loading),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        is Result.Success -> {
                            result.data.forEach {
                                val latLng = LatLng(
                                    it.lat.toString().toDouble(),
                                    it.lon.toString().toDouble()
                                )
                                mMap.addMarker(
                                    MarkerOptions().position(latLng).title(" ${it.name}")
                                        .snippet("${it.description}")
                                )
                                boundsBuilder.include(latLng)
                            }
                            val bounds: LatLngBounds = boundsBuilder.build()
                            mMap.animateCamera(
                                CameraUpdateFactory.newLatLngBounds(
                                    bounds,
                                    resources.displayMetrics.widthPixels,
                                    resources.displayMetrics.heightPixels,
                                    300
                                )
                            )
                        }
                        is Result.Error -> {
                            Toast.makeText(
                                this,
                                result.error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

            }
        }
    }
}