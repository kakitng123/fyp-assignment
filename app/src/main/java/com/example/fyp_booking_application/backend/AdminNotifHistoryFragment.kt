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
import com.example.fyp_booking_application.backend.Adapters.NotificationAdminAdapter
import com.example.fyp_booking_application.databinding.FragmentAdminNotifHistoryBinding
import com.google.firebase.firestore.*

class AdminNotifHistoryFragment : Fragment(), NotificationAdminAdapter.OnItemClickListener {

    private lateinit var binding: FragmentAdminNotifHistoryBinding
    private lateinit var notificationList: ArrayList<NotificationData>
    private lateinit var notificationAdminAdapter: NotificationAdminAdapter
    private lateinit var databaseRef: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_notif_history, container, false)
        notificationList = arrayListOf()

        dataInitialize()
        binding.notificationRecyclerView.apply{
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            notificationList.sortBy { it.referralCode }
            notificationAdminAdapter = NotificationAdminAdapter(notificationList, this@AdminNotifHistoryFragment)
            adapter = notificationAdminAdapter
        }
        return binding.root
    }

    override fun onItemClick(position: Int) {
        val currentItem = notificationList[position]
        val adminActivityView = (activity as AdminDashboardActivity)
        adminActivityView.replaceFragment(AdminNotifDetailFragment(), R.id.notificationLayout)
        setFragmentResult("toNotifDetails", bundleOf("toNotifDetails" to currentItem.notifyID))
    }

    private fun dataInitialize() {
        databaseRef = FirebaseFirestore.getInstance()
        databaseRef.collection("notification_testing1")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                @SuppressLint("NotifyDataSetChanged")
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
                    notificationList.sortByDescending { it.referralCode }
                    notificationAdminAdapter.notifyDataSetChanged()
                }
            })
    }
}