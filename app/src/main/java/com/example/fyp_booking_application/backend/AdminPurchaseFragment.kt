package com.example.fyp_booking_application.backend

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fyp_booking_application.AdminDashboardActivity
import com.example.fyp_booking_application.PurchaseData
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.backend.Adapters.PurchaseAdminAdapter
import com.example.fyp_booking_application.databinding.FragmentAdminPurchaseBinding
import com.google.firebase.firestore.*

class AdminPurchaseFragment : Fragment(), PurchaseAdminAdapter.OnItemClickListener {

    private lateinit var binding: FragmentAdminPurchaseBinding
    private lateinit var databaseRef: FirebaseFirestore
    private lateinit var purchaseList: ArrayList<PurchaseData>
    private lateinit var purchaseAdminAdapter: PurchaseAdminAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_admin_purchase, container, false)
        val adminView = (activity as AdminDashboardActivity)
        adminView.setTitle("PURCHASE MANAGEMENT")

        dataInitialize()
        binding.transactionRV.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            purchaseList = arrayListOf()
            purchaseAdminAdapter = PurchaseAdminAdapter(purchaseList, this@AdminPurchaseFragment)
            adapter = purchaseAdminAdapter
        }

        return binding.root
    }

    override fun onItemClick(position: Int) {
        val currentItem = purchaseList[position]
        val adminView = (activity as AdminDashboardActivity)
        adminView.replaceFragment(AdminPurchaseDetailFragment(), R.id.adminLayout)
        setFragmentResult("toPurchaseDetail", bundleOf("toPurchaseDetail" to currentItem.purchaseID))
    }

    private fun dataInitialize(){
        databaseRef = FirebaseFirestore.getInstance()
        databaseRef.collection("Purchases")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if(error != null){
                        Log.e("FAILED INITIALIZATION", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){
                            purchaseList.add(dc.document.toObject(PurchaseData::class.java))
                        }
                    }
                    purchaseAdminAdapter.notifyDataSetChanged()
                }
            })
    }
}