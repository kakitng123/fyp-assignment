package com.example.fyp_booking_application.backend

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentAdminVoucherManageBinding
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore

class AdminVoucherManageFragment : Fragment() {

    private lateinit var binding: FragmentAdminVoucherManageBinding
    private lateinit var databaseRef: FirebaseFirestore
    private lateinit var userList: ArrayList<String>
    private lateinit var listAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_voucher_manage, container, false)
        databaseRef = FirebaseFirestore.getInstance()
        userList = arrayListOf()

        databaseRef.collection("Users").get().addOnSuccessListener { results ->
            for (document in results){
                userList.add(document["username"].toString())
            }
            listAdapter.notifyDataSetChanged()
        }.addOnFailureListener { e ->
            Log.e("FETCHING DOCUMENTS", "ERROR FETCHING DOCUMENTS", e)
        }

        listAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, userList)
        binding.vouchUserLV.adapter = listAdapter

        binding.vouchUserSV.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (userList.contains(query)){
                    listAdapter.filter.filter(query)
                } else {
                    Toast.makeText(context, "No Match Found", Toast.LENGTH_LONG).show()
                }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                listAdapter.filter.filter(newText)
                return false
            }
        })

        binding.vouchUserLV.setOnItemClickListener { _: AdapterView<*>, _:View, position:Int, _:Long ->
            val testing = listAdapter.getItem(position).toString()
            val getUser = databaseRef.collection("Users").whereEqualTo("username", testing)
            getUser.get().addOnSuccessListener { documents ->
                for (document in documents){
                    binding.vouchUserSV.queryHint = document["userID"].toString()
                }
            }.addOnFailureListener { e ->
                Log.e("ERROR FETCH DATA", "ERROR FINDING DOCUMENT", e)
            }
            Toast.makeText(context, "Selected", Toast.LENGTH_SHORT).show()
        }

        binding.vouchAddTitleField.setOnFocusChangeListener { _, focused ->
            if(!focused && binding.vouchAddTitleField.text!!.isEmpty()){
                binding.vouchAddTitleContainer.helperText = "Title is Required"
            }
            else binding.vouchAddTitleContainer.helperText = null
        }

        binding.vouchAddMessageField.setOnFocusChangeListener { _, focused ->
            if(!focused && binding.vouchAddMessageField.text!!.isEmpty()){
                binding.vouchAddMessageContainer.helperText = "Points is Required"
            }
            else binding.vouchAddMessageContainer.helperText = null
        }

        binding.vouchAddPointField.setOnFocusChangeListener { _, focused ->
            if(!focused && binding.vouchAddPointField.text!!.isEmpty()){
                binding.vouchAddPointContainer.helperText = "Points is Required"
            }
            else binding.vouchAddPointContainer.helperText = null
        }

        binding.vouchAddCodeField.setOnFocusChangeListener { _, focused ->
            if(!focused && binding.vouchAddCodeField.text!!.isEmpty()){
                binding.vouchAddCodeContainer.helperText = "Voucher Code is Required"
            }
            else binding.vouchAddCodeContainer.helperText = null
        }

        binding.vouchDiscountField.setOnFocusChangeListener { _, focused ->
            if(!focused && binding.vouchDiscountField.text!!.isEmpty()){
                binding.vouchDiscountContainer.helperText = "Voucher Code is Required"
            }
            else binding.vouchDiscountContainer.helperText = null
        }


        binding.btnSendVoucher.setOnClickListener {
            val validTitle = binding.vouchAddTitleContainer.helperText == null
            val validMessage = binding.vouchAddMessageContainer.helperText == null
            val validAmount = binding.vouchAddPointContainer.helperText == null
            val validCode = binding.vouchAddCodeContainer.helperText == null
            val validDiscount = binding.vouchDiscountContainer.helperText == null

            if(validTitle && validMessage && validAmount && validCode && validDiscount){
                val discountAmount: Double = binding.vouchDiscountField.toString().toDouble() / 100
                val newVoucherRef = databaseRef.collection("Vouchers").document()
                val newVoucher = hashMapOf(
                    "voucherID" to newVoucherRef.id,
                    "voucherTitle" to binding.vouchAddTitleField.text.toString(),
                    "voucherMessage" to binding.vouchAddMessageField.text.toString(),
                    "pointsRequired" to binding.vouchAddPointField.text.toString(),
                    "voucherCode" to binding.vouchAddCodeField.text.toString(),
                    "voucherDiscount" to discountAmount,
                )
                newVoucherRef.set(newVoucher).addOnSuccessListener {
                    Log.d("ADD VOUCHER", "VOUCHER ADDED SUCCESSFULLY")
                    Toast.makeText(context, "VOUCHER CREATED", Toast.LENGTH_SHORT).show()

                }.addOnFailureListener { e ->
                    Log.e("ADD VOUCHER", "ERROR ADDING VOUCHER", e)
                }

                if(binding.voucherCheckBox.isChecked){
                    val userRef = databaseRef.collection("Users").whereEqualTo("isSubscribed", true)
                    userRef.get().addOnSuccessListener { documents ->
                        for (document in documents){
                            var collectionSize: Int ?= null
                            val collection = databaseRef.collection("Notifications").count()
                            collection.get(AggregateSource.SERVER).addOnCompleteListener{ task ->
                                if (task.isSuccessful){
                                    collectionSize = task.result.count.toInt()
                                }
                                val newNotifyRef = databaseRef.collection("Notifications").document()
                                val newNotify = hashMapOf(
                                    "notifyID" to newNotifyRef.id,
                                    "notifyTitle" to "VOUCHER AVAILABLE",
                                    "notifyMessage" to "YOU ARE ELIGIBLE FOR A VOUCHER CODE:  ${binding.vouchAddCodeField.text.toString()}\n\n" +
                                            "HAVE A NICE DAY WITH THIS PROMOCODE" ,
                                    "referralCode" to "R${100000+collectionSize!!}",
                                    "userID" to document["userID"].toString()
                                )
                                newNotifyRef.set(newNotify).addOnSuccessListener {
                                    Log.d("ADD NOTIFICATION", "NOTIFICATION ADDED SUCCESSFULLY")

                                }.addOnFailureListener { e ->
                                    Log.e("ADD NOTIFICATION", "ERROR ADDING NOTIFICATION", e)
                                }
                            }

                        }

                    }.addOnFailureListener { e ->
                        Log.e("FETCH DOCUMENT", "ERROR FETCHING DOCUMENT", e)
                    }
                }
                else {
                    var collectionSize: Int ?= null
                    val collection = databaseRef.collection("Notifications").count()
                    collection.get(AggregateSource.SERVER).addOnCompleteListener{ task ->
                        if (task.isSuccessful){
                            collectionSize = task.result.count.toInt()
                        }
                        val newNotifyRef = databaseRef.collection("Notifications").document()
                        val newNotify = hashMapOf(
                            "notifyID" to newNotifyRef.id,
                            "notifyTitle" to "VOUCHER AVAILABLE",
                            "notifyMessage" to "YOU ARE ELIGIBLE FOR A VOUCHER CODE:  ${binding.vouchAddCodeField.text.toString()}\n\n" +
                                    "HAVE A NICE DAY WITH THIS PROMOCODE" ,
                            "referralCode" to "R${100000+collectionSize!!}",
                            "userID" to binding.vouchUserSV.queryHint
                        )
                        newNotifyRef.set(newNotify).addOnSuccessListener {
                            Log.d("ADD NOTIFICATION", "NOTIFICATION ADDED SUCCESSFULLY")

                        }.addOnFailureListener { e ->
                            Log.e("ADD NOTIFICATION", "ERROR ADDING NOTIFICATION", e)
                        }
                    }
                }
            }
            else Toast.makeText(context, "CHECK INPUT FIELDS", Toast.LENGTH_SHORT).show()
        }


        return binding.root
    }
}