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

        //Spinner for Payment Option
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

//        var balanceAmount = 0.00
//        fstore.collection("Wallet").get().addOnSuccessListener { documents ->
//            for (document in documents){
//                balanceAmount -= document["topUpAmount"].toString().toDouble()
//            }
//            binding.balanceAmt.text = balanceAmount.toString()
//
//        }.addOnFailureListener { exception ->
//            Log.d("noexits", "Error getting documents.", exception)
//        }

        //Retrieve Booking Data and Display Booking Details in Checkout Page
        setFragmentResultListener("toCheckoutPage") { _, bundle ->
            //Bundle more than 1 data (success)
            val bookingID = bundle.getString("toCheckoutPage")
            val retrieveBookingRef = fstore.collection("Bookings").document(bookingID.toString())
            retrieveBookingRef.get().addOnCompleteListener { resultData ->
                if (resultData != null) {
                    val dateResult = resultData.result.getString("bookingDate").toString()
                    val timeResult = resultData.result.getString("bookingTime").toString()
                    val courtResult = resultData.result.getString("bookingCourt").toString()
                    val amountResult = resultData.result.getDouble("bookingPayment").toString()

                    binding.checkoutDate.text = dateResult
                    binding.checkoutTime.text = timeResult
                    binding.checkoutCourt.text = courtResult
                    binding.totalAmount.text = amountResult
                } else {
                    Log.d("noexits", "No such documents.")
                }
            }.addOnFailureListener { exception ->
                Log.d("noexits", "Error getting documents.", exception)
            }

            // Voucher Test
            binding.btnApplyVoucher.setOnClickListener {
                val voucherRef = fstore.collection("Vouchers").whereEqualTo("voucherCode", binding.voucherCodeField.text.toString())
                voucherRef.get().addOnSuccessListener { documents ->
                    for (document in documents){
                        val voucherDiscount: Double = document["voucherDiscount"].toString().toDouble()
                        val calculation = binding.totalAmount.text.toString().toDouble() * (1-voucherDiscount)
                        binding.totalAmount.text = calculation.toString()
                    }
                }
            }

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
                    "bookingStatus" to "Success",
                    "bookingPayment" to bookingPayment,
                    "bookingPaymentMethod" to spinnerPayment.selectedItem.toString()
                )
                bookingId.set(bookingUpdates, SetOptions.merge())
                // To-do if checkout successfully then take courtName and time and set availability of timeslot
                val courtRef = fstore.collection("Courts").whereEqualTo("courtName", bookingCourt)
                courtRef.get().addOnSuccessListener(){ documents ->
                    for (document in documents){
                        val courtID = document["courtID"]
                        val courtSlot = document["courtSlots"] as Map<*, *> // Used when firebase used mapOf<smtg>
                        courtSlot.let {
                            for ((key, value) in courtSlot) {
                                val timeslot = value as Map<*, *>
                                if (timeslot["timeslot"] == bookingTime){
                                    val courtUpdateRef = fstore.collection("Courts").document(courtID.toString())
                                    val updateAvailability = hashMapOf("courtSlots" to hashMapOf(
                                        "$key" to hashMapOf("availability" to false ))
                                    )
                                    courtUpdateRef.set(updateAvailability, SetOptions.merge()).addOnSuccessListener(){
                                        Toast.makeText(context, "Checkout Successfully", Toast.LENGTH_SHORT).show()
                                        userView.replaceFragment(BookingCourtFragment())
                                    }
                                }
                            }
                        }
                    }
                }.addOnFailureListener(){
                    // for now do ntg, or just logErrpr
                }
            }
        }
        return binding.root
    }
}
