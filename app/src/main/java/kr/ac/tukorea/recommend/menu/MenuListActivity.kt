package kr.ac.tukorea.recommend.menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kr.ac.tukorea.recommend.menu.databinding.ActivitySortBinding
import kr.ac.tukorea.recommend.menu.util.RestaurantAdapter
import kr.ac.tukorea.recommend.menu.util.RestaurantInfo

class MenuListActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySortBinding
    private lateinit var restaurantAdapter: RestaurantAdapter
    private var restaurants = mutableListOf<RestaurantInfo>()
    private val database = FirebaseDatabase.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySortBinding.inflate(layoutInflater)
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

        restaurantAdapter = RestaurantAdapter(restaurants)
        binding.restaurantRecyclerView.apply {
            adapter = restaurantAdapter
            layoutManager =
                LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        }

        binding.sortBy.setOnCheckedChangeListener { group, checkedId ->
            if (binding.sortByName.isChecked) {
                restaurants.sortBy { it.name }
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
        binding.homeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}