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
import com.example.fyp_booking_application.NotificationData
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.VoucherData
import com.example.fyp_booking_application.backend.Adapters.NotificationAdminAdapter
import com.example.fyp_booking_application.backend.Adapters.VoucherAdminAdapter
import com.example.fyp_booking_application.databinding.FragmentAdminVoucherHistoryBinding
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.util.*

class AdminVoucherHistoryFragment : Fragment(), VoucherAdminAdapter.OnItemClickListener {

    private lateinit var binding: FragmentAdminVoucherHistoryBinding
    private lateinit var databaseRef: FirebaseFirestore
    private lateinit var voucherList: ArrayList<VoucherData>
    private lateinit var filteredArrayList: ArrayList<VoucherData>
    private lateinit var voucherAdminAdapter: VoucherAdminAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_voucher_history, container, false)
        voucherList = arrayListOf()
        filteredArrayList = arrayListOf()

        dataInitialize()

        binding.voucherManageSV.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                filteredArrayList.clear()
                val searchText = newText!!.lowercase(Locale.getDefault())
                if (searchText.isNotEmpty()) {
                    voucherList.forEach {
                        if (it.voucherCode!!.lowercase(Locale.getDefault()).contains(searchText))
                            filteredArrayList.add(it)
                    }
                    voucherAdminAdapter.notifyDataSetChanged()
                }
                else {
                    filteredArrayList.clear()
                    filteredArrayList.addAll(voucherList)
                    voucherAdminAdapter.notifyDataSetChanged()
                }
                return false
            }
        })

        binding.voucherManageRV.apply{
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            voucherAdminAdapter = VoucherAdminAdapter(filteredArrayList, this@AdminVoucherHistoryFragment)
            adapter = voucherAdminAdapter
        }
        return binding.root
    }

    override fun onItemClick(position: Int) {
        val currentItem = voucherList[position]
        val adminActivityView = (activity as AdminDashboardActivity)
        adminActivityView.replaceFragment(AdminVoucherDetailFragment(), R.id.voucherLayout)
        setFragmentResult("toVoucherDetails", bundleOf("toVoucherDetails" to currentItem.voucherTitle))
    }

    private fun dataInitialize() {
        databaseRef = FirebaseFirestore.getInstance()
        databaseRef.collection("Vouchers")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if(error != null){
                        Log.e("FAILED INITIALIZATION", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){
                            voucherList.add(dc.document.toObject(VoucherData::class.java))
                            filteredArrayList.add(dc.document.toObject(VoucherData::class.java))
                        }
                    }
                    voucherList.sortByDescending { it.voucherTitle }
                    filteredArrayList.sortByDescending { it.voucherTitle }
                    voucherAdminAdapter.notifyDataSetChanged()
                }
            })
    }
}