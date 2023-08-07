package kr.ac.tukorea.recommend.menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CompoundButton
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.naver.maps.map.MapView
import kr.ac.tukorea.recommend.menu.databinding.ActivityRandomMenuBinding
import kr.ac.tukorea.recommend.menu.util.RestaurantInfo

class RandomMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRandomMenuBinding
    private val database = FirebaseDatabase.getInstance()
    private var restaurants = mutableListOf<RestaurantInfo>()
    private var checked_category = arrayListOf<String>()
    var index = 0
    var res: RestaurantInfo? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRandomMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database.getReference("ResData")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //restaurants.clear()
                    snapshot.children.forEach {
                        var value = it.getValue(RestaurantInfo::class.java)
                        value?.index = index++
                        restaurants.add(value!!)
                    }
                    Log.d("loadData", "$restaurants")
                }


                override fun onCancelled(error: DatabaseError) {
                    Log.d("SortActivity", "${error.toException()}")
                }
            })

        var listener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            val category = when (buttonView.id) {
                R.id.check_korean -> "한식"
                R.id.check_japanese -> "일식"
                R.id.check_chinese -> "중식"
                R.id.check_yang -> "양식"
                R.id.check_fastFood -> "패스트푸드"
                R.id.check_desert -> "디저트"
                R.id.check_extra -> "기타"
                else -> return@OnCheckedChangeListener
            }

            if (isChecked) {
                checked_category.add(category)
            } else {
                checked_category.remove(category)
            }
        }

        binding.checkKorean.setOnCheckedChangeListener(listener)
        binding.checkJapanese.setOnCheckedChangeListener(listener)
        binding.checkChinese.setOnCheckedChangeListener(listener)
        binding.checkYang.setOnCheckedChangeListener(listener)
        binding.checkFastFood.setOnCheckedChangeListener(listener)
        binding.checkDesert.setOnCheckedChangeListener(listener)
        binding.checkExtra.setOnCheckedChangeListener(listener)

        binding.randomButton.setOnClickListener {
            if (checked_category.isNotEmpty()) {
                var findIndex = arrayListOf<Int>()
                restaurants.indices.filter { restaurants[it].category in checked_category }
                    .forEach { findIndex.add(it) }

                res = restaurants[findIndex.random()]
                binding.randomButton.text = "${res?.name}\n${res?.category}\n${res?.address}\n"
                binding.reviewCount.text = "리뷰 ${res?.review_count}"
                binding.rate.text = "${res?.rate}"
                binding.naverMapButton.isEnabled = true
            } else {
                Toast.makeText(this, "체크박스를 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
        binding.naverMapButton.setOnClickListener {
            res?.let { currentRes ->
                val intent = Intent(this, MapViewActivity::class.java)
                intent.putExtra("name", currentRes.name)
                intent.putExtra("lati", currentRes.Latitude)
                intent.putExtra("long", currentRes.Longitude)
                startActivity(intent)
            }
        }
        binding.homeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}