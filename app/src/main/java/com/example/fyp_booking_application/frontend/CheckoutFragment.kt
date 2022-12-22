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
    private lateinit var binding : FragmentCheckoutBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val spinnerPaymentOption = arrayOf("My Wallet")

        // Initialise
        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()

        binding = FragmentCheckoutBinding.inflate(layoutInflater)
        val userID = auth.currentUser?.uid
        val userView = (activity as UserDashboardActivity)
        userView.setTitle("Checkout")

        //Spinner for Payment Option
        val spinnerPayment = binding.checkoutPayment
        spinnerPayment.adapter = ArrayAdapter(
            userView,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            spinnerPaymentOption
        )
        spinnerPayment.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                println("error")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val spinPaymentData: String = parent?.getItemAtPosition(position).toString()
                println(spinPaymentData)

                //Retrieve Booking Data and Display Booking Details in Checkout Page
                setFragmentResultListener("toCheckoutPage") { _, bundle ->
                    val bookingID = bundle.getString("toCheckoutPage")
                    val retrieveBookingRef = fstore.collection("Bookings").document(bookingID.toString())
                    retrieveBookingRef.get().addOnCompleteListener { resultData ->
                        if (resultData != null) {
                            val dateResult = resultData.result.getString("bookingDate").toString()
                            val timeResult = resultData.result.getString("bookingTime").toString()
                            val rateResult = resultData.result.getString("bookingRate").toString()
                            val courtResult = resultData.result.getString("bookingCourt").toString()
                            var price: String = ""

                            if (rateResult == "1 Hours") {
                                price = "10.00"
                            } else {
                                price = "20.00"
                            }

                            binding.checkoutDate.setText(dateResult)
                            binding.checkoutTime.setText(timeResult)
                            binding.checkoutRate.setText(rateResult)
                            binding.checkoutCourt.setText(courtResult)
                            binding.totalAmount.setText(price)
                        } else {
                            Log.d("noexits", "No such documents.")
                        }
                    }.addOnFailureListener { exception ->
                        Log.d("noexits", "Error getting documents.", exception)
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
                            "bookingPaymentMethod" to spinPaymentData,
                        )
                        bookingId.set(bookingUpdates, SetOptions.merge()).addOnSuccessListener {
                            Toast.makeText(context, "Checkout Successfully", Toast.LENGTH_SHORT).show()
                            userView.replaceFragment(BookingCourtFragment())
                        }
                    }
                }
            }
        }
        return binding.root
    }
}
