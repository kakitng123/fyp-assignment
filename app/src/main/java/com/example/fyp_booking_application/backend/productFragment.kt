package com.example.fyp_booking_application.backend

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.example.fyp_booking_application.AdminActivity
import com.example.fyp_booking_application.MainActivity
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentProductBinding
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class productFragment : Fragment() {

    private lateinit var binding : FragmentProductBinding
    private lateinit var productArrayList : ArrayList<productData>
    private lateinit var firestoreRef: FirebaseFirestore
    private lateinit var productAdapter : productAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Variable Declaration
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product, container, false)
        val adminactivityview = (activity as AdminActivity)

        // Jumping Fragments
        binding.btnManage.setOnClickListener(){
            adminactivityview.replaceFragment(manageProductFragment())
        }

        // Putting Data in RecyclerView (currently doing)
        dataInitialize()
        binding.productRecyclerView.apply{
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            productArrayList = arrayListOf()
            productAdapter = productAdapter(productArrayList)
            adapter = productAdapter

        }

        return binding.root
    }

    private fun dataInitialize(){
        firestoreRef = FirebaseFirestore.getInstance()
        firestoreRef.collection("products")
            .addSnapshotListener(object :  EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if(error != null){
                        Log.e("Failed", error.message.toString())
                        return
                    }
                    for (dc : DocumentChange in value?.documentChanges!! ){
                        if( dc.type ==  DocumentChange.Type.ADDED){
                            productArrayList.add(dc.document.toObject(productData::class.java))
                        }
                    }
                    productAdapter.notifyDataSetChanged()
                }

            })

        }

}