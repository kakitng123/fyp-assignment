package com.example.fyp_booking_application.frontend

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.UserDashboardActivity
import com.example.fyp_booking_application.backend.ProductData
import com.example.fyp_booking_application.databinding.FragmentUserHomeBinding
import com.example.fyp_booking_application.frontend.adapter.ProductAdapter
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
    private lateinit var productAdapter: ProductAdapter
    private lateinit var productArrayList: ArrayList<ProductData>
    private lateinit var gridView: GridView

    private lateinit var items: Array<String>
    var inflater: LayoutInflater? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Variables Declaration
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_home, container, false)
        val userView = (activity as UserDashboardActivity)


        //Initialise
        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference


//        testing123()
//        Log.d("TESTING 12", productArrayList.toString())
//
////        Log.d("TESTING 123", productDataArrayList.toString())
////        Log.d("TESTING 1234", context.toString())
//        binding.gridView.apply{
//            Log.d("TESTING 123", productArrayList.toString())
//            Log.d("TESTING 1234", context.toString())
//            productAdapter = ProductAdapter(context, productArrayList)
//            adapter = productAdapter
//        }

//        binding.gridView.adapter = CustomGridAdap
//        binding.gridView.apply{
//            productDataArrayList = arrayListOf()
//
//        }

        eventChangeListener()
        productArrayList = arrayListOf()
        gridView = binding.gridView
        gridView.adapter = context?.let { ProductAdapter(it, productArrayList) }


//         //gridView.adapter = CustomGridAdapter(Context context, String[] items)
//         //gridView.adapter =
//        productArrayList = arrayListOf()
//        productAdapter = ProductAdapter(    productArrayList, this@UserHomeFragment)


        return binding.root
    }


    private fun eventChangeListener() {
        fstore = FirebaseFirestore.getInstance()
        //storage = FirebaseStorage.getInstance()
        //storageRef = storage.reference

        fstore.collection("Products")
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

//    private var context: Context? = null
//    private var items: Array<String>
//    var inflater: LayoutInflater? = null
//



}


//
//button.setOnClickListener(new View.OnClickListener() {
//
//    @Override
//    public void onClick(View v) {
//        if(context instanceof MainActivity) {
//            ((MainActivity) context).itemClicked(position);
//        }
//    }
//});
