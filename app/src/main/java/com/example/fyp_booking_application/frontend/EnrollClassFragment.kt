package com.example.fyp_booking_application.frontend


import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResultListener
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.UserDashboardActivity
import com.example.fyp_booking_application.databinding.FragmentEnrollClassBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class EnrollClassFragment : Fragment() {
    private lateinit var auth: FirebaseAuth //get the shared instance of the FirebaseAuth object
    private lateinit var fstore: FirebaseFirestore //get the shared instance of the FirebaseAuth object
    private lateinit var binding : FragmentEnrollClassBinding
    val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Initialise
        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_enroll_class, container, false)
        val userView = (activity as UserDashboardActivity)
        userView.setTitle("Enroll Class")

        binding.btnAddEnrollDate.bringToFront()

        //Retrieve Class Detail and Display Booking Details in Enrolled Class Page
        setFragmentResultListener("toClassDetail") { _, bundle ->
            val classID = bundle.getString("toClassDetail")
            val retrieveClassRef = fstore.collection("TrainingClasses").document(classID.toString())

            retrieveClassRef.get().addOnCompleteListener { resultData ->
                if (resultData != null) {
                    // val idResult = resultData.result.getString("classID").toString()
                    val classResult = resultData.result.getString("className").toString()
                    val descResult = resultData.result.getString("classDesc").toString()

                    // No More Time and Date
                    // val dateResult = resultData.result.getString("classDate").toString()
                    // val timeResult = resultData.result.getString("classTime").toString()

                    val classTimeList = arrayListOf<String>()

                    val classTime = resultData.result.get("classSlot") as Map<*, *>
                    classTime.let{
                        for ((_, value) in classTime){ // to Iterate all slots (key -> value e.g. Slots2 -> 10.00 - 12.00 ...)
                            classTimeList.add(value.toString())
                        }
                    }
                    // Insert Class Time into Spinner
                    binding.enrollTime.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, classTimeList)

                    val priceResult = resultData.result.getDouble("classPrice").toString()
                    val assignResult = resultData.result.getString("entitledCoach").toString()

                    binding.enrollClassName.setText(classResult)
                    binding.enrollDesc.setText(descResult)

                    // No more time and date
                    // binding.enrollDate.setText(dateResult)
                    // binding.enrollTime.setText(timeResult)

                    binding.enrollPrice.setText(priceResult)
                    binding.enrollCoach.setText(assignResult)
                } else {
                    Log.d("noexits", "No such documents.")
                }
            }.addOnFailureListener { exception ->
                Log.d("noexits", "Error getting documents.", exception)
            }

            // Added Date Picker
            binding.btnAddEnrollDate.setOnClickListener {
                val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    calendar.apply {
                        set(Calendar.YEAR, year)
                        set(Calendar.MONTH, month)
                        set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    }
                    binding.enrollDate.text = SimpleDateFormat("dd/MM/yyyy").format(calendar.time)
                }
                DatePickerDialog(requireContext(), dateSetListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                ).show()
            }

            //Save Updated Data function
            binding.enrollBtn.setOnClickListener {
                var enrolledClassName: String = binding.enrollClassName.text.toString()
                var enrolledDate: String = binding.enrollDate.text.toString()
                var enrolledTime: String = binding.enrollTime.selectedItem.toString()
                var enrolledPrice: Double = binding.enrollPrice.text.toString().toDouble()

                val userID = auth.currentUser?.uid
                val enrolledId = fstore.collection("Enroll").document()
                val enroll = hashMapOf(
                    "enrollID" to enrolledId.id,
                    "enrollStatus" to "Pending",
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




