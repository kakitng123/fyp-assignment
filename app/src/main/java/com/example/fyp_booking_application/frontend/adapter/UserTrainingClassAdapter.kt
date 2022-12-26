package com.example.fyp_booking_application.frontend.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.TrainingClassData

class UserTrainingClassAdapter (
    private val trainingClassDataArrayList: ArrayList<TrainingClassData>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<UserTrainingClassAdapter.TrainingClassViewholder>() {

    //Inflate the layout for each item of recycler view.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserTrainingClassAdapter.TrainingClassViewholder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_training_class_card, parent, false) //Inflate the custom layout
        return TrainingClassViewholder(itemView) //Return a new holder instance
    }

    //Involves the populating data into the item through holder
    override fun onBindViewHolder(holder: UserTrainingClassAdapter.TrainingClassViewholder, position: Int) {
        val trainingClassModel: TrainingClassData = trainingClassDataArrayList[position] //Get the data model based on position

        //Set data to textview and imageview of each card layout
        holder.trainingClassName.text = trainingClassModel.className
        holder.trainingClassPrice.text = trainingClassModel.classPrice.toString()
    }

    override fun getItemCount(): Int {
        // this method is used for showing number of card items in recycler view.
        return trainingClassDataArrayList.size
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    //View holder class for initializing of your views such as TextView and Imageview.
    inner class TrainingClassViewholder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val trainingClassName: TextView
        val trainingClassPrice: TextView

        init {
            trainingClassName = itemView.findViewById(R.id.tvTrainingClassName)
            trainingClassPrice = itemView.findViewById(R.id.tvTrainingClassPrice)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (adapterPosition != RecyclerView.NO_POSITION){
                listener.onItemClick(adapterPosition)
            }
        }
    }
}




