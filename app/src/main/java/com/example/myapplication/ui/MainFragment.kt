package com.example.myapplication.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val photosAdapter = PhotosAdapter()
    private val viewModel: MainViewModel by viewModels()
    private val cameraPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
            if (map.values.all { it }) {
                findNavController().navigate(R.id.action_mainFragment_to_cameraFragment)
            } else {
                Toast.makeText(
                    requireContext(),
                    "You can't use camera without the permissions",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    private val locationPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
            if (map.values.all { it }) {
                findNavController().navigate(R.id.action_mainFragment_to_mapsFragment)
            } else {
                Toast.makeText(
                    requireContext(),
                    "You can't use maps without the permissions",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.photosList.apply {
            adapter = photosAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            setHasFixedSize(true)
        }
        binding.takePhotoBtn.setOnClickListener {
            if (checkPermissions(REQUEST_CAM_PERMISSIONS)) {
                findNavController().navigate(R.id.action_mainFragment_to_cameraFragment)
            } else {
                cameraPermissions.launch(REQUEST_CAM_PERMISSIONS.toTypedArray())
            }
        }
        binding.openMapBtn.setOnClickListener {
            if (checkPermissions(REQUEST_LOC_PERMISSIONS)) {
                findNavController().navigate(R.id.action_mainFragment_to_mapsFragment)
            } else {
                locationPermissions.launch(REQUEST_LOC_PERMISSIONS.toTypedArray())
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.photos.collect {
                photosAdapter.submitList(it)
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkPermissions(permissions: List<String>): Boolean {
        val isAllGranted = permissions.all { permission ->
            ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
        return isAllGranted
    }

    companion object {
        private val REQUEST_CAM_PERMISSIONS = buildList {
            add(Manifest.permission.CAMERA)
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toList()
        private val REQUEST_LOC_PERMISSIONS = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

}