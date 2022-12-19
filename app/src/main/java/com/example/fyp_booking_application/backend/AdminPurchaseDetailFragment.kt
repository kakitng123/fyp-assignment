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
import com.example.fyp_booking_application.databinding.FragmentAdminPurchaseDetailBinding
import com.google.firebase.firestore.FirebaseFirestore

class AdminPurchaseDetailFragment : Fragment() {

    private lateinit var binding: FragmentAdminPurchaseDetailBinding
    private lateinit var databaseRef: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_purchase_detail, container, false)
        databaseRef = FirebaseFirestore.getInstance()
        val adminActivityView = (activity as AdminDashboardActivity)
        adminActivityView.setTitle("PURCHASE DETAIL")

        setFragmentResultListener("toPurchaseDetail") { _, bundle ->
            val transactID = bundle.getString("toPurchaseDetail")
            val docRef = databaseRef.collection("purchase_testing1").document(transactID.toString())
            docRef.get().addOnSuccessListener { document ->
                if(document != null){
                    val transact = document.toObject(PurchaseData::class.java)

                    binding.purchaseIDField.setText(transact?.transactID.toString())
                    binding.purchasePNameField.setText(transact?.productName.toString())
                    binding.purchasePQtyField.setText(transact?.productQty.toString())
                    binding.purchaseAmtField.setText(transact?.transactAmt.toString())
                }

            }.addOnFailureListener { e -> Log.e("FETCHING DOCUMENT", "INVALID DOCUMENT", e)}
        }

        binding.tvBackPurchaseDetail.setOnClickListener{
            adminActivityView.replaceFragment(AdminPurchaseFragment(), R.id.adminLayout)
        }

        return binding.root
    }
}