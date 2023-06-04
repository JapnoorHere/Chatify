package com.japnoor.chatify

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.japnoor.chatify.databinding.ActivityHomeScreenBinding


class HomeScreen : AppCompatActivity() {
    lateinit var binding : ActivityHomeScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.allChatActivityBtn.setOnClickListener {
            var intent= Intent(this@HomeScreen,AllChatsActivity::class.java)
            startActivity(intent)
        }
    }
}