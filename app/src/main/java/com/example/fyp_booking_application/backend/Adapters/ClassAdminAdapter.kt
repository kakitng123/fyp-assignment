package com.example.fyp_booking_application.backend.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.backend.ClassData

class ClassAdminAdapter(
    private val classList: ArrayList<ClassData>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<ClassAdminAdapter.MyViewHolder>(){

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val className: TextView = itemView.findViewById(R.id.tfRC_className)
        val classPrice: TextView = itemView.findViewById(R.id.tfRC_classPrice)

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
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_class_admin,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = classList[position]

        holder.className.text = currentItem.trainingClassName
        holder.classPrice.text = currentItem.trainingClassPrice.toString()
    }

    override fun getItemCount(): Int {
        return classList.size
    }
}