package com.japnoor.anticorruption

import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.japnoor.chatify.Chat
import com.japnoor.chatify.ChatActivity
import com.japnoor.chatify.R
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class ChatAdapter(var context : ChatActivity,var chatList : ArrayList<Chat>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val ITEM_RECIEVE = 1
    val ITEM_SENT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {
            var view: View =
                LayoutInflater.from(context).inflate(R.layout.item_recieve_message, parent, false)
            return RecieveViewHolder(view)
        } else {
            var view: View =
                LayoutInflater.from(context).inflate(R.layout.item_send_message, parent, false)
            return SentViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.javaClass == SentViewHolder::class.java) {
            var viewHolder = holder as SentViewHolder
            holder.sentMessage.text = chatList[position].message
        } else {
            var viewHolder = holder as RecieveViewHolder
            holder.recieveMessage.text = chatList[position].message
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (FirebaseAuth.getInstance().currentUser?.uid.equals(chatList[position].senderId)) {
            return ITEM_SENT
        } else {
            return ITEM_RECIEVE
        }

    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var sentMessage = itemView.findViewById<TextView>(R.id.sentMessage)
        var sentDate = itemView.findViewById<TextView>(R.id.sentDate)
    }

    class RecieveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var recieveMessage = itemView.findViewById<TextView>(R.id.recieveMessage)
        var recieveDate = itemView.findViewById<TextView>(R.id.recieveDate)
    }
}