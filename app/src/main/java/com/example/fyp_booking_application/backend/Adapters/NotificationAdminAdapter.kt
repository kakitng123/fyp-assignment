package com.example.fyp_booking_application.backend.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.NotificationData
import com.example.fyp_booking_application.R

class NotificationAdminAdapter(
    private val notificationList: ArrayList<NotificationData>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<NotificationAdminAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val tvNotifyID: TextView = itemView.findViewById(R.id.tvDisplay1)
        val tvRefCode: TextView = itemView.findViewById(R.id.tvDisplay2)
        val tfNotifyID: TextView = itemView.findViewById(R.id.tfDisplay1)
        val tfRefCode: TextView = itemView.findViewById(R.id.tfDisplay2)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_admin_cardview, parent, false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = notificationList[position]

        holder.tvNotifyID.text = "Notif. Title"
        holder.tvRefCode.text = "Referral Code"

        holder.tfNotifyID.text = currentItem.notifyTitle.toString()
        holder.tfRefCode.text = currentItem.referralCode.toString()
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }
}