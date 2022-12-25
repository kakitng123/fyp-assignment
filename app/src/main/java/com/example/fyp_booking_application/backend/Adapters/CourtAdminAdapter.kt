package com.example.fyp_booking_application.backend.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.CourtData
import com.example.fyp_booking_application.R
import com.google.api.Distribution.BucketOptions.Linear

class CourtAdminAdapter(
    private val courtList: ArrayList<CourtData>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<CourtAdminAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val tvCourtName: TextView = itemView.findViewById(R.id.tvDisplay1)
        val tvCourtPrice: TextView = itemView.findViewById(R.id.tvDisplay2)
        val tfCourtName: TextView = itemView.findViewById(R.id.tfDisplay1)
        val tfCourtPrice: TextView = itemView.findViewById(R.id.tfDisplay2)
        val imgError: ImageView = itemView.findViewById(R.id.imgError)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
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
        val currentItem = courtList[position]

        holder.tvCourtName.text = "Court Name"
        holder.tvCourtPrice.textSize = 14F
        holder.tvCourtPrice.text = "Price per Booking"
        if(currentItem.courtSlots == null){
            holder.imgError.visibility = View.VISIBLE
            holder.imgError.contentDescription = "NO TIMESLOT"
        }
        holder.tfCourtName.text = currentItem.courtName
        holder.tfCourtPrice.text = "RM ${currentItem.courtPrice.toString()}"
    }

    override fun getItemCount(): Int {
        return courtList.size
    }

}
