package com.example.menurecommend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CompoundButton
import android.widget.Toast
import com.example.menurecommend.databinding.ActivityRandomMenuBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMapSdk

data class Res(
    var name: String? = null,
    var category: String? = null,
    var address: String? = null,
    var Latitude: Double? = null,
    var Longitude: Double? = null,
    var rate: Double? = null,
    var review_count: Int? = null
)

class RandomMenu : AppCompatActivity() {
    private lateinit var binding: ActivityRandomMenuBinding
    val db = FirebaseDatabase.getInstance()
    val TAG = "Random"
    var res_arr = mutableListOf<Res>()
    var checked_category = arrayListOf<String>()
    var index = 0
    var randomIndex = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRandomMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnRandom.isEnabled = false
        binding.goToNavermapBtn.isEnabled = false
        while (index < 220) {
            val myRef = db.getReference("RestaurantData/${index++}")
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val value = dataSnapshot.getValue(Res::class.java)
                    value?.let {
                        res_arr.add(it)
                        if (res_arr.size == 220){
                            binding.btnRandom.isEnabled = true
                            Log.d(TAG, "Data Load Finished")
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException())
                }
            })
        }
        var listener = CompoundButton.OnCheckedChangeListener { buttonView, iscChecked ->
            val category = when(buttonView.id) {
                R.id.check_han -> "한식"
                R.id.check_il -> "일식"
                R.id.check_jung -> "중식"
                R.id.check_yang -> "양식"
                R.id.check_fast -> "패스트푸드"
                R.id.check_de -> "디저트"
                R.id.check_gi -> "기타"
                else -> return@OnCheckedChangeListener
            }
            if(iscChecked){
                checked_category.add(category)
            }
            else{
                checked_category.remove(category)
            }
        }
        binding.checkHan.setOnCheckedChangeListener(listener)
        binding.checkIl.setOnCheckedChangeListener(listener)
        binding.checkJung.setOnCheckedChangeListener(listener)
        binding.checkYang.setOnCheckedChangeListener(listener)
        binding.checkFast.setOnCheckedChangeListener(listener)
        binding.checkDe.setOnCheckedChangeListener(listener)
        binding.checkGi.setOnCheckedChangeListener(listener)


        binding.btnRandom.setOnClickListener {
            if (checked_category.isNotEmpty()
            ) {
                var findIndex = arrayListOf<Int>()
                res_arr.indices.filter { res_arr[it].category in checked_category }
                    .forEach { findIndex.add(it) }

                randomIndex = findIndex.random()
                val res = res_arr[randomIndex]
                binding.menu.setText("${res?.name}\n${res?.category}\n${res?.address}\n${res?.rate}\n${res?.review_count}")
                binding.goToNavermapBtn.isEnabled = true
            } else{
                Toast.makeText(this, "체크박스를 선택해주세요.", Toast.LENGTH_SHORT).show()
            }

        }

        binding.goToNavermapBtn.setOnClickListener {
            var intent = Intent(this, MapView::class.java)
            Log.d(TAG, "${res_arr[randomIndex].Latitude},${res_arr[randomIndex].Longitude}")
            intent.putExtra("name", res_arr[randomIndex].name)
            intent.putExtra("lati", res_arr[randomIndex].Latitude)
            intent.putExtra("long", res_arr[randomIndex].Longitude)
            startActivity(intent)
        }


    }
}