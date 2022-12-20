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

        //Display booking data
        val retrieveBookingData = fstore.collection("Bookings").document("1KHSCnDyLFt5AhThF3XW")
        var dateResult = ""
        var timeResult = ""
        var rateResult = ""
        var courtResult = ""
        var statusResult = ""
        var priceResult = ""

        retrieveBookingData.get()
            .addOnCompleteListener { resultData ->
                if (resultData != null) {
                    Toast.makeText(activity, "masuk Successfully", Toast.LENGTH_SHORT)
                        .show()
                    dateResult= resultData.result.getString("bookingDate").toString()
                    timeResult = resultData.result.getString("bookingTime").toString()
                    rateResult = resultData.result.getString("bookingRate").toString()
                    courtResult = resultData.result.getString("bookingCourt").toString()
                    statusResult = resultData.result.getString("bookingStatus").toString()
                    priceResult = resultData.result.getString("bookingID").toString()

                    binding.checkoutDate.setText(dateResult)
                    binding.checkoutTime.setText(timeResult)
                    binding.checkoutRate.setText(rateResult)
                    binding.checkoutCourt.setText(courtResult)
                    binding.checkoutStatus.setText(statusResult)
                    binding.checkoutPrice.setText(priceResult)
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
        binding.checkoutBtn.setOnClickListener {
            var bookingDate: String = binding.checkoutDate.text.toString()
            var bookingTime: String = binding.checkoutTime.text.toString()
            var bookingRate: String = binding.checkoutRate.text.toString()
            var bookingCourt: String = binding.checkoutCourt.text.toString()
            var bookingPayment:String = binding.checkoutPrice.text.toString()

            val userID = auth.currentUser?.uid
            //val productID = bundle.getString("toProductDetails")
            //val bookingID = fstore.collection("Bookings").document().id
            val bookingId = fstore.collection("Bookings").document(userID.toString())

            val bookingUpdates = hashMapOf(
                "bookingDate" to bookingDate,
                "bookingTime" to bookingTime,
                "bookingRate" to bookingRate,
                "bookingCourt" to bookingCourt,
                "bookingStatus" to "Success",
                "bookingPayment" to bookingPayment,
            )
            bookingId.set(bookingUpdates, SetOptions.merge())

//
//            val map: MutableMap<Any, String> = HashMap()
//
//                fstore.collection("Bookings").document(userID.toString()).set(map, SetOptions.merge())
//
//            fstore.collection("Bookings").document(userID.toString())
//                    .update("spinner", spinData)
//                fstore.collection("Bookings").document(userID.toString())
//                    .update(mapOf(
//                        "spinner" to spinData
//                    ))
//
//                   val updateProduct = hashMapOf(
//                        "productImage" to "products/product${binding.tfProductDetailName.text}",
//                        "productName" to binding.tfProductDetailName.text.toString(),
//                        "productCategory" to productCategory,
//                        "productDesc" to binding.tfProductDetailDesc.text.toString(),
//                        "productPrice" to binding.tfProductDetailPrice.text.toString().toDouble(),
//                        "productQty" to binding.tfProductDetailQty.text.toString().toInt()
//                    )
//                    docRef.set(updateProduct, SetOptions.merge())
//                }
//
//            //Update Data
//            val updateData = fstore.collection("Bookings").document(userID.toString())
//            updateData.set(bookingUpdates)
//                .addOnSuccessListener { editData ->
//                    Log.d("exits", "User profile updated.")
//                    Toast.makeText(activity, "Save Successfully", Toast.LENGTH_SHORT)
//                        .show()
//                }
//                .addOnFailureListener { e ->
//                    Log.w(ContentValues.TAG, "Error adding document", e)
//                    Toast.makeText(activity, "Save Failure", Toast.LENGTH_SHORT).show()
//                }
        }
      return binding.root
    }
}
