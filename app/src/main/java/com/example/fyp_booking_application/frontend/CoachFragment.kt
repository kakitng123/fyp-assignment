package com.example.fyp_booking_application.frontend

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.CoachData
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.UserDashboardActivity
import com.example.fyp_booking_application.databinding.FragmentCoachBinding
import com.example.fyp_booking_application.frontend.adapter.UserCoachAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class CoachFragment : Fragment(), UserCoachAdapter.OnItemClickListener {
    private lateinit var binding: FragmentCoachBinding
    private lateinit var auth: FirebaseAuth //get the shared instance of the FirebaseAuth object
    private lateinit var fstore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private lateinit var coachAdapter: UserCoachAdapter
    private lateinit var coachDataArrayList: ArrayList<CoachData>
    private lateinit var coachRecView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //Declare the variable
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_coach, container, false)
        val userView = (activity as UserDashboardActivity)
        userView.setTitle("Coach")

        //Initialise
        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference
        val userID = auth.currentUser?.uid

        coachRecView = binding.coachRecyclerView
        coachRecView.layoutManager = LinearLayoutManager(context)//Set layout manager to position the items
        coachRecView.setHasFixedSize(true)
        coachDataArrayList = arrayListOf() //Set a array list data
        coachAdapter = UserCoachAdapter(coachDataArrayList, this@CoachFragment) //Create adapter passing in the array adapter data
        coachRecView.adapter = coachAdapter //Attach the adapter to the recyclerView to populate the items
        eventChangeListener()

        return binding.root
    }

    private fun eventChangeListener(){
        //Retrieve Coach Class Data
        fstore.collection("coach_testing1")
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

    override fun onItemClick(position: Int) {
        val currentItem = coachDataArrayList[position]
        val userView = (activity as UserDashboardActivity)
        userView.replaceFragment(TrainingClassFragment())
        setFragmentResult("toCoachClass", bundleOf("toCoachClass" to currentItem.coachName)) // Parse Data to Paired-Fragment
    }
}