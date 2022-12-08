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
import com.example.fyp_booking_application.backend.Adapters.ClassAdminAdapter
import com.example.fyp_booking_application.databinding.FragmentClassAdminBinding
import com.google.firebase.firestore.*

class ClassAdminFragment : Fragment(), ClassAdminAdapter.OnItemClickListener {

    private lateinit var binding: FragmentClassAdminBinding
    private lateinit var databaseRef: FirebaseFirestore
    private lateinit var classArrayList: ArrayList<ClassData2>
    private lateinit var classAdminAdapter: ClassAdminAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Variable Declarations
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_class_admin, container, false)
        val adminActivityView = (activity as AdminDashboardActivity)

        dataInitialize()
        binding.classRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            classArrayList = arrayListOf()
            classAdminAdapter = ClassAdminAdapter(classArrayList, this@ClassAdminFragment)
            adapter = classAdminAdapter
        }

        binding.btnAddClass.setOnClickListener{
           adminActivityView.replaceFragment(ClassAddAdminFragment())
        }

        return binding.root
    }

    override fun onItemClick(position: Int) {
        val currentItem = classArrayList[position]
        val adminActivityView = (activity as AdminDashboardActivity)
        adminActivityView.replaceFragment(ClassDetailAdminFragment())
        setFragmentResult("toClassDetails", bundleOf("toClassDetails" to currentItem.classID))
    }

    // Get/Parse Data into RecyclerView
    private fun dataInitialize(){
        databaseRef = FirebaseFirestore.getInstance()
        databaseRef.collection("class_testing1")
            .addSnapshotListener(object : EventListener<QuerySnapshot>{
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if(error != null){
                        Log.e("FAILED INITIALIZATION", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){
                            classArrayList.add(dc.document.toObject(ClassData2::class.java))
                        }
                    }
                    classAdminAdapter.notifyDataSetChanged()
                }
            })
    }

}