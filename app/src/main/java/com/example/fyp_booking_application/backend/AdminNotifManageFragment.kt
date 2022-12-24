package com.example.fyp_booking_application.backend

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentAdminNotifManageBinding
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore

class AdminNotifManageFragment : Fragment() {

    private lateinit var binding: FragmentAdminNotifManageBinding
    private lateinit var databaseRef: FirebaseFirestore
    private lateinit var userList: ArrayList<String>
    private lateinit var listAdapter: ArrayAdapter<String>
    private lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_notif_manage, container, false)
        databaseRef = FirebaseFirestore.getInstance()
        userList = arrayListOf()
        listView = binding.userLV

        databaseRef.collection("Users").get().addOnSuccessListener { results ->
            for (document in results){
                userList.add(document["username"].toString())
            }
            listAdapter.notifyDataSetChanged()
        }.addOnFailureListener { e ->
            Log.e("FETCHING DOCUMENTS", "ERROR FETCHING DOCUMENTS", e)
        }

        listAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, userList)
        binding.userLV.adapter = listAdapter

        binding.notifUserSearchView.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (userList.contains(query)){
                    listAdapter.filter.filter(query)
                } else {
                    Toast.makeText(context, "No Match Found", Toast.LENGTH_LONG).show()
                }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                listAdapter.filter.filter(newText)
                return false
            }
        })

        listView.setOnItemClickListener { _:AdapterView<*>, _:View, position:Int, _:Long ->
            val testing = listAdapter.getItem(position).toString()
            val getUser = databaseRef.collection("Users").whereEqualTo("username", testing)
            getUser.get().addOnSuccessListener { documents ->
                for (document in documents){
                    binding.notifUserSearchView.queryHint = document["userID"].toString()
                }
            }.addOnFailureListener { e ->
                Log.e("ERROR FETCH DATA", "ERROR FINDING DOCUMENT", e)
            }
            Toast.makeText(context, "Selected", Toast.LENGTH_SHORT).show()
        }

        binding.btnSendReminder.setOnClickListener {
            var collectionSize: Int ?= null
            val collection = databaseRef.collection("notification_testing1").count()
            collection.get(AggregateSource.SERVER).addOnCompleteListener{ task ->
                if (task.isSuccessful){
                    collectionSize = task.result.count.toInt()
                }
                val newNotifyRef = databaseRef.collection("notification_testing1").document()
                val newNotify = hashMapOf(
                    "notifyID" to newNotifyRef.id,
                    "notifyTitle" to binding.notifAddTitleField.text.toString(),
                    "notifyMessage" to binding.notifAddMessageField.text.toString(),
                    "referralCode" to "R${100000+collectionSize!!}",
                    "userID" to binding.notifUserSearchView.queryHint // GET USER ID (Do Spinner or checkbox to all Opt-in Notif)
                )
                newNotifyRef.set(newNotify).addOnSuccessListener {
                    Log.d("ADD NOTIFICATION", "NOTIFICATION ADDED SUCCESSFULLY")

                }.addOnFailureListener { e ->
                    Log.e("ADD NOTIFICATION", "ERROR ADDING NOTIFICATION", e)
                }
            }
        }

        return binding.root
    }

}