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
import com.example.fyp_booking_application.databinding.FragmentAdminClassAddBinding
import com.google.firebase.firestore.FirebaseFirestore

class AdminClassAddFragment : Fragment() {

    private lateinit var binding: FragmentAdminClassAddBinding
    private lateinit var databaseRef: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Variable Declarations
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_class_add, container, false)
        val adminActivityView = (activity as AdminDashboardActivity)
        databaseRef = FirebaseFirestore.getInstance()

        // Button to Add New Class
        binding.btnFinishAddClass.setOnClickListener{
            val newClassRef = databaseRef.collection("class_testing1").document()
            val newClass = hashMapOf(
                "classID" to newClassRef.id,
                "className" to binding.tfAddClassName.text.toString(),
                "classDesc" to binding.tfAddClassDesc.text.toString(),
                "classPrice" to binding.tfAddClassPrice.text.toString().toDouble(),
                "classDate" to binding.tfAddClassDate.text.toString(), // ClassDate can use Calendar method
                "classTime" to binding.tfAddClassTime.text.toString() // ClassTime can use Spinner
            )
            newClassRef.set(newClass)
                .addOnSuccessListener {
                    Log.d("ADD NEW CLASS", "CLASS ADDED SUCCESSFULLY")
                    adminActivityView.replaceFragment(AdminClassFragment(), R.id.adminLayout)
                }
                .addOnFailureListener { e -> Log.e("ADDING NEW CLASS", "ERROR ADDING NEW CLASS", e)}
        }

        return binding.root
    }
}