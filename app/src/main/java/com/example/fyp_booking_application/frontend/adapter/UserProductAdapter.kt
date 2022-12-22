package com.example.fyp_booking_application.frontend.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.frontend.data.ProductData
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class UserProductAdapter(
    private val userProDataArrayList: ArrayList<ProductData>,
    // private val listener: OnItemClickListener
) : RecyclerView.Adapter<UserProductAdapter.UserProViewholder>() {
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference

    // to inflate the layout for each item of recycler view.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserProductAdapter.UserProViewholder {
        //infate the custom layout
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_product_card, parent, false)
        //return a new holder instance
        return UserProViewholder(itemView)
    }

    //Involves the populating data into the item through holder
    override fun onBindViewHolder(holder: UserProductAdapter.UserProViewholder, position: Int) {
        // Initialise
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference

        //Get the data model based on position
        val userProModel: ProductData = userProDataArrayList[position]

        // to set data to textview and imageview of each card layout
        val img = storageRef.child("images/products/product_"+ userProModel.productImage)
        val file = File.createTempFile("temp", "png")

        img.getFile(file).addOnSuccessListener(){
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            holder.userProImage.setImageBitmap(bitmap)
        }

        holder.userProName.text = userProModel.productName
    }

    //Return the total count of items in the list
    override fun getItemCount(): Int {
        // this method is used for showing number of card items in recycler view.
        return userProDataArrayList.size
    }

    // View holder class for initializing of your views such as TextView and Imageview.
    inner class UserProViewholder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val userProImage: ImageView
        val userProName: TextView

        init {
            userProImage = itemView.findViewById(R.id.tvProductImage)
            userProName = itemView.findViewById(R.id.tvProductName)
        }
    }
}
