package com.example.fyp_booking_application.backend.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.EnrollData
import com.example.fyp_booking_application.R

class ClassEnrollAdminAdapter (
    private val enrollList: ArrayList<EnrollData>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<ClassEnrollAdminAdapter.MyViewHolder>(){

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val tvDisplay1: TextView = itemView.findViewById(R.id.tvDisplay1)
        val tvDisplay2: TextView = itemView.findViewById(R.id.tvDisplay2)
        val tfDisplay1: TextView = itemView.findViewById(R.id.tfDisplay1)
        val tfDisplay2: TextView = itemView.findViewById(R.id.tfDisplay2)

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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_admin_cardview,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = enrollList[position]

        holder.tvDisplay1.text = "Enroll ID"
        holder.tvDisplay2.text = "Status"

        holder.tfDisplay1.text = currentItem.enrollID
        holder.tfDisplay2.text = currentItem.enrollStatus
    }

    override fun getItemCount(): Int {
        return enrollList.size
    }
}