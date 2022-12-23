package com.example.fyp_booking_application.backend.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.ClassData
import com.example.fyp_booking_application.R

class CoachClassAdminAdapter (
    private val coachClassList: ArrayList<ClassData>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<CoachClassAdminAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val tvClassName: TextView = itemView.findViewById(R.id.rvClassName)
        val tvClassPrice: TextView = itemView.findViewById(R.id.rvClassPrice)

        init{
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION)
                listener.onItemClick(position)
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_admin_coach_class, parent, false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = coachClassList[position]

        holder.tvClassName.text = currentItem.className
        holder.tvClassPrice.text = "RM ${currentItem.classPrice.toString()}"
    }

    override fun getItemCount(): Int {
        return coachClassList.size
    }
}