package com.example.fyp_booking_application.frontend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.UserDashboardActivity

class EnrollClassHistoryFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val userView = (activity as UserDashboardActivity)
        userView.setTitle("Enroll Class History")

        return inflater.inflate(R.layout.fragment_enroll_class_history, container, false)
    }
}