package com.example.fyp_booking_application.frontend.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.backend.ProductData

class ProductAdapter(

    private val context: Context,
    private val productArrayList: ArrayList<ProductData>
) : BaseAdapter() {
    private var layoutInflater: LayoutInflater? = null
    private lateinit var textView: TextView

    override fun getCount(): Int {
        return productArrayList.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertedView = convertView
        if(layoutInflater == null){
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if (convertView == null) {
            convertedView = layoutInflater!!.inflate(R.layout.product_card, null)
        }
        textView = convertView!!.findViewById(R.id.tvUserProductName)
        textView.text = productArrayList[position].toString()

        return convertedView
    }



}