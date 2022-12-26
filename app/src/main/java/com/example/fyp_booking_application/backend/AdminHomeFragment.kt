package com.example.fyp_booking_application.backend

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.fyp_booking_application.AdminDashboardActivity
import com.example.fyp_booking_application.CourtData
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentAdminHomeBinding
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
import java.text.SimpleDateFormat
import java.util.*

class AdminHomeFragment : Fragment() {

    private lateinit var binding: FragmentAdminHomeBinding
    private lateinit var databaseRef: FirebaseFirestore
    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_home, container, false)
        databaseRef = FirebaseFirestore.getInstance()
        val adminView = (activity as AdminDashboardActivity)
        adminView.setTitle("Admin Profile")

        // Add Date Picker to filter which day of transactions
        binding.btnSalesToday.setOnClickListener {
            val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                calendar.apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                }
                val currentDateFormat = SimpleDateFormat("dd/MM/yyyy").format(calendar.time)
                binding.tvSalesToday.text = currentDateFormat

                // Filter
                var bookingSales = 0.0
                databaseRef.collection("Bookings").whereEqualTo("bookingDate", currentDateFormat).get().addOnSuccessListener { documents ->
                    for (document in documents){
                        bookingSales += document["bookingPayment"].toString().toDouble()
                    }
                    binding.tfBookingSales.text = "RM $bookingSales"
                }

                var purchaseSales = 0.0
                databaseRef.collection("Purchases").whereEqualTo("purchaseDate", currentDateFormat).get().addOnSuccessListener { documents ->
                    for (document in documents){
                        purchaseSales += document["purchasePrice"].toString().toDouble()
                    }
                    binding.tfPurchaseSales.text = "RM $purchaseSales"
                }
            }
            DatePickerDialog(requireContext(), dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
            ).show()
        }

        var bookingSales = 0.0
        databaseRef.collection("Bookings").get().addOnSuccessListener { documents ->
            for (document in documents){
                bookingSales += document["bookingPayment"].toString().toDouble()
            }
            binding.tfBookingSales.text = "RM $bookingSales"
        }

        var purchaseSales = 0.0
        databaseRef.collection("Purchases").get().addOnSuccessListener { documents ->
            for (document in documents){
                purchaseSales += document["purchasePrice"].toString().toDouble()
            }
            binding.tfPurchaseSales.text = "RM $purchaseSales"
        }

        var enrollSize: Int ?= null
        val enrollRef = databaseRef.collection("Enroll").whereEqualTo("enrollStatus", "Pending").count()
        enrollRef.get(AggregateSource.SERVER).addOnCompleteListener{ task ->
            if (task.isSuccessful){
                enrollSize = task.result.count.toInt()
                binding.tvPendingEnrollCount.text = enrollSize.toString()
            }
        }

        var bookingSize: Int ?= null
        val bookingRef = databaseRef.collection("Bookings").whereEqualTo("bookingStatus", "Pending").count()
        bookingRef.get(AggregateSource.SERVER).addOnCompleteListener{ task ->
            if (task.isSuccessful){
                enrollSize = task.result.count.toInt()
                binding.tvPendingBooksCount.text = enrollSize.toString()

            }
        }

        binding.btnResetCourt.setOnClickListener{
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Reset Court Availability")
            builder.setMessage("Confirm to reset courts??")
            builder.setPositiveButton("Confirm"){ _, _ ->
                val courtRef = databaseRef.collection("Courts")
                courtRef.get().addOnSuccessListener { documents ->
                    for (document in documents) {
                        val court = document.toObject(CourtData::class.java)
                        val courtSlotSize = document["courtSlots"] as Map<*, *>
                        courtSlotSize.let {
                            for ((key, value) in courtSlotSize){
                                val timeslot = value as Map<*, *>
                                val resetCourtData = hashMapOf(
                                    "courtSlots" to hashMapOf(
                                        "$key" to hashMapOf(
                                            "availability" to true
                                        )
                                    )
                                )
                                courtRef.document(court.courtID.toString()).set(resetCourtData, SetOptions.merge())
                                    .addOnSuccessListener {
                                        Log.d("RESET COURT", "SUCCESSMUAHAHAHA")
                                    }.addOnFailureListener { e ->
                                        Log.e ("RESET COURT", "HAHAHAHHA", e)
                                    }
                            }
                        }
                    }
                }
                Toast.makeText(context, "Court has been Reset", Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("Cancel"){ _, _ -> }
            builder.show()
        }
        return binding.root
    }
}