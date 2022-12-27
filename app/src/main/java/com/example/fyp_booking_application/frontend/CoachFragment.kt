package com.example.fyp_booking_application.frontend

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.CoachData
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.UserDashboardActivity
import com.example.fyp_booking_application.VoucherData
import com.example.fyp_booking_application.databinding.FragmentCoachBinding
import com.example.fyp_booking_application.frontend.adapter.UserCoachAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*
import kotlin.collections.ArrayList


class CoachFragment : Fragment(), UserCoachAdapter.OnItemClickListener {
    private lateinit var binding: FragmentCoachBinding
    private lateinit var auth: FirebaseAuth //get the shared instance of the FirebaseAuth object
    private lateinit var fstore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private lateinit var coachAdapter: UserCoachAdapter
    private lateinit var coachDataArrayList: ArrayList<CoachData>
    private lateinit var filteredArrayList: ArrayList<CoachData>
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

        //Search Function
        binding.coachSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean { return false }
            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                filteredArrayList.clear()
                val searchText = newText!!.lowercase(Locale.getDefault())
                if (searchText.isNotEmpty()) {
                    coachDataArrayList.forEach {
                        if (it.coachName!!.lowercase(Locale.getDefault()).contains(searchText))
                            filteredArrayList.add(it)
                    }
                    coachAdapter.notifyDataSetChanged()
                }
                else {
                    filteredArrayList.clear()
                    filteredArrayList.addAll(coachDataArrayList)
                    coachAdapter.notifyDataSetChanged()
                }
                return false
            }
        })
        coachRecView = binding.coachRecyclerView
        //Set layout manager to position the items
        coachRecView.layoutManager = LinearLayoutManager(context)
        coachRecView.setHasFixedSize(true)
        //Set a array list data
        coachDataArrayList = arrayListOf()
        filteredArrayList = arrayListOf()
        // Create adapter passing in the array adapter data
        coachAdapter = UserCoachAdapter(filteredArrayList, this@CoachFragment)
        //Attach the adapter to the recyclerView to populate the items
        coachRecView.adapter = coachAdapter
        eventChangeListener()

        return binding.root
    }

    private fun eventChangeListener(){
        //Retrieve Coach Class Data
        fstore.collection("Coaches")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if(error != null){
                        Log.e("Failed", error.message.toString())
                        return
                    }
                    for (dc : DocumentChange in value?.documentChanges!! ){
                        if( dc.type ==  DocumentChange.Type.ADDED){
                            coachDataArrayList.add(dc.document.toObject(CoachData::class.java))
                            filteredArrayList.add(dc.document.toObject(CoachData::class.java))
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