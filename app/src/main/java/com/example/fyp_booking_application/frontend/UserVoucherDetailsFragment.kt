package com.example.fyp_booking_application.frontend

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResultListener
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.UserDashboardActivity
import com.example.fyp_booking_application.databinding.FragmentUserVoucherDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class UserVoucherDetailsFragment : Fragment() {
    private lateinit var binding: FragmentUserVoucherDetailsBinding
    private lateinit var auth: FirebaseAuth //get the shared instance of the FirebaseAuth object
    private lateinit var fstore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference

    @SuppressLint("NewApi")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_voucher_details, container, false)
        val userView = (activity as UserDashboardActivity)
        userView.setTitle("Voucher Details")

        //Initialise
        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference
        val userID = auth.currentUser?.uid

        //Navigate to Home Page
        binding.btnBack.setOnClickListener() {
            userView.replaceFragment(UserVoucherFragment())
        }

        //Retrieve Voucher Data and Display Voucher Details
        setFragmentResultListener("toUserVoucherDetail") { _, bundle ->
            val voucherID = bundle.getString("toUserVoucherDetail")
            val retrieveVouDetailRef = fstore.collection("Vouchers").document(voucherID.toString())
            retrieveVouDetailRef.get().addOnCompleteListener { resultData ->
                if (resultData != null) {
                    val imageResult = resultData.result.getString("voucherImage").toString()
                    val titleResult = resultData.result.getString("voucherTitle").toString()
                    val msgResult = resultData.result.getString("voucherMessage").toString()
                    val pointResult = resultData.result.getDouble("pointsRequired").toString()
                    val codeResult = resultData.result.getString("voucherCode").toString()
                    val discountResult = resultData.result.getDouble("voucherDiscount").toString()

                    val currentProImg = storageRef.child(imageResult)
                    val file = File.createTempFile("temp", "png")
                    currentProImg.getFile(file).addOnSuccessListener {
                        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                        binding.vouImage.setImageBitmap(bitmap)
                    }

                    binding.vouTitle.setText(titleResult)
                    binding.vouMsg.setText(msgResult)
                    binding.vouPoint.setText(pointResult)
                    binding.vouCode.setText(codeResult)
                    binding.vouDiscount.setText(discountResult)

                } else {
                    Log.d("noexits", "No such documents.")
                }
                Log.d("haha", retrieveVouDetailRef.toString())
            }.addOnFailureListener { exception ->
                Log.d("noexits", "Error getting documents.", exception)
            }
        }
        return binding.root
    }
}

