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
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.*
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.backend.Adapters.CourtAdminAdapter
import com.example.fyp_booking_application.backend.Adapters.CourtTimeslotAdminAdapter
import com.example.fyp_booking_application.databinding.FragmentAdminCourtBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.*

class AdminCourtFragment : Fragment(), CourtAdminAdapter.OnItemClickListener {

    private lateinit var binding: FragmentAdminCourtBinding
    private lateinit var databaseRef: FirebaseFirestore
    private lateinit var courtList: ArrayList<CourtData>
    private lateinit var timeslotList: ArrayList<CourtDataTimeslot>
    private lateinit var courtManageAdapter: CourtAdminAdapter
    private lateinit var timeslotAdapter: CourtTimeslotAdminAdapter

    @SuppressLint("SetTextI18n")
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
            val dialogLayout = layoutInflater.inflate(R.layout.dialog_admin_add_court, null)
            val newCourtName = dialogLayout.findViewById<EditText>(R.id.courtNameField)
            val newCourtSlots = dialogLayout.findViewById<RadioGroup>(R.id.courtTimeslotField)
            val tvCourtPrice = dialogLayout.findViewById<TextView>(R.id.tvCourtPrice)

            
            newCourtSlots.setOnCheckedChangeListener { _, checkedButtonID ->
                when(checkedButtonID){
                    R.id.rdbtnTimeslot1 -> tvCourtPrice.text = "Price per booking: RM 15"
                    R.id.rdbtnTimeslot2 -> tvCourtPrice.text = "Price per booking: RM 25"
                }
            }

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Enter Court Name")
            builder.setView(dialogLayout)
            builder.setPositiveButton("Add") { _, _ ->
                if (newCourtName.text.isEmpty()) {
                    Toast.makeText(context, "EMPTY FIELD", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                } else if (!newCourtName.text.matches("^[a-zA-Z0-9_]*$".toRegex())) {
                    Toast.makeText(context, "ONLY ALPHANUMERIC ALLOWED", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                var nameValidation = 0
                databaseRef.collection("Courts").get()
                    .addOnSuccessListener { results ->
                        for (document in results) {
                            if (document["courtName"] == newCourtName.text.toString()) {
                                nameValidation += 1
                            }
                        }
                        if (nameValidation == 0) {
                            val newCourtPrice: Double = when(newCourtSlots.checkedRadioButtonId){
                                R.id.rdbtnTimeslot1 -> 15.0
                                R.id.rdbtnTimeslot2 -> 25.0
                                else -> 0.0
                            }
                            val newCourtRef = databaseRef.collection("Courts").document()
                            val newCourtData = hashMapOf(
                                "courtID" to newCourtRef.id,
                                "courtName" to newCourtName.text.toString(),
                                "courtPrice" to newCourtPrice
                            )
                            newCourtRef.set(newCourtData).addOnSuccessListener {
                                Log.d("ADDING COURT DATA", "COURT ADDED SUCCESSFULLY")
                            }.addOnFailureListener { e ->
                                Log.e("ADDING COURT DATA", "ERROR ADDING DATA", e)
                            }
                        } else Toast.makeText(context, "EXISTING COURT NAME", Toast.LENGTH_SHORT).show()
                    }

            }
            builder.setNegativeButton("Cancel") { _, _ -> }
            builder.show()
        }

        binding.imgBtnRefreshCourt.setOnClickListener {
            adminActivityView.replaceFragment(AdminCourtFragment(), R.id.adminLayout)
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedCourt: CourtData = courtList[viewHolder.adapterPosition]
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

        // Unable to add timeslot for now
        // courtPrice issue / design issue, radio btn needs to be clicked
        val ss = SpannableString("Click Here to Add Timeslot")
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                if(currentItem.courtPrice.toString() == "15"){
                    addData(1, currentItem.courtID.toString())
                }else if (currentItem.courtPrice.toString() == "25"){
                    addData(2, currentItem.courtID.toString())
                }
            }

        }
        ss.setSpan(clickableSpan, 6, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvTesting.text = ss
        binding.tvTesting.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun dataInitialize() {
        databaseRef = FirebaseFirestore.getInstance()
        databaseRef.collection("Courts")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("FAILED INITIALIZATION", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            courtList.add(dc.document.toObject(CourtData::class.java))
                        }
                    }
                    courtList.sortByDescending { it.courtName }
                    courtManageAdapter.notifyDataSetChanged()
                }
            })
    }

    private fun displayTimeslots(currentItem: CourtData) {
        timeslotList = arrayListOf()
        databaseRef = FirebaseFirestore.getInstance()

        if (currentItem.courtSlots?.size == null) {
            binding.timeslotRecyclerView.visibility = View.INVISIBLE
            binding.tvTesting.visibility = View.VISIBLE
        } else {
            binding.timeslotRecyclerView.visibility = View.VISIBLE
            binding.tvTesting.visibility = View.INVISIBLE
            val docRef = databaseRef.collection("Courts").document(currentItem.courtID.toString())
            docRef.get().addOnCompleteListener { document ->
                if (document.isSuccessful) {
                    val courtSlot = document.result["courtSlots"] as Map<*, *>
                    courtSlot.let {
                        for ((_, value) in courtSlot) {
                            val timeslot = value as Map<*, *>
                            val slots = timeslot["timeslot"]
                            val available = timeslot["availability"]
                            val testing123 = CourtDataTimeslot(slots.toString(), available.toString().toBoolean())
                            timeslotList.add(testing123)
                        }
                    }
                    binding.timeslotRecyclerView.apply {
                        timeslotList.sortBy { list -> list.timeslot }
                        layoutManager = LinearLayoutManager(context)
                        setHasFixedSize(true)
                        timeslotAdapter = CourtTimeslotAdminAdapter(timeslotList)
                        adapter = timeslotAdapter
                    }
                }
            }
        }
    }

    private fun addData(increment: Int, court_id: String) {
        val courtRef = databaseRef.collection("Courts").document(court_id)
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
