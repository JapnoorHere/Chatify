package com.japnoor.chatify

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.japnoor.chatify.databinding.FragmentSetProfileBinding
import java.io.ByteArrayOutputStream

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SetProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentSetProfileBinding
    lateinit var registerActivity: RegisterActivity
    lateinit var userRef: DatabaseReference
    private val PICK_IMAGE_REQUEST_CODE = 1
    private val CROP_IMAGE_REQUEST_CODE = 2
    var croppedImage: Bitmap? = null
    lateinit var stgRef: FirebaseStorage
    lateinit var auth: FirebaseAuth
    var profileCheck: Boolean = false

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
    ): View? {
        binding = FragmentSetProfileBinding.inflate(layoutInflater, container, false)
        registerActivity = activity as RegisterActivity
        stgRef = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        userRef = FirebaseDatabase.getInstance().reference.child("users")
        binding.profile.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    registerActivity,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    registerActivity,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    PICK_IMAGE_REQUEST_CODE
                )
            } else {
                pickImageFromGallery()
            }
        }

        binding.btnNext.setOnClickListener {
            if (binding.name.text.toString().isEmpty()) {
                binding.nameLayout.error = "Enter Name"
            } else if (!profileCheck) {
                userRef.child(auth.currentUser?.uid.toString()).child("name")
                    .setValue(binding.name.text.toString()).addOnCompleteListener {
                        userRef.child(auth.currentUser?.uid.toString()).child("profile")
                            .setValue("").addOnCompleteListener {
                                var intent =
                                    Intent(registerActivity, HomeScreen::class.java)
                                registerActivity.startActivity(intent)
                                registerActivity.finish()
                            }
                    }
            } else {
                val storageRef = FirebaseStorage.getInstance().reference
                val imagesRef = storageRef.child("profile").child(auth.currentUser?.uid.toString())
                val baos = ByteArrayOutputStream()
                croppedImage?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()
                val uploadTask = imagesRef.putBytes(data)
                uploadTask.addOnCompleteListener {
                    if (it.isSuccessful) {
                        imagesRef.downloadUrl.addOnSuccessListener { uri ->
                            userRef.child(auth.currentUser?.uid.toString()).child("name")
                                .setValue(binding.name.text.toString()).addOnCompleteListener {
                                    userRef.child(auth.currentUser?.uid.toString()).child("profile")
                                        .setValue(uri.toString()).addOnCompleteListener {
                                            var intent =
                                                Intent(registerActivity, HomeScreen::class.java)
                                            registerActivity.startActivity(intent)
                                            registerActivity.finish()
                                        }
                                }

                        }

                    } else {
                        Toast.makeText(
                            registerActivity,
                            it.exception?.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }


        return binding.root
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data

            // Crop image using built-in Android crop intent
            val cropIntent = Intent("com.android.camera.action.CROP")
            cropIntent.setDataAndType(selectedImageUri, "image/*")
            cropIntent.putExtra("crop", "true")
            cropIntent.putExtra("aspectX", 1)
            cropIntent.putExtra("aspectY", 1)
            cropIntent.putExtra("outputX", 300)
            cropIntent.putExtra("outputY", 300)
            cropIntent.putExtra("return-data", true)
            startActivityForResult(cropIntent, CROP_IMAGE_REQUEST_CODE)
        } else if (requestCode == CROP_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            // Display cropped image using Glide library
            croppedImage = data.extras?.getParcelable<Bitmap>("data")
            Glide.with(this).load(croppedImage).into(binding.profile)
            profileCheck = true
        }
    }

}