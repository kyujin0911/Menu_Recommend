package kr.ac.tukorea.recommend.menu

import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.MarkerIcons

class MapViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_view)

        val name = intent.getStringExtra("name")!!
        val lati = intent.getDoubleExtra("lati", 0.0)
        val long = intent.getDoubleExtra("long", 0.0)
        val marker = Marker()
        val markerSchool = Marker()
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