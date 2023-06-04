package com.japnoor.chatify

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.japnoor.chatify.databinding.ItemChatBinding

class AllChatAdapter(var allChatsActivity: AllChatsActivity, var usersList: ArrayList<Users>,var usersNameList : ArrayList<String>) : RecyclerView.Adapter<AllChatAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemChatBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.name.setText(usersNameList[position])
        holder.binding.phoneNumber.setText(usersList[position].phoneNo)
        holder.itemView.setOnClickListener{
            var intent= Intent(allChatsActivity,ChatActivity::class.java)
            allChatsActivity.startActivity(intent)
        }
        Glide.with(allChatsActivity).load(usersList[position].profile).into(holder.binding.profile)
    }

    override fun getItemCount(): Int {
        return usersList.size
    }
}