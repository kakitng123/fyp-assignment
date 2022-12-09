package com.example.fyp_booking_application.frontend

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.UserDashboardActivity
import com.example.fyp_booking_application.databinding.FragmentCheckoutBinding
import com.example.fyp_booking_application.databinding.FragmentUserProfileBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.File
import java.util.*

class CheckoutFragment : Fragment() {
    private lateinit var auth: FirebaseAuth //get the shared instance of the FirebaseAuth object
    private lateinit var fstore: FirebaseFirestore //get the shared instance of the FirebaseAuth object
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private lateinit var binding : FragmentCheckoutBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialise
//        auth = FirebaseAuth.getInstance()
//        fstore = FirebaseFirestore.getInstance()
//        storage = FirebaseStorage.getInstance()
//        storageRef = storage.reference
//
//        binding = FragmentCheckoutBinding.inflate(layoutInflater)
//        val userID = auth.currentUser?.uid
//        val userView = (activity as UserDashboardActivity)
//
//        binding.btnCheckout.setOnClickListener {
//
//        }
//
//        //Display user profile data
//        val retrieveBookingData = fstore.collection("Bookings").document(userID.toString())
//        var emailResult = ""
//        var nameResult = ""
//        var phoneResult = ""
//        var passResult = ""
//        var confirmPassResult = ""
//
//        retrieveBookingData.get()
//            .addOnCompleteListener { resultData ->
//                if (resultData != null) {
//                    //  Log.d("exits", "Result Document User Data: ${resultData.data}")
//                    Toast.makeText(activity, "masuk Successfully", Toast.LENGTH_SHORT)
//                        .show()
//                    emailResult= resultData.result.getString("email").toString()
//                    nameResult = resultData.result.getString("username").toString()
//                    phoneResult = resultData.result.getString("phone").toString()
//                    passResult = resultData.result.getString("password").toString()
//                    confirmPassResult = resultData.result.getString("confirmPassword").toString()
//
//                    binding.tvDate.setText(emailResult)
//                    binding.tvTime.setText(nameResult)
//                    binding.tvRate.setText(phoneResult)
//                    binding.tvCourt.setText(passResult)
//                    binding.tvStatus.setText(confirmPassResult)
//                    binding.tvPrice.setText(confirmPassResult)
//                } else {
//                    Log.d("noexits", "No such documents.")
//                }
//                Toast.makeText(activity, "Retrieve Successfully", Toast.LENGTH_SHORT)
//                    .show()
//            }.addOnFailureListener { exception ->
//                Log.d("noexits", "Error getting documents.", exception)
//                Toast.makeText(activity, "Retrieve Failure", Toast.LENGTH_SHORT)
//                    .show()
//            }
//
//        //Save Updated Data function
//        binding.saveBtn.setOnClickListener {
//            var email: String = binding.emailProfile.text.toString()
//            var username: String = binding.nameProfile.text.toString()
//            var phone: String = binding.phoneProfile.text.toString()
//            var password: String = binding.passProfile.text.toString()
//            var confirmPassword:String = binding.confirmPassProfile.text.toString()
//
//            ///Validation for all input field and match the pattern
//            if (username.isEmpty()) {
//                binding.nameProfile.setError("Username is required!")
//                binding.nameProfile.requestFocus()
//                return@setOnClickListener
//            }
//            if (phone.isEmpty()) {
//                binding.phoneProfile.setError("Phone Number is required!")
//                binding.phoneProfile.requestFocus()
//                return@setOnClickListener
//            } else {
//                if (!Patterns.PHONE.matcher(phone).matches()) {
//                    binding.phoneProfile.setError("Please provide valid phone!")
//                    binding.phoneProfile.requestFocus()
//                    return@setOnClickListener
//                }
//            }
//
//            if(password.isEmpty()){
//                binding.passProfile.setError("Password is required!")
//                binding.passProfile.requestFocus()
//                return@setOnClickListener
//            }else{
//                if(password.length < 6) {
//                    binding.passProfile.setError("Min password length should be 6 characters!")
//                    binding.passProfile.requestFocus()
//                    return@setOnClickListener
//                }
//            }
//
//            if(confirmPassword.isEmpty()){
//                binding.confirmPassProfile.setError("Password is required!")
//                binding.confirmPassProfile.requestFocus()
//                return@setOnClickListener
//            }else{
//                if(confirmPassword.length < 6) {
//                    binding.confirmPassProfile.setError("Min password length should be 6 characters!")
//                    binding.confirmPassProfile.requestFocus()
//                    return@setOnClickListener
//                }
//            }
//
//            val userID = auth.currentUser?.uid
//
//            val profileUpdates = hashMapOf(
//                "userID" to userID,
//                "email" to email,
//                "username" to username,
//                "phone" to phone,
//                "password" to password,
//                "confirmPassword" to confirmPassword,
//            )
//
//            //Update Data
//            val updateData = fstore.collection("Users").document(userID.toString())
//            updateData.set(profileUpdates)
//                .addOnSuccessListener { editData ->
//                    Log.d("exits", "User profile updated.")
//                    Toast.makeText(activity, "Save Successfully", Toast.LENGTH_SHORT)
//                        .show()
//                }
//                .addOnFailureListener { e ->
//                    Log.w(ContentValues.TAG, "Error adding document", e)
//                    Toast.makeText(activity, "Save Failure", Toast.LENGTH_SHORT).show()
//                }
//        }
      return binding.root
    }
}
