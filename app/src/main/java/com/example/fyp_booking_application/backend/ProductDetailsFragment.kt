package com.example.fyp_booking_application.backend

import android.app.AlertDialog
import android.content.ContentValues.TAG
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
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResultListener
import com.example.fyp_booking_application.AdminDashboardActivity
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentProductDetailsBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class ProductDetailsFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailsBinding
    private lateinit var databaseRef: FirebaseFirestore
    private lateinit var storageRef: StorageReference
    private lateinit var imgUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Variables Declaration
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_details, container, false)
        databaseRef = FirebaseFirestore.getInstance()

        setFragmentResultListener("toProductDetails") { _, bundle ->
            // Private Variables
            val productID = bundle.getString("toProductDetails")
            val docRef = databaseRef.collection("products").document(productID.toString())
            storageRef = FirebaseStorage.getInstance().reference

            // View Current Product Details
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val currentPhoto = storageRef.child("products/product_" + document["product_name"])
                        val file = File.createTempFile("temp", "png")

                        binding.tfProductDetailName.text = document["product_name"].toString()
                        currentPhoto.getFile(file).addOnSuccessListener {
                            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                            binding.imgViewProductDetail.setImageBitmap(bitmap)
                        }
                        binding.tfProductDetailCate.text = document["product_category"].toString()
                        binding.tfProductDetailDesc.text = document["product_desc"].toString()
                        binding.tfProductDetailPrice.text = document["product_price"].toString()
                        binding.tfProductDetailQty.text = document["product_qty"].toString()
                    } else {
                        Log.d(TAG, "Invalid Document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "FAILED with", exception)
                }

            // EDIT FUNCTIONS
            binding.tfProductDetailName.setOnClickListener {
                dialogTextView("Update Product Name", binding.tfProductDetailName)
            }
            binding.tfProductDetailDesc.setOnClickListener {
                dialogTextView("Update Product Description", binding.tfProductDetailDesc)
            }
            binding.tfProductDetailPrice.setOnClickListener {
                dialogTextView("Update Product Price", binding.tfProductDetailPrice)
            }
            binding.tfProductDetailQty.setOnClickListener {
                dialogTextView("Update Product Quantity", binding.tfProductDetailQty)
            }
            binding.tfProductDetailCate.setOnClickListener {
                dialogSpinner(binding.tfProductDetailCate)
            }
            binding.imgViewProductDetail.setOnClickListener {
                val selectImage = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(selectImage, 3)

            }

            // Confirm Button for Updating Item
            binding.imgbtnEdit.setOnClickListener {
                docRef.get().addOnSuccessListener { document ->
                    if (document != null) {
                        storageRef = FirebaseStorage.getInstance().getReference("products/product_" + binding.tfProductDetailName.text.toString())
                        storageRef.putFile(imgUri).addOnSuccessListener {
                            binding.imgViewProductDetail.setImageURI(null)
                        }
                    } else {
                        Log.d(TAG, "Invalid Document")
                    }
                }

                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Update Product Data")
                builder.setMessage("Confirm to update product data?")
                builder.setPositiveButton("Ok") { _, _ ->
                    val newProduct = hashMapOf(
                        "product_image" to ("images/products/product_" + binding.tfProductDetailName.text.toString()),
                        "product_name" to binding.tfProductDetailName.text.toString(),
                        "product_category" to binding.tfProductDetailCate.text.toString(),
                        "product_desc" to binding.tfProductDetailDesc.text.toString(),
                        "product_price" to (binding.tfProductDetailPrice.text.toString()).toDouble(),
                        "product_qty" to Integer.parseInt(binding.tfProductDetailQty.text.toString())
                    )


                    databaseRef.collection("products").document(productID.toString())
                        .set(newProduct, SetOptions.merge())
                }
                builder.setNegativeButton("Cancel") { _, _ -> }
                builder.show()
            }

            // Confirm Button for Delete Item
            binding.imgbtnDelete.setOnClickListener {
                dialogDelete(productID.toString())
            }
        }
        return binding.root
    }

    // Insert New Data Function (w/ TextView)
    private fun dialogTextView(title: String, testingTextView: TextView) {

        val dialogLayout = layoutInflater.inflate(R.layout.dialog_edittext1, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.dialog_editText)
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setView(dialogLayout)
        builder.setPositiveButton("Ok") { _, _ ->
            testingTextView.text = editText.text
        }
        builder.setNegativeButton("Cancel") { _, _ -> }
        builder.show()
    }

    // Insert New Data Function (w/ Spinner)
    private fun dialogSpinner(testingTextView: TextView) {

        val dialogLayout = layoutInflater.inflate(R.layout.dialog_spinner, null)
        val spinner = dialogLayout.findViewById<Spinner>(R.id.dialog_spinner)
        val categoryType = arrayOf("Racket", "Accessories", "Etc.")
        val spinnerAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, categoryType)
        spinner.adapter = spinnerAdapter
        spinner.setSelection(0)
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Updating Product Category")
        builder.setView(dialogLayout)
        builder.setPositiveButton("Ok") { _, _ ->
            var productCategory = "TESTING123"
            when(spinner.selectedItemPosition){
                0 -> productCategory = "Racket"
                1 -> productCategory = "Accessories"
                2 -> productCategory = "Etc."
            }
            testingTextView.text = productCategory
        }
        builder.setNegativeButton("Cancel") { _, _ -> }
        builder.show()
    }

    // Delete Item Function
    private fun dialogDelete(productID: String) {
        val adminActivityView = (activity as AdminDashboardActivity)
        val docRef = databaseRef.collection("products").document(productID)
        storageRef = FirebaseStorage.getInstance().reference

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Deleting Data")
        builder.setMessage("Delete Product Confirmation?")
        builder.setPositiveButton("Ok") { _, _ ->
            docRef.get()
                .addOnSuccessListener { document ->
                    val imagePath = document["product_image"].toString()
                    storageRef.child(imagePath).delete()
                        .addOnSuccessListener {
                            Log.d(TAG, "DELETE SUCCESSFULLY")
                        }
                        .addOnFailureListener { e ->
                            Log.d(TAG, "ERROR FOUND NOT DELETED", e)
                        }
                }
            docRef.delete()
                .addOnSuccessListener {
                    Log.d(TAG, "Document Deleted Successfully")
                    adminActivityView.replaceFragment(ProductAdminFragment())
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error Deleting Document", e)
                }

        }
        builder.setNegativeButton("Cancel") { _, _ -> }
        builder.show()
    }

    // Load Image into ImageView
    // *Issue with Updating Data !MUST SELECT PICTURE OF NOT imgUri = NULL)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 3 && data != null && data.data != null) {
            imgUri = data.data!!
            binding.imgViewProductDetail.setImageURI(imgUri)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}