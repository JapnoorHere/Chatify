package com.japnoor.chatify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.japnoor.anticorruption.ChatAdapter
import com.japnoor.chatify.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {
    lateinit var binding : ActivityChatBinding
    lateinit var chatRef : DatabaseReference
    var senderId : String=""
    var receiverId : String=""
    var senderRoom : String=""
    var receiverRoom : String=""
    lateinit var auth : FirebaseAuth
    lateinit var chatList : ArrayList<Chat>
    lateinit var chatAdapter : ChatAdapter
    lateinit var profile : String
    lateinit var name : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chatRef=FirebaseDatabase.getInstance().reference.child("chat")
        auth=FirebaseAuth.getInstance()
        chatList= ArrayList()
        receiverId= intent.getStringExtra("receiverId") as String
        profile = intent.getStringExtra("profile") as String
        name = intent.getStringExtra("name") as String
        senderId=auth.currentUser?.uid.toString()
        senderRoom=senderId + receiverId
        receiverRoom=receiverId + senderId
        chatAdapter= ChatAdapter(this@ChatActivity,chatList)


        Glide.with(this@ChatActivity).load(profile).into(binding.profile)
        binding.titleName.text=name

        binding.etMessage.doOnTextChanged { text, start, before, count ->
            if(binding.etMessage.text.toString().isEmpty()){
                binding.sendButton.visibility=View.GONE
            }
            else{
                binding.sendButton.visibility=View.VISIBLE
            }
        }

        FirebaseDatabase.getInstance().reference.child("chat").child(senderRoom).child("messages")
            .addValueEventListener(object  : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    chatList.clear()
                    for(each in snapshot.children){
                        var msg=each.getValue(Chat::class.java)
                        if(msg!=null)
                            chatList.add(msg)
                    }
                    chatAdapter.notifyDataSetChanged()
                    chatAdapter= ChatAdapter(this@ChatActivity,chatList)
                    binding.recyclerView.layoutManager= LinearLayoutManager(this@ChatActivity)
                    binding.recyclerView.adapter=chatAdapter
                    binding.recyclerView.scrollToPosition(chatList.size - 1)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


        binding.sendButton.setOnClickListener {
            val message = binding.etMessage.text.toString().trim()
            if(message.isNotEmpty()){
                val senderMessageId= chatRef.push().key.toString()
                val chat = Chat(message.trim(),senderMessageId,senderId)
                chatRef.child(senderRoom).child("messages").child(senderMessageId).setValue(chat).addOnCompleteListener {
                    val chat1 = Chat(message.trim(),senderMessageId + "receive",senderId)
                    chatRef.child(receiverRoom).child("messages").child(senderMessageId + "receive").setValue(chat1).addOnCompleteListener {

                    }

                }
            }
            binding.etMessage.text.clear()
        }


    }
}