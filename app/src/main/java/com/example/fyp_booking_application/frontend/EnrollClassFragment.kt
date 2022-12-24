package com.example.fyp_booking_application.frontend


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResultListener
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.UserDashboardActivity
import com.example.fyp_booking_application.databinding.FragmentEnrollClassBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EnrollClassFragment : Fragment() {
    private lateinit var auth: FirebaseAuth //get the shared instance of the FirebaseAuth object
    private lateinit var fstore: FirebaseFirestore //get the shared instance of the FirebaseAuth object
    private lateinit var binding : FragmentEnrollClassBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Initialise
        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_enroll_class, container, false)
        val userView = (activity as UserDashboardActivity)
        userView.setTitle("Enroll Class")

        //Retrieve Class Detail and Display Booking Details in Enrolled Class Page
        setFragmentResultListener("toClassDetail") { _, bundle ->
            val classID = bundle.getString("toClassDetail")
            val retrieveClassRef = fstore.collection("class_testing1").document(classID.toString())

            retrieveClassRef.get().addOnCompleteListener { resultData ->
                if (resultData != null) {
                    // val idResult = resultData.result.getString("classID").toString()
                    val classResult = resultData.result.getString("className").toString()
                    val descResult = resultData.result.getString("classDesc").toString()
                    val dateResult = resultData.result.getString("classDate").toString()
                    val timeResult = resultData.result.getString("classTime").toString()
                    val priceResult = resultData.result.getDouble("classPrice").toString()
                    val assignResult = resultData.result.getString("entitledCoach").toString()

                    binding.enrollClassName.setText(classResult)
                    binding.enrollDesc.setText(descResult)
                    binding.enrollDate.setText(dateResult)
                    binding.enrollTime.setText(timeResult)
                    binding.enrollPrice.setText(priceResult)
                    binding.enrollCoach.setText(assignResult)
                } else {
                    Log.d("noexits", "No such documents.")
                }
            }.addOnFailureListener { exception ->
                Log.d("noexits", "Error getting documents.", exception)
            }

            //Save Updated Data function
            binding.enrollBtn.setOnClickListener {
                var enrolledClassName: String = binding.enrollClassName.text.toString()
                var enrolledDate: String = binding.enrollDate.text.toString()
                var enrolledTime: String = binding.enrollTime.text.toString()
                var enrolledPrice: String = binding.enrollPrice.text.toString()

                val userID = auth.currentUser?.uid
                val enrolledId = fstore.collection("Enroll").document()
                val enroll = hashMapOf(
                    "enrollID" to enrolledId.id,
                    "enrollStatus" to "Success",
                    "enrollDate" to enrolledDate,
                    "enrollTime" to enrolledTime,
                    "enrollClassName" to enrolledClassName,
                    "enrollPrice" to enrolledPrice,
                    "classID" to classID,
                    "userID" to userID
                )

                // Add a new document with a generated ID
                enrolledId.set(enroll).addOnSuccessListener {
                    Toast.makeText(activity, "Enrolled Successfully", Toast.LENGTH_SHORT)
                        .show()
                    userView.replaceFragment(CoachFragment())
                }.addOnFailureListener {
                    Toast.makeText(activity, "Enrolled Failure", Toast.LENGTH_SHORT)
                        .show()
                    userView.replaceFragment(TrainingClassFragment())
                }
            }
        }
        return binding.root
    }
}




