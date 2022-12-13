package com.example.fyp_booking_application.backend

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
import com.example.fyp_booking_application.databinding.FragmentAdminProductAddBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AdminProductAddFragment : Fragment() {

    private lateinit var binding: FragmentAdminProductAddBinding
    private lateinit var storageRef: StorageReference
    private lateinit var databaseRef: FirebaseFirestore
    private lateinit var imgUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_product_add, container, false)
        databaseRef = FirebaseFirestore.getInstance()
        val adminActivityView = (activity as AdminDashboardActivity)

        val categoryType = arrayOf("Racket", "Accessories", "Etc")
        val spinnerAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, categoryType)
        binding.spinnerCat.adapter = spinnerAdapter
        binding.spinnerCat.setSelection(0)

        binding.imgProduct.setOnClickListener {
            val selectImage = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(selectImage, 3)
        }

        binding.btnFinish.setOnClickListener {
            val productName: String = binding.tfProductName.text.toString()
            val productImage = "products/product$productName"
            var productCategory = ""
            val productDesc: String = binding.tfProductDesc.text.toString()
            val productPrice: Double = binding.tfProductPrice.text.toString().toDouble()
            val productQty: Int = Integer.parseInt(binding.tfProductQty.text.toString())

            when(binding.spinnerCat.selectedItemPosition){
                0 -> productCategory = "Racket"
                1 -> productCategory = "Accessories"
                2 -> productCategory = "Etc"
            }

            storageRef = FirebaseStorage.getInstance().getReference("products/product$productName")
            storageRef.putFile(imgUri)
                .addOnSuccessListener {
                    binding.imgProduct.setImageURI(null)
                }

            val newProductRef = databaseRef.collection("Products").document()
            val newProduct = hashMapOf(
                "productID" to newProductRef.id,
                "productName" to productName,
                "productImage" to productImage,
                "productCategory" to productCategory,
                "productDesc" to productDesc,
                "productPrice" to productPrice,
                "productQty" to productQty
            )

            newProductRef.set(newProduct)
                .addOnSuccessListener { Log.d("ADDING PRODUCT", "PRODUCT SUCCESSFULLY ADDED") }
                .addOnFailureListener { e -> Log.w("ADDING PRODUCT", "ERROR ADDING PRODUCT", e) }

            adminActivityView.replaceFragment(AdminProductFragment(), R.id.adminLayout)
        }
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 3 && data != null && data.data != null) {
            imgUri = data.data!!
            binding.imgProduct.setImageURI(imgUri)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}