package com.example.fyp_booking_application.frontend.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.ProductData
import com.example.fyp_booking_application.R
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class UserProductAdapter(
    private val userProDataArrayList: ArrayList<ProductData>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<UserProductAdapter.UserProViewholder>() {
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference

    //Inflate the layout for each item of recycler view.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserProductAdapter.UserProViewholder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_product_card, parent, false)
        return UserProViewholder(itemView)
    }

    //Involves the populating data into the item through holder
    override fun onBindViewHolder(holder: UserProductAdapter.UserProViewholder, position: Int) {
        //Initialise
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        val userProModel: ProductData = userProDataArrayList[position] //Get the data model based on position

        //Set data to textview and imageview of each card layout
        val img = storageRef.child(userProModel.productImage.toString())
        val file = File.createTempFile("temp", "png")
        img.getFile(file).addOnSuccessListener(){
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            holder.userProImage.setImageBitmap(bitmap)
        }
        holder.userProName.text = userProModel.productName
        holder.userProPrice.text = userProModel.productPrice.toString()
    }

    //Return the total count of items in the list
    override fun getItemCount(): Int {
        return userProDataArrayList.size
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    //View holder class for initializing of your views such as TextView and Imageview.
    inner class UserProViewholder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val userProImage: ImageView
        val userProName: TextView
        val userProPrice: TextView
        init {
            userProImage = itemView.findViewById(R.id.tvProductImage)
            userProName = itemView.findViewById(R.id.tvProductName)
            userProPrice = itemView.findViewById(R.id.tvProductPrice)
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}
