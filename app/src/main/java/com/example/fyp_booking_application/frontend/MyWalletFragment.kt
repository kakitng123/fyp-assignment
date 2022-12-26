package com.example.fyp_booking_application.frontend

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.fyp_booking_application.UserDashboardActivity
import com.example.fyp_booking_application.databinding.FragmentMyWalletBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyWalletFragment : Fragment() {
    private lateinit var auth: FirebaseAuth //get the shared instance of the FirebaseAuth object
    private lateinit var fstore: FirebaseFirestore //get the shared instance of the FirebaseAuth object
    private lateinit var binding: FragmentMyWalletBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialise
        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()
        val userID = auth.currentUser?.uid

        binding = FragmentMyWalletBinding.inflate(layoutInflater)
        val userView = (activity as UserDashboardActivity)
        userView.setTitle("My Wallet")

//        setFragmentResultListener("toMyWallet") { _, bundle ->
//            Log.d("haha", walletID.toString())
//        val amountResult = resultData.result.getString("topUpAmount").toString()
//        val descResult = resultData.result.getString("topUpDesc").toString()
//        val statusResult = resultData.result.getString("topUpStatus").toString()
//
//        binding.balanceAmt.setText(amountResult)
//        binding.balanceAmt.setText(descResult)
//        binding.balanceAmt.setText(statusResult)
        //Retrieve balance amount

        var balanceAmount = 0.00
        fstore.collection("Wallet").get().addOnSuccessListener { documents ->
            for (document in documents){
                balanceAmount += document["topUpAmount"].toString().toDouble()
            }
//            binding.balanceAmt.text = "RM $balanceAmount"
            binding.balanceAmt.text = balanceAmount.toString()

        }.addOnFailureListener { exception ->
            Log.d("noexits", "Error getting documents.", exception)
        }

        binding.historyBookingBtn.setOnClickListener {
            Toast.makeText(context, "View Successfully", Toast.LENGTH_SHORT).show()
            userView.replaceFragment(BookingCourtHistoryFragment())
        }
        binding.historyEnrollBtn.setOnClickListener {
            Toast.makeText(context, "View Successfully", Toast.LENGTH_SHORT).show()
            userView.replaceFragment(EnrollClassHistoryFragment())
        }
        binding.historyPurchaseBtn.setOnClickListener {
            Toast.makeText(context, "View Successfully", Toast.LENGTH_SHORT).show()
            userView.replaceFragment(PurchaseProductHistoryFragment())
        }
        binding.historyTopUpBtn.setOnClickListener {
            Toast.makeText(context, "View Successfully", Toast.LENGTH_SHORT).show()
            userView.replaceFragment(TopUpWalletHistoryFragment())
        }

        binding.reloadBtn.setOnClickListener {
            Toast.makeText(context, "View Successfully", Toast.LENGTH_SHORT).show()
            userView.replaceFragment(TopUpFragment())
        }
        return binding.root
    }
}
//
//            val builder = AlertDialog.Builder(requireContext())
//            builder.setTitle("Top Up Balance")
//            builder.setMessage("Confirm to top up?")
//            builder.setPositiveButton("Top Up") { _, _ ->
//                val userID = auth.currentUser?.uid
//                val walletID = fstore.collection("Wallet").document()
//                val topUpWallet = hashMapOf(
//                    "walletID" to walletID,
//                    "topUpAmount" to binding.topUpAmount.text.toString().toDouble(),
//                    "topUpDesc" to "Top Up",
//                    "topUpStatus" to "Status",
//                )

