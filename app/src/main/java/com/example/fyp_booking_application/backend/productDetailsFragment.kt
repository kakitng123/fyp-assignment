package com.example.fyp_booking_application.backend

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResultListener
import com.example.fyp_booking_application.AdminActivity
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentProductDetailsBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class productDetailsFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailsBinding
    private lateinit var firestoreRef: FirebaseFirestore
    private lateinit var storageRef: StorageReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Variables Declaration
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_details, container, false)
        firestoreRef = FirebaseFirestore.getInstance()
        storageRef = FirebaseStorage.getInstance().getReference("images/products/")

        setFragmentResultListener("toProductDetails") { toProductDetails, bundle ->
            // Private Variables
            var productName = bundle.getString("toProductDetails")
            val docRef = firestoreRef.collection("products").document(productName.toString())
            val currentPhoto = storageRef.child("product_$productName")
            val file = File.createTempFile("temp", "png")

            // View Current Product Details
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        binding.tfProductDetailName.text = document["product_name"].toString()
                        currentPhoto.getFile(file).addOnSuccessListener() {
                            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                            binding.imgViewProductDetail.setImageBitmap(bitmap)
                        }
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

            // EDIT FUNCTIONS (TESTING)
            binding.tfProductDetailName.setOnClickListener(){
                _dialogFunc("Update Product Name", binding.tfProductDetailName)
            }
            binding.tfProductDetailDesc.setOnClickListener(){
                _dialogFunc("Update Product Description", binding.tfProductDetailDesc)
            }
            binding.tfProductDetailPrice.setOnClickListener(){
                _dialogFunc("Update Product Price", binding.tfProductDetailPrice)
            }
            binding.tfProductDetailQty.setOnClickListener(){
                _dialogFunc("Update Product Quantity", binding.tfProductDetailQty)
            }
            /* NEED TO COPY PASTA SAME CODE HERE AND UPDATE DATABASE
            binding.imgViewProductDetail.setOnClickListener(){
                Toast.makeText(context, "TESTING", Toast.LENGTH_SHORT).show()
            }
             */

            // Confirming Update / Delete Product
            binding.imgbtnEdit.setOnClickListener(){
                // Need to Update/Edit Fields (TOMORROW DO)
                Toast.makeText(context, "TESTING", Toast.LENGTH_SHORT).show()
            }
            binding.imgbtnDelete.setOnClickListener(){
                _dialogFunc("Deleting Data", productName.toString())
            }

        }
        return binding.root
    }

    private fun _dialogFunc(title: String, testingTextView : TextView){
        val dialogLayout = layoutInflater.inflate(R.layout.dialog_edittext, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.dialog_editText)
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setView(dialogLayout)
        builder.setPositiveButton("Ok"){ dialogInterface, which ->
            testingTextView.text = editText.text
        }
        builder.setNegativeButton("Cancel"){dialogInterface, which -> }
        builder.show()
    }

    private fun _dialogFunc(title:String, productName:String){
        val adminactivityview = (activity as AdminActivity)

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setMessage("Delete Product Confirmation?")
        builder.setPositiveButton("Ok"){ dialogInterface, which ->
            // Need to Delete Picture From Storage
            firestoreRef.collection("products").document(productName).delete()
                .addOnSuccessListener {
                    Log.d(TAG, "Document Successfully Deleted")
                    adminactivityview.replaceFragment(productFragment())
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error Deleting Document", e)
                }
        }
        builder.setNegativeButton("Cancel"){dialogInterface, which -> }
        builder.show()
    }
}