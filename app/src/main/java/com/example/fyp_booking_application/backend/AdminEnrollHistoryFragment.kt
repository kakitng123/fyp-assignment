package com.example.fyp_booking_application.backend

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fyp_booking_application.AdminDashboardActivity
import com.example.fyp_booking_application.EnrollData
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.backend.Adapters.ClassEnrollAdminAdapter
import com.example.fyp_booking_application.databinding.FragmentAdminEnrollHistoryBinding
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.util.*
import kotlin.collections.ArrayList

class AdminEnrollHistoryFragment : Fragment(), ClassEnrollAdminAdapter.OnItemClickListener {

    private lateinit var binding: FragmentAdminEnrollHistoryBinding
    private lateinit var databaseRef: FirebaseFirestore
    private lateinit var enrollArrayList: ArrayList<EnrollData>
    private lateinit var filteredArrayList: ArrayList<EnrollData>
    private lateinit var classEnrollAdminAdapter: ClassEnrollAdminAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_enroll_history, container, false)


        dataInitialize()
        binding.enrollHistorySV.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                filteredArrayList.clear()
                val searchText = newText!!.lowercase(Locale.getDefault())
                if (searchText.isNotEmpty()) {
                    enrollArrayList.forEach {
                        if (it.enrollClassName!!.lowercase(Locale.getDefault()).contains(searchText))
                            filteredArrayList.add(it)
                    }
                    classEnrollAdminAdapter.notifyDataSetChanged()
                }
                else {
                    filteredArrayList.clear()
                    filteredArrayList.addAll(enrollArrayList)
                    classEnrollAdminAdapter.notifyDataSetChanged()
                }
                return false
            }
        })

        binding.enrollHistoryRV.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            enrollArrayList = arrayListOf()
            filteredArrayList = arrayListOf()
            enrollArrayList.sortedByDescending { list -> list.enrollDate }
            classEnrollAdminAdapter = ClassEnrollAdminAdapter(filteredArrayList, this@AdminEnrollHistoryFragment)
            adapter = classEnrollAdminAdapter
        }
        return binding.root
    }

    override fun onItemClick(position: Int) {
        val currentItem = enrollArrayList[position]
        val adminView = (activity as AdminDashboardActivity)
        adminView.replaceFragment(AdminEnrollDetailFragment(), R.id.classAdminLayout)
        setFragmentResult("toEnrollDetails", bundleOf("toEnrollDetails" to currentItem.enrollID))
    }

    override fun onApproveBtnClick(position: Int) {
        TODO("Not yet implemented")
    }

    override fun onDeclineBtnClick(position: Int) {
        TODO("Not yet implemented")
    }

    private fun dataInitialize(){
        databaseRef = FirebaseFirestore.getInstance()
        databaseRef.collection("Enroll").whereNotEqualTo("enrollStatus", "Pending")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if(error != null){
                        Log.e("FAILED INITIALIZATION", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){
                            enrollArrayList.add(dc.document.toObject(EnrollData::class.java))
                            filteredArrayList.add(dc.document.toObject(EnrollData::class.java))
                        }
                    }
                    enrollArrayList.sortedByDescending { list -> list.enrollDate}
                    classEnrollAdminAdapter.notifyDataSetChanged()
                }
            })
    }

}