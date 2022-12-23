package com.example.fyp_booking_application.backend.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.CourtDataTimeslot
import com.example.fyp_booking_application.R

class CourtTimeslotAdminAdapter(
    private val timeslotList: ArrayList<CourtDataTimeslot>,
) : RecyclerView.Adapter<CourtTimeslotAdminAdapter.MyViewHolder>(){

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

        holder.timeslot.text = currentItem.timeslot
        holder.availability.text = currentItem.availability.toString()
    }

    override fun getItemCount(): Int {
        return timeslotList.size
    }
}