package com.example.fyp_booking_application.backend

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fyp_booking_application.AdminDashboardActivity
import com.example.fyp_booking_application.ClassData
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.backend.Adapters.CoachClassAdminAdapter
import com.example.fyp_booking_application.databinding.FragmentAdminCoachDetailBinding
import com.google.firebase.firestore.*

class AdminCoachDetailFragment : Fragment(), CoachClassAdminAdapter.OnItemClickListener {

    private lateinit var binding: FragmentAdminCoachDetailBinding
    private lateinit var databaseRef: FirebaseFirestore
    private lateinit var coachClassList: ArrayList<ClassData>
    private lateinit var coachClassAdminAdapter: CoachClassAdminAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_coach_detail, container, false)
        val adminActivityView = (activity as AdminDashboardActivity)
        adminActivityView.setTitle("COACH DETAIL")

        setFragmentResultListener("toCoachDetails"){ _, bundle ->
            val coachID = bundle.getString("toCoachDetails")
            databaseRef = FirebaseFirestore.getInstance()
            val docRef = databaseRef.collection("coach_testing1").document(coachID.toString())

            val expType = arrayListOf<String>()
            val expRef = databaseRef.collection("SystemSettings").document("experience")
            expRef.get().addOnSuccessListener { document ->
                if(document != null){
                    document.data!!.forEach { fieldName ->
                        expType.add(fieldName.value.toString())
                    }
                    val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, expType)
                    binding.coachExpField.isEnabled = false
                    binding.coachExpField.adapter = spinnerAdapter
                }
            }.addOnFailureListener { e ->
                Log.e("NO EXPERIENCE", "ERROR FETCHING EXPERIENCE", e)
            }

            docRef.get().addOnSuccessListener { document ->
                if(document != null){
                    val coach = document.toObject(CoachData::class.java)

                    dataInitialize(coach?.coachName.toString())
                    binding.coachClassRecyclerView.apply{
                        layoutManager = LinearLayoutManager(context)
                        setHasFixedSize(true)
                        coachClassList = arrayListOf()
                        coachClassAdminAdapter = CoachClassAdminAdapter(coachClassList, this@AdminCoachDetailFragment)
                        adapter = coachClassAdminAdapter
                    }

                    binding.swUpdateCoach.setOnCheckedChangeListener{ _, isChecked ->
                        binding.coachNameField.isEnabled = isChecked
                        binding.coachEmailField.isEnabled = isChecked
                        binding.coachPhoneNoField.isEnabled = isChecked
                        binding.coachExpField.isEnabled = isChecked
                    }

                    binding.coachIDField.setText(coach?.coachID.toString())
                    binding.coachNameField.setText(coach?.coachName.toString())
                    binding.coachEmailField.setText(coach?.coachEmail.toString())
                    binding.coachPhoneNoField.setText(coach?.coachPhone.toString())
                    binding.coachExpField.setSelection(getIndex(coach?.coachExp.toString()))

                    binding.coachNameField.setOnFocusChangeListener { _, focused ->
                        if(!focused && binding.coachNameField.text!!.isEmpty()){
                            binding.coachNameContainer.helperText = "Name is Required"
                        }
                        else if(!focused && !(binding.coachNameField.text!!.matches("^\\p{L}+(?: \\p{L}+)*\$".toRegex()))){
                            binding.coachNameContainer.helperText = "Invalid Name"
                        }
                        else binding.coachNameContainer.helperText = null
                    }

                    binding.coachEmailField.setOnFocusChangeListener { _, focused ->
                        if(!focused && binding.coachEmailField.text!!.isEmpty()){
                            binding.coachEmailContainer.helperText = "Email is Required"
                        }
                        else if(!focused && !Patterns.EMAIL_ADDRESS.matcher(binding.coachEmailField.text.toString()).matches()){
                            binding.coachEmailContainer.helperText = "Invalid Email"
                        }
                        else binding.coachEmailContainer.helperText = null
                    }

                    binding.coachPhoneNoField.setOnFocusChangeListener { _, focused ->
                        if(!focused && binding.coachPhoneNoField.text!!.isEmpty()){
                            binding.coachPhoneNoContainer.helperText = "Phone No. is Required"
                        }
                        else if(!focused && !(binding.coachPhoneNoField.text!!.all { it.isDigit() })){
                            binding.coachPhoneNoContainer.helperText = "Invalid Phone No."
                        }
                        else if(!focused && binding.coachPhoneNoField.text!!.length < 10){
                            binding.coachPhoneNoContainer.helperText = "Invalid Phone No."
                        }
                        else binding.coachPhoneNoContainer.helperText = null
                    }

                    binding.imgBtnUpdateCoach.setOnClickListener{
                        val validName = binding.coachNameContainer.helperText == null
                        val validEmail = binding.coachEmailContainer.helperText == null
                        val validPhone = binding.coachPhoneNoContainer.helperText == null

                        if(validName && validEmail && validPhone){
                            val builder = AlertDialog.Builder(requireContext())
                            builder.setTitle("Update Coach Details")
                            builder.setMessage("Confirm to update coach details?")
                            builder.setPositiveButton("Update"){ _, _ ->
                                val updateCoach = hashMapOf(
                                    "coachName" to binding.coachNameField.text.toString(),
                                    "coachEmail" to binding.coachEmailField.text.toString(),
                                    "coachExp" to binding.coachPhoneNoField.text.toString(),
                                    "coachPhone" to binding.coachExpField.selectedItem.toString()
                                )
                                docRef.set(updateCoach, SetOptions.merge())
                                    .addOnSuccessListener { Log.d("UPDATE COACH","COACH DETAIL UPDATED SUCCESSFULLY" ) }
                                    .addOnFailureListener { e -> Log.e("UPDATE COACH", "ERROR UPDATING COACH DETAIL", e) }
                            }
                            builder.setNegativeButton("Cancel"){ _, _ -> }
                            builder.show()
                        }
                        else Toast.makeText(context, "CHECK INPUT FIELDS", Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    Log.d("FETCHING DOCUMENT", "INVALID DOCUMENT")
                }
            }.addOnFailureListener { e ->
                Log.e("FETCHING DOCUMENT", "INVALID DOCUMENT", e)
            }

            binding.imgBtnDeleteCoach.setOnClickListener{
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Delete Coach")
                builder.setMessage("Confirm to delete coach?")
                builder.setPositiveButton("Delete"){ _, _ ->
                    docRef.delete().addOnSuccessListener {
                        Log.d("DELETE COACH", "COACH DELETED SUCCESSFULLY")
                        adminActivityView.replaceFragment(AdminCoachFragment(), R.id.adminLayout)
                    }.addOnFailureListener { e ->
                        Log.e("DELETE COACH", "ERROR DELETING COACH", e)
                    }
                }
                builder.setNegativeButton("Cancel"){ _, _ -> }
                builder.show()
            }
            binding.imgBtnAddCoachClass.setOnClickListener{
                adminActivityView.replaceFragment(AdminClassAddFragment(), R.id.adminLayout)
                setFragmentResult("toClassAdd", bundleOf("toClassAdd" to coachID))
            }
        }
        binding.tvBackCoachDetail.setOnClickListener{
            adminActivityView.replaceFragment(AdminCoachFragment(), R.id.adminLayout)
        }
        return binding.root
    }

    override fun onItemClick(position: Int) {
        val currentItem = coachClassList[position]
        val adminActivityView = (activity as AdminDashboardActivity)
        adminActivityView.replaceFragment(AdminClassDetailFragment(), R.id.adminLayout)
        setFragmentResult("toClassDetails", bundleOf("toClassDetails" to currentItem.classID))
    }

    private fun dataInitialize(coachName: String){
        databaseRef = FirebaseFirestore.getInstance()
        databaseRef.collection("class_testing1").whereEqualTo("entitledCoach", coachName)
            .addSnapshotListener(object : EventListener<QuerySnapshot>{
                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if(error != null){
                        Log.e("FAILED INITIALIZATION", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){
                            coachClassList.add(dc.document.toObject(ClassData::class.java))
                        }
                    }
                    coachClassAdminAdapter.notifyDataSetChanged()
                }

            })
    }

    private fun getIndex(exp: String): Int {
        val number: Int = when(exp) {
            "Beginner" -> 3
            "Intermediate" -> 2
            "Advanced" -> 1
            "Expert" -> 0
            else -> Log.d("Error", "Error")
        }
        return number
    }
}
