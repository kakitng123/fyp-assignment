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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.AdminDashboardActivity
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.backend.Adapters.CourtAdminAdapter
import com.example.fyp_booking_application.backend.Adapters.TimeslotAdminAdapter
import com.example.fyp_booking_application.databinding.FragmentAdminCourtBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.*

class AdminCourtFragment : Fragment(), CourtAdminAdapter.OnItemClickListener {

    private lateinit var binding: FragmentAdminCourtBinding
    private lateinit var databaseRef: FirebaseFirestore
    private lateinit var courtList: ArrayList<TestCourtData>
    private lateinit var timeslotList: ArrayList<TestCourtData2>
    private lateinit var courtManageAdapter: CourtAdminAdapter
    private lateinit var timeslotAdapter: TimeslotAdminAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_court, container, false)
        binding.imgBtnRefreshCourt.bringToFront()
        binding.imgBtnAddCourt.bringToFront()
        databaseRef = FirebaseFirestore.getInstance()
        val adminActivityView = (activity as AdminDashboardActivity)
        adminActivityView.setTitle("COURT MANAGEMENT")
        courtList = arrayListOf()

        dataInitialize()
        binding.courtRecyclerView.apply {
            courtList.sortedBy { list -> list.courtName }
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
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
                if (editText.text.isEmpty()) {
                    Toast.makeText(context, "EMPTY FIELD", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                } else if (!editText.text.matches("^[a-zA-Z0-9_]*$".toRegex())) {
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
                                "courtName" to editText.text.toString()
                            )
                            newCourtRef.set(newCourtData)
                                .addOnSuccessListener {
                                    Log.d(
                                        "ADDING COURT DATA",
                                        "COURT ADDED SUCCESSFULLY"
                                    )
                                }
                                .addOnFailureListener { e ->
                                    Log.e(
                                        "ADDING COURT DATA",
                                        "ERROR ADDING DATA",
                                        e
                                    )
                                }
                        } else Toast.makeText(context, "EXISTING COURT NAME", Toast.LENGTH_SHORT)
                            .show()
                    }
            }
            builder.setNegativeButton("Cancel") { _, _ -> }
            builder.show()
        }

        binding.imgBtnRefreshCourt.setOnClickListener {
            adminActivityView.replaceFragment(AdminCourtFragment(), R.id.adminLayout)
        }

        ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean { return false }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedCourt: TestCourtData = courtList[viewHolder.adapterPosition]
                val position = viewHolder.adapterPosition
                courtList.removeAt(viewHolder.adapterPosition)
                courtManageAdapter.notifyItemRemoved(viewHolder.adapterPosition)

                Snackbar.make(binding.courtRecyclerView, "Deleted ${deletedCourt.courtName}", Snackbar.LENGTH_LONG)
                    .setAction("Undo") {
                        courtList.add(position, deletedCourt)
                        courtManageAdapter.notifyItemInserted(position)
                    }.show()
            }
        }).attachToRecyclerView(binding.courtRecyclerView)

        return binding.root
    }

    override fun onItemClick(position: Int) {
        val currentItem = courtList[position]
        displayTimeslots(currentItem)

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
                    addData(increment, currentItem.courtID.toString())

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
        val docRef =
            databaseRef.collection("court_testing2").document(currentItem.courtID.toString())

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete Court Data")
        builder.setMessage("Confirm to delete court data?")
        builder.setPositiveButton("Delete") { _, _ ->
            docRef.delete().addOnSuccessListener {
                Log.d("DELETE COURT", "COURT DELETED SUCCESSFULLY")
            }.addOnFailureListener { e ->
                Log.e("DELETE COURT", "ERROR DELETING COURT", e)
            }
        }
        builder.setNegativeButton("Cancel") { _, _ -> }
        builder.show()
    }

    private fun dataInitialize() {
        databaseRef = FirebaseFirestore.getInstance()
        databaseRef.collection("court_testing2")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("FAILED INITIALIZATION", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            courtList.add(dc.document.toObject(TestCourtData::class.java))
                        }
                    }
                    courtManageAdapter.notifyDataSetChanged()
                }
            })
    }

    private fun displayTimeslots(currentItem: TestCourtData) {
        timeslotList = arrayListOf()
        databaseRef = FirebaseFirestore.getInstance()

        if (currentItem.courtSlots?.size == null) {
            Toast.makeText(context, "NO TIME SLOTS", Toast.LENGTH_SHORT).show()
            binding.tvTesting.visibility = View.VISIBLE
        } else {
            binding.tvTesting.visibility = View.INVISIBLE
            val docRef = databaseRef.collection("court_testing2").document(currentItem.courtID.toString())
            docRef.get().addOnCompleteListener { document ->
                if (document.isSuccessful) {
                    val courtSlot = document.result["courtSlots"] as Map<*, *>
                    courtSlot.let {
                        for ((key, value) in courtSlot) {
                            val timeslot = value as Map<*, *>
                            val slots = timeslot["timeslot"]
                            val available = timeslot["availability"]
                            val testing123 =
                                TestCourtData2(slots.toString(), available.toString().toBoolean())

                            timeslotList.add(testing123)
                        }
                    }
                    binding.timeslotRecyclerView.apply {
                        timeslotList.sortBy { list -> list.timeslot }
                        layoutManager = LinearLayoutManager(context)
                        setHasFixedSize(true)
                        timeslotAdapter = TimeslotAdminAdapter(timeslotList)
                        adapter = timeslotAdapter
                    }
                }
            }
        }
    }

    private fun addData(increment: Int, court_id: String) {
        val courtRef = databaseRef.collection("court_testing2").document(court_id)
        var alphabet = 'A'

        for (j in 10..22 step increment) {
            val newTimeslot = hashMapOf(
                "courtSlots" to hashMapOf(
                    "Slots$alphabet" to hashMapOf(
                        "timeslot" to "${j}:00 - ${j + increment}:00",
                        "availability" to true
                    )
                )
            )
            courtRef.set(newTimeslot, SetOptions.merge()).addOnSuccessListener {
                Log.d("ADD TIMESLOT", "TIMESLOT ADDED SUCCESSFULLY")
            }.addOnFailureListener { e ->
                Log.e("ADD TIMESLOT", "ERROR ADDING TIMESLOT", e)
            }
            ++alphabet
        }
    }
}
