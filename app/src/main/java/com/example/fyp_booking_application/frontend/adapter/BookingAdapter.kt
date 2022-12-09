package com.example.fyp_booking_application.frontend.adapter

import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.frontend.data.BookingData
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class BookingAdapter {
//    private val BookingDataArrayList: ArrayList<BookingData>) : RecyclerView.Adapter<BookingAdapter.BookingViewholder>()
//    private lateinit var storage: FirebaseStorage
//    private lateinit var storageRef: StorageReference
//
//    // to inflate the layout for each item of recycler view.
//     fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingAdapter.BookingViewholder {
//        val itemView =
//            LayoutInflater.from(parent.context).inflate(R.layout.coach_card, parent, false)
//        return BookingViewholder(itemView)
//    }
//
//     fun onBindViewHolder(holder: BookingAdapter.BookingViewholder, position: Int) {
//        // Initialise
//        val storage = FirebaseStorage.getInstance()
//        val storageRef = storage.reference
//
//        // to set data to textview and imageview of each card layout
//        val bookingModel: BookingData = BookingDataArrayList[position]
//
////            val img = storageRef.child("images/coachProfile/coach_" + bookingModel.coachName)
////            val file = File.createTempFile("temp", "png")
////
////            img.getFile(file).addOnSuccessListener() {
////                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
////                holder.coachImage.setImageBitmap(bitmap)
////            }
//        holder.bookingID.text = bookingModel.bookingID
//        holder.bookingCourt.text = bookingModel.bookingCourt
//        holder.bookingRate.text = bookingModel.bookingRate.toString()
//        holder.bookingPhone.text = bookingModel.bookingPhone
//        holder.bookingDate.text = bookingModel.bookingDate
//        holder.bookingTime.text = bookingModel.bookingTime
//
////        // making intent method to go another page
////        holder.itemView.setOnClickListener {
////            val intent = Intent(context, DetailActivity::class.java)
////            intent.putExtra("id", bookingModel.bookingID)
////            intent.putExtra("court", bookingModel.bookingCourt)
////            intent.putExtra("rate", bookingModel.bookingRate)
////            intent.putExtra("phone", bookingModel.bookingPhone)
////            intent.putExtra("date", bookingModel.bookingDate)
////            intent.putExtra("time", bookingModel.bookingTime)
////            intent.putExtra("type", 2)
////            context.startActivity(intent)
////        }
//    }
//
//    fun getItemCount(): Int {
//        // this method is used for showing number of card items in recycler view.
//        return BookingDataArrayList.size
//    }
//
//    // View holder class for initializing of your views such as TextView and Imageview.
//    class BookingViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val bookingID: TextView
//        val bookingCourt: TextView
//        val bookingRate: TextView
//        val bookingPhone: TextView
//        val bookingDate: TextView
//        val bookingTime: TextView
//
//        init {
//            bookingCourt = itemView.findViewById(R.id.tvCoachImage)
//            bookingRate = itemView.findViewById(R.id.tvCoachName)
//            bookingID = itemView.findViewById(R.id.tvCoachID)
//            bookingPhone = itemView.findViewById(R.id.tvCoachExperience)
//            bookingDate = itemView.findViewById(R.id.tvCoachPhone)
//            bookingTime = itemView.findViewById(R.id.tvCoachEmail)
//        }
//    }
//}
}