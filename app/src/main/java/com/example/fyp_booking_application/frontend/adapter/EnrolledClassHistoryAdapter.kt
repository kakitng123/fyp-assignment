package com.example.fyp_booking_application.frontend.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.EnrollData
import com.example.fyp_booking_application.R

class EnrolledClassHistoryAdapter(
    private val enrollClassDataArrayList: ArrayList<EnrollData>
) : RecyclerView.Adapter<EnrolledClassHistoryAdapter.EnrollHistoryViewHolder>() {

    // to inflate the layout for each item of recycler view.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EnrolledClassHistoryAdapter.EnrollHistoryViewHolder {
        //infate the custom layout
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_enroll_history_card, parent, false)
        //return a new holder instance
        return EnrollHistoryViewHolder(itemView)
    }

    //Involves the populating data into the item through holder
    override fun onBindViewHolder(holder: EnrolledClassHistoryAdapter.EnrollHistoryViewHolder, position: Int) {
        //Get the data model based on position
        val enrollHistoryModel = enrollClassDataArrayList[position]

        holder.historyClass.text = enrollHistoryModel.enrollClassName
        holder.historyPrice.text = enrollHistoryModel.enrollPrice
        holder.historyDate.text = enrollHistoryModel.enrollDate
        holder.historyTime.text = enrollHistoryModel.enrollTime
        holder.historyStatus.text = enrollHistoryModel.enrollStatus
    }

    //Return the total count of items in the list
    override fun getItemCount(): Int {
        // this method is used for showing number of card items in recycler view.
        return enrollClassDataArrayList.size
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    // View holder class for initializing of your views such as TextView and Imageview.
    inner class EnrollHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val historyClass: TextView
        val historyPrice: TextView
        val historyDate: TextView
        val historyTime: TextView
        val historyStatus: TextView
        init {
            historyClass = itemView.findViewById(R.id.tvHistoryClassName)
            historyPrice = itemView.findViewById(R.id.tvHistoryClassPrice)
            historyDate = itemView.findViewById(R.id.tvHistoryClassDate)
            historyTime = itemView.findViewById(R.id.tvHistoryClassTime)
            historyStatus = itemView.findViewById(R.id.tvHistoryClassStatus)
        }
    }
}

