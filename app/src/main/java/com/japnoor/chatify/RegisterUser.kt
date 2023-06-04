package com.japnoor.chatify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.japnoor.chatify.databinding.FragmentRegisterUserBinding
import java.util.concurrent.TimeUnit

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class RegisterUser : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding : FragmentRegisterUserBinding
    private lateinit var registerActivity: RegisterActivity

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
        binding = FragmentRegisterUserBinding.inflate(layoutInflater,container,false)
        registerActivity=activity as RegisterActivity
        var auth = FirebaseAuth.getInstance()

        binding.btnNext.setOnClickListener {
            if (binding.phoneNumber.text.toString().isEmpty()) {
                binding.phoneNumberLayout.error = "Enter Phone Number"
            } else if (binding.phoneNumber.text.toString().length < 10) {
                binding.phoneNumberLayout.error = "Enter valid Phone Number"
            } else if (binding.phoneNumber.text.toString().startsWith("0")) {
                binding.phoneNumberLayout.error = "Enter valid Phone Number"
            } else if (binding.phoneNumber.text.toString().startsWith("1")) {
                binding.phoneNumberLayout.error = "Enter valid Phone Number"
            } else if (binding.phoneNumber.text.toString().startsWith("2")) {
                binding.phoneNumberLayout.error = "Enter valid Phone Number"
            } else if (binding.phoneNumber.text.toString().startsWith("3")) {
                binding.phoneNumberLayout.error = "Enter valid Phone Number"
            } else if (binding.phoneNumber.text.toString().startsWith("4")) {
                binding.phoneNumberLayout.error = "Enter valid Phone Number"
            } else if (binding.phoneNumber.text.toString().startsWith("5")) {
                binding.phoneNumberLayout.error = "Enter valid Phone Number"
            } else {
                var callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        // Verification successful, authenticate the user with Firebase
                        auth.signInWithCredential(credential)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // User is signed in
                                    val user = task.result?.user
                                    // ...
                                } else {
                                    // Verification failed
                                    // ...
                                }
                            }
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        // Verification failed
                        // ...
                    }

                    override fun onCodeSent(
                        verificationId: String,
                        token: PhoneAuthProvider.ForceResendingToken
                    ) {
                        val bundle = Bundle()
                        bundle.putString("phoneNo", binding.phoneNumber.text.toString())
                        bundle.putString("verificationId",verificationId)
                        registerActivity.navController.navigate(R.id.action_registerUserFragment_to_verificationFragment, bundle)
                    }

                }

                var phoneNumber = "+91 ${binding.phoneNumber.text.toString()}" // Replace with the user's phone number
                val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(phoneNumber)       // Phone number to verify
                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                    .setActivity(registerActivity)                 // Activity (for callback binding)
                    .setCallbacks(callback)           // OnVerificationStateChangedCallbacks
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(options)


            }

        }

        return binding.root
    }

}