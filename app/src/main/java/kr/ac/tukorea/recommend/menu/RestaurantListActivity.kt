package kr.ac.tukorea.recommend.menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kr.ac.tukorea.recommend.menu.databinding.ActivityRestaurantListBinding
import kr.ac.tukorea.recommend.menu.databinding.ItemRestaurantBinding
import kr.ac.tukorea.recommend.menu.util.RestaurantAdapter
import kr.ac.tukorea.recommend.menu.util.RestaurantInfo

class RestaurantListActivity : AppCompatActivity(), RestaurantAdapter.ItemClickListener {
    private lateinit var binding: ActivityRestaurantListBinding
    private lateinit var restaurantAdapter: RestaurantAdapter
    private var restaurants = mutableListOf<RestaurantInfo>()
    private val database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database.getReference("ResData")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        val value = it.getValue(RestaurantInfo::class.java)
                        restaurants.add(value!!)
                    }
                    restaurantAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

        restaurantAdapter = RestaurantAdapter(restaurants, this)
        binding.restaurantRecyclerView.apply {
            adapter = restaurantAdapter
            layoutManager =
                LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        }

        binding.sortBy.setOnCheckedChangeListener { _, _ ->
            if (binding.sortByName.isChecked) {
                restaurants.sortBy {it.name}
                restaurantAdapter.notifyDataSetChanged()
            } else if (binding.sortByReview.isChecked) {
                restaurants.sortByDescending { it.review_count }
                restaurantAdapter.notifyDataSetChanged()
            } else {
                restaurants.sortWith(
                    compareBy<RestaurantInfo>(
                        { it.rate },
                        { it.review_count }).reversed()
                )
                restaurantAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onClick(restaurantInfo: RestaurantInfo) {
        restaurantInfo?.let { currentRes ->
            val intent = Intent(this, MapViewActivity::class.java)
            intent.putExtra("name", currentRes.name)
            intent.putExtra("lati", currentRes.Latitude)
            intent.putExtra("long", currentRes.Longitude)
            startActivity(intent)
        }
    }
}