package com.example.fyp_booking_application.backend

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
import com.example.fyp_booking_application.backend.Adapters.ProductAdminAdapter
import com.example.fyp_booking_application.databinding.FragmentProductBinding
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class ProductAdminFragment : Fragment(), ProductAdminAdapter.OnItemClickListener {

    private lateinit var binding : FragmentProductBinding
    private lateinit var productArrayList : ArrayList<ProductData>
    private lateinit var databaseRef: FirebaseFirestore
    private lateinit var productAdapter : ProductAdminAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Variable Declaration
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product, container, false)
        productArrayList = arrayListOf()
        val adminActivityView = (activity as AdminDashboardActivity)

        binding.btnManage.setOnClickListener{
            adminActivityView.replaceFragment(ProductAddFragment())
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
        binding.productRecyclerView.apply{
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            productArrayList = arrayListOf()
            productAdapter = ProductAdminAdapter(productArrayList, this@ProductAdminFragment)
            adapter = productAdapter

        }
        return binding.root
    }

    override fun onItemClick(position: Int) {
        val currentItem = productArrayList[position]
        val adminActivityView = (activity as AdminDashboardActivity)
        adminActivityView.replaceFragment(ProductDetailsFragment())
        // Parse Data to Paired-Fragment
        setFragmentResult("toProductDetails", bundleOf("toProductDetails" to currentItem.productID))
    }

    // Get/Parse Data into RecyclerView
    private fun dataInitialize1() {
        productArrayList.clear()
        databaseRef = FirebaseFirestore.getInstance()
        databaseRef.collection("Products")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("FAILED INITIALIZATION", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            productArrayList.add(dc.document.toObject(ProductData::class.java))
                        }
                    }
                    productAdapter.notifyDataSetChanged()
                }
            })
    }

    // Get/Parse Data into RecyclerView (w/ Diff.Category)
    private fun dataInitialize2(category: String){
        productArrayList.clear()
        databaseRef = FirebaseFirestore.getInstance()
        databaseRef.collection("Products").whereEqualTo("productCategory", category)
            .addSnapshotListener(object :  EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if(error != null){
                        Log.e("FAILED INITIALIZATION", error.message.toString())
                        return
                    }
                    for (dc : DocumentChange in value?.documentChanges!! ){
                        if( dc.type ==  DocumentChange.Type.ADDED){
                            productArrayList.add(dc.document.toObject(ProductData::class.java))
                        }
                    }
                    productAdapter.notifyDataSetChanged()
                }
            })
    }


}