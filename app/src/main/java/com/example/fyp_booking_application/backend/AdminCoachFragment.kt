package com.example.fyp_booking_application.backend

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fyp_booking_application.AdminDashboardActivity
import com.example.fyp_booking_application.CoachData
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.backend.Adapters.CoachAdminAdapter
import com.example.fyp_booking_application.databinding.FragmentAdminCoachBinding
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import java.util.*
import kotlin.collections.ArrayList

class AdminCoachFragment : Fragment(), CoachAdminAdapter.OnItemClickListener {

    private lateinit var binding: FragmentAdminCoachBinding
    private lateinit var coachArrayList: ArrayList<CoachData>
    private lateinit var filteredArrayList: ArrayList<CoachData>
    private lateinit var databaseRef: FirebaseFirestore
    private lateinit var coachAdminAdapter: CoachAdminAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_coach, container, false)
        val adminView = (activity as AdminDashboardActivity)
        adminView.setTitle("Coach Management")

        dataInitialize()
        binding.coachManageSV.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(context, "No matches found", Toast.LENGTH_SHORT).show()
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                filteredArrayList.clear()
                val searchText = newText!!.lowercase(Locale.getDefault())
                if (searchText.isNotEmpty()) {
                    coachArrayList.forEach {
                        if (it.coachName!!.lowercase(Locale.getDefault()).contains(searchText))
                            filteredArrayList.add(it)
                    }
                    coachAdminAdapter.notifyDataSetChanged()
                }
                else {
                    filteredArrayList.clear()
                    filteredArrayList.addAll(coachArrayList)
                    coachAdminAdapter.notifyDataSetChanged()
                }
                return false
            }
        })

        binding.coachRV.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            coachArrayList = arrayListOf()
            filteredArrayList = arrayListOf()
            coachAdminAdapter = CoachAdminAdapter(filteredArrayList, this@AdminCoachFragment)
            adapter = coachAdminAdapter
        }

        binding.imgBtnAddCoach.setOnClickListener{
            adminView.replaceFragment(AdminCoachAddFragment(), R.id.adminLayout)
        }

        return binding.root
    }

    override fun onItemClick(position: Int) {
        val currentItem = coachArrayList[position]
        val adminView = (activity as AdminDashboardActivity)
        adminView.replaceFragment(AdminCoachDetailFragment(), R.id.adminLayout)
        setFragmentResult("toCoachDetails", bundleOf("toCoachDetails" to currentItem.coachID))
    }

    private fun dataInitialize() {
        databaseRef = FirebaseFirestore.getInstance()
        databaseRef.collection("Coaches")
            .addSnapshotListener(object : EventListener<QuerySnapshot>{
                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if(error != null){
                        Log.e("FAILED INITIALIZATION", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){
                            coachArrayList.add(dc.document.toObject(CoachData::class.java))
                            filteredArrayList.add(dc.document.toObject(CoachData::class.java))
                        }
                    }
                    coachAdminAdapter.notifyDataSetChanged()
                }
            })
    }
}