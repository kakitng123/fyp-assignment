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
import com.example.fyp_booking_application.backend.Adapters.NotificationAdminAdapter
import com.example.fyp_booking_application.databinding.FragmentAdminNotifBinding
import com.google.firebase.firestore.*

class AdminNotifFragment : Fragment(), NotificationAdminAdapter.OnItemClickListener {

    private lateinit var binding: FragmentAdminNotifBinding
    private lateinit var notificationList: ArrayList<NotificationData>
    private lateinit var notificationAdminAdapter: NotificationAdminAdapter
    private lateinit var databaseRef: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Variable Declarations
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_notif, container, false)

        dataInitialize()
        binding.notificationRecyclerView.apply{
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            notificationList = arrayListOf()
            notificationAdminAdapter = NotificationAdminAdapter(notificationList, this@AdminNotifFragment)
            adapter = notificationAdminAdapter
        }

        return binding.root
    }

    override fun onItemClick(position: Int) {
        val currentItem = notificationList[position]
        Toast.makeText(context, "TESTING: ${currentItem.referralCode}", Toast.LENGTH_SHORT).show()
    }

    // Get/Parse Data into RecyclerView
    private fun dataInitialize() {
        databaseRef = FirebaseFirestore.getInstance()
        databaseRef.collection("notification_testing1")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if(error != null){
                        Log.e("FAILED INITIALIZATION", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){
                            notificationList.add(dc.document.toObject(NotificationData::class.java))
                        }
                    }
                    notificationAdminAdapter.notifyDataSetChanged()
                }
            })
    }
}