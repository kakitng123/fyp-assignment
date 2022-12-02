package com.example.fyp_booking_application.backend.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.backend.CourtData

class CourtManageAdminAdapter(
    private val courtList: ArrayList<CourtData>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<CourtManageAdminAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val courtID: TextView = itemView.findViewById(R.id.tfRC_courtIDManage)
        val courtName: TextView = itemView.findViewById(R.id.tfRC_courtNameManage)

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
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_court_manage, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = courtList[position]

        holder.courtID.text = currentItem.courtID
        holder.courtName.text = currentItem.courtName
    }

    override fun getItemCount(): Int {
        return courtList.size
    }

}