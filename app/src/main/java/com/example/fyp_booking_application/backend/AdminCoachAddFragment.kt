package com.example.fyp_booking_application.backend

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.fyp_booking_application.AdminDashboardActivity
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentAdminCoachAddBinding
import com.google.firebase.firestore.FirebaseFirestore

class AdminCoachAddFragment : Fragment() {

    private lateinit var binding: FragmentAdminCoachAddBinding
    private lateinit var databaseRef: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_coach_add, container, false)
        val adminView = (activity as AdminDashboardActivity)
        adminView.setTitle("Add New Coach")
        databaseRef = FirebaseFirestore.getInstance()

        binding.tfAddCoachName.setOnFocusChangeListener { _, focused ->
            if(!focused && binding.tfAddCoachName.text!!.isEmpty()){
                binding.coachNameContainer.helperText = "Name is Required"
            }
            else binding.coachNameContainer.helperText = null
        }

        binding.tfAddCoachEmail.setOnFocusChangeListener { _, focused ->
            if(!focused && binding.tfAddCoachEmail.text!!.isEmpty()){
                binding.coachEmailContainer.helperText = "Email is Required"
            }
            else if(!focused && !Patterns.EMAIL_ADDRESS.matcher(binding.tfAddCoachEmail.text.toString()).matches()){
                binding.coachEmailContainer.helperText = "Invalid Email"
            }
            else binding.coachEmailContainer.helperText = null
        }

        binding.tfAddCoachPhone.setOnFocusChangeListener { _, focused ->
            if(!focused && binding.tfAddCoachPhone.text!!.isEmpty()){
                binding.coachPhoneContainer.helperText = "Phone No. is Required"
            }
            else if(!focused && !(binding.tfAddCoachPhone.text!!.all { it.isDigit() })){
                binding.coachPhoneContainer.helperText = "Invalid Phone No."
            }
            else if(!focused && binding.tfAddCoachPhone.text!!.length < 10){
                binding.coachPhoneContainer.helperText = "Invalid Phone No."
            }
            else binding.coachPhoneContainer.helperText = null
        }

        val expType = arrayListOf<String>()
        val expRef = databaseRef.collection("SystemSettings").document("experience")
        expRef.get().addOnSuccessListener { document ->
            if(document != null){
                document.data!!.forEach { fieldName ->
                    expType.add(fieldName.value.toString())
                }
                val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, expType)
                binding.spinnerExpField.adapter = spinnerAdapter
                binding.spinnerExpField.setSelection(3)
            }
        }.addOnFailureListener { e ->
            Log.e("NO EXPERIENCE", "ERROR FETCHING EXPERIENCE", e)
        }

        binding.imgBtnAddNewCoach.setOnClickListener {
            val validName = binding.coachNameContainer.helperText == null
            val validEmail = binding.coachEmailContainer.helperText == null
            val validPhone = binding.coachPhoneContainer.helperText == null

            if(validName && validEmail && validPhone){
                var nameValidation = 0
                databaseRef.collection("Coaches").get()
                    .addOnSuccessListener { results ->
                        for (document in results) {
                            if (document["coachName"] == binding.tfAddCoachName.text.toString()) {
                                nameValidation += 1
                            }
                        }
                        if(nameValidation == 0){
                            val newCoachRef = databaseRef.collection("Coaches").document()
                            val newCoach = hashMapOf(
                                "coachID" to newCoachRef.id,
                                "coachName" to binding.tfAddCoachName.text.toString(),
                                "coachEmail" to binding.tfAddCoachEmail.text.toString(),
                                "coachExp" to binding.spinnerExpField.selectedItem.toString(),
                                "coachPhone" to binding.tfAddCoachPhone.text.toString(),
                                "coachImage" to ""
                            )
                            newCoachRef.set(newCoach)
                                .addOnSuccessListener {
                                    Log.d("ADDING NEW COACH", "COACH ADDED SUCCESSFULLY")
                                    adminView.replaceFragment(AdminCoachFragment(), R.id.adminLayout)
                                }.addOnFailureListener { e ->
                                    Log.e("ADDING NEW COACH", "ERROR ADDING NEW COACH", e)
                                }
                        } else Toast.makeText(context, "EXISTING CLASS NAME", Toast.LENGTH_SHORT).show()

                    }.addOnFailureListener { e -> Log.e("FETCHING DOCUMENT", "INVALID DOCUMENT", e) }
            } else Toast.makeText(context, "CHECK INPUT FIELDS", Toast.LENGTH_SHORT).show()
        }
        binding.tvBackCoachAdd.setOnClickListener {
            adminView.replaceFragment(AdminCoachFragment(),R.id.adminLayout)
        }
        return binding.root
    }
}