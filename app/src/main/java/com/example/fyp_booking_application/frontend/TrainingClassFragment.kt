package com.example.fyp_booking_application.frontend

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
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.TrainingClassData
import com.example.fyp_booking_application.UserDashboardActivity
import com.example.fyp_booking_application.VoucherData
import com.example.fyp_booking_application.databinding.FragmentTrainingClassBinding
import com.example.fyp_booking_application.frontend.adapter.UserTrainingClassAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.util.*
import kotlin.collections.ArrayList

class TrainingClassFragment : Fragment(), UserTrainingClassAdapter.OnItemClickListener{
    private lateinit var binding: FragmentTrainingClassBinding
    private lateinit var auth: FirebaseAuth //get the shared instance of the FirebaseAuth object
    private lateinit var fstore: FirebaseFirestore
    private lateinit var trainingClassAdapter: UserTrainingClassAdapter
    private lateinit var trainingClassDataArrayList: ArrayList<TrainingClassData>
    private lateinit var filteredArrayList: ArrayList<TrainingClassData>
    private lateinit var trainingClassRecView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //Declare the variable
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_training_class, container, false)
        val userView = (activity as UserDashboardActivity)
        userView.setTitle("Training Class")

        // Initialise
        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()
        val userID = auth.currentUser?.uid

        //Search Function
        binding.classSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                filteredArrayList.clear()
                val searchText = newText!!.lowercase(Locale.getDefault())
                if (searchText.isNotEmpty()) {
                    trainingClassDataArrayList.forEach {
                        if (it.className!!.lowercase(Locale.getDefault()).contains(searchText))
                            filteredArrayList.add(it)
                    }
                    trainingClassAdapter.notifyDataSetChanged()
                }
                else {
                    filteredArrayList.clear()
                    filteredArrayList.addAll(trainingClassDataArrayList)
                    trainingClassAdapter.notifyDataSetChanged()
                }
                return false
            }
        })


        trainingClassRecView = binding.classRecyclerView
        trainingClassRecView.layoutManager = LinearLayoutManager(context) //set layout manager to position the items
        trainingClassRecView.setHasFixedSize(true)
        trainingClassDataArrayList = arrayListOf() //Set a array list data
        filteredArrayList = arrayListOf() //Set a array list data
        trainingClassAdapter = UserTrainingClassAdapter(filteredArrayList, this@TrainingClassFragment) //Create adapter passing in the array adapter data
        trainingClassRecView.adapter = trainingClassAdapter //Attach the adapter to the recyclerView to populate the items
        eventChangeListener()

        return binding.root
    }

    private fun eventChangeListener(){
        setFragmentResultListener("toCoachClass") { _, bundle ->
            val coachName = bundle.getString("toCoachClass")
            Log.d("haha", coachName.toString())

            //Retrieve Training Class Data
            fstore.collection("TrainingClasses").whereEqualTo("entitledCoach",coachName.toString())
                .addSnapshotListener(object : EventListener<QuerySnapshot> {
                    override fun onEvent(
                        value: QuerySnapshot?,
                        error: FirebaseFirestoreException?
                    ) {
                        if (error != null) {
                            Log.e("Failed", error.message.toString())
                            return
                        }
                        for (dc: DocumentChange in value?.documentChanges!!) {
                            if (dc.type == DocumentChange.Type.ADDED) {
                                trainingClassDataArrayList.add(dc.document.toObject(TrainingClassData::class.java))
                                filteredArrayList.add(dc.document.toObject(TrainingClassData::class.java))
                            }
                        }
                        trainingClassAdapter.notifyDataSetChanged()
                    }
                })
        }
    }

    override fun onItemClick(position: Int) {
        val currentItem = trainingClassDataArrayList[position]
        val userView = (activity as UserDashboardActivity)
        userView.replaceFragment(EnrollClassFragment())
        setFragmentResult("toClassDetail", bundleOf("toClassDetail" to currentItem.classID)) // Parse Data to Paired-Fragment
    }
}