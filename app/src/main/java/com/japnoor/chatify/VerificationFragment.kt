package com.japnoor.chatify

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.japnoor.chatify.databinding.FragmentVerificationBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class VerificationFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentVerificationBinding
    private lateinit var registerActivity: RegisterActivity
    private var phoneNumber : String = ""
    private var verificationId : String = ""
    lateinit var auth : FirebaseAuth
    lateinit var userRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentVerificationBinding.inflate(layoutInflater,container,false)
        registerActivity=activity as RegisterActivity
        auth= FirebaseAuth.getInstance()
        userRef=FirebaseDatabase.getInstance().reference.child("users")

        arguments.let {
            phoneNumber=it?.getString("phoneNo").toString()
            verificationId=it?.getString("verificationId").toString()
        }

        binding.btnNext.setOnClickListener {
            if(binding.verificationCode.text.toString().isEmpty()){
                binding.verificationCodeLayout.error="Enter Verification Code"
            }
            else {
                val credential = PhoneAuthProvider.getCredential(verificationId!!, binding.verificationCode.text.toString())
                auth.signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            userRef.child(auth.currentUser?.uid.toString()).child("phoneNo").setValue("+91 $phoneNumber").addOnCompleteListener {
                                registerActivity.navController.navigate(R.id.action_verificationFragment_to_setProfileFragment)
                            }
                        } else {
                            Toast.makeText(registerActivity, task.exception?.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        return binding.root
    }


}