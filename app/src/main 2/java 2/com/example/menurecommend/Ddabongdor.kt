package com.example.menurecommend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.menurecommend.databinding.ActivityDdabongdorBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

data class Res2(
    var name: String? = null,
    var category: String? = null,
    var address: String? = null,
    var Latitude: Double? = null,
    var Longitude: Double? = null,
    var rate: Double? = null,
    var review_count: Int? = null,
    var ddabong: Int? = null
)


class Ddabongdor : AppCompatActivity() {
    private lateinit var binding: ActivityDdabongdorBinding
    val db = FirebaseDatabase.getInstance()
    var index = 0
    var res_arr = mutableListOf<Res2>()
    val TAG = "Ddabong"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDdabongdorBinding.inflate(layoutInflater)
        setContentView(binding.root)


        while (index < 287) {
            val myRef = db.getReference("RestaurantData/${index++}")
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val value = dataSnapshot.getValue(Res2::class.java)
                    value?.let {
                        res_arr.add(it)
                        if (res_arr.size == 287){
                            Log.d(TAG, "Data Load Finished")
                            Log.d(TAG, "${res_arr[0]},${res_arr[286]}")
                            res_arr.sortWith(compareBy<Res2>{ it.ddabong}.thenBy { it.name})
                            binding.firstDdabong.setText("${res_arr[0]?.name}\n${res_arr[0]?.category}\n${res_arr[0]?.address}\n")
                            binding.secondDdabong.setText("${res_arr[1]?.name}\n${res_arr[1]?.category}\n${res_arr[1]?.address}\n")
                            binding.thirdDdabong.setText("${res_arr[2]?.name}\n${res_arr[2]?.category}\n${res_arr[2]?.address}\n")
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException())
                }
            })
        }








    }
}
