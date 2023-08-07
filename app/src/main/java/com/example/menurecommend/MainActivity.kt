package com.example.menurecommend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.menurecommend.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var iintent: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.menuListButton.setOnClickListener {
            iintent = Intent(this, SortActivity::class.java)
            startActivity(iintent)
        }
        binding.randomButton.setOnClickListener {
            iintent = Intent(this, RandomMenu::class.java)
            startActivity(iintent)
        }
        binding.DdabongButton.setOnClickListener {
            iintent = Intent(this, Ddabongdor::class.java)
            startActivity(iintent)
        }
    }
}