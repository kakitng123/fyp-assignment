package com.example.fyp_booking_application.backend

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fyp_booking_application.EnrollData
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.backend.Adapters.ClassEnrollAdminAdapter
import com.example.fyp_booking_application.databinding.FragmentAdminClassEnrolBinding
import com.google.firebase.firestore.*

class AdminClassEnrollFragment : Fragment(), ClassEnrollAdminAdapter.OnItemClickListener {

    private lateinit var binding: FragmentAdminClassEnrolBinding
    private lateinit var databaseRef: FirebaseFirestore
    private lateinit var enrollArrayList: ArrayList<EnrollData>
    private lateinit var classEnrollAdminAdapter: ClassEnrollAdminAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_class_enrol, container, false)

        dataInitialize()
        binding.classPendingRV.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            enrollArrayList = arrayListOf()
            classEnrollAdminAdapter = ClassEnrollAdminAdapter(enrollArrayList, this@AdminClassEnrollFragment)
            adapter = classEnrollAdminAdapter
        }

        return binding.root
    }

    override fun onItemClick(position: Int) {
        val currentItem = enrollArrayList[position]
        //val adminActivityView = (activity as AdminDashboardActivity)
        //adminActivityView.replaceFragment(AdminClassDetailFragment(), R.id.classAdminLayout)
        //setFragmentResult("toClassDetails", bundleOf("toClassDetails" to currentItem.classID))
        Toast.makeText(context, "Selected: ${currentItem.enrollID}", Toast.LENGTH_SHORT).show()
    }

    private fun dataInitialize(){
        databaseRef = FirebaseFirestore.getInstance()
        databaseRef.collection("Enroll")
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
                        }
                    }
                    classEnrollAdminAdapter.notifyDataSetChanged()
                }
            })
    }
}