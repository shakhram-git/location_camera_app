package com.example.myapplication.ui

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.CustomInfoWindowBinding
import com.example.myapplication.databinding.FragmentMapsBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.LocationSource
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@SuppressLint("MissingPermission")
class MapsFragment : Fragment() {

    private var locationListener: LocationSource.OnLocationChangedListener? = null
    private var map: GoogleMap? = null
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private var infoWindowBinding: CustomInfoWindowBinding? = null

    private lateinit var locationCallback: LocationCallback

    private val viewModel: MapsViewModel by viewModels()

    private lateinit var fusedClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                p0.lastLocation?.let {
                    locationListener?.onLocationChanged(it)
                }
            }
        }
        fusedClient = LocationServices.getFusedLocationProviderClient(requireContext())
        startLocation()
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync { googleMap ->
            map = googleMap
            with(googleMap.uiSettings) {
                isZoomControlsEnabled = true
                isMyLocationButtonEnabled = true
                isCompassEnabled = true
            }
            googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID
            googleMap.isMyLocationEnabled = true
            googleMap.setOnMarkerClickListener { marker ->
                map?.animateCamera(CameraUpdateFactory.newLatLng(marker.position))
                infoWindowBinding =
                    CustomInfoWindowBinding.inflate(LayoutInflater.from(requireContext()))
                viewModel.getLandmarkInfo(marker.snippet!!)
                AlertDialog.Builder(requireContext())
                    .setView(infoWindowBinding!!.root)
                    .show()
                true
            }
            googleMap.setLocationSource(object : LocationSource {
                override fun activate(p0: LocationSource.OnLocationChangedListener) {
                    locationListener = p0
                }

                override fun deactivate() {
                    locationListener = null
                }
            })
        }
        sendLocation()
        Log.d("collect", "viewCreated")

        binding.refreshBtn.setOnClickListener {
            ObjectAnimator.ofFloat(it, View.ROTATION, 0f, 360f).apply {
                duration = 1000
                interpolator = AccelerateDecelerateInterpolator()
            }.start()
            map?.clear()
            sendLocation()
        }

        lifecycleScope.launchWhenStarted {
            viewModel.landmarks.collect { landmarksList ->
                val result = landmarksList.result
                result.forEach {
                    map?.addMarker(
                        MarkerOptions()
                            .position(LatLng(it.lat, it.lon))
                            .title(it.name)
                            .snippet(it.xid)
                    )
                }
                Log.d("collect", "collected")
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.landmarkInfo.collect { landmarkInfo ->
                infoWindowBinding?.let { binding ->
                    binding.progress.isVisible =
                        landmarkInfo !is MapsViewModel.LandmarkInfoStatus.Loaded
                    binding.infoContainer.isVisible =
                        landmarkInfo is MapsViewModel.LandmarkInfoStatus.Loaded
                    if (landmarkInfo is MapsViewModel.LandmarkInfoStatus.Loaded) {
                        val info = landmarkInfo.result
                        binding.country.text = info.country
                        binding.name.text = info.name
                        binding.address.text = getString(
                            R.string.address_format,
                            info.city ?: "",
                            info.street ?: "",
                            info.houseNumber ?: ""
                        )
                        binding.text.text = info.text
                        Glide.with(requireContext())
                            .load(info.imageSource)
                            .error(R.drawable.ic_baseline_image_not_supported_24)
                            .into(binding.imageContainer)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fusedClient.removeLocationUpdates(locationCallback)
        _binding = null
    }

    private fun startLocation() {
        map?.isMyLocationEnabled = true
        val request = LocationRequest.create()
            .setInterval(10000)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)

        fusedClient.requestLocationUpdates(
            request,
            locationCallback,
            Looper.getMainLooper()
        )

        fusedClient.lastLocation.addOnCompleteListener {
            val location = it.result
            map?.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    (LatLng(
                        location.latitude,
                        location.longitude
                    )), 14f
                )
            )
        }
    }

    private fun sendLocation() {
        fusedClient.lastLocation.addOnCompleteListener { task ->
            val loc = task.result
            viewModel.getLandmarksAround(loc.longitude, loc.latitude)
            Log.d("collect", "location send")
        }
    }
}