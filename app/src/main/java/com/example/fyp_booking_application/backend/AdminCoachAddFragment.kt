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
import com.example.fyp_booking_application.databinding.FragmentAdminCoachAddBinding
import com.google.firebase.firestore.FirebaseFirestore

class AdminCoachAddFragment : Fragment() {

    private lateinit var binding: FragmentAdminCoachAddBinding
    private lateinit var databaseRef: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_coach_add, container, false)
        val adminActivityView = (activity as AdminDashboardActivity)
        databaseRef = FirebaseFirestore.getInstance()

        binding.btnFinishAddCoach.setOnClickListener {
            val newCoachRef = databaseRef.collection("coach_testing1").document()
            val newCoach = hashMapOf(
                "coachID" to newCoachRef.id,
                "coachName" to binding.tfAddCoachName.text.toString(),
                "coachEmail" to binding.tfAddCoachEmail.text.toString(),
                "coachExp" to binding.tfAddCoachExp.text.toString(), // Could Use Spinner
                "coachPhone" to binding.tfAddCoachPhone.text.toString(),
            )
            newCoachRef.set(newCoach)
                .addOnSuccessListener {
                    Log.d("ADD NEW COACH", "COACH ADDED SUCCESSFULLY")
                    adminActivityView.replaceFragment(AdminCoachFragment(),R.id.adminLayout)
                }
                .addOnFailureListener { e -> Log.e("ADDING NEW COACH", "ERROR ADDING NEW COACH", e)}
        }

        return binding.root
    }
}