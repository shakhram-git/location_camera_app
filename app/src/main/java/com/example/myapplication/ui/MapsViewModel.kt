package com.example.myapplication.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.Repository
import com.example.myapplication.entity.Landmark
import com.example.myapplication.entity.LandmarkInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _landmarks = MutableStateFlow(LandmarksAroundList(emptyList()))
    val landmarks = _landmarks.asStateFlow()

    private val _landMarkInfo: MutableStateFlow<LandmarkInfoStatus> =
        MutableStateFlow(LandmarkInfoStatus.Loading)
    val landmarkInfo = _landMarkInfo.asStateFlow()

    fun getLandmarksAround(lon: Double, lat: Double) {
        viewModelScope.launch {
            //val result = repository.getLandmarksAround(lon, lat).shuffled()
            val result = repository.getLandmarksAround(lon, lat)
            _landmarks.value = LandmarksAroundList(result)
            Log.d("collect", _landmarks.value.toString())
        }
    }

    fun getLandmarkInfo(xid: String) {
        viewModelScope.launch {
            _landMarkInfo.value = LandmarkInfoStatus.Loading
            val result = repository.getLandmarkInfo(xid)
            _landMarkInfo.value = LandmarkInfoStatus.Loaded(result)
        }
    }

    sealed class LandmarkInfoStatus {
        object Loading : LandmarkInfoStatus()
        data class Loaded(val result: LandmarkInfo) : LandmarkInfoStatus()
    }

    class LandmarksAroundList(
        val result: List<Landmark>
    )

}

