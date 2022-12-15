package com.example.fyp_booking_application.backend.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.backend.CourtData

class CourtAdminAdapter(
    private val courtList: ArrayList<CourtData>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<CourtAdminAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val courtID: TextView = itemView.findViewById(R.id.tfDisplay1)
        val courtName: TextView = itemView.findViewById(R.id.tfDisplay2)
        val tvCourtID: TextView = itemView.findViewById(R.id.tvDisplay1)
        val tvCourtName: TextView = itemView.findViewById(R.id.tvDisplay2)
        val imgBtnDelete: ImageButton = itemView.findViewById(R.id.imgBtnDeleteItem)

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
        fun onButtonClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_admin_cardview, parent, false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = courtList[position]

        holder.tvCourtID.text = "Court ID"
        holder.tvCourtName.text = "Court Name"
        holder.imgBtnDelete.visibility = View.VISIBLE
        holder.imgBtnDelete.setOnClickListener(){
            listener.onButtonClick(position)
        }
        holder.courtID.text = currentItem.courtID
        holder.courtName.text = currentItem.courtName
    }

    override fun getItemCount(): Int {
        return courtList.size
    }

}
