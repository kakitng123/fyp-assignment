package com.example.fyp_booking_application.frontend

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.UserDashboardActivity
import com.example.fyp_booking_application.databinding.FragmentAdminProductBinding
import com.example.fyp_booking_application.databinding.FragmentCheckoutBinding
import com.example.fyp_booking_application.databinding.FragmentEnrollClassBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class EnrollClassFragment : Fragment() {
    private lateinit var auth: FirebaseAuth //get the shared instance of the FirebaseAuth object
    private lateinit var fstore: FirebaseFirestore //get the shared instance of the FirebaseAuth object
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private lateinit var binding : FragmentEnrollClassBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Initialise
        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_enroll_class, container, false)
        val userView = (activity as UserDashboardActivity)
        userView.setTitle("Enroll Class")

        //Retrieve Class Detail
        //Display Booking Details in Checkout Page
        setFragmentResultListener("toClassDetail") { _, bundle ->
            val classID = bundle.getString("toClassDetail")
            Log.d("testing1", classID.toString())
            val retrieveClassRef = fstore.collection("class_testing1").document(classID.toString())

            Log.d("testing", retrieveClassRef.toString())
            retrieveClassRef.get().addOnCompleteListener { resultData ->
                if (resultData != null) {
                   // val idResult = resultData.result.getString("classID").toString()
                    val classResult = resultData.result.getString("className").toString()
                    val descResult = resultData.result.getString("classDesc").toString()
                    val dateResult = resultData.result.getString("classDate").toString()
                    val timeResult = resultData.result.getString("classTime").toString()
                    val priceResult = resultData.result.getDouble("classPrice").toString()
                    val assignResult = resultData.result.getString("entitledCoach").toString()

                   // binding.enrollClassID.setText(idResult)
                    binding.enrollClassName.setText(classResult)
                    binding.enrollDesc.setText(descResult)
                    binding.enrollDate.setText(dateResult)
                    binding.enrollTime.setText(timeResult)
                    binding.enrollPrice.setText(priceResult)
                    binding.enrollCoach.setText(assignResult)
                } else {
                    Log.d("noexits", "No such documents.")
                }
                Toast.makeText(activity, "Retrieve Successfully", Toast.LENGTH_SHORT)
                    .show()
            }.addOnFailureListener { exception ->
                Log.d("noexits", "Error getting documents.", exception)
                Toast.makeText(activity, "Retrieve Failure", Toast.LENGTH_SHORT)
                    .show()
            }

            //Save Updated Data function
            binding.enrollBtn.setOnClickListener {
                var enrolledClassName: String = binding.enrollClassName.text.toString()
                var enrolledDate: String = binding.enrollDate.text.toString()
                var enrolledTime: String = binding.enrollTime.text.toString()
                var enrolledPrice: String = binding.enrollPrice.text.toString()

                val userID = auth.currentUser?.uid
                val enrolledId = fstore.collection("Enroll").document()
                val enroll = hashMapOf(
                    "enrollID" to enrolledId.id,
                    "enrollStatus" to "Success",
                    "enrollDate" to enrolledDate,
                    "enrollTime" to enrolledTime,
                    "enrollClassID" to classID,
                    "enrollClassName" to enrolledClassName,
                    "enrollPrice" to enrolledPrice,
                    "userID" to userID
                )
                // Add a new document with a generated ID
                Log.d("testing1", enroll.toString())

                enrolledId.set(enroll).addOnSuccessListener {

                    Toast.makeText(activity, "Save Successfully", Toast.LENGTH_SHORT)
                        .show()
                    }.addOnFailureListener {
                    Toast.makeText(activity, "Save Failure", Toast.LENGTH_SHORT)
                        .show()
                    }

            }
        }
        return binding.root
    }
}
//
//bookingId.set(booking, SetOptions.merge()).addOnSuccessListener {
//    Toast.makeText(activity, "Added Successfully", Toast.LENGTH_SHORT).show()
//    setFragmentResult(
//        "toCheckoutPage",
//        bundleOf("toCheckoutPage" to bookingId.id)
//    )
//    userView.replaceFragment(CheckoutFragment())
//}.addOnFailureListener() {
//    Toast.makeText(activity, "Added Failure", Toast.LENGTH_SHORT).show()
//}
//
//
//        bookingId.set(bookingUpdates, SetOptions.merge())





