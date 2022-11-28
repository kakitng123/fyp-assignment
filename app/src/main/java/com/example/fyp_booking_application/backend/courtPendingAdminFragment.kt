package com.example.fyp_booking_application.backend

import android.content.ContentValues
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
import com.example.fyp_booking_application.databinding.FragmentCourtPendingAdminBinding
import com.google.firebase.firestore.*

class courtPendingAdminFragment : Fragment(), courtAdminAdapter.OnItemClickListener {

    private lateinit var binding: FragmentCourtPendingAdminBinding
    private lateinit var courtPendingArrayList: ArrayList<CourtPendingData>
    private lateinit var firestoreRef: FirebaseFirestore
    private lateinit var courtAdminAdapter: courtAdminAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Variable Declarations
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_court_pending_admin, container, false)

        // Example Button (Add New Booking)
        binding.btnAddCourt.setOnClickListener() {
            val newCourtRef = firestoreRef.collection("court_testing").document()
            val newCourt = hashMapOf(
                "document_id" to newCourtRef.id,
                "court_id" to "A1",
                "bookingTime" to "10:00am - 12:00pm",
                "bookingDate" to "29_11_2022",
                "players" to 6
            )

            newCourtRef.set(newCourt)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "Document Successfully Added!")
                    Toast.makeText(context, "DOCUMENT ADDED", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error Adding Document", e)
                }
        }

        dataInitialize()
        binding.courtRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            courtPendingArrayList = arrayListOf()
            courtAdminAdapter = courtAdminAdapter(courtPendingArrayList, this@courtPendingAdminFragment)
            adapter = courtAdminAdapter

        }

        return binding.root
    }

    private fun dataInitialize() {
        firestoreRef = FirebaseFirestore.getInstance()
        firestoreRef.collection("court_testing")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("Failed", error.message.toString())
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

    override fun onItemClick(position: Int, action: String) {
        // Private Variables
        val currentItem = courtPendingArrayList[position]

        Toast.makeText(context, "TESTING: $action", Toast.LENGTH_SHORT).show()
    }
}