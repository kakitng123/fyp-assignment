package com.example.fyp_booking_application.backend

import android.annotation.SuppressLint
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
import com.example.fyp_booking_application.ClassData
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.backend.Adapters.ClassManageAdminAdapter
import com.example.fyp_booking_application.databinding.FragmentAdminClassManageBinding
import com.google.firebase.firestore.*

class AdminClassManageFragment : Fragment(), ClassManageAdminAdapter.OnItemClickListener  {

    private lateinit var binding: FragmentAdminClassManageBinding
    private lateinit var databaseRef: FirebaseFirestore
    private lateinit var classArrayList: ArrayList<ClassData>
    private lateinit var classAdminAdapter: ClassManageAdminAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_class_manage, container, false)

        dataInitialize()
        binding.classManageRV.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            classArrayList = arrayListOf()
            classAdminAdapter = ClassManageAdminAdapter(classArrayList, this@AdminClassManageFragment)
            adapter = classAdminAdapter
        }
        return binding.root
    }

    override fun onItemClick(position: Int) {
        val currentItem = classArrayList[position]
        val adminActivityView = (activity as AdminDashboardActivity)
        adminActivityView.replaceFragment(AdminClassDetailFragment(), R.id.classAdminLayout)
        setFragmentResult("toClassDetails", bundleOf("toClassDetails" to currentItem.classID))
    }

    private fun dataInitialize(){
        databaseRef = FirebaseFirestore.getInstance()
        databaseRef.collection("class_testing1")
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
                        }
                    }
                    classAdminAdapter.notifyDataSetChanged()
                }
            })
    }
}