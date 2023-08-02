package kr.ac.tukorea.recommend.menu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kr.ac.tukorea.recommend.menu.databinding.ActivityDdabongdorBinding
import kr.ac.tukorea.recommend.menu.util.RestaurantInfo

class DdabongdorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDdabongdorBinding
    private val database = FirebaseDatabase.getInstance()
    var index = 0
    var restaurants = mutableListOf<RestaurantInfo>()
    val TAG = "Ddabong"
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
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })

        restaurants.sortWith(compareBy<RestaurantInfo>{it.ddabong}.thenBy { it.name })

    }
}