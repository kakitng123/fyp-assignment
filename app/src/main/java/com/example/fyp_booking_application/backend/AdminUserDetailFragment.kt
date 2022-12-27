package com.example.fyp_booking_application.backend

import android.app.AlertDialog
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
import com.example.fyp_booking_application.TestUserData
import com.example.fyp_booking_application.databinding.FragmentAdminUserDetailBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class AdminUserDetailFragment : Fragment() {

    private lateinit var binding: FragmentAdminUserDetailBinding
    private lateinit var databaseRef: FirebaseFirestore


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_user_detail, container, false)
        databaseRef = FirebaseFirestore.getInstance()
        val adminView = (activity as AdminDashboardActivity)
        adminView.setTitle("USER DETAIL")

        setFragmentResultListener("toUserDetail"){ _, bundle ->
            val userID = bundle.getString("toUserDetail")
            val docRef = databaseRef.collection("Users").document(userID.toString())
            docRef.get().addOnSuccessListener { document ->
                val user = document.toObject(TestUserData::class.java)

                binding.swUpdateUser.setOnCheckedChangeListener{ _, isChecked ->
                    binding.userNameField.isEnabled = isChecked
                    binding.userEmailField.isEnabled = isChecked
                    binding.userPasswordField.isEnabled = isChecked
                    binding.userPhoneField.isEnabled = isChecked
                    binding.userGenderField.isEnabled = isChecked
                    binding.userSubscribeField.isEnabled = isChecked
                }
                //Log.d("CHECK ITEM", user?.isSubscribed.toString())

                binding.userIDField.setText(user?.userID.toString())
                binding.userNameField.setText(user?.username.toString())
                binding.userEmailField.setText(user?.email.toString())
                binding.userPasswordField.setText(user?.password.toString())
                binding.userPhoneField.setText(user?.phone.toString())
                binding.userGenderField.setText(user?.gender.toString())
                binding.userSubscribeField.isChecked = user?.isSubscribed!!

                binding.imgBtnUpdateUser.setOnClickListener {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle("Update User Details")
                    builder.setMessage("Confirm to update user details?")
                    builder.setPositiveButton("Update"){ _, _ ->
                        val updateClass = hashMapOf(
                            "username" to binding.userNameField.text.toString(),
                            "email" to binding.userEmailField.text.toString(),
                            "password" to binding.userPasswordField.text.toString(),
                            "isSubscribe" to binding.userSubscribeField.isChecked
                        )
                        docRef.set(updateClass, SetOptions.merge())
                            .addOnSuccessListener { Log.d("UPDATE USER","USER DETAIL UPDATED SUCCESSFULLY" ) }
                            .addOnFailureListener { e -> Log.e("UPDATE USER", "ERROR UPDATING USER DETAIL", e) }
                    }
                    builder.setNegativeButton("Cancel"){ _, _ -> }
                    builder.show()
                }
            }.addOnFailureListener { e -> Log.e("FETCHING DOCUMENT", "INVALID DOCUMENT", e) }

            binding.imgBtnDeleteUser.setOnClickListener {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Delete User")
                builder.setMessage("Confirm to delete user?")
                builder.setPositiveButton("Delete"){ _, _ ->
                    docRef.delete().addOnSuccessListener {
                        Log.d("DELETE USER", "USER DELETED SUCCESSFULLY")
                        adminView.replaceFragment(AdminClassFragment(), R.id.adminLayout)
                    }.addOnFailureListener { e ->
                        Log.e("DELETE USER", "ERROR DELETING USER", e)
                    }
                }
                builder.setNegativeButton("Cancel"){ _, _ -> }
                builder.show()
            }
        }

        binding.tvBackUserDetail.setOnClickListener {
            adminView.replaceFragment(AdminUserFragment(), R.id.adminLayout)
        }

        return binding.root
    }
}