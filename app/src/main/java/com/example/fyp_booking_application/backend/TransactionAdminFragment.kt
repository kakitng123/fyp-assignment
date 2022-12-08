package com.example.fyp_booking_application.backend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.fyp_booking_application.AdminDashboardActivity
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentTransactionBinding

class TransactionAdminFragment : Fragment() {

    private lateinit var binding: FragmentTransactionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Variable Declarations
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_transaction, container, false)
        val adminActivityView = (activity as AdminDashboardActivity)

        // Directing Navigation
        binding.transactionNavBar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_booking -> Toast.makeText(context, "BOOKING CLICKED", Toast.LENGTH_SHORT).show()
                R.id.nav_purchase -> Toast.makeText(context, "PURCHASES CLICKED", Toast.LENGTH_SHORT).show()
                R.id.nav_notif -> Toast.makeText(context, "NOTIFICATION CLICKED", Toast.LENGTH_SHORT).show()
            }
            true
        }

        return binding.root
    }
}