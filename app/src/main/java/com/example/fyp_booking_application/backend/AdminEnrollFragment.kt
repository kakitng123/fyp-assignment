package com.example.fyp_booking_application.backend

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fyp_booking_application.AdminDashboardActivity
import com.example.fyp_booking_application.EnrollData
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.backend.Adapters.ClassEnrollAdminAdapter
import com.example.fyp_booking_application.databinding.FragmentAdminClassEnrolBinding
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase

class AdminEnrollFragment : Fragment(), ClassEnrollAdminAdapter.OnItemClickListener {

    private lateinit var binding: FragmentAdminClassEnrolBinding
    private lateinit var databaseRef: FirebaseFirestore
    private lateinit var enrollArrayList: ArrayList<EnrollData>
    private lateinit var classEnrollAdminAdapter: ClassEnrollAdminAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_class_enrol, container, false)
        enrollArrayList = arrayListOf()

        dataInitialize()
        binding.classPendingRV.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            classEnrollAdminAdapter = ClassEnrollAdminAdapter(enrollArrayList, this@AdminEnrollFragment)
            adapter = classEnrollAdminAdapter
        }

        return binding.root
    }

    override fun onItemClick(position: Int) {
        val currentItem = enrollArrayList[position]
        val adminView = (activity as AdminDashboardActivity)
        adminView.replaceFragment(AdminEnrollDetailFragment(), R.id.classAdminLayout)
        setFragmentResult("toEnrollDetails", bundleOf("toEnrollDetails" to currentItem.enrollID))
    }

    override fun onApproveBtnClick(position: Int) {
        val currentItem = enrollArrayList[position]
        databaseRef = FirebaseFirestore.getInstance()
        val docRef = databaseRef.collection("Enroll").document(currentItem.enrollID.toString())
        val updateStatus = hashMapOf(
            "enrollStatus" to "Approved"
        )
        docRef.set(updateStatus, SetOptions.merge()).addOnSuccessListener {
            Log.d("UPDATE DOCUMENT", "UPDATE DOCUMENT SUCCESSFULLY")
        }.addOnFailureListener { e ->
            Log.e("UPDATE DOCUMENT", "ERROR UPDATING DOCUMENT", e)
        }

        var collectionSize: Int ?= null
        val collection = databaseRef.collection("Notifications").count()
        collection.get(AggregateSource.SERVER).addOnCompleteListener{ task ->
            if (task.isSuccessful){
                collectionSize = task.result.count.toInt()
            }
            val newNotifyRef = databaseRef.collection("Notifications").document()
            val newNotify = hashMapOf(
                "notifyID" to newNotifyRef.id,
                "notifyTitle" to "Enrollment Successful!",
                "notifyMessage" to "Your enrollment for Class ${currentItem.enrollClassName} " +
                        "on ${currentItem.enrollDate} - ${currentItem.enrollTime} has been Approved!",
                "referralCode" to "R${100000+collectionSize!!}",
                "userID" to currentItem.userID.toString()
            )
            newNotifyRef.set(newNotify).addOnSuccessListener {
                Log.d("ADD NOTIFICATION", "NOTIFICATION ADDED SUCCESSFULLY")

            }.addOnFailureListener { e ->
                Log.e("ADD NOTIFICATION", "ERROR ADDING NOTIFICATION", e)
            }
        }
        classEnrollAdminAdapter.notifyItemChanged(position)
        Toast.makeText(context, "Enrollment Approved", Toast.LENGTH_SHORT).show()
    }

    override fun onDeclineBtnClick(position: Int) {
        val currentItem = enrollArrayList[position]

        val docRef = databaseRef.collection("Enroll").document(currentItem.enrollID.toString())
        val builder = AlertDialog.Builder(context)
        val dialogLayout = layoutInflater.inflate(R.layout.dialog_admin_single_field, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.singleFieldData)
        builder.setTitle("Reason for Rejection")
        builder.setView(dialogLayout)
        builder.setPositiveButton("Ok"){ _, _ ->
            val updateStatus = hashMapOf(
                "enrollStatus" to "Rejected"
            )
            docRef.set(updateStatus, SetOptions.merge()).addOnSuccessListener {
                Log.d("UPDATE DOCUMENT", "UPDATE DOCUMENT SUCCESSFULLY")

            }.addOnFailureListener { e ->
                Log.e("UPDATE DOCUMENT", "ERROR UPDATING DOCUMENT", e)
            }

            var collectionSize: Int ?= null
            val collection = databaseRef.collection("Notifications").count()
            collection.get(AggregateSource.SERVER).addOnCompleteListener{ task ->
                if (task.isSuccessful){
                    collectionSize = task.result.count.toInt()
                }
                val newNotifyRef = databaseRef.collection("Notifications").document()
                val newNotify = hashMapOf(
                    "notifyID" to newNotifyRef.id,
                    "notifyTitle" to "Enrollment Rejected",
                    "notifyMessage" to "Your enrollment for Class ${currentItem.enrollClassName} " +
                            "on ${currentItem.enrollDate} - ${currentItem.enrollTime} has been Rejected. \n\n " +
                            "Reason: ${editText.text}",
                    "referralCode" to "R${100000+collectionSize!!}",
                    "userID" to currentItem.userID.toString()
                )
                newNotifyRef.set(newNotify).addOnSuccessListener {
                    Log.d("ADD NOTIFICATION", "NOTIFICATION ADDED SUCCESSFULLY")
                    Toast.makeText(context, "Enrollment Rejected", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener { e ->
                    Log.e("ADD NOTIFICATION", "ERROR ADDING NOTIFICATION", e)
                }
            }
        }
        builder.setNegativeButton("Cancel"){ _, _ -> }
        builder.show()
    }

    private fun dataInitialize(){
        databaseRef = FirebaseFirestore.getInstance()
        databaseRef.collection("Enroll").whereEqualTo("enrollStatus", "Pending")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if(error != null){
                        Log.e("FAILED INITIALIZATION", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){
                            enrollArrayList.add(dc.document.toObject(EnrollData::class.java))
                        }
                    }
                    if (enrollArrayList.size == 0){
                        binding.tvClassPending.visibility = View.VISIBLE
                    }
                    else binding.tvClassPending.visibility = View.GONE

                    classEnrollAdminAdapter.notifyDataSetChanged()
                }
            })
    }
}