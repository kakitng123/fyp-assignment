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
import com.example.fyp_booking_application.databinding.FragmentAdminClassDetailBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class AdminClassDetailFragment : Fragment() {

    private lateinit var binding: FragmentAdminClassDetailBinding
    private lateinit var databaseRef: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_class_detail, container, false)
        val adminActivityView = (activity as AdminDashboardActivity)

        setFragmentResultListener("toClassDetails") { _, bundle ->
            val classID = bundle.getString("toClassDetails")
            databaseRef = FirebaseFirestore.getInstance()
            val docRef = databaseRef.collection("class_testing1").document(classID.toString())

            docRef.get().addOnSuccessListener { document ->
                if(document != null){
                    val testing = document.toObject(ClassData2::class.java)

                    binding.swUpdateClass.setOnCheckedChangeListener { _, isChecked ->
                        binding.tfClassName.isEnabled = isChecked
                        binding.tfClassDesc.isEnabled = isChecked
                        binding.tfClassPrice.isEnabled = isChecked
                        binding.tfClassDate.isEnabled = isChecked
                        binding.tfClassTime.isEnabled = isChecked
                    }

                    binding.tfClassID.setText(testing?.classID.toString())
                    binding.tfClassName.setText(testing?.className.toString())
                    binding.tfClassDesc.setText(testing?.classDesc.toString())
                    binding.tfClassPrice.setText(testing?.classPrice.toString())
                    binding.tfClassDate.setText(testing?.classDate.toString())
                    binding.tfClassTime.setText(testing?.classTime.toString())

                    binding.imgbtnUpdateClass.setOnClickListener {
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setTitle("Update Class Details")
                        builder.setMessage("Confirm to update class details?")
                        builder.setPositiveButton("Update"){ _, _ ->
                            val updateClass = hashMapOf(
                                "className" to binding.tfClassName.text.toString(),
                                "classDesc" to binding.tfClassDesc.text.toString(),
                                "classPrice" to binding.tfClassPrice.text.toString().toDouble(),
                                "classDate" to binding.tfClassDate.text.toString(), // ClassDate can use Calendar
                                "classTime" to binding.tfClassTime.text.toString() // ClassTime can use Spinner
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

            binding.imgbtnDeleteClass.setOnClickListener {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Delete Class")
                builder.setMessage("Confirm to delete class?")
                builder.setPositiveButton("Delete"){ _, _ ->
                    docRef.delete().addOnSuccessListener {
                        Log.d("DELETE CLASS", "CLASS DELETED SUCCESSFULLY")
                        adminActivityView.replaceFragment(AdminClassFragment(), R.id.adminLayout)
                    }.addOnFailureListener { e ->
                        Log.e("DELETE CLASS", "ERROR DELETING CLASS", e)
                    }
                }
                builder.setNegativeButton("Cancel"){ _, _ -> }
                builder.show()
            }

        }
        binding.tvBackClassDetail.setOnClickListener {
            adminActivityView.replaceFragment(AdminClassFragment(), R.id.adminLayout)
        }

        return binding.root
    }
}