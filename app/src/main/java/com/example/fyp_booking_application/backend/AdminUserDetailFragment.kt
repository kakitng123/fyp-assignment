package com.example.fyp_booking_application.backend

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResultListener
import com.example.fyp_booking_application.AdminDashboardActivity
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentAdminUserDetailBinding
import com.google.firebase.firestore.FirebaseFirestore

class AdminUserDetailFragment : Fragment() {

    private lateinit var binding: FragmentAdminUserDetailBinding
    private lateinit var databaseRef: FirebaseFirestore


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_user_detail, container, false)
        databaseRef = FirebaseFirestore.getInstance()
        val adminActivityView = (activity as AdminDashboardActivity)
        adminActivityView.setTitle("USER DETAIL")

        setFragmentResultListener("toUserDetail"){ _, bundle ->
            val userID = bundle.getString("toUserDetail")
            val docRef = databaseRef.collection("Users").document(userID.toString())
            docRef.get().addOnSuccessListener { document ->
                val user = document.toObject(UserData2::class.java)

                binding.swUpdateUser.setOnCheckedChangeListener{ _, isChecked ->
                    binding.userNameField.isEnabled = isChecked
                    binding.userEmailField.isEnabled = isChecked
                    binding.userPasswordField.isEnabled = isChecked
                    binding.userTypeField.isEnabled = isChecked
                }

                binding.userIDField.setText(user?.userID.toString())
                binding.userNameField.setText(user?.username.toString())
                binding.userEmailField.setText(user?.email.toString())
                binding.userPasswordField.setText(user?.password.toString())
                binding.userTypeField.setText(user?.userType.toString())

            }.addOnFailureListener { e -> Log.e("FETCHING DOCUMENT", "INVALID DOCUMENT", e) }

        }

        binding.tvBackUserDetail.setOnClickListener {
            adminActivityView.replaceFragment(AdminUserFragment(), R.id.adminLayout)
        }

        return binding.root
    }
}