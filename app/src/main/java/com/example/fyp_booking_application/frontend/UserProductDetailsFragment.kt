package com.example.fyp_booking_application.frontend

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResultListener
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.UserDashboardActivity
import com.example.fyp_booking_application.backend.ProductData
import com.example.fyp_booking_application.databinding.FragmentCheckoutBinding
import com.example.fyp_booking_application.databinding.FragmentUserProductDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class UserProductDetailsFragment : Fragment() {
    private lateinit var binding: FragmentUserProductDetailsBinding
    private lateinit var auth: FirebaseAuth //get the shared instance of the FirebaseAuth object
    private lateinit var fstore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_product_details, container, false)
        val userView = (activity as UserDashboardActivity)
        userView.setTitle("Product Details")

        //Initialise
        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference
        val userID = auth.currentUser?.uid

        //Navigate to Home Page
        binding.btnBack.setOnClickListener() {
            userView.replaceFragment(UserHomeFragment())
        }

        //Number quantity picker function
        val numberPicker = binding.tvProQtyPicker
        numberPicker.minValue = 0
        numberPicker.maxValue = 10
        numberPicker.wrapSelectorWheel = true

        //Retrieve Product Data and Display Product Details
        setFragmentResultListener("toUserProductDetail") { _, bundle ->
            val productID = bundle.getString("toUserProductDetail")
            Log.d("haha", productID.toString())

            val retrieveProDetailRef = fstore.collection("Products").document(productID.toString())
            retrieveProDetailRef.get().addOnCompleteListener { resultData ->
                if (resultData != null) {
                    val nameResult = resultData.result.getString("productName").toString()
                    val imageResult = resultData.result.getString("productImage").toString()
                    val descResult = resultData.result.getString("productDesc").toString()
                    val priceResult = resultData.result.getDouble("productPrice").toString()

                    val currentProImg = storageRef.child(imageResult)
                    val file = File.createTempFile("temp", "png")
                    currentProImg.getFile(file).addOnSuccessListener {
                        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                        binding.proImage.setImageBitmap(bitmap)
                    }

                    binding.proName.setText(nameResult)
                    binding.proDesc.setText(descResult)
                    binding.proPrice.setText(priceResult)

                } else {
                    Log.d("noexits", "No such documents.")
                }
                Log.d("haha", retrieveProDetailRef.toString())
            }.addOnFailureListener { exception ->
                Log.d("noexits", "Error getting documents.", exception)
            }

            //Purchase Function
            binding.btnPurchase.setOnClickListener() {

                var purchaseName: String = binding.proName.text.toString()
                //var purchaseImg: String = binding.proImage.toString()
                //var purchaseDesc: String = binding.proDesc.text.toString()
                var purchasePrice: Double = binding.proPrice.text.toString().toDouble()
                var purchaseQty: Int = numberPicker.value
                val userID = auth.currentUser?.uid
                val purchaseId = fstore.collection("Purchases").document()
                val purchase = hashMapOf(
                    "purchaseID" to purchaseId.id,
                    "purchaseName" to purchaseName,
                    // "purchaseImage" to purchaseImg,
                    // "purchaseDesc" to purchaseDesc,
                    "purchasePrice" to purchasePrice,
                    "purchaseQty" to purchaseQty,
                    "purchaseStatus" to "Success",
                    "productID" to productID,
                    "userID" to userID
                )

                // Add a new document with a generated ID
                purchaseId.set(purchase).addOnSuccessListener {
                    Toast.makeText(activity, "Purchase Successfully", Toast.LENGTH_SHORT)
                        .show()
                    Log.d("haha", purchaseId.toString())
                    //   userView.replaceFragment(CoachFragment())  //receipt
                }.addOnFailureListener {
                    Toast.makeText(activity, "Purchase Failure", Toast.LENGTH_SHORT)
                        .show()
                    userView.replaceFragment(UserHomeFragment())
                }
            }
        }
        return binding.root
    }
}

