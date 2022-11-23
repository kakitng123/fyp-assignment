package com.example.fyp_booking_application.backend

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.AdminActivity
import com.example.fyp_booking_application.R
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class productAdapter(private val productList: ArrayList<productData>) : RecyclerView.Adapter<productAdapter.MyViewHolder>() {

    private lateinit var storageRef : StorageReference
    private lateinit var clickListener : onButtonClickListener


    interface onButtonClickListener{
        fun onEditButtonClicked(position: Int)
        fun onDeleteButtonClicked(position: Int)
    }

    fun setOnButtonClickListener(listener: onButtonClickListener){
        clickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.product_recyclerview, parent, false)
        return MyViewHolder(itemView, clickListener)
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
        holder.product_category.text = currentItem.product_category
        holder.product_desc.text = currentItem.product_desc
        holder.product_price.text = currentItem.product_price.toString()
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    class MyViewHolder(itemView: View, listener: onButtonClickListener) : RecyclerView.ViewHolder(itemView) {
        val product_image: ImageView = itemView.findViewById(R.id.img_productView)
        val product_name: TextView = itemView.findViewById(R.id.tv_productNameView)
        val product_category: TextView = itemView.findViewById(R.id.tv_categoryView)
        val product_desc: TextView = itemView.findViewById(R.id.tv_descView)
        val product_price: TextView = itemView.findViewById(R.id.tv_priceView)
        val btnEdit: Button = itemView.findViewById(R.id.btn_editView)
        val btnDelete: Button = itemView.findViewById(R.id.btn_deleteView)

        init{
            btnEdit.setOnClickListener(){
                listener.onEditButtonClicked(adapterPosition)
            }
            btnDelete.setOnClickListener(){
                listener.onDeleteButtonClicked(adapterPosition)
            }
        }
    }
}