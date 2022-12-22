package com.example.fyp_booking_application.frontend


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.UserDashboardActivity
import com.example.fyp_booking_application.databinding.FragmentUserHomeBinding
import com.example.fyp_booking_application.frontend.adapter.UserCoachAdapter
import com.example.fyp_booking_application.frontend.adapter.UserProductAdapter
import com.example.fyp_booking_application.frontend.data.ProductData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class UserHomeFragment : Fragment() {
    private lateinit var binding: FragmentUserHomeBinding
    private lateinit var auth: FirebaseAuth //get the shared instance of the FirebaseAuth object
    private lateinit var fstore: FirebaseFirestore //get the shared instance of the FirebaseAuth object
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private lateinit var productAdapter: UserProductAdapter
    private lateinit var userProDataArrayList: ArrayList<ProductData>
    private lateinit var userProRecView: RecyclerView
    //private lateinit var gridView: GridView

    var inflater: LayoutInflater? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Variables Declaration
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_home, container, false)
        val userView = (activity as UserDashboardActivity)
        userView.setTitle("Home")

        //Initialise
        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        eventChangeListener()
        userProRecView = binding.userProductRecyclerView
        //set layout manager to position the items
        userProRecView.layoutManager = LinearLayoutManager(context)
        val linearLayoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        userProRecView.setLayoutManager(linearLayoutManager)
//        userProRecView.isHorizontalScrollBarEnabled
        userProRecView.setHasFixedSize(true)
        //Set a array list data
        userProDataArrayList = arrayListOf()
        //create adapter passing in the array adapter data
        productAdapter = UserProductAdapter(userProDataArrayList)
        //Attach the adapter to the recyclerView to populate the items
        userProRecView.adapter = productAdapter


//        eventChangeListener()
//        productArrayList = arrayListOf()
//        gridView = binding.gridView
//        gridView.adapter = context?.let { UserProductAdapter(it, productArrayList) }
//        //gridView.adapter = productArrayList = arrayListOf()
//        //productAdapter = ProductAdapter(    productArrayList, this@UserHomeFragment

        return binding.root
    }

    private fun eventChangeListener() {
        fstore = FirebaseFirestore.getInstance()

        fstore.collection("Products")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("FAILED INITIALIZATION", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            userProDataArrayList.add(dc.document.toObject(ProductData::class.java))
                        }
                    }
                    productAdapter.notifyDataSetChanged()
                }
            })
    }
}
