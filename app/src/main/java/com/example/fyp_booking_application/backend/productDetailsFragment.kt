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

class productDetailsFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailsBinding
    private lateinit var firestoreRef: FirebaseFirestore
    private lateinit var storageRef: StorageReference
    private lateinit var imgUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Variables Declaration
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_details, container, false)
        firestoreRef = FirebaseFirestore.getInstance()

        setFragmentResultListener("toProductDetails") { toProductDetails, bundle ->
            // Private Variables
            val productid = bundle.getString("toProductDetails")
            val docRef = firestoreRef.collection("products").document(productid.toString())
            storageRef = FirebaseStorage.getInstance().getReference()

            // View Current Product Details
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val currentPhoto = storageRef.child("images/products/product_" + document["product_name"])
                        val file = File.createTempFile("temp", "png")

                        binding.tfProductDetailName.text = document["product_name"].toString()
                        currentPhoto.getFile(file).addOnSuccessListener() {
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
            binding.tfProductDetailName.setOnClickListener() {
                dialogTextView("Update Product Name", binding.tfProductDetailName)
            }
            binding.tfProductDetailDesc.setOnClickListener() {
                dialogTextView("Update Product Description", binding.tfProductDetailDesc)
            }
            binding.tfProductDetailPrice.setOnClickListener() {
                dialogTextView("Update Product Price", binding.tfProductDetailPrice)
            }
            binding.tfProductDetailQty.setOnClickListener() {
                dialogTextView("Update Product Quantity", binding.tfProductDetailQty)
            }
            binding.tfProductDetailCate.setOnClickListener() {
                dialogSpinner("Update Product Category", binding.tfProductDetailCate)
            }
            binding.imgViewProductDetail.setOnClickListener() {
                val selectImage = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(selectImage, 3)

            }

            // Confirm Button for Updating Item
            binding.imgbtnEdit.setOnClickListener() {
                docRef.get().addOnSuccessListener { document ->
                    if (document != null) {
                        storageRef = FirebaseStorage.getInstance().getReference("images/products/product_" + binding.tfProductDetailName.text.toString())
                        storageRef.putFile(imgUri).addOnSuccessListener() {
                            binding.imgViewProductDetail.setImageURI(null)
                        }
                    } else {
                        Log.d(TAG, "Invalid Document")
                    }
                }

                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Update Product Data")
                builder.setMessage("Confirm to update product data?")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                    val newProduct = hashMapOf(
                        "product_image" to ("images/products/product_" + binding.tfProductDetailName.text.toString()),
                        "product_name" to binding.tfProductDetailName.text.toString(),
                        "product_category" to binding.tfProductDetailCate.text.toString(),
                        "product_desc" to binding.tfProductDetailDesc.text.toString(),
                        "product_price" to (binding.tfProductDetailPrice.text.toString()).toDouble(),
                        "product_qty" to Integer.parseInt(binding.tfProductDetailQty.text.toString())
                    )


                    firestoreRef.collection("products").document(productid.toString())
                        .set(newProduct, SetOptions.merge())
                }
                builder.setNegativeButton("Cancel") { dialogInterface, which -> }
                builder.show()
            }

            // Confirm Button for Delete Item
            binding.imgbtnDelete.setOnClickListener() {
                dialogDelete("Deleting Data", productid.toString())
            }
        }
        return binding.root
    }

    // Insert New Data Function (w/ TextView)
    private fun dialogTextView(title: String, testingTextView: TextView) {

        val dialogLayout = layoutInflater.inflate(R.layout.dialog_edittext, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.dialog_editText)
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setView(dialogLayout)
        builder.setPositiveButton("Ok") { dialogInterface, which ->
            testingTextView.text = editText.text
        }
        builder.setNegativeButton("Cancel") { dialogInterface, which -> }
        builder.show()
    }

    // Insert New Data Function (w/ Spinner)
    private fun dialogSpinner(title: String, testingTextView: TextView) {

        val dialogLayout = layoutInflater.inflate(R.layout.dialog_spinner, null)
        val spinner = dialogLayout.findViewById<Spinner>(R.id.dialog_spinner)
        val categoryType = arrayOf("Racket", "Accessories", "Etc.")
        val spinnerAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, categoryType)
        spinner.adapter = spinnerAdapter
        spinner.setSelection(0)
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setView(dialogLayout)
        builder.setPositiveButton("Ok") { dialogInterface, which ->
            var product_category = "TESTING123"
            when(spinner.selectedItemPosition){
                0 -> product_category = "Racket"
                1 -> product_category = "Accessories"
                2 -> product_category = "Etc."
            }
            testingTextView.text = product_category
        }
        builder.setNegativeButton("Cancel") { dialogInterface, which -> }
        builder.show()
    }

    // Delete Item Function
    private fun dialogDelete(title: String, productid: String) {
        val adminactivityview = (activity as AdminDashboardActivity)
        val docRef = firestoreRef.collection("products").document(productid)
        storageRef = FirebaseStorage.getInstance().reference

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setMessage("Delete Product Confirmation?")
        builder.setPositiveButton("Ok") { dialogInterface, which ->
            docRef.get()
                .addOnSuccessListener() { document ->
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
                    adminactivityview.replaceFragment(productFragment())
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error Deleting Document", e)
                }

        }
        builder.setNegativeButton("Cancel") { dialogInterface, which -> }
        builder.show()
    }

    // Load Image into ImageView
    // *Issue with Updating Data !MUST SELECT PICTURE OF NOT IMGURI = NULL)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 3 && data != null && data.getData() != null) {
            imgUri = data.getData()!!;
            binding.imgViewProductDetail.setImageURI(imgUri)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}