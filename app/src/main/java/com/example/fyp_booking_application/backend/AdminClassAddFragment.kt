package com.example.fyp_booking_application.backend

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
import com.example.fyp_booking_application.databinding.FragmentAdminClassAddBinding
import com.google.firebase.firestore.FirebaseFirestore

class AdminClassAddFragment : Fragment() {

    private lateinit var binding: FragmentAdminClassAddBinding
    private lateinit var databaseRef: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_class_add, container, false)
        binding.tvBackClassAdd.bringToFront()

        val adminActivityView = (activity as AdminDashboardActivity)
        adminActivityView.setTitle("ADD CLASS")
        databaseRef = FirebaseFirestore.getInstance()

        setFragmentResultListener("toClassAdd") { _, bundle ->
            val coachID = bundle.getString("toClassAdd")
            binding.tfAddClassName.setOnFocusChangeListener { _, focused ->
                if (!focused && binding.tfAddClassName.text!!.isEmpty()) {
                    binding.nameContainer.helperText = "Name is Required"
                } else if (!focused && !(binding.tfAddClassName.text!!.matches("^\\p{L}+(?: \\p{L}+)*\$".toRegex()))) {
                    binding.nameContainer.helperText = "Invalid Name"
                } else binding.nameContainer.helperText = null
            }

            binding.tfAddClassDesc.setOnFocusChangeListener { _, focused ->
                if (!focused && binding.tfAddClassDesc.text!!.isEmpty()) {
                    binding.descContainer.helperText = "Description is Required"
                } else binding.descContainer.helperText = null
            }

            binding.tfAddClassPrice.setOnFocusChangeListener { _, focused ->
                if (!focused && binding.tfAddClassPrice.text!!.isEmpty()) {
                    binding.priceContainer.helperText = "Price is Required"
                } else if (!focused && !(binding.tfAddClassPrice.text!!.all { it.isDigit() })) {
                    binding.priceContainer.helperText = "Only Numbers Allowed"
                } else binding.priceContainer.helperText = null
            }

            binding.imgBtnAddClass.setOnClickListener {
                val coachRef = databaseRef.collection("coach_testing1").document(coachID.toString())
                coachRef.get().addOnSuccessListener { documentSnapshot ->
                    val coach = documentSnapshot.toObject(CoachData::class.java)

                    val validName = binding.nameContainer.helperText == null
                    val validDesc = binding.descContainer.helperText == null
                    val validPrice = binding.priceContainer.helperText == null
                    if (validName && validDesc && validPrice) {
                        var nameValidation = 0
                        databaseRef.collection("class_testing2").get()
                            .addOnSuccessListener { results ->
                                for (document in results) {
                                    if (document["className"] == binding.tfAddClassName.text.toString()) {
                                        nameValidation += 1
                                    }
                                }
                                if (nameValidation == 0) {
                                    val newClassRef = databaseRef.collection("class_testing2").document()
                                    val newClass = hashMapOf(
                                        "classID" to newClassRef.id,
                                        "className" to binding.tfAddClassName.text.toString(),
                                        "classDesc" to binding.tfAddClassDesc.text.toString(),
                                        "classPrice" to binding.tfAddClassPrice.text.toString().toDouble(),
                                        "classSlots" to hashMapOf(
                                            "Slots1" to "10:00 - 12:00",
                                            "Slots2" to "14:00 - 16:00",
                                            "Slots3" to "18:00 - 20:00"
                                        ),
                                        "entitledCoach" to coach?.coachName.toString()
                                    )
                                    newClassRef.set(newClass).addOnSuccessListener {
                                        Log.d("ADDING NEW CLASS", "CLASS ADDED SUCCESSFULLY")
                                        adminActivityView.replaceFragment(AdminCoachFragment(), R.id.adminLayout)
                                    }.addOnFailureListener { e ->
                                        Log.e("ADDING NEW CLASS", "ERROR ADDING NEW CLASS", e)
                                    }
                                } else Toast.makeText(context, "EXISTING CLASS NAME", Toast.LENGTH_SHORT).show()
                            }.addOnFailureListener { e ->
                                Log.e("FETCHING DOCUMENT", "INVALID DOCUMENT", e)
                            }
                    } else Toast.makeText(context, "CHECK INPUT FIELDS", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener { e ->
                    Log.e("FETCHING DOCUMENT", "INVALID DOCUMENT", e)
                }

                binding.tvBackClassAdd.setOnClickListener {
                    adminActivityView.replaceFragment(AdminCoachFragment(), R.id.adminLayout)
                }
            }
        }
        return binding.root
    }
}

// Extra Codes

// Date/Timepicker
/*
            binding.imgBtnCalendarAdd.setOnClickListener {
                DatePickerDialog(
                    requireContext(), this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

            binding.imgBtnTimeAdd.setOnClickListener {
                TimePickerDialog(
                    requireContext(), AlertDialog.THEME_HOLO_LIGHT, this,
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                ).show()
            }


    private fun displayFormattedDate(timestamp: Long) {
        binding.tfAddClassDate.setText(dateFormatter.format(timestamp))
    }

    private fun displayFormattedTime(timestamp: Long) {
        binding.tfAddClassTime.setText(timeFormatter.format(timestamp))
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(year, month, dayOfMonth)
        displayFormattedDate(calendar.timeInMillis)
    }

    override fun onTimeSet(view: TimePicker?, hour: Int, minute: Int) {
        calendar.apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }
        displayFormattedTime(calendar.timeInMillis)
    }

    private val calendar = Calendar.getInstance()

    @SuppressLint("SimpleDateFormat")
    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy")

    @SuppressLint("SimpleDateFormat")
    private val timeFormatter = SimpleDateFormat("HH:mm")
            */