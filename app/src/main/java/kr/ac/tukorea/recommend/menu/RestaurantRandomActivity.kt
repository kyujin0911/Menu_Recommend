package kr.ac.tukorea.recommend.menu

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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kr.ac.tukorea.recommend.menu.databinding.ActivityRestaurantRandomBinding
import kr.ac.tukorea.recommend.menu.databinding.ToastTinoBinding
import kr.ac.tukorea.recommend.menu.util.RestaurantInfo
import kr.ac.tukorea.recommend.menu.util.TinoToast

class RestaurantRandomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRestaurantRandomBinding
    private val database = FirebaseDatabase.getInstance()
    private var restaurants = mutableListOf<RestaurantInfo>()
    private var checkedCategory = arrayListOf<String>()
    private var index = 0
    var restaurant: RestaurantInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantRandomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.randomButton.isEnabled = false
        binding.naverMapButton.isEnabled = false
        binding.DdabongButton.isEnabled = false
        binding.clipboardButton.isEnabled = false

        database.getReference("ResData")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        var value = it.getValue(RestaurantInfo::class.java)
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
                checkedCategory.add(category)
            } else {
                checkedCategory.remove(category)
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
            restaurants.indices.filter { restaurants[it].category in checkedCategory }
                .forEach { findIndex.add(it) }
            restaurant = if (checkedCategory.isNotEmpty()) {
                restaurants[findIndex.random()]
            } else {
                restaurants.random()
            }

            binding.restaurantName.text = "식당: ${restaurant?.name}"
            binding.restaurantCategory.text = "분류: ${restaurant?.category}"
            binding.restaurantAddress.text = "주소: ${restaurant?.address}"
            binding.reviewCount.text = "리뷰 ${restaurant?.review_count}"
            binding.rate.text = "${restaurant?.rate}"
            binding.naverMapButton.isEnabled = true
            binding.DdabongButton.isEnabled = true
            binding.clipboardButton.isEnabled = true

        }
        binding.naverMapButton.setOnClickListener {
            restaurant?.let { currentRes ->
                val intent = Intent(this, MapViewActivity::class.java)
                intent.putExtra("name", currentRes.name)
                intent.putExtra("lati", currentRes.Latitude)
                intent.putExtra("long", currentRes.Longitude)
                startActivity(intent)
            }
        }

        binding.DdabongButton.setOnClickListener {
            restaurant?.let { currentRes ->
                val ddabongCnt = currentRes.ddabong!! + 1
                val temp = database.getReference("ResData/${currentRes.index}")
                temp.updateChildren(mapOf("ddabong" to ddabongCnt))
                    .addOnSuccessListener {
                        val context = this
                        TinoToast.customToastView(context, "따봉~")?.show()
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
            val clip = ClipData.newPlainText("address", "${restaurant?.address}")
            clipboard.setPrimaryClip(clip)
            val context = this
            TinoToast.customToastView(context, "식당 주소가 복사되었습니다!")?.show()
        }
    }
}