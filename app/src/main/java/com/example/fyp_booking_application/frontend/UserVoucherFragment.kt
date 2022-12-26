package com.example.fyp_booking_application.frontend


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.ProductData
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.UserDashboardActivity
import com.example.fyp_booking_application.VoucherData
import com.example.fyp_booking_application.databinding.FragmentUserVoucherBinding
import com.example.fyp_booking_application.frontend.adapter.UserNotificationAdapter
import com.example.fyp_booking_application.frontend.adapter.UserVoucherAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*
import kotlin.collections.ArrayList

class UserVoucherFragment : Fragment(), UserVoucherAdapter.OnItemClickListener {
    private lateinit var binding: FragmentUserVoucherBinding
    private lateinit var auth: FirebaseAuth //get the shared instance of the FirebaseAuth object
    private lateinit var fstore: FirebaseFirestore //get the shared instance of the FirebaseAuth object
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private lateinit var voucherAdapter: UserVoucherAdapter
    private lateinit var userVouDataArrayList: ArrayList<VoucherData>
    private lateinit var filteredArrayList: ArrayList<VoucherData>
    private lateinit var userVouRecView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        //Declare the variable
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_voucher, container, false)
        val userView = (activity as UserDashboardActivity)
        userView.setTitle("Voucher")

        //Initialise
        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference
        val userID = auth.currentUser?.uid

        //Search Function
        binding.voucherSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                filteredArrayList.clear()
                val searchText = newText!!.lowercase(Locale.getDefault())
                if (searchText.isNotEmpty()) {
                    userVouDataArrayList.forEach {
                        if (it.voucherTitle!!.lowercase(Locale.getDefault()).contains(searchText))
                            filteredArrayList.add(it)
                    }
                    voucherAdapter.notifyDataSetChanged()
                }
                else {
                    filteredArrayList.clear()
                    filteredArrayList.addAll(userVouDataArrayList)
                    voucherAdapter.notifyDataSetChanged()
                }
                return false
            }
        })

        //User Voucher Recycler View
        userVouRecView = binding.userVoucherRecyclerView
        userVouRecView.layoutManager = LinearLayoutManager(context) //Set layout manager to position the items
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false) //Set layout to horizontal
        userVouRecView.setLayoutManager(linearLayoutManager)
        userVouRecView.setHasFixedSize(true)
        userVouDataArrayList = arrayListOf()//Set a array list data
        filteredArrayList = arrayListOf()
        voucherAdapter = UserVoucherAdapter(filteredArrayList,this@UserVoucherFragment) //Create adapter passing in the array adapter data
        userVouRecView.adapter = voucherAdapter //Attach the adapter to the recyclerView to populate the items

        eventChangeListener()
        return binding.root
    }

    private fun eventChangeListener() {
        //Retrieve Voucher Class Data
        fstore.collection("Vouchers")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("Failed", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            userVouDataArrayList.add(dc.document.toObject(VoucherData::class.java))
                            filteredArrayList.add(dc.document.toObject(VoucherData::class.java))
                        }
                    }
                    voucherAdapter.notifyDataSetChanged()
                }
            })
    }

    override fun onItemClick(position: Int) {
        val currentVouItem = userVouDataArrayList[position]
        val userView = (activity as UserDashboardActivity)
        userView.replaceFragment(UserVoucherDetailsFragment())
        // Parse Data to Paired-Fragment
        setFragmentResult("toUserVoucherDetail", bundleOf("toUserVoucherDetail" to currentVouItem.voucherID))
    }
}

