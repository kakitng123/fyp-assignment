package com.example.fyp_booking_application.frontend

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fyp_booking_application.EnrollData
import com.example.fyp_booking_application.PurchaseData
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.UserDashboardActivity
import com.example.fyp_booking_application.databinding.FragmentPurchaseProductHistoryBinding
import com.example.fyp_booking_application.frontend.adapter.EnrolledClassHistoryAdapter
import com.example.fyp_booking_application.frontend.adapter.PurchaseProductHistoryAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class PurchaseProductHistoryFragment : Fragment() {
    private lateinit var binding: FragmentPurchaseProductHistoryBinding
    private lateinit var auth: FirebaseAuth //get the shared instance of the FirebaseAuth object
    private lateinit var fstore: FirebaseFirestore
    private lateinit var purchaseHistoryAdapter: PurchaseProductHistoryAdapter
    private lateinit var purchaseHistoryDataArrayList: ArrayList<PurchaseData>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_purchase_product_history, container, false)
        val userView = (activity as UserDashboardActivity)
        userView.setTitle("Purchase Product History")

        eventChangeListener()
        binding.historyPurchaseRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            purchaseHistoryDataArrayList = arrayListOf()
            purchaseHistoryAdapter = PurchaseProductHistoryAdapter(purchaseHistoryDataArrayList)
            adapter = purchaseHistoryAdapter
        }
        return binding.root
    }

    private fun eventChangeListener(){
        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()
        val userID = auth.currentUser?.uid

        //Retrieve Training Class Data
        val purchaseHistory = fstore.collection("Purchases").whereEqualTo("userID",userID)
        purchaseHistory.addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?
            ) {
                if (error != null) {
                    Log.e("Failed", error.message.toString())
                    return
                }
                for (dc: DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        purchaseHistoryDataArrayList.add(dc.document.toObject(
                            PurchaseData::class.java
                        )
                        )
                    }
                }
                purchaseHistoryAdapter.notifyDataSetChanged()
            }
        })
    }
}
