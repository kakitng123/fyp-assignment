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
import com.example.fyp_booking_application.databinding.FragmentCourtPendingAdminBinding
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject

class CourtPendingAdminFragment : Fragment(), CourtPendingAdminAdapter.OnItemClickListener {

    private lateinit var binding: FragmentCourtPendingAdminBinding
    private lateinit var courtPendingArrayList: ArrayList<CourtPendingData>
    private lateinit var databaseRef: FirebaseFirestore
    private lateinit var docRef : DocumentReference
    private lateinit var courtRef : DocumentReference
    private lateinit var courtAdminAdapter: CourtPendingAdminAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Variable Declarations
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_court_pending_admin, container, false)

        // Example Button (Add New Booking)
        binding.btnAddCourtPending.setOnClickListener {
            val newBookingRef = databaseRef.collection("court_testing").document()
            val newCourt = hashMapOf(
                "booking_id" to newBookingRef.id,
                "court_id" to "hdKxtkb7ooszWQNBiyPg",
                "bookingTime" to "10:00 - 11:00",
                "bookingDate" to "29_11_2022",
                "players" to 6,
                "status" to "Pending"
            )

            newBookingRef.set(newCourt)
                .addOnSuccessListener {
                    Log.d("ADDING COURT DATABASE", "Document Successfully Added!")
                    Toast.makeText(context, "DOCUMENT ADDED", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e -> Log.w("ADDING COURT DATABASE", "Error Adding Document", e) }
        }

        dataInitialize()
        binding.courtRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            courtPendingArrayList = arrayListOf()
            courtAdminAdapter = CourtPendingAdminAdapter(courtPendingArrayList, this@CourtPendingAdminFragment)
            adapter = courtAdminAdapter

        }

        return binding.root
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
                           // courtPendingArrayList.sortedWith(compareBy<CourtData> { it.courtName})
                        }
                    }
                    courtAdminAdapter.notifyDataSetChanged()
                }
            })
    }

    // RecyclerView onItemClick
    override fun onItemClick(position: Int, action: String) {
        // Private Variables
        val currentItem = courtPendingArrayList[position]
        databaseRef = FirebaseFirestore.getInstance()
        docRef = databaseRef.collection("court_testing").document(currentItem.document_id.toString())
        courtRef = databaseRef.collection("court_testing2").document(currentItem.court_id.toString())
        //val testRef = databaseRef.collection("court_testing2")

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
    }
}