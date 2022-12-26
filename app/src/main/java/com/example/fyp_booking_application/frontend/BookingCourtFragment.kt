package com.example.fyp_booking_application.frontend

import android.app.DatePickerDialog
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.hashMapOf

class BookingCourtFragment : Fragment() {
    private lateinit var binding: FragmentBookingCourtBinding
    private lateinit var auth: FirebaseAuth //get the shared instance of the FirebaseAuth object
    private lateinit var fstore: FirebaseFirestore //get the shared instance of the FirebaseAuth object
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    val calendar = Calendar.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Initialise
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_booking_court, container, false)
        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        val userView = (activity as UserDashboardActivity)
        userView.setTitle("Booking Court")

        binding.btnAddBookingDate.bringToFront()

        // Variable Declaration
        val spinnerCourt = binding.courtNameBooking
        val spinnerCourtTime = binding.courtTimeBooking

        // Retrieve Available Court (KK - From Database)
        val availableCourtData = arrayListOf<String>()
        val availableCourtRef = fstore.collection("Courts") // Used to get ALL document from this collection

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
                val availableTimeSlotRef = fstore.collection("Courts").whereEqualTo("courtName", itemSelected)
                availableTimeSlotRef.get().addOnSuccessListener { documents ->
                    for (document in documents) {
                        binding.courtPriceBooking.setText(document["courtPrice"].toString())
                        val courtSlot = document["courtSlots"] as Map<*, *> // Used when firebase used mapOf<smtg>
                        courtSlot.let {
                            for ((_, value) in courtSlot) {
                                // Validation will be done later for now usable *only will show true
                                val timeslot = value as Map<*, *> // this func is used to loop through maps . so this function is only used to mapOf<mapOf<smtg>>
                                // for validation purposes
                                if (timeslot["availability"] == true){
                                    availableTimeslotData.add(timeslot["timeslot"].toString())
                                }
                            }
                        }
                        availableTimeslotData.sort()
                        Log.d("CHECK TIMESLOT DATA", "$availableTimeslotData")
                    }

                    spinnerCourtTime.adapter = ArrayAdapter(userView, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, availableTimeslotData)
                }
            }

        }

        // Added Date Picker
        binding.btnAddBookingDate.setOnClickListener {
            val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                calendar.apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                }
                binding.courtDateBooking.setText(SimpleDateFormat("dd/MM/yyyy").format(calendar.time))
            }
            DatePickerDialog(requireContext(), dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
            ).show()
        }

        //Save Updated Data function
        binding.nextBtn.setOnClickListener {
            val bookingPhone: String = binding.courtPhoneBooking.text.toString()
            val bookingDate: String = binding.courtDateBooking.text.toString()
            val bookingPayment: Double = binding.courtPriceBooking.text.toString().toDouble()

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

            val userID = auth.currentUser?.uid
            val bookingID = fstore.collection("Bookings").document()
            val booking = hashMapOf(
                "bookingID" to bookingID.id,
                "bookingCourt" to spinnerCourt.selectedItem.toString(),
                "bookingPhone" to bookingPhone,
                "bookingDate" to bookingDate,
                "bookingTime" to spinnerCourtTime.selectedItem.toString(),
                "bookingStatus" to "Pending",
                "bookingPayment" to bookingPayment,
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