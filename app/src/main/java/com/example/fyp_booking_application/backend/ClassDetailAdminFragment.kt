package com.example.fyp_booking_application.backend

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResultListener
import com.example.fyp_booking_application.AdminDashboardActivity
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentClassDetailAdminBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class ClassDetailAdminFragment : Fragment() {

    private lateinit var binding: FragmentClassDetailAdminBinding
    private lateinit var databaseRef: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Variable Declarations
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_class_detail_admin, container, false)
        val adminActivityView = (activity as AdminDashboardActivity)

        // Get Data from Paired Fragment
        setFragmentResultListener("toClassDetails") { _, bundle ->
            val classID = bundle.getString("toClassDetails")
            databaseRef = FirebaseFirestore.getInstance()
            val docRef = databaseRef.collection("class_testing1").document(classID.toString())

            // Get Document
            docRef.get().addOnSuccessListener { document ->
                if(document != null){
                    val testing = document.toObject(ClassData2::class.java)

                    // To Enable Editable Fields
                    binding.swUpdateClass.setOnCheckedChangeListener(){ _, isChecked ->
                        binding.tfClassName.isEnabled = isChecked
                        binding.tfClassDesc.isEnabled = isChecked
                        binding.tfClassPrice.isEnabled = isChecked
                        binding.tfClassDate.isEnabled = isChecked
                        binding.tfClassTime.isEnabled = isChecked
                    }

                    // Set Text for EditText
                    binding.tfClassID.setText(testing?.classID.toString())
                    binding.tfClassName.setText(testing?.className.toString())
                    binding.tfClassDesc.setText(testing?.classDesc.toString())
                    binding.tfClassPrice.setText(testing?.classPrice.toString())
                    binding.tfClassDate.setText(testing?.classDate.toString())
                    binding.tfClassTime.setText(testing?.classTime.toString())

                    // Confirm Button for Updating Item
                    binding.imgbtnUpdateClass.setOnClickListener(){
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setTitle("Update Class Details")
                        builder.setMessage("Confirm to update class details?")
                        builder.setPositiveButton("Update"){ _, _ ->
                            val updateClass = hashMapOf(
                                "className" to "Class B",
                                "classDesc" to "Test DescB",
                                "classPrice" to 40,
                                "classDate" to "9/12/2022",
                                "classTime" to "14:00 - 16:00",
                            )
                            docRef.set(updateClass, SetOptions.merge())
                                .addOnSuccessListener { Log.d("UPDATE CLASS","CLASS DETAIL UPDATED SUCCESSFULLY" ) }
                                .addOnFailureListener { e -> Log.e("UPDATE CLASS", "ERROR UPDATING CLASS DETAIL", e) }
                        }
                        builder.setNegativeButton("Cancel"){ _, _ -> }
                        builder.show()
                    }
                }
                else
                    Log.d("FETCHING DOCUMENT", "INVALID DOCUMENT")
            }.addOnFailureListener { e ->
                Log.e("FETCHING DOCUMENT", "INVALID DOCUMENT", e)
            }

            // Confirm Button for Deleting Item
            binding.imgbtnDeleteClass.setOnClickListener(){
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Delete Class")
                builder.setMessage("Confirm to delete class?")
                builder.setPositiveButton("Delete"){ _, _ ->
                    docRef.delete().addOnSuccessListener {
                        Log.d("DELETE CLASS", "CLASS DELETED SUCCESSFULLY")
                        adminActivityView.replaceFragment(CoachAdminFragment())
                    }.addOnFailureListener { e ->
                        Log.e("DELETE CLASS", "ERROR DELETING CLASS", e)
                    }
                }
                builder.setNegativeButton("Cancel"){ _, _ -> }
                builder.show()
            }

        }
        return binding.root
    }
}