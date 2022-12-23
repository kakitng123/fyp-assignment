package com.example.fyp_booking_application.frontend

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.*
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.UserDashboardActivity
import com.example.fyp_booking_application.databinding.FragmentBookingCourtBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlin.collections.hashMapOf

class BookingCourtFragment : Fragment() {
    private lateinit var auth: FirebaseAuth //get the shared instance of the FirebaseAuth object
    private lateinit var fstore: FirebaseFirestore //get the shared instance of the FirebaseAuth object
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference

    // Ka Kit's Binding
    private lateinit var binding: FragmentBookingCourtBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialise
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_booking_court, container, false)
        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        val userView = (activity as UserDashboardActivity)
        userView.setTitle("Booking Court")

        // Variable Declaration
        val spinnerCourt = binding.courtNameBooking
        val spinnerCourtTime = binding.courtTimeBooking

        // Retrieve Available Court (KK - From Database)
        val availableCourtData = arrayListOf<String>()
        val availableCourtRef = fstore.collection("court_testing1") // Used to get ALL document from this collection

        availableCourtRef.get().addOnSuccessListener { documents -> // <- more than 1 data
            if (documents != null) {
                for (document in documents) { // Used to LOOP THROUGH documents, cuz documents is more than 1
                    availableCourtData.add(document["courtName"].toString()) // You can take specific field from each document like this, best practice
                    Log.d("CHECK COURT DATA", "${document["courtName"].toString()} \n") // this log i put here for you
                }
                availableCourtData.sort()
                val spinnerAdapter = ArrayAdapter(userView,
                    androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, availableCourtData)
                spinnerCourt.adapter = spinnerAdapter
                spinnerCourt.setSelection(0)
            }
        }.addOnFailureListener { e -> Log.e("No Available Court", "ERROR FETCHING COURT", e) }

        spinnerCourt.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                println("error")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val itemSelected = parent?.getItemAtPosition(position).toString()
                val availableTimeslotData = arrayListOf<String>()
                val availableTimeSlotRef = fstore.collection("court_testing1").whereEqualTo("courtName", itemSelected)
                availableTimeSlotRef.get().addOnSuccessListener { documents ->
                    for (document in documents) {
                        val courtSlot = document["courtSlots"] as Map<*, *> // Used when firebase used mapOf<smtg>
                        courtSlot.let {
                            for ((_, value) in courtSlot) {
                                // Validation will be done later for now usable *only will show true


                                val timeslot = value as Map<*, *> // this func is used to loop through maps . so this function is only used to mapOf<mapOf<smtg>>,
                                availableTimeslotData.add(timeslot["timeslot"].toString()) // just like my courtslot
                            }
                        }
                        availableTimeslotData.sort()
                        Log.d("CHECK TIMESLOT DATA", "$availableTimeslotData")
                    }

                    spinnerCourtTime.adapter = ArrayAdapter(userView, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, availableTimeslotData)
                }
            }

        }

        //Save Updated Data function
        binding.nextBtn.setOnClickListener {
            val bookingPhone: String = binding.courtPhoneBooking.text.toString()
            val bookingDate: String = binding.courtDateBooking.text.toString()

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

            // this can use DatePicker, Ill do this all the way later
            // format for all dates in our system will be dd/MM/yyyy
            if (bookingDate.isEmpty()) {
                binding.courtDateBooking.setError("Booking Date is required!")
                binding.courtDateBooking.requestFocus()
                return@setOnClickListener
            }

            val userID = auth.currentUser?.uid
            val bookingID = fstore.collection("Bookings").document()
            val booking = hashMapOf(
                "bookingID" to bookingID.id,
                "bookingCourt" to spinnerCourt.selectedItem.toString(),
                "bookingPhone" to bookingPhone,
                "bookingDate" to bookingDate,
                "bookingTime" to spinnerCourtTime.selectedItem.toString(),
                "bookingStatus" to "Pending",
                "userID" to userID
            )
            bookingID.set(booking, SetOptions.merge()).addOnSuccessListener {
                Toast.makeText(context, "Booking Successfully", Toast.LENGTH_SHORT).show()
                setFragmentResult("toCheckoutPage", bundleOf("toCheckoutPage" to bookingID.id))
                userView.replaceFragment(CheckoutFragment())
            }.addOnFailureListener {
                Toast.makeText(context, "Added Failure", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }
}
// Below codes are redundant codes, I sekali comment over it
// We do not need phoneNo field nor bookingRate, user alr has phone reg, and Ill add courtPrice after that
// My Plan is to have courtType A (all 1 hour interval) and courtType B (2 hour interval) both will have fixed price
// So you dont have to choose here, it depend on A1 OR B2 to determine price

//// Ill put all the unneeded codes here for now
//        val spinnerRateData = arrayOf("1 Hours", "2 Hours")
//        val binding = FragmentBookingCourtBinding.inflate(layoutInflater)
//        // Until Here
//        val spinnerCourtRate = binding.courtRateBooking // Not Needed, I explained below
//
//
//
//
//
//        spinnerCourtRate.onItemSelectedListener =
//            object : AdapterView.OnItemSelectedListener {
//                override fun onNothingSelected(parent: AdapterView<*>?) {
//                    println("error")
//                }
//
//                override fun onItemSelected(
//                    parent: AdapterView<*>?,
//                    view: View?,
//                    position: Int,
//                    id: Long
//                ) {
//                    val spinBookingRateData: String = parent?.getItemAtPosition(position).toString()
//                    println(spinBookingRateData)
//
//                    //Spinner for available court
//                    spinnerCourt.onItemSelectedListener =
//                        object : AdapterView.OnItemSelectedListener {
//                            override fun onNothingSelected(parent: AdapterView<*>?) {
//                                println("error")
//                            }
//
//                            override fun onItemSelected(
//                                parent: AdapterView<*>?,
//                                view: View?,
//                                position: Int,
//                                id: Long
//                            ) {
//                                val spinAvailableCourtData: String =
//                                    parent?.getItemAtPosition(position).toString()
//                                println(spinAvailableCourtData)
//
//                                //Spinner for available timeslot
//                                spinnerCourtTime.onItemSelectedListener =
//                                    object : AdapterView.OnItemSelectedListener {
//                                        override fun onNothingSelected(parent: AdapterView<*>?) {
//                                            println("error")
//                                        }
//
//                                        override fun onItemSelected(
//                                            parent: AdapterView<*>?,
//                                            view: View?,
//                                            position: Int,
//                                            id: Long
//                                        ) {
//                                            val spinAvailableTimeData: String =
//                                                parent?.getItemAtPosition(position).toString()
//                                            println(spinAvailableTimeData)
//
//                                        }
//                                    }
//                            }
//                        }
//                }
//            }
//    }
//}
//

