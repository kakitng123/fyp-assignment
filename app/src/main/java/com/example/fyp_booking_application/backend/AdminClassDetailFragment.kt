package com.example.fyp_booking_application.backend

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResultListener
import com.example.fyp_booking_application.AdminDashboardActivity
import com.example.fyp_booking_application.ClassData
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentAdminClassDetailBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class AdminClassDetailFragment : Fragment() {

    private lateinit var binding: FragmentAdminClassDetailBinding
    private lateinit var databaseRef: FirebaseFirestore
    private lateinit var trainingClassList: ArrayList<String>
    private lateinit var listAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_class_detail, container, false)
        val adminView = (activity as AdminDashboardActivity)
        adminView.setTitle("Class Details")

        setFragmentResultListener("toClassDetails") { _, bundle ->
            val classID = bundle.getString("toClassDetails")
            databaseRef = FirebaseFirestore.getInstance()
            val docRef = databaseRef.collection("TrainingClasses").document(classID.toString())
            trainingClassList = arrayListOf()

            docRef.get().addOnSuccessListener { document ->
                if(document != null){
                    val trainingClass = document.toObject(ClassData::class.java)
                    val classSlots = trainingClass?.classSlot as Map<*, *>
                    classSlots.let {
                        for ((key, value) in classSlots){
                            val listString = "$key -> $value"
                            trainingClassList.add(listString)
                        }

                    }
                    listAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, trainingClassList)
                    binding.classSlotLV.adapter = listAdapter

                    binding.swUpdateClass.setOnCheckedChangeListener { _, isChecked ->
                        binding.classNameField.isEnabled = isChecked
                        binding.classDescField.isEnabled = isChecked
                        binding.classPriceField.isEnabled = isChecked
                    }

                    binding.classIDField.setText(trainingClass.classID.toString())
                    binding.classNameField.setText(trainingClass.className.toString())
                    binding.classDescField.setText(trainingClass.classDesc.toString())
                    binding.classPriceField.setText(trainingClass.classPrice.toString())
                    binding.classCoachField.setText(trainingClass.entitledCoach.toString())

                    binding.classNameField.setOnFocusChangeListener { _, focused ->
                        if(!focused && binding.classNameField.text!!.isEmpty()){
                            binding.classNameContainer.helperText = "Name is Required"
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
                        else binding.classPriceContainer.helperText = null
                    }

                    binding.imgBtnUpdateClass.setOnClickListener {
                        val validName = binding.classNameContainer.helperText == null
                        val validDesc = binding.classDescContainer.helperText == null
                        val validPrice = binding.classPriceContainer.helperText == null

                        if(validName && validDesc && validPrice){
                            val builder = AlertDialog.Builder(requireContext())
                            builder.setTitle("Update Class Details")
                            builder.setMessage("Confirm to update class details?")
                            builder.setPositiveButton("Update"){ _, _ ->
                                val updateClass = hashMapOf(
                                    "className" to binding.classNameField.text.toString(),
                                    "classDesc" to binding.classDescField.text.toString(),
                                    "classPrice" to binding.classPriceField.text.toString().toDouble(),
                                )
                                docRef.set(updateClass, SetOptions.merge())
                                    .addOnSuccessListener {
                                        Log.d("UPDATE CLASS","CLASS DETAIL UPDATED SUCCESSFULLY" )
                                        adminView.replaceFragment(AdminClassFragment(), R.id.adminLayout)
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
                        adminView.replaceFragment(AdminClassFragment(), R.id.adminLayout)
                    }.addOnFailureListener { e ->
                        Log.e("DELETE CLASS", "ERROR DELETING CLASS", e)
                    }
                }
                builder.setNegativeButton("Cancel"){ _, _ -> }
                builder.show()
            }

        }
        binding.tvBackClassDetail.setOnClickListener {
            adminView.replaceFragment(AdminClassFragment(), R.id.adminLayout)
        }


        return binding.root
    }
}