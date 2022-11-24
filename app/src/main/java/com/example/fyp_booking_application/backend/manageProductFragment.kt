package com.example.fyp_booking_application.backend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.fyp_booking_application.AdminDashboardActivity
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentManageProductBinding

class manageProductFragment : Fragment() {

    private lateinit var binding : FragmentManageProductBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Variables Declaration
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_manage_product, container, false)
        val adminactivityview = (activity as AdminDashboardActivity)

        binding.btnAddProduct.setOnClickListener{
            adminactivityview.replaceFragment(addProductFragment())
        }
        binding.btnEditProduct.setOnClickListener{

        }
        binding.btnDeleteProduct.setOnClickListener{

        }

        return binding.root
    }
}