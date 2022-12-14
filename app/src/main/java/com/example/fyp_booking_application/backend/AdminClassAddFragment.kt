package com.example.fyp_booking_application.backend

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.fyp_booking_application.AdminDashboardActivity
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentAdminClassAddBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class AdminClassAddFragment : Fragment() {

    private lateinit var binding: FragmentAdminClassAddBinding
    private lateinit var databaseRef: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_class_add, container, false)
        val adminActivityView = (activity as AdminDashboardActivity)
        databaseRef = FirebaseFirestore.getInstance()

        // Input Validations
        binding.tfAddClassName.setOnFocusChangeListener { _, focused ->
            if(!focused && binding.tfAddClassName.text!!.isEmpty()){
                binding.nameContainer.helperText = "Name is Required"
            }
            else if(!focused && !(binding.tfAddClassName.text!!.matches("^\\p{L}+(?: \\p{L}+)*\$".toRegex()))){
                binding.nameContainer.helperText = "Invalid Name"
            }
            else binding.nameContainer.helperText = null
        }

        binding.tfAddClassDesc.setOnFocusChangeListener { _, focused ->
            if(!focused && binding.tfAddClassDesc.text!!.isEmpty()){
                binding.descContainer.helperText = "Description is Required"
            }
            else binding.descContainer.helperText = null
        }

        binding.tfAddClassPrice.setOnFocusChangeListener { _, focused ->
            if(!focused && binding.tfAddClassPrice.text!!.isEmpty()){
                binding.priceContainer.helperText = "Price is Required"
            }
            else if(!focused && !(binding.tfAddClassPrice.text!!.all { it.isDigit() })){
                binding.priceContainer.helperText = "Invalid Price"
            }
            else binding.priceContainer.helperText = null
        }

        binding.tfAddClassDate.setOnFocusChangeListener { _, focused ->
            if(!focused && binding.tfAddClassDate.text!!.isEmpty()){
                binding.dateContainer.helperText = "Name is Required"
            }
            else binding.dateContainer.helperText = null
        }

        binding.tfAddClassTime.setOnFocusChangeListener { _, focused ->
            if(!focused && binding.tfAddClassTime.text!!.isEmpty()){
                binding.timeContainer.helperText = "Name is Required"
            }
            else binding.timeContainer.helperText = null
        }

        binding.btnFinishAddClass.setOnClickListener{
            val validName = binding.nameContainer.helperText == null
            val validDesc = binding.descContainer.helperText == null
            val validPrice = binding.priceContainer.helperText == null
            val validDate = binding.dateContainer.helperText == null
            val validTime = binding.timeContainer.helperText == null

            if(validName && validDesc && validPrice && validDate && validTime) {
                var nameValidation = 0
                databaseRef.collection("class_testing1").get()
                    .addOnSuccessListener{ results ->
                        for (document in results){
                            if(document["className"] == binding.tfAddClassName.text.toString()){
                                nameValidation += 1
                            }
                        }
                        if (nameValidation == 0){
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
                                    Log.d("ADDING NEW CLASS", "CLASS ADDED SUCCESSFULLY")
                                    adminActivityView.replaceFragment(AdminClassFragment(), R.id.adminLayout)
                                }
                                .addOnFailureListener { e ->
                                    Log.e("ADDING NEW CLASS", "ERROR ADDING NEW CLASS", e)
                                }
                        } else Toast.makeText(context, "EXISTING CLASS NAME", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener(){ e ->
                        Log.e("FETCHING DOCUMENT", "INVALID DOCUMENT", e)
                    }
            }
            else Toast.makeText(context, "CHECK INPUT FIELDS", Toast.LENGTH_SHORT).show()
        }

        binding.tvBackClassAdd.setOnClickListener(){
            adminActivityView.replaceFragment(AdminClassFragment(), R.id.adminLayout)
        }
        return binding.root
    }
}



