package com.example.fyp_booking_application.backend

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResultListener
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentCoachDetailAdminBinding
import com.google.firebase.firestore.FirebaseFirestore

class CoachDetailAdminFragment : Fragment() {

    private lateinit var binding: FragmentCoachDetailAdminBinding
    private lateinit var databaseRef: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Variable Declarations
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_coach_detail_admin, container, false)

        setFragmentResultListener("toCoachDetails"){ _, bundle ->
            val coachID = bundle.getString("toCoachDetails")
            databaseRef = FirebaseFirestore.getInstance()
            val docRef = databaseRef.collection("CoachProfile").whereEqualTo("coachID", coachID)
            docRef.get()
                .addOnSuccessListener { document ->
                    val testing = document.toObjects(CoachData::class.java)

                    // could use a better method to access
                    binding.tfCoachDetailID.text = testing[0].coachID
                    binding.tfCoachDetailName.text = testing[0].coachName
                    binding.tfCoachDetailEmail.text = testing[0].coachEmail
                    binding.tfCoachDetailPhone.text = testing[0].coachPhone
                    binding.tfCoachDetailExp.text = testing[0].coachExperience
                }
                .addOnFailureListener { e ->
                    Log.e("FETCHING DOCUMENT", "INVALID DOCUMENT", e)
                }
        }

        return binding.root
    }
}
