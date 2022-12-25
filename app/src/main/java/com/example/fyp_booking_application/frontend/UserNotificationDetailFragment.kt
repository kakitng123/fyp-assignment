package com.example.fyp_booking_application.frontend

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import com.example.fyp_booking_application.NotificationData
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentUserNotificationBinding
import com.example.fyp_booking_application.databinding.FragmentUserNotificationDetailBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class UserNotificationDetailFragment : Fragment() {

    private lateinit var binding: FragmentUserNotificationDetailBinding
    private lateinit var databaseRef: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_notification_detail, container, false)
        databaseRef = FirebaseFirestore.getInstance()

        // Havent do design but ignore for now
        setFragmentResultListener("toUserNotifDetails"){ _, bundle ->
            val testing123 = bundle.getString("toUserNotifDetails")
            val docRef = databaseRef.collection("Notifications").document(testing123.toString())

            docRef.get().addOnSuccessListener { document ->
                if (document != null){
                    val notification = document.toObject(NotificationData::class.java)

                    binding.tvUserNotifDetailTitle.text = notification?.notifyTitle.toString()
                    binding.tvUserNotifDetailMsg.text = notification?.notifyMessage.toString()
                    binding.tvUserNotifDetailRef.text = notification?.referralCode.toString()
                }
                else Log.d("FETCHING DOCUMENT", "ERROR FETCHING DOCUMENT")

            }.addOnFailureListener { e ->
                Log.e("FETCHING DOCUMENT", "ERROR FETCHING DOCUMENT", e)
            }
        }

        return binding.root
    }
}