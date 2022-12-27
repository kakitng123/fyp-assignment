package com.example.fyp_booking_application.frontend

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fyp_booking_application.*
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentTopUpWalletHistoryBinding
import com.example.fyp_booking_application.frontend.adapter.TopUpWalletHistoryAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class TopUpWalletHistoryFragment : Fragment() {
    private lateinit var binding: FragmentTopUpWalletHistoryBinding
    private lateinit var auth: FirebaseAuth //get the shared instance of the FirebaseAuth object
    private lateinit var fstore: FirebaseFirestore
    private lateinit var topUpHistoryAdapter: TopUpWalletHistoryAdapter
    private lateinit var topUpHistoryDataArrayList: ArrayList<WalletData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_top_up_wallet_history, container, false)
        val userView = (activity as UserDashboardActivity)
        userView.setTitle("Wallet History")

        eventChangeListener()
        binding.historyTopUpRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            topUpHistoryDataArrayList = arrayListOf()
            topUpHistoryAdapter = TopUpWalletHistoryAdapter(topUpHistoryDataArrayList)
            adapter = topUpHistoryAdapter
        }
        return binding.root
    }

    private fun eventChangeListener() {
        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()
        val userID = auth.currentUser?.uid

        //Retrieve Training Class Data
        val topUpHistory = fstore.collection("Wallet").whereEqualTo("userID", userID)
        topUpHistory.addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?
            ) {
                if (error != null) {
                    Log.e("Failed", error.message.toString())
                    return
                }
                for (dc: DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        topUpHistoryDataArrayList.add(dc.document.toObject(
                            WalletData::class.java)
                        )
                    }
                }
                topUpHistoryAdapter.notifyDataSetChanged()
            }
        })
        Log.d("haha", topUpHistory.toString())
    }
}