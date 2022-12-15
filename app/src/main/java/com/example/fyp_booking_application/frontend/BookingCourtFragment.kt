package com.example.fyp_booking_application.frontend

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
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
    private lateinit var bookingRef: DocumentReference
    private lateinit var binding : FragmentBookingCourtBinding

    private lateinit var courtList : ArrayList<CourtData>
    private lateinit var timeslotList : ArrayList<CourtTimeslots>
    private lateinit var courtManageAdapter : CourtAdminAdapter
    private lateinit var timeslotAdapter : TimeslotAdminAdapter

    private lateinit var testing: ArrayList<BookingData>



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val spinnerData = arrayOf("A1","A2","A3")
        // binding = DataBindingUtil.inflate()

        // Initialise
        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        //bookingRef = fstore.collection("court_testing2").document()
        val spinnerData1: List<String> = ArrayList()

        val binding = FragmentBookingCourtBinding.inflate(layoutInflater)
        val userView = (activity as UserDashboardActivity)

        val spinner = binding.spinner
        spinner.adapter = ArrayAdapter(userView, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, spinnerData)
        spinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                println("error")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val spinData:String = parent?.getItemAtPosition(position).toString()
                println(spinData)
                Log.d("TESTING123", "$spinData")

                //Save Updated Data function
                binding.nextBtn.setOnClickListener {
                    var bookingCourt: String = binding.courtNameBooking.text.toString()
                    var bookingRate: String = binding.courtRateBooking.text.toString()
                    var bookingPhone: String = binding.courtPhoneBooking.text.toString()
                    var bookingDate: String = binding.courtDateBooking.text.toString()
                    var bookingTime:String = binding.courtTimeBooking.text.toString()

                    ///Validation for all input field and match the pattern
                    if (bookingCourt.isEmpty()) {
                        binding.courtNameBooking.setError("Booking Court is required!")
                        binding.courtNameBooking.requestFocus()
                        return@setOnClickListener
                    }

                    if (bookingRate.isEmpty()) {
                        binding.courtRateBooking.setError("Booking Rate is required!")
                        binding.courtRateBooking.requestFocus()
                        return@setOnClickListener
                    }

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

                    if(bookingDate.isEmpty()){
                        binding.courtDateBooking.setError("Booking Date is required!")
                        binding.courtDateBooking.requestFocus()
                        return@setOnClickListener
                    }

                    if(bookingTime.isEmpty()){
                        binding.courtTimeBooking.setError("Booking Time is required!")
                        binding.courtTimeBooking.requestFocus()
                        return@setOnClickListener
                    }

                    val userID = auth.currentUser?.uid
                    val bookingId = fstore.collection("Bookings").document().id
                    val booking = hashMapOf(
                        "bookingID" to bookingId,
                        "bookingCourt" to bookingCourt,
                        "bookingRate" to bookingRate,
                        "bookingPhone" to bookingPhone,
                        "bookingDate" to bookingDate,
                        "bookingTime" to bookingTime,
                        "bookingStatus" to "Pending",
                        "spinner" to spinData,
                        "userID" to userID
                    )
                    // testing
                    // bookingId.set(booking)

                    //Add Data
                    //fstore.collection("Bookings").document(userID.toString()).collection("Bookings").document().parent.add(booking)
                    val createData1 = fstore.collection("Bookings").document(userID.toString())
                        createData1.parent.add(booking)
                        .addOnSuccessListener { createData ->
                            Log.d("exits", "Booking court record updated.")
                            Toast.makeText(activity, "Added Successfully", Toast.LENGTH_SHORT)
                                .show()
                        }
                        .addOnFailureListener { e ->
                            Log.w(ContentValues.TAG, "Error adding document", e)
                            Toast.makeText(activity, "Added Failure", Toast.LENGTH_SHORT).show()
                        }
                }
//                val data = hashMapOf("spinner" to spinData)
//                val userID = auth.currentUser?.uid
//                val postsRef = fstore.collection("Bookings")
//                val postIdRef = postsRef.document(userID.toString())
//                postIdRef.update(data as Map<String, Any>)

//                val map: MutableMap<Any, String> = HashMap()
//                map["spinner"] = "A5"
//                fstore.collection("Bookings").document(userID.toString()).set(map, SetOptions.merge())
                //    booking.put("spinner", spinData);
                // docRef = database.collection(yourCollection).document()
                // newData["id"] = docRef.id
                // docRef.set(newData, SetOptions.merge())
                //data["spinner"] = spinData


//                fstore.collection("Bookings").document(userID.toString())
//                    .update("spinner", spinData)
//                fstore.collection("Bookings").document(userID.toString())
//                    .update(mapOf(
//                        "spinner" to spinData
//                    ))
            }
        }

//        val spinnerData: List<String> = ArrayList()
//        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(ApplicationProvider.getApplicationContext(), android.R.layout.simple_spinner_item, spinnerData)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinner.adapter = adapter
//        courtRef.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot?> { task ->
//            if (task.isSuccessful) {
//                for (document in task.result) {
//                    val subject = document.getString("bookingCourt")
//                    spinnerData.add(subject)
//                }
//                adapter.notifyDataSetChanged()
//            }
//        })

        return binding.root
    }
}