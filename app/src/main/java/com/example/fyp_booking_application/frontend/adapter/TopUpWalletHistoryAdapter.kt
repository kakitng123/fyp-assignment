package com.example.fyp_booking_application.frontend.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.TopUpData

class TopUpWalletHistoryAdapter(
    private val topUpWalletDataArrayList: ArrayList<TopUpData>
) : RecyclerView.Adapter<TopUpWalletHistoryAdapter.TopUpHistoryViewHolder>() {

    // to inflate the layout for each item of recycler view.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopUpWalletHistoryAdapter.TopUpHistoryViewHolder {
        //infate the custom layout
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_top_up_wallet_history_card, parent, false)
        //return a new holder instance
        return TopUpHistoryViewHolder(itemView)
    }

    //Involves the populating data into the item through holder
    override fun onBindViewHolder(holder: TopUpWalletHistoryAdapter.TopUpHistoryViewHolder, position: Int) {
        //Get the data model based on position
        val topUpHistoryModel = topUpWalletDataArrayList[position]
        holder.topUpTitle.text = topUpHistoryModel.topUpTitle.toString()
        holder.topUpAmount.text = topUpHistoryModel.topUpAmount.toString()
        holder.topUpDesc.text = topUpHistoryModel.topUpDesc.toString()
        holder.topUpStatus.text = topUpHistoryModel.topUpStatus.toString()
    }

    //Return the total count of items in the list
    override fun getItemCount(): Int {
        // this method is used for showing number of card items in recycler view.
        return topUpWalletDataArrayList.size
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    // View holder class for initializing of your views such as TextView and Imageview.
    inner class TopUpHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val topUpTitle : TextView
        val topUpAmount: TextView
        val topUpDesc: TextView
        val topUpStatus: TextView
        init {
            topUpTitle = itemView.findViewById(R.id.tvTopUpTitle)
            topUpAmount = itemView.findViewById(R.id.tvTopUpAmount)
            topUpDesc = itemView.findViewById(R.id.tvTopUpDesc)
            topUpStatus = itemView.findViewById(R.id.tvTopUpStatus)
        }
    }
}


