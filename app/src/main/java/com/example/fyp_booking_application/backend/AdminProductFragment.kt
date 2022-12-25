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
import com.example.fyp_booking_application.ProductData
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.backend.Adapters.ProductAdminAdapter
import com.example.fyp_booking_application.databinding.FragmentAdminProductBinding
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import java.util.*
import kotlin.collections.ArrayList

class AdminProductFragment : Fragment(), ProductAdminAdapter.OnItemClickListener {

    private lateinit var binding : FragmentAdminProductBinding
    private lateinit var databaseRef: FirebaseFirestore
    private lateinit var productArrayList: ArrayList<ProductData>
    private lateinit var filteredArrayList: ArrayList<ProductData>
    private lateinit var productAdapter: ProductAdminAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_product, container, false)
        productArrayList = arrayListOf()
        filteredArrayList = arrayListOf()
        val adminView = (activity as AdminDashboardActivity)
        adminView.setTitle("PRODUCT MANAGEMENT")

        binding.imgBtnAddProduct.setOnClickListener{
            adminView.replaceFragment(AdminProductAddFragment(), R.id.adminLayout)
        }

        dataInitialize1()
        binding.productNavView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.product_all -> dataInitialize1()
                R.id.product_racket -> dataInitialize2("Racket")
                R.id.product_acc -> dataInitialize2("Accessories")
                R.id.product_etc ->dataInitialize2("Etc")
            }
            true
        }
        binding.productManageSV.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                filteredArrayList.clear()
                val searchText = newText!!.lowercase(Locale.getDefault())
                if (searchText.isNotEmpty()) {
                    productArrayList.forEach {
                        if (it.productName!!.lowercase(Locale.getDefault()).contains(searchText))
                            filteredArrayList.add(it)
                    }
                    productAdapter.notifyDataSetChanged()
                }
                else {
                    filteredArrayList.clear()
                    filteredArrayList.addAll(productArrayList)
                    productAdapter.notifyDataSetChanged()
                }
                return false
            }
        })


        binding.productRecyclerView.apply{
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            productArrayList = arrayListOf()
            filteredArrayList = arrayListOf()
            productAdapter = ProductAdminAdapter(filteredArrayList, this@AdminProductFragment)
            adapter = productAdapter
        }

        return binding.root
    }

    override fun onItemClick(position: Int) {
        val currentItem = productArrayList[position]
        val adminView = (activity as AdminDashboardActivity)
        adminView.replaceFragment(AdminProductDetailFragment(), R.id.adminLayout)
        setFragmentResult("toProductDetails", bundleOf("toProductDetails" to currentItem.productID))
    }

    private fun dataInitialize1() {
        productArrayList.clear()
        filteredArrayList.clear()
        databaseRef = FirebaseFirestore.getInstance()
        databaseRef.collection("Products")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("FAILED INITIALIZATION", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            productArrayList.add(dc.document.toObject(ProductData::class.java))
                            filteredArrayList.add(dc.document.toObject(ProductData::class.java))
                        }
                    }
                    productAdapter.notifyDataSetChanged()
                }
            })
    }

    private fun dataInitialize2(category: String){
        productArrayList.clear()
        filteredArrayList.clear()
        databaseRef = FirebaseFirestore.getInstance()
        databaseRef.collection("Products").whereEqualTo("productCategory", category)
            .addSnapshotListener(object :  EventListener<QuerySnapshot> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if(error != null){
                        Log.e("FAILED INITIALIZATION", error.message.toString())
                        return
                    }
                    for (dc : DocumentChange in value?.documentChanges!! ){
                        if( dc.type ==  DocumentChange.Type.ADDED){
                            productArrayList.add(dc.document.toObject(ProductData::class.java))
                            filteredArrayList.add(dc.document.toObject(ProductData::class.java))
                        }
                    }
                    productAdapter.notifyDataSetChanged()
                }
            })
    }


}