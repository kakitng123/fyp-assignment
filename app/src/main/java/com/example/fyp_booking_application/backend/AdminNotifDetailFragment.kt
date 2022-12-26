package com.example.fyp_booking_application.backend

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResultListener
import com.example.fyp_booking_application.AdminDashboardActivity
import com.example.fyp_booking_application.NotificationData
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentAdminNotifDetailBinding
import com.google.firebase.firestore.FirebaseFirestore

class AdminNotifDetailFragment : Fragment() {

    private lateinit var binding: FragmentAdminNotifDetailBinding
    private lateinit var databaseRef: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_notif_detail, container, false)
        val adminView = (activity as AdminDashboardActivity)
        adminView.setTitle("Notification Details")
        databaseRef = FirebaseFirestore.getInstance()

        setFragmentResultListener("toNotifDetails") { _, bundle ->
            val notifyID = bundle.getString("toNotifDetails")
            val docRef = databaseRef.collection("Notifications").document(notifyID.toString())

            docRef.get().addOnSuccessListener { document ->
                if(document != null){
                    val notification = document.toObject(NotificationData::class.java)

                    binding.notifIDField.setText(notification?.notifyID.toString())
                    binding.notifTitleField.setText(notification?.notifyTitle.toString())
                    binding.notifMsgField.text = notification?.notifyMessage.toString()
                    binding.notifRefCodeField.setText(notification?.referralCode.toString())
                    binding.notifUserField.setText(notification?.userID.toString()) // Get Name instead of UserID
                }
                else
                    Log.d("FETCHING DOCUMENT", "INVALID DOCUMENT")
            }
        }
        binding.tvBackNotifDetail.setOnClickListener {
            adminView.replaceFragment(AdminNotifHistoryFragment(), R.id.notificationLayout)
        }

        return binding.root
    }
}