package com.example.fyp_booking_application.backend.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.VoucherData

class VoucherAdminAdapter(
    private val voucherList: ArrayList<VoucherData>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<VoucherAdminAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val tvVoucherTitle: TextView = itemView.findViewById(R.id.tvDisplay1)
        val tvVoucherCode: TextView = itemView.findViewById(R.id.tvDisplay2)
        val tfVoucherTitle: TextView = itemView.findViewById(R.id.tfDisplay1)
        val tfVoucherCode: TextView = itemView.findViewById(R.id.tfDisplay2)

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

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_admin_cardview, parent, false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = voucherList[position]

        holder.tvVoucherTitle.text = "Voucher Title"
        holder.tvVoucherCode.text = "Voucher Code"

        holder.tfVoucherTitle.text = currentItem.voucherTitle
        holder.tfVoucherCode.text = currentItem.voucherCode
    }

    override fun getItemCount(): Int {
        return voucherList.size
    }
}