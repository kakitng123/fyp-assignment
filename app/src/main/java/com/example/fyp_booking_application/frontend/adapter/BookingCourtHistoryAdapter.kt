package com.example.fyp_booking_application.frontend.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.BookingData
import com.example.fyp_booking_application.R

class BookingCourtHistoryAdapter(
    private val bookingHistoryDataArrayList: ArrayList<BookingData>
) : RecyclerView.Adapter<BookingCourtHistoryAdapter.BookingHistoryViewHolder>() {

    // to inflate the layout for each item of recycler view.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingCourtHistoryAdapter.BookingHistoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_booking_history_card, parent, false)
        return BookingHistoryViewHolder(itemView)
    }

    //Involves the populating data into the item through holder
    override fun onBindViewHolder(holder: BookingCourtHistoryAdapter.BookingHistoryViewHolder, position: Int) {
        //Get the data model based on position
        val bookingHistoryModel = bookingHistoryDataArrayList[position]
        holder.historyCourt.text = bookingHistoryModel.bookingCourt
        holder.historyPrice.text = bookingHistoryModel.bookingPayment.toString()
        holder.historyDate.text = bookingHistoryModel.bookingDate
        holder.historyTime.text = bookingHistoryModel.bookingTime
        holder.historyStatus.text = bookingHistoryModel.bookingStatus
        holder.deleteBookingBtn.setOnClickListener(View.OnClickListener {
            bookingHistoryDataArrayList.removeAt(position) // remove the item from list
            notifyItemRemoved(position) // notify the adapter about the removed item
        })
    }
    //Return the total count of items in the list
    override fun getItemCount(): Int {
        return bookingHistoryDataArrayList.size
    }
    //On Item Click Function
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
        val deleteBookingBtn: Button
        init {
            historyCourt = itemView.findViewById(R.id.tvHistoryCourtName)
            historyPrice = itemView.findViewById(R.id.tvHistoryCourtPrice)
            historyDate = itemView.findViewById(R.id.tvHistoryCourtDate)
            historyTime = itemView.findViewById(R.id.tvHistoryCourtTime)
            historyStatus = itemView.findViewById(R.id.tvHistoryCourtStatus)
            deleteBookingBtn = itemView.findViewById(R.id.deleteBookingBtn)
        }
    }
}
