package com.example.fyp_booking_application.backend.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.TestUserData

class UserAdminAdapter(
    private val userList: ArrayList<TestUserData>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<UserAdminAdapter.MyViewHolder>(){

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val tvUsername: TextView = itemView.findViewById(R.id.tvDisplay1)
        val tvEmail: TextView = itemView.findViewById(R.id.tvDisplay2)
        val tfUsername: TextView = itemView.findViewById(R.id.tfDisplay1)
        val tfEmail: TextView = itemView.findViewById(R.id.tfDisplay2)

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?){
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION)
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
        val currentItem = userList[position]

        holder.tvUsername.text = "Username"
        holder.tvEmail.text = "Email"

        holder.tfUsername.text = currentItem.username
        holder.tfEmail.text = currentItem.email
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}