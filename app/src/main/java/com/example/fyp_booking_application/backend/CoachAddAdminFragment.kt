package com.example.fyp_booking_application.backend

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.fyp_booking_application.AdminDashboardActivity
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentCoachAddAdminBinding
import com.google.firebase.firestore.FirebaseFirestore

class CoachAddAdminFragment : Fragment() {

    private lateinit var binding: FragmentCoachAddAdminBinding
    private lateinit var databaseRef: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Variable Declaration
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_coach_add_admin, container, false)
        val adminActivityView = (activity as AdminDashboardActivity)
        databaseRef = FirebaseFirestore.getInstance()

        // Button to Add New Coach
        binding.btnFinishAddCoach.setOnClickListener {
            val newCoachRef = databaseRef.collection("coach_testing1").document()
            val newCoach = hashMapOf(
                "coachID" to newCoachRef.id,
                "coachName" to "TestName1",
                "coachEmail" to "TEST@gmail.com",
                "coachExp" to "Expert",
                "coachPhone" to "010-3456789",
            )
            newCoachRef.set(newCoach)
                .addOnSuccessListener {
                    Log.d("ADD NEW COACH", "COACH ADDED SUCCESSFULLY")
                    adminActivityView.replaceFragment(CoachAdminFragment())
                }
                .addOnFailureListener { e -> Log.e("ADDING NEW COACH", "ERROR ADDING NEW COACH", e)}
        }

        return binding.root
    }
}