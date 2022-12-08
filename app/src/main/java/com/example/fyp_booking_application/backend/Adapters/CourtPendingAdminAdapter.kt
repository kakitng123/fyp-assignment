package com.example.fyp_booking_application.backend.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.backend.CourtPendingData

class CourtPendingAdminAdapter(
    private val courtPendingList: ArrayList<CourtPendingData>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<CourtPendingAdminAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val courtID: TextView = itemView.findViewById(R.id.tfRC_courtID)
        val bookingTime: TextView = itemView.findViewById(R.id.tfRC_courtTime)
        val bookingDate: TextView = itemView.findViewById(R.id.tfRC_courtDate)
        private val btnReminder: Button = itemView.findViewById(R.id.btnReminder)

        init {
            btnReminder.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_court_pending, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = courtPendingList[position]

        holder.courtID.text = currentItem.courtID
        holder.bookingTime.text = currentItem.bookingTime
        holder.bookingDate.text = currentItem.bookingDate
    }

    override fun getItemCount(): Int {
        return courtPendingList.size
    }
}