package com.example.fyp_booking_application.frontend

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResultListener
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.UserDashboardActivity
import com.example.fyp_booking_application.databinding.FragmentUserProductDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.type.Date
import com.google.type.DateTime
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UserProductDetailsFragment : Fragment() {
    private lateinit var binding: FragmentUserProductDetailsBinding
    private lateinit var auth: FirebaseAuth //get the shared instance of the FirebaseAuth object
    private lateinit var fstore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference

    @SuppressLint("NewApi")
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

        //Retrieve Product Data and Display Product Details
        setFragmentResultListener("toUserProductDetail") { _, bundle ->
            val productID = bundle.getString("toUserProductDetail")
            val numberPicker = binding.tvProQtyPicker
            Log.d("haha", productID.toString())

            val retrieveProDetailRef = fstore.collection("Products").document(productID.toString())
            retrieveProDetailRef.get().addOnCompleteListener { resultData ->
                if (resultData != null) {
                    val nameResult = resultData.result.getString("productName").toString()
                    val imageResult = resultData.result.getString("productImage").toString()
                    val descResult = resultData.result.getString("productDesc").toString()
                    val priceResult = resultData.result.getDouble("productPrice").toString()
                    val qtyResult = resultData.result.get("productQty").toString().toInt()

                    if (qtyResult == 0){
                        binding.btnPurchase.isEnabled = false
                        binding.btnPurchase.text = "OUT OF STOCK"
                    }

                    //Number quantity picker function
                    numberPicker.minValue = 0
                    numberPicker.maxValue = qtyResult
                    numberPicker.wrapSelectorWheel = true

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
                val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

                var purchaseName: String = binding.proName.text.toString()
                //var purchaseImg: String = binding.proImage.toString()
                //var purchaseDesc: String = binding.proDesc.text.toString()
                val purchasePrice: Double = binding.proPrice.text.toString().toDouble()
                val purchaseQty: Int = numberPicker.value

                val totalAmount: Double = purchasePrice * purchaseQty

                val userID = auth.currentUser?.uid
                val purchaseId = fstore.collection("Purchases").document()
                val purchase = hashMapOf(
                    "purchaseID" to purchaseId.id,
                    "purchaseName" to purchaseName,
                    // "purchaseImage" to purchaseImg,
                    // "purchaseDesc" to purchaseDesc,
                    // Add Date / Time into this part
                    "purchasePrice" to totalAmount,
                    "purchaseQty" to purchaseQty,
                    "purchaseStatus" to "Success",
                    "productID" to productID,
                    "purchaseDate" to LocalDateTime.now().format(dateFormatter),
                    "purchaseTime" to LocalDateTime.now().format(timeFormatter),
                    "userID" to userID
                )

                // Add a new document with a generated ID
                purchaseId.set(purchase).addOnSuccessListener {
                    Toast.makeText(activity, "Purchase Successfully", Toast.LENGTH_SHORT).show()
                    Log.d("haha", purchaseId.toString())
                    //   userView.replaceFragment(CoachFragment())  //receipt
                }.addOnFailureListener {
                    Toast.makeText(activity, "Purchase Failure", Toast.LENGTH_SHORT)
                        .show()
                    userView.replaceFragment(UserHomeFragment())
                }

                // Need to deduct stock from productsQty
                val testing123 = numberPicker.maxValue - numberPicker.value
                val productRef = fstore.collection("Products").document(productID.toString())
                val updateProductQty = hashMapOf(
                    "productQty" to testing123
                )
                productRef.set(updateProductQty, SetOptions.merge()).addOnSuccessListener {
                    Toast.makeText(activity, "Purchase Successfully", Toast.LENGTH_SHORT).show()
                    Log.d("haha", purchaseId.toString())
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

