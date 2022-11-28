package com.example.fyp_booking_application.backend

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.example.fyp_booking_application.AdminDashboardActivity
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentAddProductBinding
import com.google.firebase.firestore.FirebaseFirestore
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
        val adminActivityView = (activity as AdminDashboardActivity)
        val categoryType = arrayOf("Racket", "Accessories", "Etc.")
        val spinnerAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, categoryType)
        binding.spinnerCat.adapter = spinnerAdapter
        binding.spinnerCat.setSelection(0)

        // Selecting Image
        binding.imgProduct.setOnClickListener() {
            val selectImage = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(selectImage, 3)
        }

        // Add Product into Database
        binding.btnFinish.setOnClickListener() {
            val productName: String = binding.tfProductName.text.toString()
            val productImage = "images/products/product_$productName"
            var productCategory = ""
            val productDesc: String = binding.tfProductDesc.text.toString()
            val productPrice: Double = binding.tfProductPrice.text.toString().toDouble()
            val productQty: Int = Integer.parseInt(binding.tfProductQty.text.toString())

            when(binding.spinnerCat.selectedItemPosition){
                0 -> productCategory = "Racket"
                1 -> productCategory = "Accessories"
                2 -> productCategory = "Etc."
            }

            storageRef = FirebaseStorage.getInstance().getReference("images/products/product_$productName")
            storageRef.putFile(imgUri)
                .addOnSuccessListener() {
                    binding.imgProduct.setImageURI(null)
                }

            val newProductRef = firestoreRef.collection("products").document()
            val newProduct = hashMapOf(
                "product_id" to newProductRef.id,
                "product_name" to productName,
                "product_image" to productImage,
                "product_category" to productCategory,
                "product_desc" to productDesc,
                "product_price" to productPrice,
                "product_qty" to productQty
            )

            newProductRef.set(newProduct)
                .addOnSuccessListener { Log.d(TAG, "Document Successfully Added!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error Adding Document", e) }

            adminActivityView.replaceFragment(productAdminFragment())
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