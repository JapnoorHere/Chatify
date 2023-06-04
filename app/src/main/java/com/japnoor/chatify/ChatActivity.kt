package com.japnoor.chatify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import com.japnoor.chatify.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {
    lateinit var binding : ActivityChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etMessage.doOnTextChanged { text, start, before, count ->
            if(binding.etMessage.text.toString().isEmpty()){
                binding.sendButton.visibility=View.GONE
            }
            else{
                binding.sendButton.visibility=View.VISIBLE
            }
        }
    }
}