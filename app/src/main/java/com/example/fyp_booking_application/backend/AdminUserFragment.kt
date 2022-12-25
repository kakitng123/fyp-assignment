package com.example.fyp_booking_application.backend

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fyp_booking_application.AdminDashboardActivity
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.TestUserData
import com.example.fyp_booking_application.backend.Adapters.UserAdminAdapter
import com.example.fyp_booking_application.databinding.FragmentAdminUserBinding
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.util.*
import kotlin.collections.ArrayList

class AdminUserFragment : Fragment(), UserAdminAdapter.OnItemClickListener {

    private lateinit var binding: FragmentAdminUserBinding
    private lateinit var databaseRef: FirebaseFirestore
    private lateinit var userList: ArrayList<TestUserData>
    private lateinit var filteredArrayList: ArrayList<TestUserData>
    private lateinit var userAdminAdapter: UserAdminAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_user, container, false)
        val adminView = (activity as AdminDashboardActivity)
        adminView.setTitle("USER MANAGEMENT")

        dataInitialize()

        binding.userManageSV.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                filteredArrayList.clear()
                val searchText = newText!!.lowercase(Locale.getDefault())
                if (searchText.isNotEmpty()) {
                    userList.forEach {
                        if (it.username!!.lowercase(Locale.getDefault()).contains(searchText))
                            filteredArrayList.add(it)
                    }
                    userAdminAdapter.notifyDataSetChanged()
                }
                else {
                    filteredArrayList.clear()
                    filteredArrayList.addAll(userList)
                    userAdminAdapter.notifyDataSetChanged()
                }
                return false
            }
        })

        binding.userRV.apply{
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            userList = arrayListOf()
            filteredArrayList = arrayListOf()
            userAdminAdapter = UserAdminAdapter(filteredArrayList, this@AdminUserFragment)
            adapter = userAdminAdapter
        }

        return binding.root
    }

    override fun onItemClick(position: Int) {
        val currentItem = userList[position]
        val adminView = (activity as AdminDashboardActivity)
        adminView.replaceFragment(AdminUserDetailFragment(), R.id.adminLayout)
        setFragmentResult("toUserDetail", bundleOf("toUserDetail" to currentItem.userID))
    }

    private fun dataInitialize(){
        databaseRef = FirebaseFirestore.getInstance()
        databaseRef.collection("Users")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if(error != null){
                        Log.e("FAILED INITIALIZATION", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){
                            userList.add(dc.document.toObject(TestUserData::class.java))
                            filteredArrayList.add(dc.document.toObject(TestUserData::class.java))
                        }
                    }
                    userAdminAdapter.notifyDataSetChanged()
                }
            })
    }
}