package com.japnoor.chatify

import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.japnoor.chatify.databinding.ActivityAllChatsBinding


class AllChatsActivity : AppCompatActivity() {
    lateinit var binding: ActivityAllChatsBinding
    lateinit var contactHashMap: HashMap<String, String>
    lateinit var contactPhoneNumberList: ArrayList<String>
    lateinit var fetchContactPhoneNumberList: ArrayList<String>
    lateinit var contactNameList: ArrayList<String>
    lateinit var allChatAdapter: AllChatAdapter
    lateinit var usersList: ArrayList<Users>
    lateinit var usersNameList: ArrayList<String>
    lateinit var firebaseDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllChatsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        contactHashMap = HashMap()
        contactNameList = ArrayList()
        contactPhoneNumberList = ArrayList()
        fetchContactPhoneNumberList = ArrayList()
        usersList = ArrayList()
        usersNameList = ArrayList()


        // Request permission to read contacts
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Get the contacts
            val cursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                null
            )
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    // Get the contact name and phone number
                    val name =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    val phone =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    contactHashMap.put(name, phone)
                }
              for(each in contactHashMap.values){
                    if(!(each.startsWith("+91"))){
                        contactPhoneNumberList.add("+91$each")
                    }
                    else {
                        contactPhoneNumberList.add(each)
                    }
                }


              for(each in contactHashMap.keys){
                  contactNameList.add(each.toString() )
                }
                println(contactNameList)
                println(contactPhoneNumberList)
                println(contactNameList.size)
                println(contactPhoneNumberList.size)
                firebaseDatabase.reference.child("users")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            usersList.clear()
                            for (each in snapshot.children) {
                                var user = each.getValue(Users::class.java)
                                println(user?.phoneNo?.replace(" ",""))
                                contactPhoneNumberList.forEachIndexed{index,value->
                                if ( user !=null  && user.phoneNo.replace(" ","").equals(value)) {
                                        usersList.add(user)
                                        usersNameList.add(contactNameList.get(index))
                                        println(user)
                                        println("jjjj" + usersList)
                                    }
                                }
                                println("jjjj" + usersList)
                                allChatAdapter= AllChatAdapter(this@AllChatsActivity,usersList,usersNameList)
                                binding.recyclerView.layoutManager=LinearLayoutManager(this@AllChatsActivity)
                                binding.recyclerView.adapter=allChatAdapter
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })


                cursor.close()
            }

            // Do something with the contacts list
        } else {
            // Request permission to read contacts
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_CONTACTS),
                1
            )
        }


    }


}