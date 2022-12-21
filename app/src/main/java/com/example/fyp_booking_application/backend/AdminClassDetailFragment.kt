package com.example.fyp_booking_application.backend

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
        adminActivityView.setTitle("CLASS DETAIL")

        setFragmentResultListener("toClassDetails") { _, bundle ->
            val classID = bundle.getString("toClassDetails")
            databaseRef = FirebaseFirestore.getInstance()
            val docRef = databaseRef.collection("class_testing1").document(classID.toString())

            docRef.get().addOnSuccessListener { document ->
                if(document != null){
                    val trainingClass = document.toObject(ClassData2::class.java)

                    binding.swUpdateClass.setOnCheckedChangeListener { _, isChecked ->
                        binding.classNameField.isEnabled = isChecked
                        binding.classDescField.isEnabled = isChecked
                        binding.classPriceField.isEnabled = isChecked
                        binding.classDateField.isEnabled = isChecked
                        binding.classTimeField.isEnabled = isChecked
                    }

                    binding.classIDField.setText(trainingClass?.classID.toString())
                    binding.classNameField.setText(trainingClass?.className.toString())
                    binding.classDescField.setText(trainingClass?.classDesc.toString())
                    binding.classPriceField.setText(trainingClass?.classPrice.toString())
                    binding.classDateField.setText(trainingClass?.classDate.toString())
                    binding.classTimeField.setText(trainingClass?.classTime.toString())
                    binding.classCoachField.setText(trainingClass?.entitledCoach.toString())

                    binding.classNameField.setOnFocusChangeListener { _, focused ->
                        if(!focused && binding.classNameField.text!!.isEmpty()){
                            binding.classNameContainer.helperText = "Name is Required"
                        }
                        else if(!focused && !(binding.classNameField.text!!.matches("^\\p{L}+(?: \\p{L}+)*\$".toRegex()))){
                            binding.classNameContainer.helperText = "Only Alphabets Allowed"
                        }
                        else binding.classNameContainer.helperText = null
                    }

                    binding.classDescField.setOnFocusChangeListener { _, focused ->
                        if(!focused && binding.classDescField.text!!.isEmpty()){
                            binding.classDescContainer.helperText = "Description is Required"
                        }
                        else binding.classDescContainer.helperText = null
                    }

                    binding.classPriceField.setOnFocusChangeListener { _, focused ->
                        if(!focused && binding.classPriceField.text!!.isEmpty()){
                            binding.classPriceContainer.helperText = "Price is Required"
                        }
                        else if(!focused && !(binding.classPriceField.text!!.all { it.isDigit() })){
                            binding.classPriceContainer.helperText = "Only Numbers Allowed"
                        }
                        else binding.classPriceContainer.helperText = null
                    }

                    binding.classDateField.setOnFocusChangeListener { _, focused ->
                        if(!focused && binding.classDateField.text!!.isEmpty()){
                            binding.classDateContainer.helperText = "Date is Required"
                        }
                        else binding.classDateContainer.helperText = null
                    }

                    binding.classTimeField.setOnFocusChangeListener { _, focused ->
                        if(!focused && binding.classTimeField.text!!.isEmpty()){
                            binding.classTimeContainer.helperText = "Time is Required"
                        }
                        else binding.classTimeContainer.helperText = null
                    }

                    binding.imgBtnUpdateClass.setOnClickListener {
                        val validName = binding.classNameContainer.helperText == null
                        val validDesc = binding.classDescContainer.helperText == null
                        val validPrice = binding.classPriceContainer.helperText == null
                        val validDate = binding.classDateContainer.helperText == null
                        val validTime = binding.classTimeContainer.helperText == null

                        if(validName && validDesc && validPrice && validDate && validTime){
                            val builder = AlertDialog.Builder(requireContext())
                            builder.setTitle("Update Class Details")
                            builder.setMessage("Confirm to update class details?")
                            builder.setPositiveButton("Update"){ _, _ ->
                                val updateClass = hashMapOf(
                                    "className" to binding.classNameField.text.toString(),
                                    "classDesc" to binding.classDescField.text.toString(),
                                    "classPrice" to binding.classPriceField.text.toString().toDouble(),
                                    "classDate" to binding.classDateField.text.toString(), // ClassDate can use Calendar
                                    "classTime" to binding.classTimeField.text.toString() // ClassTime can use Spinner
                                )
                                docRef.set(updateClass, SetOptions.merge())
                                    .addOnSuccessListener {
                                        Log.d("UPDATE CLASS","CLASS DETAIL UPDATED SUCCESSFULLY" )
                                        adminActivityView.replaceFragment(AdminClassFragment(), R.id.adminLayout)
                                    }
                                    .addOnFailureListener { e -> Log.e("UPDATE CLASS", "ERROR UPDATING CLASS DETAIL", e) }
                            }
                            builder.setNegativeButton("Cancel"){ _, _ -> }
                            builder.show()
                        }
                        else Toast.makeText(context, "CHECK INPUT FIELDS", Toast.LENGTH_SHORT).show()
                    }
                }
                else
                    Log.d("FETCHING DOCUMENT", "INVALID DOCUMENT")
            }.addOnFailureListener { e ->
                Log.e("FETCHING DOCUMENT", "INVALID DOCUMENT", e)
            }

            binding.imgBtnDeleteClass.setOnClickListener {
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