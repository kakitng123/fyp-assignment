package com.example.fyp_booking_application.frontend

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResult
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.UserDashboardActivity
import com.example.fyp_booking_application.backend.NotificationData
import com.example.fyp_booking_application.databinding.FragmentUserNotificationBinding
import com.example.fyp_booking_application.frontend.adapter.UserNotificationAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User

class UserNotificationFragment : Fragment() {

    private lateinit var binding: FragmentUserNotificationBinding
    private lateinit var databaseRef: FirebaseFirestore
    private lateinit var userNotifArrayList: ArrayList<NotificationData>
    private lateinit var userNotifListAdapter: UserNotificationAdapter
    private lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_notification, container, false)
        databaseRef = FirebaseFirestore.getInstance()
        listView = binding.userNotificationLV
        userNotifArrayList = arrayListOf()
        // TESTING PURPOSES
        val userView = (activity as UserDashboardActivity)

        listView.apply {
            userNotifArrayList.clear()
            // need to get auth?.userID smtg here
            setListData("ZbK2533bHaW9c5VJ41ND28jssR72")
            userNotifListAdapter = UserNotificationAdapter(context, userNotifArrayList)
            adapter = userNotifListAdapter
        }

        listView.setOnItemClickListener { _: AdapterView<*>, _:View, position:Int, _:Long ->
            val testing = userNotifArrayList[position]
            setFragmentResult("toUserNotifDetails", bundleOf("toUserNotifDetails" to testing.notifyID))
            userView.replaceFragment(UserNotificationDetailFragment())
        }

        return binding.root
    }

    private fun setListData(userID: String){
        databaseRef = FirebaseFirestore.getInstance()
        databaseRef.collection("notification_testing1").get().addOnSuccessListener { documents ->
            // Entong, need your help with this, can you sub in auth?.userID into this part, so that during real testing you can check
            // for now I hardcode stuff to double check
            for (document in documents) {
                // Sub the auth?.userID here to work properly with firebase
                if(document["userID"] == userID) {
                    userNotifArrayList.add(document.toObject(NotificationData::class.java))
                }
                userNotifListAdapter.notifyDataSetChanged()
            }
        }.addOnFailureListener { e ->
            Log.e("TEST DATA", "Error getting documents: ", e)
        }
    }
}