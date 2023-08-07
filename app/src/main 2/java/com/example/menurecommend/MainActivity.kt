package com.example.menurecommend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.menurecommend.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.randomButton.setOnClickListener {
            var intent = Intent(this, RandomMenu::class.java)

            startActivity(intent)
        }

        binding.menuListButton.setOnClickListener {
            var intent = Intent(this, SortActivity::class.java)

            startActivity(intent)
        }

        binding.Ddabongdor.setOnClickListener {
            var intent = Intent(this, Ddabongdor::class.java)

            startActivity(intent)
        }


    }
}