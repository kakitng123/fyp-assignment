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
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.UserDashboardActivity
import com.example.fyp_booking_application.databinding.FragmentCheckoutBinding
import com.example.fyp_booking_application.databinding.FragmentUserProfileBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
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
        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        binding = FragmentCheckoutBinding.inflate(layoutInflater)
        val userID = auth.currentUser?.uid
        val userView = (activity as UserDashboardActivity)
        userView.setTitle("Checkout")

        //Retrieve Booking Data
        //Display Booking Details in Checkout Page
        setFragmentResultListener("toCheckoutPage"){_, bundle ->
            val bookingID = bundle.getString("toCheckoutPage")
            val retrieveBookingRef = fstore.collection("Bookings").document(bookingID.toString())
            retrieveBookingRef.get().addOnCompleteListener { resultData ->
                if (resultData != null) {
                    val dateResult= resultData.result.getString("bookingDate").toString()
                    val timeResult = resultData.result.getString("bookingTime").toString()
                    val rateResult = resultData.result.getString("bookingRate").toString()
                    val courtResult = resultData.result.getString("bookingCourt").toString()
                    val statusResult = resultData.result.getString("bookingStatus").toString()
                    var price:String = ""

                    if(rateResult == "1 Hours"){
                        price = "RM10.00"
                    }else{
                        price = "RM20.00"
                    }

                    binding.checkoutDate.setText(dateResult)
                    binding.checkoutTime.setText(timeResult)
                    binding.checkoutRate.setText(rateResult)
                    binding.checkoutCourt.setText(courtResult)
                    binding.checkoutStatus.setText(statusResult)
//                    binding.totalAmount.setText(price)
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

            //Save Updated Booking Data
            binding.checkoutBtn.setOnClickListener {
                var bookingDate: String = binding.checkoutDate.text.toString()
                var bookingTime: String = binding.checkoutTime.text.toString()
                var bookingRate: String = binding.checkoutRate.text.toString()
                var bookingCourt: String = binding.checkoutCourt.text.toString()
                var bookingPayment: String = binding.totalAmount.text.toString()

                val userID = auth.currentUser?.uid
                val bookingId = fstore.collection("Bookings").document(bookingID.toString())

                val bookingUpdates = hashMapOf(
                    "bookingDate" to bookingDate,
                    "bookingTime" to bookingTime,
                    "bookingRate" to bookingRate,
                    "bookingCourt" to bookingCourt,
                    "bookingStatus" to "Success",
                    "bookingPayment" to bookingPayment,
                )
                bookingId.set(bookingUpdates, SetOptions.merge())
            }
        }
      return binding.root
    }
}
