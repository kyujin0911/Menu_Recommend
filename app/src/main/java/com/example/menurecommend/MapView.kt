package com.example.menurecommend

import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.ColorFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapOptions
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.MarkerIcons
import java.security.Permission

class MapView : AppCompatActivity() {


    var permissions = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

    val TAG = "latilong"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_view)


        val name = intent.getStringExtra("name")!!
        val lati = intent.getDoubleExtra("lati", 0.0)
        val long = intent.getDoubleExtra("long", 0.0)
        val marker = Marker()
        val markerSchool = Marker()
        val cameraUpdate = CameraUpdate.scrollTo(LatLng(lati, long))
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.navermap) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.navermap, it).commit()
            }



        mapFragment.getMapAsync { naverMap ->
            val averageLatitude = (lati + 37.340276827203006) / 2
            val averageLongitude = (long + 126.73154260098929) / 2


            val cameraUpdate = CameraUpdate.scrollTo(LatLng(averageLatitude, averageLongitude))

            naverMap.moveCamera(cameraUpdate)

            Log.d(TAG, "${lati},${long}")
            marker.position = LatLng(lati, long)
            markerSchool.position = LatLng(37.340276827203006, 126.73154260098929)
            marker.icon = MarkerIcons.BLACK
            markerSchool.icon = MarkerIcons.LIGHTBLUE
            markerSchool.iconTintColor = Color.BLUE
            markerSchool.captionText = "한국공학대학교"
            markerSchool.captionTextSize = 15f
            marker.iconTintColor = Color.RED
            marker.captionText = name
            marker.captionTextSize = 15f
            marker.map = naverMap
            markerSchool.map = naverMap
            naverMap.minZoom = 5.0


        }


    }
}
