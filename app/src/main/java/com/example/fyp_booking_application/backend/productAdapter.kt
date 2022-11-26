package com.example.fyp_booking_application.backend

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.R
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class productAdapter(
    private val productList: ArrayList<productData>,
    private val listener: OnItemClickListener
    ) : RecyclerView.Adapter<productAdapter.MyViewHolder>() {

    private lateinit var storageRef : StorageReference

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val product_image: ImageView = itemView.findViewById(R.id.img_productView)
        val product_name: TextView = itemView.findViewById(R.id.tv_productNameView)
        val product_desc: TextView = itemView.findViewById(R.id.tv_descView)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION)
                listener.onItemClick(position)
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_products, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = productList[position]

        storageRef = FirebaseStorage.getInstance().getReference()
        val currentPhoto = storageRef.child("images/products/product_"+currentItem.product_name)
        val file = File.createTempFile("temp", "png")

        currentPhoto.getFile(file).addOnSuccessListener(){
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            holder.product_image.setImageBitmap(bitmap)
        }
        holder.product_name.text = currentItem.product_name
        holder.product_desc.text = currentItem.product_desc
    }

    override fun getItemCount(): Int {
        return productList.size
    }

}
