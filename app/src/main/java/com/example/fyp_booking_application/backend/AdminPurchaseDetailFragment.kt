package com.example.fyp_booking_application.backend

import android.os.Bundle
import android.provider.ContactsContract.Data
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentAdminPurchaseDetailBinding

class AdminPurchaseDetailFragment : Fragment() {

    private lateinit var binding: FragmentAdminPurchaseDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_purchase_detail, container, false)


        return binding.root
    }
}