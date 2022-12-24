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
import android.widget.Toast
import androidx.core.net.toUri
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
        adminActivityView.setTitle("ADD PRODUCT")

        binding.tfAddProductName.setOnFocusChangeListener { _, focused ->
            if(!focused && binding.tfAddProductName.text!!.isEmpty()){
                binding.pNameContainer.helperText = "Name is Required"
            }
            else if(!focused && !(binding.tfAddProductName.text!!.matches("^[a-zA-Z0-9_]*$".toRegex()))){
                binding.pNameContainer.helperText = "Invalid Name"
            }
            else binding.pNameContainer.helperText = null
        }

        binding.imgProduct.setOnClickListener {
            val selectImage = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(selectImage, 100)
        }

        val categoryType = arrayListOf<String>()
        val categoryRef = databaseRef.collection("SystemSettings").document("category")
        categoryRef.get().addOnSuccessListener { document ->
            if(document != null){
                document.data!!.forEach { fieldName ->
                    categoryType.add(fieldName.value.toString())
                }
                val spinnerAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, categoryType)
                binding.spinnerAddProductCat.adapter = spinnerAdapter
                binding.spinnerAddProductCat.setSelection(0)
            }
        }.addOnFailureListener { e ->
            Log.e("NO CATEGORY", "ERROR FETCHING CATEGORY", e)
        }

        binding.tfAddProductDesc.setOnFocusChangeListener { _, focused ->
            if(!focused && binding.tfAddProductDesc.text!!.isEmpty()){
                binding.pDescContainer.helperText = "Description is Required"
            }
            else binding.pDescContainer.helperText = null
        }

        binding.tfAddProductPrice.setOnFocusChangeListener { _, focused ->
            if(!focused && binding.tfAddProductPrice.text!!.isEmpty()){
                binding.pPriceContainer.helperText = "Price is Required"
            }
            else binding.pPriceContainer.helperText = null
        }

        binding.tfAddProductQty.setOnFocusChangeListener { _, focused ->
            if(!focused && binding.tfAddProductQty.text!!.isEmpty()){
                binding.pQtyContainer.helperText = "Quantity is Required"
            }
            else if(!focused && !(binding.tfAddProductQty.text!!.all { it.isDigit() })){
                binding.pQtyContainer.helperText = "Invalid Quantity"
            }
            else binding.pQtyContainer.helperText = null
        }

        binding.imgBtnAddNewProduct.setOnClickListener {
            val validName = binding.pNameContainer.helperText == null
            val validDesc = binding.pDescContainer.helperText == null
            val validPrice = binding.pPriceContainer.helperText == null
            val validQty = binding.pQtyContainer.helperText == null
            val validImage = binding.imgProduct != null

            if(validName && validDesc && validPrice && validQty) {
                var nameValidation = 0
                databaseRef.collection("Products").get()
                    .addOnSuccessListener { results ->
                        for (document in results) {
                            if (document["productName"] == binding.tfAddProductName.text.toString()) {
                                nameValidation += 1
                            }
                        }
                        if(nameValidation == 0){
                            val productName: String = binding.tfAddProductName.text.toString()
                            storageRef = FirebaseStorage.getInstance().getReference("products/product$productName")
                            storageRef.putFile(imgUri).addOnSuccessListener {
                                binding.imgProduct.setImageURI(null)
                            }
                            val newProductRef = databaseRef.collection("Products").document()
                            val newProduct = hashMapOf(
                                "productID" to newProductRef.id,
                                "productName" to productName,
                                "productImage" to "products/product$productName",
                                "productCategory" to binding.spinnerAddProductCat.selectedItem.toString(),
                                "productDesc" to binding.tfAddProductDesc.text.toString(),
                                "productPrice" to binding.tfAddProductPrice.text.toString().toDouble(),
                                "productQty" to binding.tfAddProductQty.text.toString().toInt()
                            )

                            newProductRef.set(newProduct)
                                .addOnSuccessListener { Log.d("ADDING PRODUCT", "PRODUCT SUCCESSFULLY ADDED") }
                                .addOnFailureListener { e -> Log.w("ADDING PRODUCT", "ERROR ADDING PRODUCT", e) }

                            adminActivityView.replaceFragment(AdminProductFragment(), R.id.adminLayout)
                        }
                    }.addOnFailureListener{ e -> Log.e("FETCHING DOCUMENT", "INVALID DOCUMENT", e) }
            } else {
                Toast.makeText(context, "CHECK INPUT FIELDS", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }
        binding.tvBackAddProduct.setOnClickListener{
            adminActivityView.replaceFragment(AdminProductFragment(), R.id.adminLayout)
        }
        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 100 && data != null && data.data != null) {
            imgUri = data.data!!
            binding.imgProduct.setImageURI(imgUri)
        } else Log.d("NO IMAGE URI", "NO IMAGE HAHA")
        super.onActivityResult(requestCode, resultCode, data)
    }
}