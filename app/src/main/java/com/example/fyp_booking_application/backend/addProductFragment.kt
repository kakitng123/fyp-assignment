package com.example.fyp_booking_application.backend

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.fyp_booking_application.AdminActivity
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentAddProductBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class addProductFragment : Fragment() {

    private lateinit var binding: FragmentAddProductBinding
    private lateinit var storageRef: StorageReference
    private lateinit var firestoreRef: FirebaseFirestore
    private lateinit var imgUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Variable Declarations
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_product, container, false)
        firestoreRef = FirebaseFirestore.getInstance()
        val categoryType = arrayOf("Racket", "Accessories", "Etc.")
        val spinnerAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, categoryType)
        binding.spinnerCat.adapter = spinnerAdapter
        binding.spinnerCat.setSelection(0)
        val adminactivityview = (activity as AdminActivity)

        // Selecting Image
        binding.imgProduct.setOnClickListener() {
            val selectImage =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(selectImage, 3)
        }

        // Add Product to Firestore
        binding.btnFinish.setOnClickListener() {
            var product_category = ""
            var product_name: String = binding.tfProductName.text.toString()
            var product_desc: String = binding.tfProductDesc.text.toString()
            var product_price: Double = binding.tfProductPrice.text.toString().toDouble()
            var product_image: String = "images/product/product_" + product_name


            when(binding.spinnerCat.selectedItemPosition){
                0 -> product_category = "Racket"
                1 -> product_category = "Accessories"
                2 -> product_category = "Etc."
            }

            storageRef = FirebaseStorage.getInstance().getReference("images/products/product_$product_name")
            storageRef.putFile(imgUri).addOnSuccessListener() {
                binding.imgProduct.setImageURI(null)
            }

            val newProduct: MutableMap<String, Any> = HashMap()
            newProduct["product_image"] = product_image
            newProduct["product_name"] = product_name
            newProduct["product_category"] = product_category
            newProduct["product_desc"] = product_desc
            newProduct["product_price"] = product_price

            firestoreRef.collection("products").document(product_name).set(newProduct)

            adminactivityview.replaceFragment(adminHomeFragment())


        }
        return binding.root
    }

    // Load Image into ImageView
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 3 && data != null && data.getData() != null) {

            imgUri = data.getData()!!;
            binding.imgProduct.setImageURI(imgUri)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}