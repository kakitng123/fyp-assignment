package com.example.fyp_booking_application.frontend

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.UserDashboardActivity
import com.example.fyp_booking_application.databinding.FragmentEnrollClassHistoryBinding
import com.example.fyp_booking_application.frontend.adapter.EnrolledClassHistoryAdapter
import com.example.fyp_booking_application.frontend.data.EnrollData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class EnrollClassHistoryFragment : Fragment() {
    private lateinit var binding: FragmentEnrollClassHistoryBinding
    private lateinit var auth: FirebaseAuth //get the shared instance of the FirebaseAuth object
    private lateinit var fstore: FirebaseFirestore
    private lateinit var enrollHistoryAdapter: EnrolledClassHistoryAdapter
    private lateinit var enrollHistoryDataArrayList: ArrayList<EnrollData>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_enroll_class_history, container, false)
        val userView = (activity as UserDashboardActivity)
        userView.setTitle("Enroll Class History")

        eventChangeListener()
        binding.historyEnrollRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            enrollHistoryDataArrayList = arrayListOf()
            enrollHistoryAdapter = EnrolledClassHistoryAdapter(enrollHistoryDataArrayList)
            adapter = enrollHistoryAdapter
        }
        return binding.root
    }

    private fun eventChangeListener(){
        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()
        val userID = auth.currentUser?.uid

        //Retrieve Training Class Data
        val enrollHistory = fstore.collection("Enroll").whereEqualTo("userID",userID)
        enrollHistory.addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?
            ) {
                if (error != null) {
                    Log.e("Failed", error.message.toString())
                    return
                }
                for (dc: DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        enrollHistoryDataArrayList.add(dc.document.toObject(
                            EnrollData::class.java
                        )
                        )
                    }
                }
                enrollHistoryAdapter.notifyDataSetChanged()
            }
        })
    }
}
