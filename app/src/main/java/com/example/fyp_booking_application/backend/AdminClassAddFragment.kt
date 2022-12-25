package com.example.fyp_booking_application.backend

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResultListener
import com.example.fyp_booking_application.AdminDashboardActivity
import com.example.fyp_booking_application.CoachData
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentAdminClassAddBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class AdminClassAddFragment : Fragment(){

    private lateinit var binding: FragmentAdminClassAddBinding
    private lateinit var databaseRef: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_class_add, container, false)
        binding.tvBackClassAdd.bringToFront()

        val adminView = (activity as AdminDashboardActivity)
        adminView.setTitle("Add New Training Class")
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

            binding.LLSlot1.setOnClickListener {
                addClassSlot(1, binding.tvClass1Time)
            }

            binding.LLSlot2.setOnClickListener {
                addClassSlot(2, binding.tvClass2Time)
            }

            binding.LLSlot3.setOnClickListener {
                addClassSlot(3, binding.tvClass3Time)
            }


            binding.imgBtnAddClass.setOnClickListener {
                val coachRef = databaseRef.collection("Coaches").document(coachID.toString())
                coachRef.get().addOnSuccessListener { documentSnapshot ->
                    val coach = documentSnapshot.toObject(CoachData::class.java)

                    val validName = binding.nameContainer.helperText == null
                    val validDesc = binding.descContainer.helperText == null
                    val validPrice = binding.priceContainer.helperText == null
                    val validTimeSlot1 = binding.tvClass1Time.text != null
                    val validTimeSlot2 = binding.tvClass2Time.text != null
                    val validTimeSlot3 = binding.tvClass3Time.text != null

                    if (validName && validDesc && validPrice && validTimeSlot1 && validTimeSlot2 && validTimeSlot3) {
                        var nameValidation = 0
                        databaseRef.collection("TrainingClasses").get()
                            .addOnSuccessListener { results ->
                                for (document in results) {
                                    if (document["className"] == binding.tfAddClassName.text.toString()) {
                                        nameValidation += 1
                                    }
                                }
                                if (nameValidation == 0) {
                                    val newClassRef = databaseRef.collection("TrainingClasses").document()
                                    val newClass = hashMapOf(
                                        "classID" to newClassRef.id,
                                        "className" to binding.tfAddClassName.text.toString(),
                                        "classDesc" to binding.tfAddClassDesc.text.toString(),
                                        "classPrice" to binding.tfAddClassPrice.text.toString().toDouble(),
                                        "classSlot" to hashMapOf(
                                            "Slots1" to binding.tvClass1Time.text,
                                            "Slots2" to binding.tvClass2Time.text,
                                            "Slots3" to binding.tvClass3Time.text
                                        ),
                                        "entitledCoach" to coach?.coachName.toString()
                                    )
                                    newClassRef.set(newClass).addOnSuccessListener {
                                        Log.d("ADDING NEW CLASS", "CLASS ADDED SUCCESSFULLY")
                                        adminView.replaceFragment(AdminCoachFragment(), R.id.adminLayout)
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
                    adminView.replaceFragment(AdminCoachFragment(), R.id.adminLayout)
                }
            }
        }
        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    private fun addClassSlot(title: Int, textView: TextView) {
        var stringOmega = ""

        val dialogLayout = layoutInflater.inflate(R.layout.dialog_admin_add_classtime, null)
        val builder = AlertDialog.Builder(context)
        val calendar = Calendar.getInstance()
        val tfStartTime = dialogLayout.findViewById<EditText>(R.id.dialog_tfStart)
        val tfEndTime = dialogLayout.findViewById<EditText>(R.id.dialog_tfEnd)
        val imgBtnStartTime = dialogLayout.findViewById<ImageButton>(R.id.imgBtnStartTime)
        val imgBtnEndTime = dialogLayout.findViewById<ImageButton>(R.id.imgBtnEndTime)

        imgBtnStartTime.setOnClickListener {
            val timeSetListener =
                TimePickerDialog.OnTimeSetListener { _: TimePicker?, hour: Int, minute: Int ->
                    calendar.apply {
                        set(Calendar.HOUR_OF_DAY, hour)
                        set(Calendar.MINUTE, minute)
                    }
                    tfStartTime.setText(SimpleDateFormat("HH:mm").format(calendar.time))
                }

            TimePickerDialog(
                context, AlertDialog.THEME_HOLO_LIGHT, timeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }

        imgBtnEndTime.setOnClickListener {
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _: TimePicker?, hour: Int, minute: Int ->
                    calendar.apply {
                        set(Calendar.HOUR_OF_DAY, hour)
                        set(Calendar.MINUTE, minute)
                    }
                    tfEndTime.setText(SimpleDateFormat("HH:mm").format(calendar.time))
                }
            TimePickerDialog(
                context, AlertDialog.THEME_HOLO_LIGHT, timeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }

        builder.setTitle("Enter Time for Slots${title}")
        builder.setView(dialogLayout)
        builder.setPositiveButton("Add") { _, _ ->
            stringOmega += "${tfStartTime.text} - ${tfEndTime.text}"
            textView.text = stringOmega
        }
        builder.setNegativeButton("Cancel") { _, _ -> }
        builder.show()
    }
}