package com.example.menurecommend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.menurecommend.databinding.ActivityDdabongdorBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener



class Ddabongdor : AppCompatActivity() {
    private lateinit var binding: ActivityDdabongdorBinding
    val db = FirebaseDatabase.getInstance()
    var index = 0
    var res_arr = mutableListOf<Res>()
    val TAG = "Ddabong"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDdabongdorBinding.inflate(layoutInflater)
        setContentView(binding.root)


        while (index < 284) {
            val myRef = db.getReference("ResData/${index++}")
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val value = dataSnapshot.getValue(Res::class.java)
                    value?.let {
                        res_arr.add(it)
                        if (res_arr.size == 284){
                            Log.d(TAG, "Data Load Finished")
                            Log.d(TAG, "${res_arr[0]},${res_arr[283]}")
                            res_arr.sortWith(compareBy<Res>{it.ddabong}.thenBy {it.name})
                            binding.firstDdabong.setText("${res_arr[283]?.name}\n${res_arr[283]?.category}\n")
                            binding.secondDdabong.setText("${res_arr[282]?.name}\n${res_arr[282]?.category}\n")
                            binding.thirdDdabong.setText("${res_arr[281]?.name}\n${res_arr[281]?.category}\n")
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
