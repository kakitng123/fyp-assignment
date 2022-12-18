package com.example.fyp_booking_application.backend

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fyp_booking_application.AdminDashboardActivity
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.backend.Adapters.BookingAdminAdapter
import com.example.fyp_booking_application.databinding.FragmentAdminBookingBinding
import com.google.firebase.firestore.*

class AdminBookingFragment : Fragment(), BookingAdminAdapter.OnItemClickListener{

    private lateinit var binding: FragmentAdminBookingBinding
    private lateinit var databaseRef: FirebaseFirestore
    private lateinit var bookingArrayList: ArrayList<BookingDataTesting>
    private lateinit var bookingAdminAdapter: BookingAdminAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_admin_booking, container, false)
        bookingArrayList = arrayListOf()

        dataApproved()
        binding.bookingNavView.setOnItemSelectedListener {
            binding.bookingRecyclerView.visibility = View.VISIBLE
            when(it.itemId){
                R.id.nav_bookingPending -> dataPending()
                R.id.nav_bookingHistory -> dataApproved()
            }
            true
        }

        binding.bookingRecyclerView.apply{
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            bookingArrayList = arrayListOf()
            bookingAdminAdapter = BookingAdminAdapter(bookingArrayList, this@AdminBookingFragment)
            adapter = bookingAdminAdapter
        }

        return binding.root
    }

    override fun onItemClick(position: Int) {
        val currentItem = bookingArrayList[position]
        val adminActivityView = (activity as AdminDashboardActivity)
        adminActivityView.replaceFragment(AdminBookingDetailFragment(), R.id.adminLayout)
        setFragmentResult("toAdminBookingDetails", bundleOf("toAdminBookingDetails" to currentItem.bookingID))
    }

    override fun onButtonClick(position: Int) {
        val currentItem = bookingArrayList[position]
        databaseRef = FirebaseFirestore.getInstance()

        // TESTING (USABLE)
        val newNotifyRef = databaseRef.collection("notification_testing1").document()
        val newNotify = hashMapOf(
            "notifyID" to newNotifyRef.id,
            "userID" to currentItem.userID.toString(),
            "notifyTitle" to "PENDING PAYMENT",
            "notifyMessage" to "YOUR PAYMENT FOR BOOKING ID:${currentItem.bookingID} IS STILL PENDING, " +
                    "PLEASE PROCEED TO XXX PAGE TO CONTINUE PAYMENT!",
            "referralCode" to "R100001" // NEED TO GENERATE
        )
        newNotifyRef.set(newNotify).addOnSuccessListener {
            Log.d("ADD NOTIFICATION", "NOTIFICATION ADDED SUCCESSFULLY")
        }.addOnFailureListener { e ->
            Log.e("ADD NOTIFICATION", "ERROR ADDING NOTIFICATION", e)
        }
    }

    // Could make into one function
    private fun dataPending() {
        bookingArrayList.clear()
        databaseRef = FirebaseFirestore.getInstance()
        databaseRef.collection("court_testing").whereEqualTo("status","Pending")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("FAILED INITIALIZATION", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            bookingArrayList.add(dc.document.toObject(BookingDataTesting::class.java))
                        }
                    }
                    bookingAdminAdapter.notifyDataSetChanged()
                }
            })
    }

    private fun dataApproved() {
        bookingArrayList.clear()
        databaseRef = FirebaseFirestore.getInstance()
        databaseRef.collection("court_testing").whereNotEqualTo("status","Pending")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("FAILED INITIALIZATION", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            bookingArrayList.add(dc.document.toObject(BookingDataTesting::class.java))
                        }
                    }
                    bookingAdminAdapter.notifyDataSetChanged()
                }
            })
    }
}