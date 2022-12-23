package com.example.fyp_booking_application.backend.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.backend.CoachData

class CoachAdminAdapter(
    private val coachList: ArrayList<CoachData>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<CoachAdminAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val tvCoachID: TextView = itemView.findViewById(R.id.tvDisplay1)
        val tvCoachName: TextView = itemView.findViewById(R.id.tvDisplay2)
        val tfCoachID: TextView = itemView.findViewById(R.id.tfDisplay1)
        val tfCoachName: TextView = itemView.findViewById(R.id.tfDisplay2)

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
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_admin_cardview, parent, false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = coachList[position]

        holder.tvCoachID.text = "Coach ID"
        holder.tvCoachName.text = "Coach Name"

        holder.tfCoachID.text = currentItem.coachID
        holder.tfCoachName.text = currentItem.coachName
    }

    override fun getItemCount(): Int {
        return coachList.size
    }

}
