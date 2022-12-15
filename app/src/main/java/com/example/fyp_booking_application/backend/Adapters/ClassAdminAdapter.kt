package com.example.fyp_booking_application.backend.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.backend.ClassData2

class ClassAdminAdapter(
    private val classList: ArrayList<ClassData2>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<ClassAdminAdapter.MyViewHolder>(){

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val className: TextView = itemView.findViewById(R.id.tfDisplay1)
        val classPrice: TextView = itemView.findViewById(R.id.tfDisplay2)
        val tvClassName: TextView = itemView.findViewById(R.id.tvDisplay1)
        val tvClassPrice: TextView = itemView.findViewById(R.id.tvDisplay2)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION)
                listener.onItemClick(position)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_admin_cardview,parent,false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = classList[position]

        holder.tvClassName.text = "Class Name"
        holder.tvClassPrice.text = "Class Price"

        holder.className.text = currentItem.className
        holder.classPrice.text = currentItem.classPrice.toString()
    }

    override fun getItemCount(): Int {
        return classList.size
    }
}