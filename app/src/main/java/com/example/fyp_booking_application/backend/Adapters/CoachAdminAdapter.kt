package com.example.fyp_booking_application.backend.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.backend.CoachData

class CoachAdminAdapter(
    private val coachList: ArrayList<CoachData>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<CoachAdminAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val coachID: TextView = itemView.findViewById(R.id.tfRC_coachID)
        val coachName: TextView = itemView.findViewById(R.id.tfRC_coachName)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION)
                listener.onItemClick(position)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_coach_admin, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = coachList[position]

        holder.coachID.text = currentItem.coachID
        holder.coachName.text = currentItem.coachName
    }

    override fun getItemCount(): Int {
        return coachList.size
    }

}
