package com.example.fyp_booking_application.backend

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fyp_booking_application.AdminDashboardActivity
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.backend.Adapters.CoachClassAdminAdapter
import com.example.fyp_booking_application.databinding.FragmentAdminCoachDetailBinding
import com.google.firebase.firestore.*

class AdminCoachDetailFragment : Fragment(), CoachClassAdminAdapter.OnItemClickListener {

    private lateinit var binding: FragmentAdminCoachDetailBinding
    private lateinit var databaseRef: FirebaseFirestore
    private lateinit var coachClassList: ArrayList<ClassData2>
    private lateinit var coachClassAdminAdapter: CoachClassAdminAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_coach_detail, container, false)
        val adminActivityView = (activity as AdminDashboardActivity)

        setFragmentResultListener("toCoachDetails"){ _, bundle ->
            val coachID = bundle.getString("toCoachDetails")
            databaseRef = FirebaseFirestore.getInstance()
            val docRef = databaseRef.collection("coach_testing1").document(coachID.toString())
            docRef.get().addOnSuccessListener { document ->
                if(document != null){
                    val coach = document.toObject(CoachData::class.java)

                    dataInitialize(coach?.coachName.toString())
                    binding.rvCoachDetailClasses.apply{
                        layoutManager = LinearLayoutManager(context)
                        setHasFixedSize(true)
                        coachClassList = arrayListOf()
                        coachClassAdminAdapter = CoachClassAdminAdapter(coachClassList, this@AdminCoachDetailFragment)
                        adapter = coachClassAdminAdapter
                    }

                    binding.swUpdateCoach.setOnCheckedChangeListener{ _, isChecked ->
                        binding.tfCoachDetailName.isEnabled = isChecked
                        binding.tfCoachDetailEmail.isEnabled = isChecked
                        binding.tfCoachDetailPhone.isEnabled = isChecked
                        binding.tfCoachDetailExp.isEnabled = isChecked
                    }

                    binding.tfCoachDetailID.setText(coach?.coachID.toString())
                    binding.tfCoachDetailName.setText(coach?.coachName.toString())
                    binding.tfCoachDetailEmail.setText(coach?.coachEmail.toString())
                    binding.tfCoachDetailPhone.setText(coach?.coachPhone.toString())
                    binding.tfCoachDetailExp.setText(coach?.coachExp.toString())

                    binding.imgbtnUpdateCoach.setOnClickListener{
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setTitle("Update Coach Details")
                        builder.setMessage("Confirm to update coach details?")
                        builder.setPositiveButton("Update"){ _, _ ->
                            val updateCoach = hashMapOf(
                                "coachName" to binding.tfCoachDetailName.text.toString(),
                                "coachEmail" to binding.tfCoachDetailEmail.text.toString(),
                                "coachExp" to binding.tfCoachDetailExp.text.toString(),
                                "coachPhone" to binding.tfCoachDetailPhone.text.toString()
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

            binding.imgbtnDeleteCoach.setOnClickListener{
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


        }
        binding.tvCoachBack.setOnClickListener{
            adminActivityView.replaceFragment(AdminCoachFragment(), R.id.adminLayout)
        }

        binding.imgbtnAddClass.setOnClickListener{
            adminActivityView.replaceFragment(AdminClassAddFragment(), R.id.adminLayout)
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
                            coachClassList.add(dc.document.toObject(ClassData2::class.java))
                        }
                    }
                    coachClassAdminAdapter.notifyDataSetChanged()
                }

            })
    }
}
