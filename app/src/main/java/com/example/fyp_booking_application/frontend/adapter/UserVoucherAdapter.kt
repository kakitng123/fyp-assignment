package com.example.fyp_booking_application.frontend.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.VoucherData
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class UserVoucherAdapter (
    private val userVoucherDataArrayList: ArrayList<VoucherData>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<UserVoucherAdapter.UserVoucherViewholder>() {
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference

    //Inflate the layout for each item of recycler view.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserVoucherAdapter.UserVoucherViewholder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_voucher_card, parent, false)
        return UserVoucherViewholder(itemView)
    }

    //Involves the populating data into the item through holder
    override fun onBindViewHolder(holder: UserVoucherAdapter.UserVoucherViewholder, position: Int) {
        //Initialise
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        val userVoucherModel: VoucherData = userVoucherDataArrayList[position] //Get the data model based on position
        //Set data to textview and imageview of each card layout
        val img = storageRef.child(userVoucherModel.voucherImage.toString())
        val file = File.createTempFile("temp", "png")

        img.getFile(file).addOnSuccessListener(){
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            holder.userVouImage.setImageBitmap(bitmap)
        }

        holder.userVouTitle.text = userVoucherModel.voucherTitle
        holder.userVouDiscount.text = userVoucherModel.voucherDiscount.toString()
    }

    //Return the total count of items in the list
    override fun getItemCount(): Int {
        return userVoucherDataArrayList.size
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    //View holder class for initializing of your views such as TextView and Imageview.
    inner class UserVoucherViewholder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val userVouImage: ImageView
        val userVouTitle: TextView
        val userVouDiscount: TextView
        init {
            userVouImage = itemView.findViewById(R.id.tvVoucherImage)
            userVouTitle = itemView.findViewById(R.id.tvVoucherTitle)
            userVouDiscount = itemView.findViewById(R.id.tvVoucherDiscount)
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}

