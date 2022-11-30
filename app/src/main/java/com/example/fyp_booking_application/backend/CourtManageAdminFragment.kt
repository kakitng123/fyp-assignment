package com.example.fyp_booking_application.backend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentCourtManageAdminBinding
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class CourtManageAdminFragment : Fragment() {

    private lateinit var binding: FragmentCourtManageAdminBinding
    private lateinit var databaseRef: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Variable Declarations
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_court_manage_admin, container, false)
        databaseRef = FirebaseFirestore.getInstance()

        // EXAMPLE ADD BUTTON (NOT DONE *able to function as intended, need tidy up)
        binding.btnAddCourt.setOnClickListener{

            val newCourtData = hashMapOf (
                "courtID" to "C0001",
                "courtName" to "A1",
                "courtSlots" to arrayListOf<CourtTimeslots>()
            )

            databaseRef.collection("court_testing2").document("PBpkL1uptOhvzpCQAIQq").set(newCourtData)
                .addOnSuccessListener {
                    Toast.makeText(context, "ADDED SUCCESSFULLY", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "FAILED SUCCESSFULLY", Toast.LENGTH_SHORT).show()
                }
        }

        binding.btnAddTimeslot.setOnClickListener{
            val testing = Integer.parseInt(binding.tfTime.text.toString())
            for (i in testing..testing+5){
                addData(i, 1)
            }



            /*
            val nestedData = hashMapOf(
                "availability" to true,
                "timeslot" to binding.tfTime.text.toString()
            )

            //val courtSlots = HashMap<String, Any>()
            //courtSlots["courtSlots"] = nestedData

            val testing1 = databaseRef.collection("court_testing2").document("PBpkL1uptOhvzpCQAIQq")
            testing1.update("courtSlots", FieldValue.arrayUnion(nestedData))

             */


            /*
            // ABLE TO READ SELECTED TIMESLOT
            val testing = databaseRef.collection("court_testing2").document("PBpkL1uptOhvzpCQAIQq")
            testing.get().addOnSuccessListener { document ->
                if(document != null){
                    val doc = document.toObject(CourtDocument::class.java)
                    if( doc != null){
                        val timeslots = doc.courtSlots
                        if(timeslots != null){
                            // MANUALLY CHANGE (FOR NOW)
                            binding.tvTESTING.text = timeslots[0].timeslot.toString()
                            binding.tvTESTING2.text = timeslots[0].availability.toString()
                        }
                    }
                }
            }

             */

            Toast.makeText(context, "SUCCESS", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }


    //SEARCH FUNCTION
    /*

            val kekw = databaseRef.collection("court_testing2/PBpkL1uptOhvzpCQAIQq/courtSlots").whereEqualTo("availability",true)
            val testing = kekw.count()


            testing.get(AggregateSource.SERVER).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val snapshot = task.result
                    Log.d(TAG, "Count: ${snapshot.count}")
                    binding.tvTESTING.text = task.result.toString()
                } else {
                    Log.d(TAG, "Count failed: ", task.getException())
                }
            }
     */

    // Able to function as intended but need tidy up
    private fun addData(number: Int, increment: Int){

        val nestedData = hashMapOf(
            "availability" to true,
            "timeslot" to "${number}:00 - ${number+increment}:00"
        )
        val testing1 = databaseRef.collection("court_testing2").document("PBpkL1uptOhvzpCQAIQq")
        testing1.update("courtSlots", FieldValue.arrayUnion(nestedData))
    }
}