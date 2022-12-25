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
import com.example.fyp_booking_application.EnrollData
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentAdminEnrollDetailBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class AdminEnrollDetailFragment : Fragment() {

    private lateinit var binding: FragmentAdminEnrollDetailBinding
    private lateinit var databaseRef: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_enroll_detail, container, false)
        databaseRef = FirebaseFirestore.getInstance()
        val adminView = (activity as AdminDashboardActivity)

        setFragmentResultListener("toEnrollDetails") {_, bundle ->
            val enrollID = bundle.getString("toEnrollDetails")

            val docRef = databaseRef.collection("Enroll").document(enrollID.toString())
            docRef.get().addOnSuccessListener { document ->
                val enrollRef = document.toObject(EnrollData::class.java)

                binding.enrollIDField.setText(enrollRef?.enrollID.toString())
                binding.enrollClassIDField.setText(enrollRef?.classID.toString())
                binding.enrollClassNameField.setText(enrollRef?.enrollClassName.toString())
                binding.enrollDateField.setText(enrollRef?.enrollDate.toString())
                binding.enrollTimeField.setText(enrollRef?.enrollTime.toString())
                binding.enrollStatusField.setText(enrollRef?.enrollStatus.toString())
                binding.enrollPriceField.setText(enrollRef?.enrollPrice.toString())
                binding.enrollUserIDField.setText(enrollRef?.userID.toString())

            }.addOnFailureListener { e ->
                Log.e("FETCH DOCUMENT", "ERROR FETCHING DOCUMENT", e)
            }
        }

        binding.tvBackEnrollDetail.setOnClickListener {
            adminView.replaceFragment(AdminEnrollHistoryFragment(), R.id.classAdminLayout)
        }

        return binding.root
    }

}