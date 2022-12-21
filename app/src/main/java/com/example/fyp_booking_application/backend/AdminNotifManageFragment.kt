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
import android.widget.SearchView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentAdminNotifManageBinding
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.NonDisposableHandle.parent

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
        listView = binding.userListView

        databaseRef.collection("Users").get().addOnSuccessListener(){ results ->
            for (document in results){
                userList.add(document["username"].toString())

            }
        }.addOnFailureListener(){ e ->
            Log.d("TEST DATA", "Error getting documents: ", e)
        }
        Log.d("ADD NOTIFICATION", userList.toString())

        listAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, userList)
        binding.userListView.adapter = listAdapter

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

        listView.onItemSelectedListener

        listView.setOnItemClickListener { parent:AdapterView<*>, view:View, position:Int, id:Long ->
            Toast.makeText(context, userList[position], Toast.LENGTH_SHORT).show()
        }


        // TESTING (USABLE)
        binding.btnSendReminder.setOnClickListener {
            val newNotifyRef = databaseRef.collection("notification_testing1").document()
            val newNotify = hashMapOf(
                "notifyID" to newNotifyRef.id,
                "notifyTitle" to binding.notifAddTitleField.text.toString(),
                "notifyMessage" to binding.notifAddMessageField.text.toString(),
                "referralCode" to "R100001", // NEED TO GENERATE
                "userID" to "HKzMy78ARMLHghaN6dEk" // GET USER ID (Do Spinner or checkbox to all Opt-in Notif)
            )
            newNotifyRef.set(newNotify).addOnSuccessListener {
                Log.d("ADD NOTIFICATION", "NOTIFICATION ADDED SUCCESSFULLY")
            }.addOnFailureListener { e ->
                Log.e("ADD NOTIFICATION", "ERROR ADDING NOTIFICATION", e)
            }
        }

        return binding.root
    }

}