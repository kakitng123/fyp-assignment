package com.example.fyp_booking_application.frontend.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.frontend.data.EnrollData
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class EnrolledClassHistoryAdapter(
    private val enrolledHistoryDataArrayList: ArrayList<EnrollData>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<EnrolledClassHistoryAdapter.enrolledClassViewHolder>() {
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference

    // to inflate the layout for each item of recycler view.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EnrolledClassHistoryAdapter.enrolledClassViewHolder {
        //infate the custom layout
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_enroll_history_card, parent, false)
        //return a new holder instance
        return enrolledClassViewHolder(itemView)
    }

    //Involves the populating data into the item through holder
    override fun onBindViewHolder(holder: EnrolledClassHistoryAdapter.enrolledClassViewHolder, position: Int) {

        //Get the data model based on position
        val enrollHistoryModel: EnrollData = enrolledHistoryDataArrayList[position]

        holder.historyClass.text = enrollHistoryModel.enrollClassName
        holder.historyPrice.text = enrollHistoryModel.enrollPrice.toString()
        holder.historyDate.text = enrollHistoryModel.enrollDate
        holder.historyTime.text = enrollHistoryModel.enrollTime
        holder.historyStatus.text = enrollHistoryModel.enrollStatus

    }

    //Return the total count of items in the list
    override fun getItemCount(): Int {
        // this method is used for showing number of card items in recycler view.
        return enrolledHistoryDataArrayList.size
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    // View holder class for initializing of your views such as TextView and Imageview.
    inner class enrolledClassViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val historyClass: TextView
        val historyPrice: TextView
        val historyDate: TextView
        val historyTime: TextView
        val historyStatus: TextView
        init {
            historyClass = itemView.findViewById(R.id.historyClassName)
            historyPrice = itemView.findViewById(R.id.historyClassPrice)
            historyDate = itemView.findViewById(R.id.historyClassDate)
            historyTime = itemView.findViewById(R.id.historyClassTime)
            historyStatus = itemView.findViewById(R.id.historyClassStatus)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (adapterPosition != RecyclerView.NO_POSITION){
                listener.onItemClick(adapterPosition)
            }
        }
    }
}
