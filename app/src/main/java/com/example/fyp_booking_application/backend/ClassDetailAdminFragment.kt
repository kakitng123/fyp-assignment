package com.example.fyp_booking_application.backend

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResultListener
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentClassDetailAdminBinding
import com.google.firebase.firestore.FirebaseFirestore

class ClassDetailAdminFragment : Fragment() {

    private lateinit var binding: FragmentClassDetailAdminBinding
    private lateinit var databaseRef: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Variable Declarations
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_class_detail_admin, container, false)

        setFragmentResultListener("toClassDetails") { _, bundle ->
            val className = bundle.getString("toClassDetails")
            databaseRef = FirebaseFirestore.getInstance()
            val docRef = databaseRef.collection("TrainingClass").whereEqualTo("trainingClassName", className)
            docRef.get()
                .addOnSuccessListener { document ->
                    val testing = document.toObjects(ClassData::class.java)
                    binding.tfClassDate.text = testing[0].trainingClassDate
                    binding.tfClassName.text = testing[0].trainingClassName
                    binding.tfClassPrice.text = testing[0].trainingClassPrice.toString()
                    binding.tfClassTime.text = testing[0].trainingClassTime
                }
                .addOnFailureListener { e ->
                    Log.e("FETCHING DOCUMENT", "INVALID DOCUMENT", e)
                }
        }

        return binding.root
    }
}