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
import android.widget.Spinner
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResultListener
import com.example.fyp_booking_application.AdminDashboardActivity
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
        val adminActivityView = (activity as AdminDashboardActivity)
        adminActivityView.setTitle("PRODUCT DETAIL")

        setFragmentResultListener("toProductDetails") { _, bundle ->
            val productID = bundle.getString("toProductDetails")
            val docRef = databaseRef.collection("Products").document(productID.toString())
            storageRef = FirebaseStorage.getInstance().reference
            val categoryType = arrayOf("Racket", "Accessories", "Etc")
            val spinnerAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, categoryType)
            binding.tfProductDetailCate.isEnabled = false
            binding.tfProductDetailCate.adapter = spinnerAdapter

            docRef.get().addOnSuccessListener { document ->
                if (document != null) {
                    val product = document.toObject(ProductData::class.java)
                    val prevProductImgPath = product?.productImage.toString() // used to delete previous image
                    var productCategory = product?.productCategory.toString()

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
                    binding.tfProductDetailCate.setSelection(getIndex(binding.tfProductDetailCate, productCategory))
                    binding.tfProductDetailDesc.setText(product?.productDesc.toString())
                    binding.tfProductDetailPrice.setText(product?.productPrice.toString())
                    binding.tfProductDetailQty.setText(product?.productQty.toString())

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

                        when(binding.tfProductDetailCate.selectedItemPosition){
                            0 -> productCategory = "Racket"
                            1 -> productCategory = "Accessories"
                            2 -> productCategory = "Etc"
                        }

                        val builder = AlertDialog.Builder(requireContext())
                        builder.setTitle("Update Product Details")
                        builder.setMessage("Confirm to update product details?")
                        builder.setPositiveButton("Update") { _, _ ->
                            if(binding.tfProductDetailName.text != null &&  binding.tfProductDetailDesc.text != null
                                && binding.tfProductDetailPrice.text != null && binding.tfProductDetailQty.text != null){
                                val updateProduct = hashMapOf(
                                    "productImage" to "products/product${binding.tfProductDetailName.text}",
                                    "productName" to binding.tfProductDetailName.text.toString(),
                                    "productCategory" to productCategory,
                                    "productDesc" to binding.tfProductDetailDesc.text.toString(),
                                    "productPrice" to binding.tfProductDetailPrice.text.toString().toDouble(),
                                    "productQty" to binding.tfProductDetailQty.text.toString().toInt()
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
                        adminActivityView.replaceFragment(AdminProductFragment(), R.id.adminLayout)
                    }.addOnFailureListener { e ->
                        Log.e("DELETING PRODUCT", "ERROR DELETING PRODUCT", e)
                    }
                }
                builder.setNegativeButton("Cancel") { _, _ -> }
                builder.show()
            }
        }
        binding.tvBackProductDetail.setOnClickListener{
            adminActivityView.replaceFragment(AdminProductFragment(), R.id.adminLayout)
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

    private fun getIndex(spinner: Spinner, category: String): Int {
        for (i in 0..spinner.count){
            if(spinner.getItemAtPosition(i).toString() == category)
                return i
        }
        return 0
    }
}