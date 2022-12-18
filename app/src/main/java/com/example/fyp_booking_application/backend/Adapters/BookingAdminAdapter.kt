package com.example.fyp_booking_application.backend.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.backend.BookingDataTesting

class BookingAdminAdapter(
    private val bookingList: ArrayList<BookingDataTesting>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<BookingAdminAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val tfBookingDate: TextView = itemView.findViewById(R.id.tfBookingTime)
        val tfBookingTime: TextView = itemView.findViewById(R.id.tfBookingDate)
        val tfBookingStatus: TextView = itemView.findViewById(R.id.tfBookingStatus)
        val btnSendNotif: Button = itemView.findViewById(R.id.btnSendNotif)

        init {
            itemView.setOnClickListener(this)
            btnSendNotif.setOnClickListener(this)
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
        fun onButtonClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_admin_booking, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = bookingList[position]

        holder.tfBookingDate.text = currentItem.bookingDate
        holder.tfBookingTime.text = currentItem.bookingTime
        holder.tfBookingStatus.text = currentItem.status
        if(currentItem.status == "Pending"){ holder.btnSendNotif.visibility = View.VISIBLE }
        else holder.btnSendNotif.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        return bookingList.size
    }
}