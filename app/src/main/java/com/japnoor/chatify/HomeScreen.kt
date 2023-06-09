package com.japnoor.chatify

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.japnoor.chatify.databinding.ActivityHomeScreenBinding


class HomeScreen : AppCompatActivity() {
    lateinit var binding: ActivityHomeScreenBinding
    lateinit var usersList: ArrayList<Any>
    lateinit var userRef: DatabaseReference
    lateinit var homeScreenAdapter: HomeScreenAdapter
    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth=FirebaseAuth.getInstance()
        userRef = FirebaseDatabase.getInstance().reference.child("chat")
        homeScreenAdapter = HomeScreenAdapter()
        usersList = ArrayList()

        binding.allChatActivityBtn.setOnClickListener {
            var intent = Intent(this@HomeScreen, AllChatsActivity::class.java)
            startActivity(intent)
        }

        userRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(each in snapshot.children){
                    var users=each.getValue(Any::class.java)
                    println(each.getValue(Any::class.java))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}