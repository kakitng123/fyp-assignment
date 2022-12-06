package com.example.fyp_booking_application.backend

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fyp_booking_application.AdminDashboardActivity
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.backend.Adapters.CoachAdminAdapter
import com.example.fyp_booking_application.databinding.FragmentCoachAdminBinding
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class CoachAdminFragment : Fragment(), CoachAdminAdapter.OnItemClickListener {

    private lateinit var binding: FragmentCoachAdminBinding
    private lateinit var coachArrayList: ArrayList<CoachData>
    private lateinit var databaseRef: FirebaseFirestore
    private lateinit var coachAdminAdapter: CoachAdminAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Variable Declarations
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_coach_admin, container, false)

        dataInitialize()
        binding.coachRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            coachArrayList = arrayListOf()
            coachAdminAdapter = CoachAdminAdapter(coachArrayList, this@CoachAdminFragment)
            adapter = coachAdminAdapter
        }
        return binding.root
    }

    override fun onItemClick(position: Int) {
        val currentItem = coachArrayList[position]
        val adminActivityView = (activity as AdminDashboardActivity)
        adminActivityView.replaceFragment(CoachDetailAdminFragment())
        setFragmentResult("toCoachDetails", bundleOf("toCoachDetails" to currentItem.coachID))
    }

    // Get/Parse Data into RecyclerView
    private fun dataInitialize() {
        databaseRef = FirebaseFirestore.getInstance()
        databaseRef.collection("CoachProfile")
            .addSnapshotListener(object : EventListener<QuerySnapshot>{
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if(error != null){
                        Log.e("FAILED INITIALIZATION", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){
                            coachArrayList.add(dc.document.toObject(CoachData::class.java))
                        }
                    }
                    coachAdminAdapter.notifyDataSetChanged()
                }
            })
    }
}