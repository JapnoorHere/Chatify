package com.japnoor.chatify

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.japnoor.chatify.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.navControllerRegister)

    }

    override fun onStart() {
        super.onStart()
        var user = FirebaseAuth.getInstance().currentUser?.uid.toString()
        if (user != null) {
            var intent = Intent(
                this@RegisterActivity,
                HomeScreen::class.java
            )
            startActivity(intent)
        }
    }

}