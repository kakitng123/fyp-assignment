package com.example.fyp_booking_application.backend

import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
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
import androidx.fragment.app.setFragmentResultListener
import com.example.fyp_booking_application.AdminDashboardActivity
import com.example.fyp_booking_application.ProductData
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentAdminProductDetailsBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class AdminProductDetailFragment : Fragment() {

    private lateinit var binding: FragmentAdminProductDetailsBinding
    private lateinit var databaseRef: FirebaseFirestore
    private lateinit var storageRef: StorageReference
    private lateinit var imgUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_product_details, container, false)
        databaseRef = FirebaseFirestore.getInstance()
        val adminView = (activity as AdminDashboardActivity)
        adminView.setTitle("PRODUCT DETAIL")

        setFragmentResultListener("toProductDetails") { _, bundle ->
            val productID = bundle.getString("toProductDetails")
            val docRef = databaseRef.collection("Products").document(productID.toString())
            storageRef = FirebaseStorage.getInstance().reference

            val categoryType = arrayListOf<String>()
            val categoryRef = databaseRef.collection("SystemSettings").document("category")
            categoryRef.get().addOnSuccessListener { document ->
                if(document != null){
                    document.data!!.forEach { fieldName ->
                        categoryType.add(fieldName.value.toString())
                    }
                    val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, categoryType)
                    binding.tfProductDetailCate.isEnabled = false
                    binding.tfProductDetailCate.adapter = spinnerAdapter
                }
            }.addOnFailureListener { e ->
                Log.e("NO CATEGORY", "ERROR FETCHING CATEGORY", e)
            }

            docRef.get().addOnSuccessListener { document ->
                if (document != null) {
                    val product = document.toObject(ProductData::class.java)
                    val prevProductImgPath = product?.productImage.toString() // used to delete previous image
                    val productCategory = product?.productCategory.toString()

                    val currentPhoto = storageRef.child(product?.productImage.toString())
                    val file = File.createTempFile("temp", "png")
                    currentPhoto.getFile(file).addOnSuccessListener {
                        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                        binding.imgViewProductDetail.setImageBitmap(bitmap)
                    }

                    binding.swUpdatePDetail.setOnCheckedChangeListener { _, isChecked ->
                        binding.tfProductDetailName.isEnabled = isChecked
                        binding.tfProductDetailCate.isEnabled = isChecked
                        binding.tfProductDetailDesc.isEnabled = isChecked
                        binding.tfProductDetailPrice.isEnabled = isChecked
                        binding.tfProductDetailQty.isEnabled = isChecked
                    }

                    binding.tfProductDetailName.setText(product?.productName.toString())
                    binding.tfProductDetailDesc.setText(product?.productDesc.toString())
                    binding.tfProductDetailPrice.setText(product?.productPrice.toString())

                    binding.tfProductDetailQty.minValue = 1
                    binding.tfProductDetailQty.maxValue = 100
                    //binding.tfProductDetailQty.wrapSelectorWheel = true
                    binding.tfProductDetailQty.value = product?.productQty!!
                    binding.tfProductDetailCate.setSelection(getIndex(productCategory))

                    binding.tfProductDetailName.setOnFocusChangeListener { _, focused ->
                        if(!focused && binding.tfProductDetailName.text.isEmpty()){
                            binding.tfProductDetailName.error = "Name is Required"
                        }
                        else if(!focused && !(binding.tfProductDetailName.text!!.matches("^[a-zA-Z0-9_]*$".toRegex()))){
                            binding.tfProductDetailName.error = "Invalid Name"
                        }
                        else binding.tfProductDetailName.error = null
                    }

                    binding.tfProductDetailDesc.setOnFocusChangeListener { _, focused ->
                        if(!focused && binding.tfProductDetailDesc.text!!.isEmpty()){
                            binding.tfProductDetailDesc.error = "Description is Required"
                        }
                        else binding.tfProductDetailDesc.error = null
                    }

                    binding.tfProductDetailPrice.setOnFocusChangeListener { _, focused ->
                        if(!focused && binding.tfProductDetailPrice.text!!.isEmpty()){
                            binding.tfProductDetailPrice.error = "Price is Required"
                        }
                        else binding.tfProductDetailPrice.error = null
                    }

                    binding.imgViewProductDetail.setOnClickListener {
                        val selectImage = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        startActivityForResult(selectImage, 3)

                        storageRef.child(prevProductImgPath).delete()
                            .addOnSuccessListener {
                                Log.d("DELETING PROD.IMAGE", "PROD.IMG DELETED SUCCESSFULLY")
                            }
                            .addOnFailureListener { e ->
                                Log.d("DELETING PROD.IMAGE", "ERROR DELETING PROD.IMG", e)
                            }
                    }

                    binding.imgBtnEditPDetail.setOnClickListener {
                        storageRef = FirebaseStorage.getInstance().getReference("products/product${binding.tfProductDetailName.text}")
                        storageRef.putFile(imgUri).addOnSuccessListener {
                            binding.imgViewProductDetail.setImageURI(null)
                        }

                        val builder = AlertDialog.Builder(requireContext())
                        builder.setTitle("Update Product Details")
                        builder.setMessage("Confirm to update product details?")
                        builder.setPositiveButton("Update") { _, _ ->
                            if(binding.tfProductDetailName.text != null &&  binding.tfProductDetailDesc.text != null
                                && binding.tfProductDetailPrice.text != null){
                                val updateProduct = hashMapOf(
                                    "productImage" to "products/product${binding.tfProductDetailName.text}",
                                    "productName" to binding.tfProductDetailName.text.toString(),
                                    "productCategory" to binding.tfProductDetailCate.selectedItem.toString(),
                                    "productDesc" to binding.tfProductDetailDesc.text.toString(),
                                    "productPrice" to binding.tfProductDetailPrice.text.toString().toDouble(),
                                    "productQty" to binding.tfProductDetailQty.value
                                )
                                docRef.set(updateProduct, SetOptions.merge())
                            }
                            else {
                                Toast.makeText(context, "CANNOT HAVE EMPTY FIELD", Toast.LENGTH_SHORT).show()
                                return@setPositiveButton
                            }
                        }
                        builder.setNegativeButton("Cancel") { _, _ -> }
                        builder.show()
                    }
                }
                else {
                    Log.d("FETCHING DOCUMENT", "INVALID DOCUMENT")
                }
            }.addOnFailureListener { e ->
                Log.e("FETCHING DOCUMENT", "INVALID DOCUMENT", e)
            }

            binding.imgBtnDltPDetail.setOnClickListener {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Deleting Data")
                builder.setMessage("Delete Product Confirmation?")
                builder.setPositiveButton("Delete") { _, _ ->
                    docRef.get().addOnSuccessListener { document ->
                        val imagePath = document["product_image"].toString()
                        storageRef.child(imagePath).delete()
                            .addOnSuccessListener {
                                Log.d("DELETING PROD.IMG", "PROD.IMG DELETED SUCCESSFULLY")
                            }
                            .addOnFailureListener { e ->
                                Log.d("DELETING PROD.IMG", "ERROR DELETING PROD.IMG", e)
                            }
                    }.addOnFailureListener { e ->
                        Log.e("FETCHING PRODUCT", "INVALID PRODUCT", e)
                    }
                    docRef.delete().addOnSuccessListener {
                        Log.d("DELETING PRODUCT", "PRODUCT DELETED SUCCESSFULLY")
                        adminView.replaceFragment(AdminProductFragment(), R.id.adminLayout)
                    }.addOnFailureListener { e ->
                        Log.e("DELETING PRODUCT", "ERROR DELETING PRODUCT", e)
                    }
                }
                builder.setNegativeButton("Cancel") { _, _ -> }
                builder.show()
            }
        }
        binding.tvBackProductDetail.setOnClickListener{
            adminView.replaceFragment(AdminProductFragment(), R.id.adminLayout)
        }
        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 3 && data != null && data.data != null) {
            imgUri = data.data!!
            binding.imgViewProductDetail.setImageURI(imgUri)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun getIndex( category: String): Int {
        val number: Int = when(category){
            "Racket" -> 0
            "Accessories" -> 1
            "Etc" -> 2
            else -> Log.d("Error", "Error")
        }
        return number
    }
}