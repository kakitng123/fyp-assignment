package com.example.fyp_booking_application.frontend

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.UserDashboardActivity
import com.example.fyp_booking_application.databinding.FragmentTrainingClassBinding
import com.example.fyp_booking_application.frontend.adapter.CoachAdapter
import com.example.fyp_booking_application.frontend.data.CoachData
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class TrainingClassFragment : Fragment() {
    private lateinit var binding: FragmentTrainingClassBinding
    private lateinit var storage: FirebaseStorage
    private lateinit var fstore: FirebaseFirestore
    private lateinit var storageRef: StorageReference
    private lateinit var coachAdapter: CoachAdapter
    private lateinit var coachDataArrayList: ArrayList<CoachData>
    private lateinit var recView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Declare the variable
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_training_class, container, false)
        val userView = (activity as UserDashboardActivity)

        recView = binding.recyclerView
        recView.layoutManager = LinearLayoutManager(context)
        recView.setHasFixedSize(true)
        coachDataArrayList = arrayListOf()
        coachAdapter = CoachAdapter(coachDataArrayList)
        recView.adapter = coachAdapter
        eventChangeListener()

        return binding.root
    }
    private fun eventChangeListener(){
        fstore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        fstore.collection("CoachProfile")
        .addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if(error != null){
                    Log.e("Failed", error.message.toString())
                    return
                }
                for (dc : DocumentChange in value?.documentChanges!! ){
                    if( dc.type ==  DocumentChange.Type.ADDED){
                        coachDataArrayList.add(dc.document.toObject(CoachData::class.java))
                    }
                }
                coachAdapter.notifyDataSetChanged()
            }
        })
    }
}