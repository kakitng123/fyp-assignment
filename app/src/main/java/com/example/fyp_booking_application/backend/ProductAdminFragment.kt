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
        val adminActivityView = (activity as AdminDashboardActivity)

        // Jumping Fragments
        binding.btnManage.setOnClickListener{
            adminActivityView.replaceFragment(AddProductFragment())
        }

        // Putting Data in RecyclerView
        dataInitialize()
        binding.productRecyclerView.apply{
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            productArrayList = arrayListOf()
            productAdapter = ProductAdminAdapter(productArrayList, this@ProductAdminFragment)
            adapter = productAdapter

        }

        // DO NAVIGATION VIEW (w/ DIFF CATEGORY)
        return binding.root
    }

    private fun dataInitialize(){
        databaseRef = FirebaseFirestore.getInstance()
        databaseRef.collection("products")
            .addSnapshotListener(object :  EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if(error != null){
                        Log.e("Failed", error.message.toString())
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

    override fun onItemClick(position: Int) {
        // Private Variables
        val currentItem = productArrayList[position]
        val adminActivityView = (activity as AdminDashboardActivity)
        adminActivityView.replaceFragment(ProductDetailsFragment())
        setFragmentResult("toProductDetails", bundleOf("toProductDetails" to currentItem.product_id))
    }

}