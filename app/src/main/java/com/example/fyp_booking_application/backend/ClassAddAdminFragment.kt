package com.example.fyp_booking_application.backend

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.fyp_booking_application.AdminDashboardActivity
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentClassAddBinding
import com.google.firebase.firestore.FirebaseFirestore

class ClassAddAdminFragment : Fragment() {

    private lateinit var binding: FragmentClassAddBinding
    private lateinit var databaseRef: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Variable Declarations
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_class_add, container, false)
        val adminActivityView = (activity as AdminDashboardActivity)
        databaseRef = FirebaseFirestore.getInstance()

        // Button to Add New Class
        binding.btnFinishAddClass.setOnClickListener{
            val newClassRef = databaseRef.collection("class_testing1").document()
            val newClass = hashMapOf(
                "classID" to newClassRef.id,
//                "className" to binding.tfAddClassName.text,
//                "classDesc" to binding.tfAddClassName.text,
//                "classPrice" to binding.tfAddClassName.text,
//                "classDate" to binding.tfAddClassName.text,
//                "classTime" to binding.tfAddClassName.text
                "className" to "Class A",
                "classDesc" to "Test DescA",
                "classPrice" to 30,
                "classDate" to "8/12/2022",
                "classTime" to "12:00 - 14:00",
            )
            newClassRef.set(newClass)
                .addOnSuccessListener {
                    Log.d("ADD NEW CLASS", "CLASS ADDED SUCCESSFULLY")
                    adminActivityView.replaceFragment(ClassAdminFragment())
                }
                .addOnFailureListener { e -> Log.e("ADDING NEW CLASS", "ERROR ADDING NEW CLASS", e)}
        }

        return binding.root
    }
}