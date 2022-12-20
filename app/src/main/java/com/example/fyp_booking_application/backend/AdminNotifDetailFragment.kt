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
        val adminActivityView = (activity as AdminDashboardActivity)
        adminActivityView.setTitle("NOTIFICATION DETAIL")
        databaseRef = FirebaseFirestore.getInstance()

        setFragmentResultListener("toNotifDetails") { _, bundle ->
            val notifyID = bundle.getString("toNotifDetails")
            val docRef = databaseRef.collection("notification_testing1").document(notifyID.toString())

            docRef.get().addOnSuccessListener { document ->
                if(document != null){
                    val testing = document.toObject(NotificationData::class.java)

                    // Set Text for EditText
                    binding.notifIDField.setText(testing?.notifyID.toString())
                    binding.notifTitleField.setText(testing?.notifyTitle.toString())
                    binding.notifMsgField.setText(testing?.notifyMessage.toString())
                    binding.notifRefCodeField.setText(testing?.referralCode.toString())
                    binding.notifUserField.setText(testing?.userID.toString()) // Get Name instead of UserID
                }
                else
                    Log.d("FETCHING DOCUMENT", "INVALID DOCUMENT")
            }
        }
        binding.tvBackNotifDetail.setOnClickListener {
            adminActivityView.replaceFragment(AdminNotifManageFragment(), R.id.notificationLayout)
        }

        return binding.root
    }
}