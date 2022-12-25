package com.example.fyp_booking_application.backend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResultListener
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentAdminEnrollDetailBinding

class AdminEnrollDetailFragment : Fragment() {

    private lateinit var binding: FragmentAdminEnrollDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_enroll_detail, container, false)

        setFragmentResultListener("toEnrollDetails") {_, bundle ->
            val enrollID = bundle.getString("toEnrollDetails")

            binding.tvTEST123.text = enrollID
        }

        return binding.root
    }

}