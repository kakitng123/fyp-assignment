package com.example.fyp_booking_application.backend

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fyp_booking_application.AdminDashboardActivity
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.backend.Adapters.CourtAdminAdapter
import com.example.fyp_booking_application.backend.Adapters.TimeslotAdminAdapter
import com.example.fyp_booking_application.databinding.FragmentAdminCourtBinding
import com.google.firebase.firestore.*

class AdminCourtFragment : Fragment(), CourtAdminAdapter.OnItemClickListener {

    private lateinit var binding: FragmentAdminCourtBinding
    private lateinit var databaseRef: FirebaseFirestore
    private lateinit var courtList : ArrayList<CourtData>
    private lateinit var timeslotList : ArrayList<CourtTimeslots>
    private lateinit var courtManageAdapter : CourtAdminAdapter
    private lateinit var timeslotAdapter : TimeslotAdminAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_court, container, false)
        databaseRef = FirebaseFirestore.getInstance()
        val adminActivityView = (activity as AdminDashboardActivity)
        adminActivityView.setTitle("COURT MANAGEMENT")

        dataInitialize()
        binding.courtRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            courtList = arrayListOf()
            courtManageAdapter = CourtAdminAdapter(courtList, this@AdminCourtFragment)
            adapter = courtManageAdapter
        }

        binding.imgBtnAddCourt.setOnClickListener {
            val dialogLayout = layoutInflater.inflate(R.layout.dialog_admin_edittext1, null)
            val editText = dialogLayout.findViewById<EditText>(R.id.dataEditText)
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Enter Court Name")
            builder.setView(dialogLayout)
            builder.setPositiveButton("Add") { _, _ ->
                if(editText.text.isEmpty()){
                    Toast.makeText(context, "EMPTY FIELD", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                else if(!editText.text.matches("^[a-zA-Z0-9_]*$".toRegex())){
                    Toast.makeText(context, "ONLY ALPHANUMERIC ALLOWED", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                var nameValidation = 0
                databaseRef.collection("court_testing2").get()
                    .addOnSuccessListener { results ->
                        for (document in results) {
                            if (document["courtName"] == editText.text.toString()) {
                                nameValidation += 1
                            }
                        }
                        if (nameValidation == 0) {
                            val newCourtRef = databaseRef.collection("court_testing2").document()
                            val newCourtData = hashMapOf(
                                "courtID" to newCourtRef.id,
                                "courtName" to editText.text.toString(),
                                "courtSlots" to arrayListOf<CourtTimeslots>()
                            )
                            newCourtRef.set(newCourtData)
                                .addOnSuccessListener { Log.d("ADDING COURT DATA", "COURT ADDED SUCCESSFULLY") }
                                .addOnFailureListener { e -> Log.e("ADDING COURT DATA", "ERROR ADDING DATA", e ) }
                        } else Toast.makeText(context, "EXISTING COURT NAME", Toast.LENGTH_SHORT).show()
                    }
            }
            builder.setNegativeButton("Cancel"){ _, _ -> }
            builder.show()
        }
        binding.imgBtnRefreshCourt.setOnClickListener{
            adminActivityView.replaceFragment(AdminCourtFragment(), R.id.adminLayout)
        }
        return binding.root
    }

    override fun onItemClick(position: Int) {
        val currentItem = courtList[position]

        displayTimeslots(currentItem, position)

        val ss = SpannableString("Click Here to Add Timeslot")
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                val dialogLayout = layoutInflater.inflate(R.layout.dialog_admin_add_timeslot, null)
                val radioGroup = dialogLayout.findViewById<RadioGroup>(R.id.radioGroup1)
                val builder = AlertDialog.Builder(requireContext())

                builder.setTitle("Select Timeslot")
                builder.setView(dialogLayout)
                builder.setPositiveButton("Add") { _, _ ->
                    val increment: Int = when (radioGroup.checkedRadioButtonId) {
                        R.id.rdBtnSlotA -> 1
                        R.id.rdBtnSlotB -> 2
                        else -> {
                            Log.d("FAIL TIMESLOT", "FAIL TIMESLOT")
                        }
                    }
                    for (i in 10..22 step increment) {
                        addData(i, increment, currentItem.courtID.toString())
                    }
                }
                builder.setNegativeButton("Cancel") { _, _ -> }
                builder.show()
            }
        }
        ss.setSpan(clickableSpan, 6, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvTesting.text = ss
        binding.tvTesting.movementMethod = LinkMovementMethod.getInstance()

    }

    override fun onButtonClick(position: Int) {
        val currentItem = courtList[position]
        databaseRef = FirebaseFirestore.getInstance()
        val docRef = databaseRef.collection("court_testing2").document(currentItem.courtID.toString())

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete Court Data")
        builder.setMessage("Confirm to delete court data?")
        builder.setPositiveButton("Delete"){ _, _ ->
            docRef.delete().addOnSuccessListener {
                Log.d("DELETE COURT", "COURT DELETED SUCCESSFULLY")
            }.addOnFailureListener { e ->
                Log.e("DELETE COURT", "ERROR DELETING COURT", e)
            }
        }
        builder.setNegativeButton("Cancel"){ _, _ -> }
        builder.show()
    }


    private fun dataInitialize(){
        databaseRef = FirebaseFirestore.getInstance()
        databaseRef.collection("court_testing2") //.whereEqualTo("courtName", "A1")
            .addSnapshotListener(object : EventListener<QuerySnapshot>{
                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if(error != null){
                        Log.e("FAILED INITIALIZATION", error.message.toString())
                        return
                    }
                    for (dc : DocumentChange in value?.documentChanges!! ){
                        if( dc.type ==  DocumentChange.Type.ADDED){
                            courtList.add(dc.document.toObject(CourtData::class.java))
                        }
                    }
                    courtManageAdapter.notifyDataSetChanged()
                }
            })
    }

    private fun addData(number: Int, increment: Int, document_id:String){
        val nestedData = hashMapOf(
            "availability" to true,
            "timeslot" to "${number}:00 - ${number+increment}:00"
        )
        val testing1 = databaseRef.collection("court_testing2").document(document_id)
        testing1.update("courtSlots", FieldValue.arrayUnion(nestedData))
    }

    private fun displayTimeslots(currentItem: CourtData, position: Int){
        timeslotList = arrayListOf()

        val timeslotSize = (courtList[position].courtSlots!!.size)-1
        if (timeslotSize < 0){
            timeslotList.clear()
            Toast.makeText(context, "NO TIME SLOTS", Toast.LENGTH_SHORT).show()
            binding.tvTesting.visibility = View.VISIBLE
        }
        else {
            binding.tvTesting.visibility = View.INVISIBLE
            for (i in 0..timeslotSize){
                timeslotList.add(currentItem.courtSlots!![i])
            }
        }
        binding.timeslotRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            timeslotAdapter = TimeslotAdminAdapter(timeslotList)
            adapter = timeslotAdapter
        }
    }
}