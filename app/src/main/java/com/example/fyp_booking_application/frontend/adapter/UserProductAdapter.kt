package com.example.fyp_booking_application.frontend.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.frontend.data.ProductData

class UserProductAdapter(
    private val context: Context,
    private val productList: List<ProductData>

) : BaseAdapter() {
    private var layoutInflater: LayoutInflater? = null
    private lateinit var textView: TextView
    private lateinit var imageView: ImageView


    override fun getCount(): Int {
        return productList.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    // in below function we are getting individual item of grid view.
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView = convertView
        if (layoutInflater == null) {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if (convertView == null) {
            convertView = layoutInflater!!.inflate(R.layout.usser_product_card, null)
        }

        textView = convertView!!.findViewById(R.id.textView)
        //textView = convertView!!.findViewById(R.id.textView)
        // on below line we are setting image for our course image view.
       // courseIV.setImageResource(productList.get(position))
        // on below line we are setting text in our course text view.
        textView.setText(productList.get(position).productName)
        // at last we are returning our convert view.
        return convertView

//    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
//        var convertView = convertView
//        if (layoutInflater == null) {
//            layoutInflater =
//                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        }
//        if (convertView == null) {
//            convertView = layoutInflater!!.inflate(R.layout.product_card, null)
//        }
//        //imageView = convertView!!.findViewById(R.id.tvProductImage)
//        textView = convertView!!.findViewById(R.id.tvProductName)
//       // imageView.setImageResource(imageView[position])
//        textView.text = productList[position].toString()
//
//
//        return convertView
    }


}