package com.example.fyp_booking_application.frontend


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.fyp_booking_application.MainActivity
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentBookingCourtBinding
import com.example.fyp_booking_application.databinding.FragmentUserProfileBinding
import com.shuhart.stepview.StepView


class BookingCourtFragment : Fragment() {
    private lateinit var binding : FragmentBookingCourtBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookingCourtBinding.inflate(layoutInflater)
        val userView = (activity as MainActivity)


        return binding.root
    }
}