package kr.ac.tukorea.recommend.menu

import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kr.ac.tukorea.recommend.menu.databinding.ActivitySortBinding

class SortActivity : AppCompatActivity() {
    lateinit var binding: ActivitySortBinding
    lateinit var restaurantAdapter: RestaurantAdapter
    val database = FirebaseDatabase.getInstance()
    var restaurants = mutableListOf<RestaurantItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySortBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database.getReference("RestaurantData")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    restaurants.clear()
                    snapshot.children.forEach {
                        val value = it.getValue(RestaurantItem::class.java)
                        restaurants.add(value!!)
                    }
                    restaurantAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("SortActivity", "${error.toException()}")
                }
            })

        restaurantAdapter = RestaurantAdapter(restaurants)
        binding.restaurantRecyclerView.apply {
            adapter = restaurantAdapter
            layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        }

        binding.sortBy.setOnCheckedChangeListener { group, checkedId ->
            if(binding.sortByName.isChecked){
                restaurants.sortBy { it.name }
                restaurantAdapter.notifyDataSetChanged()
            }
            else if(binding.sortByReview.isChecked){
                restaurants.sortByDescending { it.review_count }
                restaurantAdapter.notifyDataSetChanged()
            }
            else{
                restaurants.sortWith(compareBy<RestaurantItem>({it.rate}, {it.review_count}).reversed())
                restaurantAdapter.notifyDataSetChanged()
            }
        }
    }
}