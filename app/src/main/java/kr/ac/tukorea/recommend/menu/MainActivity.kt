package kr.ac.tukorea.recommend.menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.ac.tukorea.recommend.menu.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var iintent: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.menuListButton.setOnClickListener {
            iintent = Intent(this, RestaurantListActivity::class.java)
            startActivity(iintent)
        }
        binding.randomButton.setOnClickListener {
            iintent = Intent(this, RestaurantRandomActivity::class.java)
            startActivity(iintent)
        }
        binding.DdabongButton.setOnClickListener {
            iintent = Intent(this, DdabongdorActivity::class.java)
            startActivity(iintent)
        }
    }
}