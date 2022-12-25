package com.example.fyp_booking_application.backend.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.PurchaseData
import com.example.fyp_booking_application.R

class PurchaseAdminAdapter(
    private val purchaseList: ArrayList<PurchaseData>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<PurchaseAdminAdapter.MyViewHolder>(){

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val tvTransID: TextView = itemView.findViewById(R.id.tvDisplay1)
        val tvTransAmt: TextView = itemView.findViewById(R.id.tvDisplay2)
        val tfTransID: TextView = itemView.findViewById(R.id.tfDisplay1)
        val tfTransAmt: TextView = itemView.findViewById(R.id.tfDisplay2)

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
        val currentItem = purchaseList[position]

        holder.tvTransID.text = "Transact ID"
        holder.tvTransAmt.text = "Transact Amount"

        holder.tfTransID.text = currentItem.purchaseID
        holder.tfTransAmt.text = currentItem.purchasePrice.toString()
    }

    override fun getItemCount(): Int {
        return purchaseList.size
    }

}