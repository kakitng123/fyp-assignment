package com.example.fyp_booking_application.frontend

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener
import com.example.fyp_booking_application.UserDashboardActivity
import com.example.fyp_booking_application.databinding.FragmentCheckoutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class CheckoutFragment : Fragment() {
    private lateinit var auth: FirebaseAuth //get the shared instance of the FirebaseAuth object
    private lateinit var fstore: FirebaseFirestore //get the shared instance of the FirebaseAuth object
    private lateinit var binding: FragmentCheckoutBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val spinnerPaymentOption = arrayOf("My Wallet")

        // Initialise
        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()

        binding = FragmentCheckoutBinding.inflate(layoutInflater)
        val userID = auth.currentUser?.uid
        val userView = (activity as UserDashboardActivity)
        userView.setTitle("Checkout")

        //Spinner for Payment Option (KK - Double Checking)
        val spinnerPayment = binding.checkoutPayment
        spinnerPayment.adapter = ArrayAdapter(userView, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, spinnerPaymentOption)

        spinnerPayment.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                println("error")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val spinPaymentData: String = parent?.getItemAtPosition(position).toString()
                println(spinPaymentData)
            }
        }


        //Retrieve Booking Data and Display Booking Details in Checkout Page
        setFragmentResultListener("toCheckoutPage") { _, bundle ->
            val bookingID = bundle.getString("toCheckoutPage")
            val retrieveBookingRef = fstore.collection("Bookings").document(bookingID.toString())
            retrieveBookingRef.get().addOnCompleteListener { resultData ->
                if (resultData != null) {
                    val dateResult = resultData.result.getString("bookingDate").toString()
                    val timeResult = resultData.result.getString("bookingTime").toString()
                    val courtResult = resultData.result.getString("bookingCourt").toString()

                    binding.checkoutDate.text = dateResult
                    binding.checkoutTime.text = timeResult
                    binding.checkoutCourt.text = courtResult
                    binding.totalAmount.text = "100.0"
                } else {
                    Log.d("noexits", "No such documents.")
                }
            }.addOnFailureListener { exception ->
                Log.d("noexits", "Error getting documents.", exception)
            }

            // few comments, actually when you use SetOptions.merge(), you dont have to get dateResult/timeResult/courtResult
            // SetOptions.merge() work as if "fieldName" exists == update, if no exists then add new "fieldName"
            // only if you have to change "fieldName" then you put into new hashMapOf()
            // but right now I comment here first before I confused you

            //Save Updated Booking Data
            binding.checkoutBtn.setOnClickListener {
                val bookingDate: String = binding.checkoutDate.text.toString()
                val bookingTime: String = binding.checkoutTime.text.toString()
                val bookingCourt: String = binding.checkoutCourt.text.toString()
                val bookingPayment: Double = binding.totalAmount.text.toString().toDouble()

                // Comment first cuz not used
                // val userID = auth.currentUser?.uid

                val bookingId = fstore.collection("Bookings").document(bookingID.toString())
                val bookingUpdates = hashMapOf(
                    "bookingDate" to bookingDate, // Example this is just "get" you do ntg, then no need put here
                    "bookingTime" to bookingTime, // same here
                    "bookingCourt" to bookingCourt, // same here
                    // that means you only need to have these below 3 inside this bookingUpdates = hashMapOf()
                    // if you don't understand I'll explain to you again
                    "bookingStatus" to "Success",
                    "bookingPayment" to bookingPayment,
                    "bookingPaymentMethod" to spinnerPayment.selectedItem.toString()
                )
                bookingId.set(bookingUpdates, SetOptions.merge()).addOnSuccessListener {
                    Toast.makeText(context, "Checkout Successfully", Toast.LENGTH_SHORT).show()
                    userView.replaceFragment(BookingCourtFragment())

                    // To-do if checkout successfully then take courtName and time and set availability of timeslot


                }
            }
        }
        return binding.root
    }
}
