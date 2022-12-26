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
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.VoucherData
import com.example.fyp_booking_application.databinding.FragmentAdminVoucherDetailBinding
import com.google.firebase.firestore.FirebaseFirestore

class AdminVoucherDetailFragment : Fragment() {

    private lateinit var binding: FragmentAdminVoucherDetailBinding
    private lateinit var databaseRef: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_voucher_detail, container, false)
        val adminView = (activity as AdminDashboardActivity)
        adminView.setTitle("Voucher Details")
        databaseRef = FirebaseFirestore.getInstance()

        setFragmentResultListener("toVoucherDetails") { _, bundle ->
            val notifyID = bundle.getString("toVoucherDetails")
            val docRef = databaseRef.collection("Vouchers").document(notifyID.toString())

            docRef.get().addOnSuccessListener { document ->
                if(document != null){
                    val voucher = document.toObject(VoucherData::class.java)

                    binding.voucherIDField.setText(voucher?.voucherID.toString())
                    binding.voucherTitleField.setText(voucher?.voucherTitle.toString())
                    binding.voucherMsgField.text = voucher?.voucherMessage.toString()
                    binding.voucherCodeField.setText(voucher?.voucherCode.toString())
                    binding.voucherDiscountField.setText(voucher?.voucherDiscount.toString())
                    binding.voucherPointsField.setText(voucher?.pointsRequired.toString())
                }
                else
                    Log.d("FETCHING DOCUMENT", "INVALID DOCUMENT")
            }
        }
        binding.tvBackVoucherDetail.setOnClickListener {
            adminView.replaceFragment(AdminNotifHistoryFragment(), R.id.notificationLayout)
        }

        return binding.root
    }

}