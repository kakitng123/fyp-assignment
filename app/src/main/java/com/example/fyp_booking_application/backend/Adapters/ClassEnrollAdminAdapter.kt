package com.example.fyp_booking_application.backend.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.EnrollData
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.TestUserData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class ClassEnrollAdminAdapter (
    private val enrollList: ArrayList<EnrollData>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<ClassEnrollAdminAdapter.MyViewHolder>(){

    private lateinit var databaseRef: FirebaseFirestore

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val tfClassName: TextView = itemView.findViewById(R.id.tfEnroll1)
        val tfClassUser: TextView = itemView.findViewById(R.id.tfEnroll2)
        val tfClassDateTime: TextView = itemView.findViewById(R.id.tfEnroll3)
        val tfClassStatus: TextView = itemView.findViewById(R.id.tfEnroll4)
        val btnApprove: Button = itemView.findViewById(R.id.btnApproveEnroll)
        val btnDecline: Button = itemView.findViewById(R.id.btnDeclineEnroll)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION)
                listener.onItemClick(position)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onApproveBtnClick (position: Int)
        fun onDeclineBtnClick (position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_admin_enroll,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = enrollList[position]
        databaseRef = FirebaseFirestore.getInstance()

        val docRef = databaseRef.collection("Users").document(currentItem.userID.toString())
        docRef.get().addOnSuccessListener { document ->
            val user = document.toObject(TestUserData::class.java)
            holder.tfClassUser.text = user?.username

        }.addOnFailureListener { e ->
            Log.e("FETCHING DOCUMENT", "ERROR FETCHING DOCUMENT", e)
        }
        holder.tfClassName.text = currentItem.enrollClassName
        holder.tfClassDateTime.text = "${currentItem.enrollDate} - ${currentItem.enrollTime}"
        holder.tfClassStatus.text = currentItem.enrollStatus
        holder.btnApprove.setOnClickListener{
            listener.onApproveBtnClick(position)
            notifyDataSetChanged()
        }
        holder.btnDecline.setOnClickListener{
            listener.onDeclineBtnClick(position)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return enrollList.size
    }
}