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

        // Add Product to Firestore
        binding.btnFinish.setOnClickListener() {
            val product_name: String = binding.tfProductName.text.toString()
            val product_image = "images/products/product_$product_name"
            var product_category = ""
            val product_desc: String = binding.tfProductDesc.text.toString()
            val product_price: Double = binding.tfProductPrice.text.toString().toDouble()
            val product_qty: Int = Integer.parseInt(binding.tfProductQty.text.toString())

            when(binding.spinnerCat.selectedItemPosition){
                0 -> product_category = "Racket"
                1 -> product_category = "Accessories"
                2 -> product_category = "Etc."
            }

            storageRef = FirebaseStorage.getInstance().getReference("images/products/product_$product_name")
            storageRef.putFile(imgUri)
                .addOnSuccessListener() {
                    binding.imgProduct.setImageURI(null)
                }

            val newProductRef = firestoreRef.collection("products").document()
            val newProduct = hashMapOf(
                "product_id" to newProductRef.id,
                "product_name" to product_name,
                "product_image" to product_image,
                "product_category" to product_category,
                "product_desc" to product_desc,
                "product_price" to product_price,
                "product_qty" to product_qty
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