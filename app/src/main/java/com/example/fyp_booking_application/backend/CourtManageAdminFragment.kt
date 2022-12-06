package com.example.fyp_booking_application.backend

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
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.backend.Adapters.CourtManageAdminAdapter
import com.example.fyp_booking_application.backend.Adapters.TimeslotAdminAdapter
import com.example.fyp_booking_application.databinding.FragmentCourtManageAdminBinding
import com.google.firebase.firestore.*

class CourtManageAdminFragment : Fragment(), CourtManageAdminAdapter.OnItemClickListener {

    private lateinit var binding: FragmentCourtManageAdminBinding
    private lateinit var databaseRef: FirebaseFirestore
    private lateinit var courtList : ArrayList<CourtData>
    private lateinit var timeslotList : ArrayList<CourtTimeslots>
    private lateinit var courtManageAdapter : CourtManageAdminAdapter
    private lateinit var timeslotAdapter : TimeslotAdminAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Variable Declarations
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_court_manage_admin, container, false)
        databaseRef = FirebaseFirestore.getInstance()

        dataInitialize()
        binding.courtRecyclerView.apply{
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            courtList = arrayListOf()
            courtManageAdapter = CourtManageAdminAdapter(courtList, this@CourtManageAdminFragment)
            adapter = courtManageAdapter
        }

        // Adding new court into database
        binding.imgbtnAddCourt.setOnClickListener {
            val dialogLayout = layoutInflater.inflate(R.layout.dialog_edittext1, null)
            val editText = dialogLayout.findViewById<EditText>(R.id.dialog_editText)
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Enter Court Name")
            builder.setView(dialogLayout)
            builder.setPositiveButton("Add"){ _, _ ->
                // Validation can be done here to not allow redundant court name
                val newCourtRef = databaseRef.collection("court_testing2").document()
                val newCourtData = hashMapOf(
                    "courtID" to newCourtRef.id, // CourtID = DocumentID *for now, could change later.
                    "courtName" to editText.text.toString(),
                    "courtSlots" to arrayListOf<CourtTimeslots>()
                )
                newCourtRef.set(newCourtData)
                    .addOnSuccessListener { Log.d("ADDING COURT DATA", "COURT ADDED SUCCESSFULLY") }
                    .addOnFailureListener { e -> Log.e("ADDING COURT DATA", "ERROR ADDING DATA", e) }
            }
            builder.setNegativeButton("Cancel"){ _, _ -> }
            builder.show()
        }
        return binding.root
    }

    override fun onItemClick(position: Int) {
        val currentItem = courtList[position]

        displayTimeslots(currentItem, position)

        val ss = SpannableString("Click Here to Add Timeslot")
        val clickableSpan: ClickableSpan = object : ClickableSpan(){
            override fun onClick(textView: View) {
                val dialogLayout = layoutInflater.inflate(R.layout.dialog_addtimeslot, null)
                val radioGroup = dialogLayout.findViewById<RadioGroup>(R.id.radioGroup1)
                val builder = AlertDialog.Builder(requireContext())

                builder.setTitle("Select Timeslot")
                builder.setView(dialogLayout)
                builder.setPositiveButton("Add"){ _, _ ->
                    val increment: Int = when(radioGroup.checkedRadioButtonId){
                        R.id.rdBtnSlotA -> 1
                        R.id.rdBtnSlotB -> 2
                        else -> {
                            Log.d("TESTING WATER", "HAHAHHAHAHAH")
                        }
                    }
                    for (i in 10..22 step increment){
                        addData(i, increment, currentItem.courtID.toString())
                    }
                }
                builder.setNegativeButton("Cancel"){_,_ -> }
                builder.show()
            }
        }
        ss.setSpan(clickableSpan, 6, 10 , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvTesting.text = ss
        binding.tvTesting.movementMethod = LinkMovementMethod.getInstance()

    }

    // Get/Parse Data into RecyclerView
    private fun dataInitialize(){
        databaseRef = FirebaseFirestore.getInstance()
        databaseRef.collection("court_testing2") //.whereEqualTo("courtName", "A1")
            .addSnapshotListener(object : EventListener<QuerySnapshot>{
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

    // Able to function as intended but need tidy up (INCOMPLETE)
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
            Toast.makeText(context, "ERROR: EMPTY ARRAY", Toast.LENGTH_SHORT).show()
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