package com.example.fyp_booking_application.frontend

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResult
import com.example.fyp_booking_application.NotificationData
import com.example.fyp_booking_application.ProductData
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.UserDashboardActivity
import com.example.fyp_booking_application.databinding.FragmentUserNotificationBinding
import com.example.fyp_booking_application.frontend.adapter.UserNotificationAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*
import kotlin.collections.ArrayList

class UserNotificationFragment : Fragment() {

    private lateinit var binding: FragmentUserNotificationBinding
    private lateinit var auth: FirebaseAuth //get the shared instance of the FirebaseAuth object
    private lateinit var fstore: FirebaseFirestore //get the shared instance of the FirebaseAuth object
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private lateinit var userNotifArrayDataList: ArrayList<NotificationData>
    private lateinit var filteredArrayList: ArrayList<NotificationData>
    private lateinit var notifyAdapter: UserNotificationAdapter
    private lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Declare the variable
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_notification, container, false)
        userNotifArrayDataList = arrayListOf()
        filteredArrayList = arrayListOf()
        val userView = (activity as UserDashboardActivity)
        userView.setTitle("Notification")

        //Initialise
        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference
        val userID = auth.currentUser?.uid

        binding.notifySearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                filteredArrayList.clear()
                val searchText = newText!!.lowercase(Locale.getDefault())
                if (searchText.isNotEmpty()) {
                    userNotifArrayDataList.forEach {
                        if (it.notifyTitle!!.lowercase(Locale.getDefault()).contains(searchText))
                            filteredArrayList.add(it)
                    }
                    notifyAdapter.notifyDataSetChanged()
                }
                else {
                    filteredArrayList.clear()
                    filteredArrayList.addAll(userNotifArrayDataList)
                    notifyAdapter.notifyDataSetChanged()
                }
                return false
            }
        })

        //User Notification ListView
        listView = binding.userNotificationLV
        userNotifArrayDataList = arrayListOf()
        filteredArrayList = arrayListOf()
        listView.apply {
            userNotifArrayDataList.clear()
            setListData(userID.toString())
            notifyAdapter = UserNotificationAdapter(context, userNotifArrayDataList)
            adapter = notifyAdapter
        }

        listView.setOnItemClickListener { _: AdapterView<*>, _:View, position:Int, _:Long ->
            val testing = userNotifArrayDataList[position]
            setFragmentResult("toUserNotifDetails", bundleOf("toUserNotifDetails" to testing.notifyID))
            userView.replaceFragment(UserNotificationDetailFragment())
        }
        return binding.root
    }

    private fun setListData(userID: String){
        fstore.collection("Notifications").get().addOnSuccessListener { documents ->
            for (document in documents) {
                if(document["userID"] == userID) {
                    userNotifArrayDataList.add(document.toObject(NotificationData::class.java))
                    filteredArrayList.add(document.toObject(NotificationData::class.java))
                }
                notifyAdapter.notifyDataSetChanged()
            }
        }.addOnFailureListener { e ->
            Log.e("TEST DATA", "Error getting documents: ", e)
        }
    }
}