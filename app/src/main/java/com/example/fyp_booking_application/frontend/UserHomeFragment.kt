package com.example.fyp_booking_application.frontend

import android.content.Context
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
import com.example.fyp_booking_application.backend.productAdapter
import com.example.fyp_booking_application.backend.productData
import com.example.fyp_booking_application.databinding.FragmentUserHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class UserHomeFragment : Fragment() {
//    private lateinit var binding: FragmentUserHomeBinding
//    private lateinit var auth: FirebaseAuth //get the shared instance of the FirebaseAuth object
//    private lateinit var fstore: FirebaseFirestore //get the shared instance of the FirebaseAuth object
//    private lateinit var storage: FirebaseStorage
//    private lateinit var storageRef: StorageReference
//    private lateinit var productAdapter: productAdapter
//    private lateinit var productDataArrayList: ArrayList<productData>
//    private lateinit var gridView: GridView
//
//    private lateinit var items: Array<String>
//    var inflater: LayoutInflater? = null
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Variables Declaration
//        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_home, container, false)
//        val userView = (activity as UserDashboardActivity)
//
//        //Initialise
//        auth = FirebaseAuth.getInstance()
//        fstore = FirebaseFirestore.getInstance()
//        storage = FirebaseStorage.getInstance()
//        storageRef = storage.reference
//
//        gridView = binding.gridView
//        //gridView.adapter = CustomGridAdapter(Context context, String[] items)
//        //gridView.adapter = CustomGridAdapter(activity, items)
//      //  gridView.setAdapter(CustomGridAdapter(this, items))
//        productDataArrayList = arrayListOf()
//       // productAdapter = productAdapter(productDataArrayList, this@UserHomeFragment)
//        eventChangeListener()
//
//        return binding.root
//    }
//
//    private fun eventChangeListener() {
//        fstore = FirebaseFirestore.getInstance()
//        storage = FirebaseStorage.getInstance()
//        storageRef = storage.reference
//
//        fstore.collection("CoachProfile")
//            .addSnapshotListener(object : EventListener<QuerySnapshot> {
//                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
//                    if (error != null) {
//                        Log.e("Failed", error.message.toString())
//                        return
//                    }
//                    for (dc: DocumentChange in value?.documentChanges!!) {
//                        if (dc.type == DocumentChange.Type.ADDED) {
//                            productDataArrayList.add(dc.document.toObject(productData::class.java))
//                        }
//                    }
//                    productAdapter.notifyDataSetChanged()
//                }
//            })
//    }
////    private var context: Context? = null
////    private var items: Array<String>
////    var inflater: LayoutInflater? = null
////
////    private fun CustomGridAdapter(context: Context, items: Array<String?>) {
////        this.context = context
////        this.items = items
////        inflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
////    }
//}
//
////
////button.setOnClickListener(new View.OnClickListener() {
////
////    @Override
////    public void onClick(View v) {
////        if(context instanceof MainActivity) {
////            ((MainActivity) context).itemClicked(position);
////        }
////    }
////});
}