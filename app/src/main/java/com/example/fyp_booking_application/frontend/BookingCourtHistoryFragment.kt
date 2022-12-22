package com.example.fyp_booking_application.frontend

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.UserDashboardActivity
import com.example.fyp_booking_application.databinding.FragmentBookingCourtHistoryBinding
import com.example.fyp_booking_application.databinding.FragmentTrainingClassBinding
import com.example.fyp_booking_application.frontend.adapter.BookingCourtHistoryAdapter
import com.example.fyp_booking_application.frontend.adapter.UserTrainingClassAdapter
import com.example.fyp_booking_application.frontend.data.BookingData
import com.example.fyp_booking_application.frontend.data.TrainingClassData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class BookingCourtHistoryFragment : Fragment()  {
    private lateinit var binding: FragmentBookingCourtHistoryBinding
    private lateinit var auth: FirebaseAuth //get the shared instance of the FirebaseAuth object
    private lateinit var fstore: FirebaseFirestore
    private lateinit var bookingHistoryAdapter: BookingCourtHistoryAdapter
    private lateinit var bookingHistoryDataArrayList: ArrayList<BookingData>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_booking_court_history, container, false)
        val userView = (activity as UserDashboardActivity)
        userView.setTitle("Booking Court History")

       eventChangeListener()
        binding.historyBookingRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            bookingHistoryDataArrayList = arrayListOf()
            bookingHistoryAdapter = BookingCourtHistoryAdapter(bookingHistoryDataArrayList)
            adapter = bookingHistoryAdapter
        }
        return binding.root
    }

    private fun eventChangeListener(){
        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()
        val userID = auth.currentUser?.uid

        //Retrieve Training Class Data
        val bookingHistory = fstore.collection("Bookings").whereEqualTo("userID",userID)
            bookingHistory.addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?
                ) {
                    if (error != null) {
                        Log.e("Failed", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            bookingHistoryDataArrayList.add(dc.document.toObject(
                                    BookingData::class.java
                                )
                            )
                        }
                    }
                    bookingHistoryAdapter.notifyDataSetChanged()
                }
            })
        }
}
