package com.example.fyp_booking_application.frontend.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.frontend.data.BookingData
import com.google.firebase.storage.FirebaseStorage

class BookingCourtHistoryAdapter(
    private val bookingHistoryDataArrayList: ArrayList<BookingData>
) : RecyclerView.Adapter<BookingCourtHistoryAdapter.BookingHistoryViewHolder>() {

    // to inflate the layout for each item of recycler view.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingCourtHistoryAdapter.BookingHistoryViewHolder {
        //infate the custom layout
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_booking_history_card, parent, false)
        //return a new holder instance
        return BookingHistoryViewHolder(itemView)
    }

    //Involves the populating data into the item through holder
    override fun onBindViewHolder(holder: BookingCourtHistoryAdapter.BookingHistoryViewHolder, position: Int) {
        //Get the data model based on position
        val bookingHistoryModel = bookingHistoryDataArrayList[position]

        holder.historyCourt.text = bookingHistoryModel.bookingCourt
        holder.historyPrice.text = bookingHistoryModel.bookingPayment
        holder.historyDate.text = bookingHistoryModel.bookingDate
        holder.historyTime.text = bookingHistoryModel.bookingTime
        holder.historyStatus.text = bookingHistoryModel.bookingStatus
    }

    //Return the total count of items in the list
    override fun getItemCount(): Int {
        // this method is used for showing number of card items in recycler view.
        return bookingHistoryDataArrayList.size
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    // View holder class for initializing of your views such as TextView and Imageview.
    inner class BookingHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val historyCourt: TextView
        val historyPrice: TextView
        val historyDate: TextView
        val historyTime: TextView
        val historyStatus: TextView
        init {
            historyCourt = itemView.findViewById(R.id.historyCourtName)
            historyPrice = itemView.findViewById(R.id.historyCourtPrice)
            historyDate = itemView.findViewById(R.id.historyCourtDate)
            historyTime = itemView.findViewById(R.id.historyCourtTime)
            historyStatus = itemView.findViewById(R.id.historyCourtStatus)
        }
    }
}
