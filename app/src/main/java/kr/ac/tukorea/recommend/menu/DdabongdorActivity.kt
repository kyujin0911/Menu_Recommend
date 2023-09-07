package kr.ac.tukorea.recommend.menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kr.ac.tukorea.recommend.menu.databinding.ActivityDdabongdorBinding
import kr.ac.tukorea.recommend.menu.util.RestaurantInfo
import okhttp3.internal.wait

class DdabongdorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDdabongdorBinding
    private val database = FirebaseDatabase.getInstance()
    private var restaurants = mutableListOf<RestaurantInfo>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDdabongdorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database.getReference("ResData")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        val value = it.getValue(RestaurantInfo::class.java)
                        restaurants.add(value!!)
                    }
                    val lastIndex = restaurants.lastIndex
                    restaurants.sortWith(compareBy<RestaurantInfo>{it.ddabong}.thenBy { it.name })
                    if (lastIndex != null) {
                        binding.firstDdabong.text = "${restaurants[lastIndex].name}\n" +
                                "${restaurants[lastIndex].category}"
                        binding.firstDdabongCount.text = restaurants[lastIndex].ddabong.toString()
                        binding.secondDdabong.text = "${restaurants[lastIndex-1].name}\n" +
                                "${restaurants[lastIndex-1].category}"
                        binding.secondDdabongCount.text = restaurants[lastIndex-1].ddabong.toString()
                        binding.thirdDdabong.text = "${restaurants[lastIndex-2].name}\n" +
                                "${restaurants[lastIndex-2].category}"
                        binding.thirdDdabongCount.text = restaurants[lastIndex-2].ddabong.toString()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
}