package com.example.fyp_booking_application.backend

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fyp_booking_application.AdminDashboardActivity
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.backend.Adapters.PurchaseAdminAdapter
import com.example.fyp_booking_application.backend.Adapters.UserAdminAdapter
import com.example.fyp_booking_application.databinding.FragmentAdminUserBinding
import com.google.firebase.firestore.*

class AdminUserFragment : Fragment(), UserAdminAdapter.OnItemClickListener {

    private lateinit var binding: FragmentAdminUserBinding
    private lateinit var databaseRef: FirebaseFirestore
    private lateinit var userList: ArrayList<UserData2>
    private lateinit var userAdminAdapter: UserAdminAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_user, container, false)

        dataInitialize()
        binding.userRecyclerView.apply{
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            userList = arrayListOf()
            userAdminAdapter = UserAdminAdapter(userList, this@AdminUserFragment)
            adapter = userAdminAdapter
        }

        return binding.root
    }

    override fun onItemClick(position: Int) {
        val currentItem = userList[position]
        val adminActivityView = (activity as AdminDashboardActivity)
        adminActivityView.replaceFragment(AdminUserDetailFragment(), R.id.adminLayout)
        setFragmentResult("toUserDetail", bundleOf("toUserDetail" to currentItem.username))
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
                            userList.add(dc.document.toObject(UserData2::class.java))
                        }
                    }
                    userAdminAdapter.notifyDataSetChanged()
                }
            })
    }
}