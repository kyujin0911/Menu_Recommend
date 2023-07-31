package com.example.menu

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.menu.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private var auth : FirebaseAuth? = null
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.hasExtra("ID")) {
            binding.accountLayout.visibility = View.GONE
            binding.logout.visibility = View.VISIBLE
            binding.idText.visibility = View.VISIBLE
            binding.idText.setText("id : ${intent.getStringExtra("ID")}")
        }

        binding.toRandomMenuBtn.setOnClickListener {
            var intent = Intent(this, RandomMenu::class.java)

            startActivity(intent)
        }
        binding.newMembershipButtonInMain.setOnClickListener {
            var intent = Intent(this, NewMembership::class.java)

            startActivity(intent)
        }
        binding.loginButtonInMain.setOnClickListener {
            var intent = Intent(this, Login::class.java)

            startActivity(intent)
        }

        binding.logout.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("로그아웃")
                .setMessage("로그아웃을 하시겠습니까?")
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, which ->
                        auth?.signOut()
                        binding.accountLayout.visibility = View.VISIBLE
                        binding.logout.visibility = View.GONE
                        binding.idText.visibility = View.INVISIBLE
                    }
                    )
                .setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, which -> null })

            builder.show()

        }

    }
}