package com.example.fyp_booking_application.backend.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.backend.NotificationData

class NotificationAdminAdapter(
    private val notificationList: ArrayList<NotificationData>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<NotificationAdminAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val tvNotifyID: TextView = itemView.findViewById(R.id.tvText1)
        val tvRefCode: TextView = itemView.findViewById(R.id.tvText2)
        val tfNotifyID: TextView = itemView.findViewById(R.id.tfText1)
        val tfRefCode: TextView =itemView.findViewById(R.id.tfText2)

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
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_notification_admin, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = notificationList[position]

        holder.tvNotifyID.text = "NOTIFICATION ID"
        holder.tvRefCode.text = "Referral Code"
        holder.tfNotifyID.text = currentItem.notifyID.toString()
        holder.tfRefCode.text = currentItem.referralCode.toString()
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }
}