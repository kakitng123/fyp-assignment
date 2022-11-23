package com.example.fyp_booking_application.backend

import android.content.Context
import android.net.Uri
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fyp_booking_application.R

class productAdapter(private val productList: ArrayList<productData>) :
    RecyclerView.Adapter<productAdapter.MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.product_recyclerview, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = productList[position]
        Glide.with(holder.itemView).load(currentItem).into(holder.product_image)
        holder.product_name.text = currentItem.product_name
        holder.product_category.text = currentItem.product_category
        holder.product_desc.text = currentItem.product_desc
        holder.product_price.text = currentItem.product_price.toString()
        //holder.testing.text = currentItem.product_image
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val product_image: ImageView = itemView.findViewById(R.id.img_productView)
        val product_name: TextView = itemView.findViewById(R.id.tv_productNameView)
        val product_category: TextView = itemView.findViewById(R.id.tv_categoryView)
        val product_desc: TextView = itemView.findViewById(R.id.tv_descView)
        val product_price: TextView = itemView.findViewById(R.id.tv_priceView)
        //val testing: TextView = itemView.findViewById(R.id.tvTest)
    }
}