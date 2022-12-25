package com.example.fyp_booking_application.frontend.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.fyp_booking_application.NotificationData
import com.example.fyp_booking_application.R

class UserNotificationAdapter(
    private val context: Context,
    private val notificationList: ArrayList<NotificationData>
) : BaseAdapter() {
    override fun getCount(): Int {
        return notificationList.size
    }

    override fun getItem(position: Int): Any {
        return notificationList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = View.inflate(context, R.layout.listview_user_notification, null)

        val currentItem = notificationList[position]
        val notifTitle:TextView = view.findViewById(R.id.listViewText1)
        val notifDesc:TextView = view.findViewById(R.id.listViewText2)

        notifTitle.text = currentItem.notifyTitle
        notifDesc.text = currentItem.notifyMessage

        return view
    }


}