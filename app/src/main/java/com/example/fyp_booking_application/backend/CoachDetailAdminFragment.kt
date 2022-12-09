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
import com.example.fyp_booking_application.databinding.FragmentCoachDetailAdminBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class CoachDetailAdminFragment : Fragment() {

    private lateinit var binding: FragmentCoachDetailAdminBinding
    private lateinit var databaseRef: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Variable Declarations
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_coach_detail_admin, container, false)
        val adminActivityView = (activity as AdminDashboardActivity)

        // Get Data from Paired-Fragment
        setFragmentResultListener("toCoachDetails"){ _, bundle ->
            val coachID = bundle.getString("toCoachDetails")
            databaseRef = FirebaseFirestore.getInstance()
            val docRef = databaseRef.collection("coach_testing1").document(coachID.toString())

            // GET DOCUMENT
            docRef.get().addOnSuccessListener { document ->
                if(document != null){
                    val testing = document.toObject(CoachData::class.java)

                    // To Enable Editable Fields
                    binding.swUpdateCoach.setOnCheckedChangeListener{ _, isChecked ->
                        binding.tfCoachDetailName.isEnabled = isChecked
                        binding.tfCoachDetailEmail.isEnabled = isChecked
                        binding.tfCoachDetailPhone.isEnabled = isChecked
                        binding.tfCoachDetailExp.isEnabled = isChecked
                    }

                    // Set Text for EditText
                    binding.tfCoachDetailID.setText(testing?.coachID.toString())
                    binding.tfCoachDetailName.setText(testing?.coachName.toString())
                    binding.tfCoachDetailEmail.setText(testing?.coachEmail.toString())
                    binding.tfCoachDetailPhone.setText(testing?.coachPhone.toString())
                    binding.tfCoachDetailExp.setText(testing?.coachExp.toString())

                    // Confirm Button for Updating Item
                    binding.imgbtnUpdateCoach.setOnClickListener(){
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setTitle("Update Coach Details")
                        builder.setMessage("Confirm to update coach details?")
                        builder.setPositiveButton("Update"){ _, _ ->
                            val updateCoach = hashMapOf(
                                "coachName" to "TestName2",
                                "coachEmail" to "TEST2@gmail.com",
                                "coachExp" to "Expert+1",
                                "coachPhone" to "011-3456789",
                                "coachExample" to "TESTING"
                            )
                            docRef.set(updateCoach, SetOptions.merge())
                                .addOnSuccessListener { Log.d("UPDATE COACH","COACH DETAIL UPDATED SUCCESSFULLY" ) }
                                .addOnFailureListener { e -> Log.e("UPDATE COACH", "ERROR UPDATING COACH DETAIL", e) }
                        }
                        builder.setNegativeButton("Cancel"){ _, _ -> }
                        builder.show()
                    }
                }
                else {
                    Log.d("FETCHING DOCUMENT", "INVALID DOCUMENT")
                }
            }.addOnFailureListener { e ->
                Log.e("FETCHING DOCUMENT", "INVALID DOCUMENT", e)
            }

            // Confirm Button for Deleting Item
            binding.imgbtnDeleteCoach.setOnClickListener{
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Delete Coach")
                builder.setMessage("Confirm to delete coach?")
                builder.setPositiveButton("Delete"){ _, _ ->
                    docRef.delete().addOnSuccessListener {
                        Log.d("DELETE COACH", "COACH DELETED SUCCESSFULLY")
                        adminActivityView.replaceFragment(CoachAdminFragment())
                    }.addOnFailureListener { e ->
                        Log.e("DELETE COACH", "ERROR DELETING COACH", e)
                    }
                }
                builder.setNegativeButton("Cancel"){ _, _ -> }
                builder.show()
            }


        }
        return binding.root
    }
}
