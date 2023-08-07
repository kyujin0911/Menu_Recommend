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

data class Res(
    var name: String? = null,
    var category: String? = null,
    var address: String? = null,
    var Latitude: Double? = null,
    var Longitude: Double? = null,
    var rate: Double? = null,
    var review_count: Int? = null,
    var ddabong: Int = 0,
    var index: Int = 0
)

class RandomMenu : AppCompatActivity() {
    private lateinit var binding: ActivityRandomMenuBinding
    val db = FirebaseDatabase.getInstance()
    val TAG = "Random"
    var res_arr = mutableListOf<Res>()
    var checked_category = arrayListOf<String>()


    var res: Res? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRandomMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.randomButton.isEnabled = false
        binding.naverMapButton.isEnabled = false
        binding.DdabongBtn.isEnabled = false

        for (i in 0 until 284) {
            val myRef = db.getReference("ResData/${i}")
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val value = dataSnapshot.getValue(Res::class.java)
                    value?.let { res ->
                        res.index = i
                        res_arr.add(res)
                        if (res_arr.size == 284) {
                            binding.randomButton.isEnabled = true
                            binding.DdabongBtn.isEnabled = true
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

        var listener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            val category = when(buttonView.id) {
                R.id.check_korean -> "한식"
                R.id.check_japanese -> "일식"
                R.id.check_chinese -> "중식"
                R.id.check_yang -> "양식"
                R.id.check_fastFood -> "패스트푸드"
                R.id.check_desert -> "디저트"
                R.id.check_extra -> "기타"
                else -> return@OnCheckedChangeListener
            }

            if(isChecked){
                checked_category.add(category)
            }
            else{
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
                res_arr.indices.filter { res_arr[it].category in checked_category }
                    .forEach { findIndex.add(it) }

                res = res_arr[findIndex.random()]
            } else{
                res = res_arr.random()
            }
            binding.randomButton.text = "${res?.name}\n${res?.category}\n${res?.address}\n"
            binding.reviewCount.text = "리뷰 ${res?.review_count}"
            binding.rate.text = "${res?.rate}"
            binding.naverMapButton.isEnabled = true
        }

        binding.DdabongBtn.setOnClickListener {
            res?.let { currentRes ->
                val ddabong_cnt = currentRes.ddabong + 1
                val temp = db.getReference("ResData/${currentRes.index}")
                temp.updateChildren(mapOf("ddabong" to ddabong_cnt))
                    .addOnSuccessListener {
                        Toast.makeText(this, "따봉~", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "${currentRes.index},${currentRes.name}")
                        binding.DdabongBtn.isEnabled = false
                    }
                    .addOnFailureListener {
                        Log.d(TAG, "Failed to update data: ${it.message}")
                    }
            } ?: run {
                Toast.makeText(this, "메뉴 추첨 버튼부터 눌러주세요!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.naverMapButton.setOnClickListener {
            res?.let { currentRes ->
                val intent = Intent(this, MapView::class.java)
                Log.d(TAG, "${currentRes.Latitude},${currentRes.Longitude}")
                intent.putExtra("name", currentRes.name)
                intent.putExtra("lati", currentRes.Latitude)
                intent.putExtra("long", currentRes.Longitude)
                startActivity(intent)
            }
        }
        binding.gohome.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}
