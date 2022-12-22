package com.example.fyp_booking_application.frontend.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.frontend.data.TrainingClassData

class UserTrainingClassAdapter (
    private val trainingClassDataArrayList: ArrayList<TrainingClassData>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<UserTrainingClassAdapter.TrainingClassViewholder>() {

    // to inflate the layout for each item of recycler view.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserTrainingClassAdapter.TrainingClassViewholder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_training_class_card, parent, false)
        return TrainingClassViewholder(itemView)
    }

    override fun onBindViewHolder(holder: UserTrainingClassAdapter.TrainingClassViewholder, position: Int) {
        // to set data to textview and imageview of each card layout
        val trainingClassModel: TrainingClassData = trainingClassDataArrayList[position]

        holder.trainingClassName.text = trainingClassModel.className
        holder.trainingClassPrice.text = trainingClassModel.classPrice.toString()
        holder.trainingClassDate.text = trainingClassModel.classDate
        holder.trainingClassTime.text = trainingClassModel.classTime
    }

    override fun getItemCount(): Int {
        // this method is used for showing number of card items in recycler view.
        return trainingClassDataArrayList.size
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    // View holder class for initializing of your views such as TextView and Imageview.
    inner class TrainingClassViewholder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val trainingClassName: TextView
        val trainingClassPrice: TextView
        val trainingClassDate: TextView
        val trainingClassTime: TextView

        init {
            trainingClassName = itemView.findViewById(R.id.tvTrainingClassName)
            trainingClassPrice = itemView.findViewById(R.id.tvTrainingClassPrice)
            trainingClassDate = itemView.findViewById(R.id.tvTrainingClassDate)
            trainingClassTime = itemView.findViewById(R.id.tvTrainingClassTime)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (adapterPosition != RecyclerView.NO_POSITION){
                listener.onItemClick(adapterPosition)
            }
        }
    }
}




