package com.example.fyp_booking_application.frontend

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.fyp_booking_application.AdminDashboardActivity
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.UserDashboardActivity
import com.example.fyp_booking_application.backend.addProductFragment
import com.example.fyp_booking_application.databinding.FragmentProductDetailsBinding
import com.example.fyp_booking_application.databinding.ProductCardBinding
import com.example.fyp_booking_application.databinding.ProductCardDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ProductCardDetailFragment : Fragment() {
    private lateinit var binding: ProductCardDetailsBinding
    private lateinit var auth: FirebaseAuth //get the shared instance of the FirebaseAuth object
    private lateinit var fstore: FirebaseFirestore //get the shared instance of the FirebaseAuth object
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Variables Declaration
        binding = DataBindingUtil.inflate(inflater, R.layout.product_card_details, container, false)
        val userView = (activity as UserDashboardActivity)

        //Initialise
        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference
        val userID = auth.currentUser?.uid

//        // Navigate to Home Page
//        binding.btnBack.setOnClickListener(){
//            userView.replaceFragment(UserHomeFragment)
//        }
//


        return binding.root
    }
}

