package com.example.fyp_booking_application.backend.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.backend.CourtTimeslots
import com.example.fyp_booking_application.backend.TestCourtData

class TimeslotAdminAdapter(
    private val timeslotList: ArrayList<TestCourtData>,
) : RecyclerView.Adapter<TimeslotAdminAdapter.MyViewHolder>(){

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val timeslot: TextView = itemView.findViewById(R.id.tvTimeslot)
        val availability: TextView = itemView.findViewById(R.id.tvAvailability)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_admin_timeslot, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = timeslotList[position]

        holder.timeslot.text = currentItem.courtSlots!!["timeslot"].toString()
        holder.availability.text = currentItem.courtSlots["availability"].toString()
    }

    override fun getItemCount(): Int {
        return timeslotList.size
    }
}