package com.example.fyp_booking_application.backend.Adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.backend.ProductData
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class ProductAdminAdapter(
    private val productList: ArrayList<ProductData>,
    private val listener: OnItemClickListener
    ) : RecyclerView.Adapter<ProductAdminAdapter.MyViewHolder>() {

    private lateinit var storageRef : StorageReference

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val productImage: ImageView = itemView.findViewById(R.id.img_productView)
        val productName: TextView = itemView.findViewById(R.id.tv_productNameView)
        val productDesc: TextView = itemView.findViewById(R.id.tv_descView)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION)
                listener.onItemClick(position)
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_admin_products, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = productList[position]

        storageRef = FirebaseStorage.getInstance().reference
        val currentPhoto = storageRef.child("products/product"+currentItem.productName)
        val file = File.createTempFile("temp", "png")

        currentPhoto.getFile(file).addOnSuccessListener{
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            holder.productImage.setImageBitmap(bitmap)
        }
        holder.productName.text = currentItem.productName
        holder.productDesc.text = currentItem.productDesc
    }

    override fun getItemCount(): Int {
        return productList.size
    }

}
