package com.example.fyp_booking_application.backend

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentAdminNotifManageBinding
import com.google.firebase.firestore.FirebaseFirestore

class AdminNotifManageFragment : Fragment() {

    private lateinit var binding: FragmentAdminNotifManageBinding
    private lateinit var databaseRef: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_notif_manage, container, false)
        databaseRef = FirebaseFirestore.getInstance()

        // TESTING (USABLE)
        binding.imgBtnAddNotif.setOnClickListener {
            val newNotifyRef = databaseRef.collection("notification_testing1").document()
            val newNotify = hashMapOf(
                "notifyID" to newNotifyRef.id,
                "userID" to "HKzMy78ARMLHghaN6dEk", // GET USER ID (Do Spinner or checkbox to all Opt-in Notif)
                "notifyTitle" to binding.tfAddNotifTitle.text.toString(),
                "notifyMessage" to binding.tfAddNotifMsg.text.toString(),
                "referralCode" to "R100001" // NEED TO GENERATE
            )
            newNotifyRef.set(newNotify).addOnSuccessListener {
                Log.d("ADD NOTIFICATION", "NOTIFICATION ADDED SUCCESSFULLY")
            }.addOnFailureListener { e ->
                Log.e("ADD NOTIFICATION", "ERROR ADDING NOTIFICATION", e)
            }
        }

        return binding.root
    }

}