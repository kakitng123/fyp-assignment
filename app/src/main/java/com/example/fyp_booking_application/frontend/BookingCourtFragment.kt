package com.example.fyp_booking_application.frontend

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.example.fyp_booking_application.ForgotPasswordActivity
import com.example.fyp_booking_application.UserDashboardActivity
import com.example.fyp_booking_application.backend.Adapters.CourtAdminAdapter
import com.example.fyp_booking_application.backend.Adapters.TimeslotAdminAdapter
import com.example.fyp_booking_application.backend.CourtData
import com.example.fyp_booking_application.backend.CourtTimeslots
import com.example.fyp_booking_application.databinding.FragmentBookingCourtBinding
import com.example.fyp_booking_application.frontend.data.BookingData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlin.collections.ArrayList
import kotlin.collections.List
import kotlin.collections.hashMapOf


class BookingCourtFragment : Fragment() {
    private lateinit var auth: FirebaseAuth //get the shared instance of the FirebaseAuth object
    private lateinit var fstore: FirebaseFirestore //get the shared instance of the FirebaseAuth object
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val spinnerCourtData = arrayOf("A1", "A2", "A3")
        val spinnerRateData = arrayOf("1 Hours", "2 Hours")

        // Initialise
        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        val binding = FragmentBookingCourtBinding.inflate(layoutInflater)
        val userView = (activity as UserDashboardActivity)
        userView.setTitle("Booking Court")

        val spinnerCourtName = binding.courtNameBooking
        val spinnerCourtRate = binding.courtRateBooking
        //Spinner for Court Name
        spinnerCourtName.adapter = ArrayAdapter(
            userView,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            spinnerCourtData
        )
        spinnerCourtName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                println("error")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val spinBookingCourtData: String = parent?.getItemAtPosition(position).toString()
                println(spinBookingCourtData)

                //Spinner for Hours Rate
                spinnerCourtRate.adapter = ArrayAdapter(
                    userView,
                    androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                    spinnerRateData
                )
                spinnerCourtRate.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            println("error")
                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            val spinBookingRateData: String =
                                parent?.getItemAtPosition(position).toString()
                            println(spinBookingRateData)

                            //Save Updated Data function
                            binding.nextBtn.setOnClickListener {
                                var bookingPhone: String = binding.courtPhoneBooking.text.toString()
                                var bookingDate: String = binding.courtDateBooking.text.toString()
                                var bookingTime: String = binding.courtTimeBooking.text.toString()

                                if (bookingPhone.isEmpty()) {
                                    binding.courtPhoneBooking.setError("Booking Phone Number is required!")
                                    binding.courtPhoneBooking.requestFocus()
                                    return@setOnClickListener
                                } else {
                                    if (!Patterns.PHONE.matcher(bookingPhone).matches()) {
                                        binding.courtPhoneBooking.setError("Please provide valid phone!")
                                        binding.courtPhoneBooking.requestFocus()
                                        return@setOnClickListener
                                    }
                                }

                                if (bookingDate.isEmpty()) {
                                    binding.courtDateBooking.setError("Booking Date is required!")
                                    binding.courtDateBooking.requestFocus()
                                    return@setOnClickListener
                                }

                                if (bookingTime.isEmpty()) {
                                    binding.courtTimeBooking.setError("Booking Time is required!")
                                    binding.courtTimeBooking.requestFocus()
                                    return@setOnClickListener
                                }

                                val userID = auth.currentUser?.uid
                                val bookingId = fstore.collection("Bookings").document()
                                val booking = hashMapOf(
                                    "bookingID" to bookingId.id,
                                    "bookingCourt" to spinBookingCourtData,
                                    "bookingRate" to spinBookingRateData,
                                    "bookingPhone" to bookingPhone,
                                    "bookingDate" to bookingDate,
                                    "bookingTime" to bookingTime,
                                    "bookingStatus" to "Pending",
                                    "userID" to userID
                                )
                                Log.d("testing", booking.toString())
                                bookingId.set(booking, SetOptions.merge()).addOnSuccessListener {
                                    Toast.makeText(
                                        context,
                                        "Added Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    setFragmentResult(
                                        "toCheckoutPage",
                                        bundleOf("toCheckoutPage" to bookingId.id)
                                    )
                                    userView.replaceFragment(CheckoutFragment())
                                }.addOnFailureListener() {
                                    Toast.makeText(context, "Added Failure", Toast.LENGTH_SHORT)
                                        .show()
                                }
                                Log.d("testing2", bookingId.toString())
                            }
                        }
                    }
            }
        }
        return binding.root
    }
}