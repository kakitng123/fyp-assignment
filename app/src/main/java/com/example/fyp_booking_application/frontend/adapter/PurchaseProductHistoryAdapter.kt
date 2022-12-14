package com.example.fyp_booking_application.frontend.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.PurchaseData
import com.example.fyp_booking_application.R

class PurchaseProductHistoryAdapter(
    private val purchaseProductDataArrayList: ArrayList<PurchaseData>

) : RecyclerView.Adapter<PurchaseProductHistoryAdapter.PurchaseHistoryViewHolder>() {

    // to inflate the layout for each item of recycler view.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchaseProductHistoryAdapter.PurchaseHistoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_purchase_history_card, parent, false)
        return PurchaseHistoryViewHolder(itemView)
    }

    //Involves the populating data into the item through holder
    override fun onBindViewHolder(holder: PurchaseProductHistoryAdapter.PurchaseHistoryViewHolder, position: Int) {
        //Get the data model based on position
        val purchaseHistoryModel = purchaseProductDataArrayList[position]
        holder.purchaseName.text = purchaseHistoryModel.purchaseName
        holder.purchaseQty.text = purchaseHistoryModel.purchaseQty.toString()
        holder.purchaseStatus.text = purchaseHistoryModel.purchaseStatus
        holder.purchasePrice.text = purchaseHistoryModel.purchasePrice.toString()
        holder.deletePurchaseBtn.setOnClickListener(View.OnClickListener {
            purchaseProductDataArrayList.removeAt(position) // remove the item from list
            notifyItemRemoved(position) // notify the adapter about the removed item
        })
    }

    //Return the total count of items in the list
    override fun getItemCount(): Int {
        return purchaseProductDataArrayList.size
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    // View holder class for initializing of your views such as TextView and Imageview.
    inner class PurchaseHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val purchaseName: TextView
        val purchaseQty: TextView
        val purchaseStatus: TextView
        val purchasePrice: TextView
        val deletePurchaseBtn: Button
        init {
            purchaseName = itemView.findViewById(R.id.tvPurchaseName)
            purchaseQty = itemView.findViewById(R.id.tvPurchaseQty)
            purchaseStatus = itemView.findViewById(R.id.tvPurchaseStatus)
            purchasePrice = itemView.findViewById(R.id.tvPurchasePrice)
            deletePurchaseBtn = itemView.findViewById(R.id.deletePurchaseBtn)
        }
    }
}

