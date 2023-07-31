package com.example.menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.menu.databinding.ActivityLoginBinding
import com.example.menu.databinding.ActivityMainBinding
import com.example.menu.databinding.ActivityNewMembershipBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase



class NewMembership : AppCompatActivity() {
    private var auth : FirebaseAuth? = null
    private lateinit var binding: ActivityNewMembershipBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewMembershipBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth



        binding.newMembershipButton.setOnClickListener {
            createAccount(binding.ID.text.toString(), binding.passwd.text.toString())
        }

    }
    private fun createAccount(email: String, password: String) {

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth?.createUserWithEmailAndPassword(email, password)
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this, "계정 생성 완료.",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish() // 가입창 종료
                    } else {
                        Toast.makeText(
                            this, "계정 생성 실패",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}