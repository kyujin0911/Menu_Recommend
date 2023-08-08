package com.example.menurecommend

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.CompoundButton
import android.widget.Toast
import com.example.menurecommend.databinding.ActivityRandomMenuBinding
import com.example.menurecommend.databinding.ToastTinoBinding
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
    private val database = FirebaseDatabase.getInstance()
    private var restaurants = mutableListOf<Res>()
    private var checked_category = arrayListOf<String>()
    var index = 0
    var res: Res? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRandomMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.randomButton.isEnabled = false
        binding.naverMapButton.isEnabled = false
        binding.DdabongButton.isEnabled = false
        binding.clipboardButton.isEnabled = false

        database.getReference("ResData")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //restaurants.clear()
                    snapshot.children.forEach {
                        var value = it.getValue(Res::class.java)
                        value?.index = index++
                        restaurants.add(value!!)
                    }
                    binding.randomButton.isEnabled = true
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

            var findIndex = arrayListOf<Int>()
            restaurants.indices.filter { restaurants[it].category in checked_category }
                .forEach { findIndex.add(it) }

            res = if (checked_category.isNotEmpty()) {
                restaurants[findIndex.random()]
            } else {
                restaurants.random()
            }

            binding.restaurantName.text = "식당: ${res?.name}"
            binding.restaurantCategory.text = "분류: ${res?.category}"
            binding.restaurantAddress.text = "주소: ${res?.address}"
            binding.reviewCount.text = "리뷰 ${res?.review_count}"
            binding.rate.text = "${res?.rate}"
            binding.naverMapButton.isEnabled = true
            binding.DdabongButton.isEnabled = true
            binding.clipboardButton.isEnabled = true

        }
        binding.naverMapButton.setOnClickListener {
            res?.let { currentRes ->
                val intent = Intent(this, MapView::class.java)
                intent.putExtra("name", currentRes.name)
                intent.putExtra("lati", currentRes.Latitude)
                intent.putExtra("long", currentRes.Longitude)
                startActivity(intent)
            }
        }

        binding.DdabongButton.setOnClickListener {
            res?.let { currentRes ->
                val ddabong_cnt = currentRes.ddabong!! + 1
                val temp = database.getReference("ResData/${currentRes.index}")
                temp.updateChildren(mapOf("ddabong" to ddabong_cnt))
                    .addOnSuccessListener {
                        val context = this
                        tinoToast.customToastView(context, "따봉~")?.show()
                        binding.DdabongButton.isEnabled = false
                    }
                    .addOnFailureListener {
                    }
            } ?: run {
                Toast.makeText(this, "메뉴 추천 버튼부터 눌러주세요!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.homeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.clipboardButton.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("address", "${res?.address}")
            clipboard.setPrimaryClip(clip)
            //Toast.makeText(this, "식당 주소가 복사되었습니다!", Toast.LENGTH_SHORT).show()
            val context = this
            tinoToast.customToastView(context, "식당 주소가 복사되었습니다!")?.show()
        }
    }
    object tinoToast {
        fun customToastView(context: Context, message: String): Toast? {
            val inflater = LayoutInflater.from(context)
            val binding: ToastTinoBinding = ToastTinoBinding.inflate(inflater)
            binding.toastText.text = message

            return Toast(context).apply {
                setGravity(Gravity.BOTTOM or Gravity.CENTER, 0, 120.toPx())
                duration = Toast.LENGTH_SHORT
                view = binding.root
            }
        }
        private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
    }
}