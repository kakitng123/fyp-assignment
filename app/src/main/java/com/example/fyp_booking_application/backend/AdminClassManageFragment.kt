package com.example.fyp_booking_application.backend

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fyp_booking_application.AdminDashboardActivity
import com.example.fyp_booking_application.ClassData
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.backend.Adapters.ClassManageAdminAdapter
import com.example.fyp_booking_application.databinding.FragmentAdminClassManageBinding
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.util.*
import kotlin.collections.ArrayList

class AdminClassManageFragment : Fragment(), ClassManageAdminAdapter.OnItemClickListener  {

    private lateinit var binding: FragmentAdminClassManageBinding
    private lateinit var databaseRef: FirebaseFirestore
    private lateinit var classArrayList: ArrayList<ClassData>
    private lateinit var filteredArrayList: ArrayList<ClassData>
    private lateinit var classAdminAdapter: ClassManageAdminAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_class_manage, container, false)

        dataInitialize()
        binding.classManageSV.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                filteredArrayList.clear()
                val searchText = newText!!.lowercase(Locale.getDefault())
                if (searchText.isNotEmpty()) {
                    classArrayList.forEach {
                        if (it.className!!.lowercase(Locale.getDefault()).contains(searchText))
                            filteredArrayList.add(it)
                    }
                    classAdminAdapter.notifyDataSetChanged()
                }
                else {
                    filteredArrayList.clear()
                    filteredArrayList.addAll(classArrayList)
                    classAdminAdapter.notifyDataSetChanged()
                }
                return false
            }
        })

        binding.classManageRV.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            classArrayList = arrayListOf()
            filteredArrayList = arrayListOf()
            classAdminAdapter = ClassManageAdminAdapter(filteredArrayList, this@AdminClassManageFragment)
            adapter = classAdminAdapter
        }
        return binding.root
    }

    override fun onItemClick(position: Int) {
        val currentItem = classArrayList[position]
        val adminView = (activity as AdminDashboardActivity)
        adminView.replaceFragment(AdminClassDetailFragment(), R.id.adminLayout)
        setFragmentResult("toClassDetails", bundleOf("toClassDetails" to currentItem.classID))
    }

    private fun dataInitialize(){
        databaseRef = FirebaseFirestore.getInstance()
        databaseRef.collection("TrainingClasses")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if(error != null){
                        Log.e("FAILED INITIALIZATION", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){
                            classArrayList.add(dc.document.toObject(ClassData::class.java))
                            filteredArrayList.add(dc.document.toObject(ClassData::class.java))
                        }
                    }
                    classAdminAdapter.notifyDataSetChanged()
                }
            })
    }
}