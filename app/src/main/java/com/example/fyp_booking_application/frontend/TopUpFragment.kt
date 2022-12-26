package com.example.fyp_booking_application.frontend

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.fyp_booking_application.UserDashboardActivity
import com.example.fyp_booking_application.databinding.FragmentTopUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TopUpFragment : Fragment() {
    private lateinit var binding: FragmentTopUpBinding
    private lateinit var auth: FirebaseAuth //get the shared instance of the FirebaseAuth object
    private lateinit var fstore: FirebaseFirestore //get the shared instance of the FirebaseAuth object

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Initialise
        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()
        val userID = auth.currentUser?.uid

        binding = FragmentTopUpBinding.inflate(layoutInflater)
        val userView = (activity as UserDashboardActivity)
        userView.setTitle("Top Up")

        //Top Up Function in My Wallet
        binding.topUpBtn.setOnClickListener {
            val balance: Double = binding.topUpAmount.text.toString().toDouble()

            val userID = auth.currentUser?.uid
            val walletID = fstore.collection("Wallet").document()
            val topUpWallet = hashMapOf(
                "topUpID" to walletID.id,
                "topUpTitle" to "Quick Reload Payment",
                "topUpAmount" to balance,
                "topUpDesc" to "Transfer to Wallet",
                "topUpStatus" to "Success",
                "userID" to userID,
            )
            walletID.set(topUpWallet).addOnSuccessListener {
                Toast.makeText(context, "Top Up Successfully", Toast.LENGTH_SHORT).show()
                userView.replaceFragment(MyWalletFragment())
                Log.d("haha", walletID.toString())
            }.addOnFailureListener {
                Toast.makeText(context, "Top Up Failure", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }
}
