package com.example.menu

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.menu.databinding.ActivityMapViewBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.MarkerIcons

class MapView : AppCompatActivity() {

    val TAG = "latilong"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_view)

        val name = intent.getStringExtra("name")!!
        val lati = intent.getDoubleExtra("lati", 0.0)
        val long = intent.getDoubleExtra("long", 0.0)
        val marker = Marker()
        val cameraUpdate = CameraUpdate.scrollTo(LatLng(lati, long))
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.navermap) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.navermap, it).commit()
            }



        mapFragment.getMapAsync { naverMap ->
            Log.d(TAG, "${lati},${long}")
            marker.position = LatLng(lati, long)
            marker.icon = MarkerIcons.BLACK
            marker.iconTintColor = Color.RED
            marker.captionText = name
            marker.captionTextSize = 15f
            marker.map = naverMap
            naverMap.moveCamera(cameraUpdate)

        }

    }
}
