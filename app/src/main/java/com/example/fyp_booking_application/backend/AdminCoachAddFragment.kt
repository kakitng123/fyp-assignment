package com.example.fyp_booking_application.backend

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        val adminActivityView = (activity as AdminDashboardActivity)
        adminActivityView.setTitle("ADD COACH")
        databaseRef = FirebaseFirestore.getInstance()

        // Input Validations
        binding.tfAddCoachName.setOnFocusChangeListener { _, focused ->
            if(!focused && binding.tfAddCoachName.text!!.isEmpty()){
                binding.coachNameContainer.helperText = "Name is Required"
            }
            else if(!focused && !(binding.tfAddCoachName.text!!.matches("^\\p{L}+(?: \\p{L}+)*\$".toRegex()))){
                binding.coachNameContainer.helperText = "Invalid Name"
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

        binding.tfAddCoachExp.setOnFocusChangeListener { _, focused ->
            if(!focused && binding.tfAddCoachExp.text!!.isEmpty()){
                binding.coachExpContainer.helperText = "Experience is Required"
            }
            else binding.coachExpContainer.helperText = null
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

        binding.imgBtnAddCoach.setOnClickListener {
            val validName = binding.coachNameContainer.helperText == null
            val validEmail = binding.coachEmailContainer.helperText == null
            val validExp = binding.coachExpContainer.helperText == null
            val validPhone = binding.coachPhoneContainer.helperText == null

            if(validName && validEmail && validExp && validPhone){
                var nameValidation = 0
                databaseRef.collection("coach_testing1").get()
                    .addOnSuccessListener { results ->
                        for (document in results) {
                            if (document["coachName"] == binding.tfAddCoachName.text.toString()) {
                                nameValidation += 1
                            }
                        }
                        if(nameValidation == 0){
                            val newCoachRef = databaseRef.collection("coach_testing1").document()
                            val newCoach = hashMapOf(
                                "coachID" to newCoachRef.id,
                                "coachName" to binding.tfAddCoachName.text.toString(),
                                "coachEmail" to binding.tfAddCoachEmail.text.toString(),
                                "coachExp" to binding.tfAddCoachExp.text.toString(), // Could Use Spinner
                                "coachPhone" to binding.tfAddCoachPhone.text.toString(),
                            )
                            newCoachRef.set(newCoach)
                                .addOnSuccessListener {
                                    Log.d("ADDING NEW COACH", "COACH ADDED SUCCESSFULLY")
                                    adminActivityView.replaceFragment(AdminCoachFragment(),R.id.adminLayout)
                                }
                                .addOnFailureListener { e -> Log.e("ADDING NEW COACH", "ERROR ADDING NEW COACH", e)}
                        } else Toast.makeText(context, "EXISTING CLASS NAME", Toast.LENGTH_SHORT).show()

                    }.addOnFailureListener { e -> Log.e("FETCHING DOCUMENT", "INVALID DOCUMENT", e) }
            } else Toast.makeText(context, "CHECK INPUT FIELDS", Toast.LENGTH_SHORT).show()
        }
        binding.tvBackCoachAdd.setOnClickListener {
            adminActivityView.replaceFragment(AdminCoachFragment(),R.id.adminLayout)
        }
        return binding.root
    }
}