package com.example.fyp_booking_application.backend

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.backend.Adapters.CourtPendingAdminAdapter
import com.example.fyp_booking_application.databinding.FragmentAdminBookingPendingBinding
import com.google.firebase.firestore.*

class AdminBookingPendingFragment : Fragment(), CourtPendingAdminAdapter.OnItemClickListener {

    private lateinit var binding: FragmentAdminBookingPendingBinding
    private lateinit var courtPendingArrayList: ArrayList<CourtPendingData>
    private lateinit var databaseRef: FirebaseFirestore
    private lateinit var courtAdminAdapter: CourtPendingAdminAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Variable Declarations
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_booking_pending, container, false)

        // Example Button (Add New Booking)
        binding.btnAddCourtPending.setOnClickListener {
            val newBookingRef = databaseRef.collection("court_testing").document()
            val newCourt = hashMapOf(
                "bookingID" to newBookingRef.id,
                "courtID" to "9MfN7fPAjhk1Fol6BHT1",
                "userID" to "HKzMy78ARMLHghaN6dEk",
                "bookingTime" to "10:00 - 11:00",
                "bookingDate" to "29/11/2022",
                "status" to "Pending"
            )

            newBookingRef.set(newCourt)
                .addOnSuccessListener {
                    Log.d("ADDING NEW COURT", "Document Successfully Added!")
                    //Toast.makeText(context, "DOCUMENT ADDED", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e -> Log.w("ADDING NEW COURT", "Error Adding Document", e) }
        }

        dataInitialize()
        binding.courtRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            courtPendingArrayList = arrayListOf()
            courtAdminAdapter = CourtPendingAdminAdapter(courtPendingArrayList, this@AdminBookingPendingFragment)
            adapter = courtAdminAdapter
        }

        return binding.root
    }

    override fun onItemClick(position: Int) {
        // Private Variables
        val currentItem = courtPendingArrayList[position]
        databaseRef = FirebaseFirestore.getInstance()

        Toast.makeText(context, "BUTTON CLICKED", Toast.LENGTH_SHORT).show()

        // TESTING
        val newNotifyRef = databaseRef.collection("notification_testing1").document()
        val newNotify = hashMapOf(
            "notifyID" to newNotifyRef.id,
            "userID" to "HKzMy78ARMLHghaN6dEk",
            "notifyTitle" to "PENDING PAYMENT",
            "notifyMessage" to "YOUR PAYMENT FOR BOOKING ID:${currentItem.bookingID} IS STILL PENDING, " +
                    "PLEASE PROCEED TO XXX PAGE TO CONTINUE PAYMENT!",
            "referralCode" to "R100001"
        )
        newNotifyRef.set(newNotify).addOnSuccessListener {
            Toast.makeText(context, "ADDED", Toast.LENGTH_SHORT).show()
        }

    }

    // Parsing Data into PendingBookingRecyclerView
    private fun dataInitialize() {
        databaseRef = FirebaseFirestore.getInstance()
        databaseRef.collection("court_testing").whereEqualTo("status","Pending")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("FAILED INITIALIZATION", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            courtPendingArrayList.add(dc.document.toObject(CourtPendingData::class.java))
                        }
                    }
                    courtAdminAdapter.notifyDataSetChanged()
                }
            })
    }


}
// EXTRA CODES ATM
/*
        if(action == "Approve"){
            docRef.update("status", "APPROVED")
            Toast.makeText(context, "TESTING: $action", Toast.LENGTH_SHORT).show()

            //testRef.whereArrayContains("timeslot", "10:00 - 11:00")
            //Toast.makeText(context, "TESTING: $action", Toast.LENGTH_SHORT).show()
            val nestedData1 = hashMapOf(
                "availability" to true,
                "timeslot" to currentItem.bookingTime
            )
            val nestedData2 = hashMapOf(
                "availability" to false,
                "timeslot" to currentItem.bookingTime
            )
            courtRef.update("courtSlots", FieldValue.arrayRemove(nestedData1))
                .addOnFailureListener { e -> Log.e("TESTING123", e.message.toString()) }
            courtRef.update("courtSlots", FieldValue.arrayUnion(nestedData2))
                .addOnFailureListener { e -> Log.e("TESTING123", e.message.toString()) }

        }
        else if (action == "Decline"){
            docRef.update("status", "DECLINE")
            Toast.makeText(context, "TESTING: $action", Toast.LENGTH_SHORT).show()
        }

         */