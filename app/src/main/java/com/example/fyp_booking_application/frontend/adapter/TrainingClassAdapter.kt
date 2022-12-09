package com.example.fyp_booking_application.frontend.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.frontend.data.CoachData
import com.example.fyp_booking_application.frontend.data.TrainingClassData
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class TrainingClassAdapter (
        private val trainingClassDataArrayList: ArrayList<TrainingClassData>) : RecyclerView.Adapter<TrainingClassAdapter.TrainingClassViewholder>() {
        private lateinit var storage: FirebaseStorage
        private lateinit var storageRef: StorageReference

        // to inflate the layout for each item of recycler view.
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingClassAdapter.TrainingClassViewholder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.training_class_card, parent, false)
            return TrainingClassViewholder(itemView)
        }

        override fun onBindViewHolder(holder: TrainingClassAdapter.TrainingClassViewholder, position: Int) {
            // Initialise
            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.reference

            // to set data to textview and imageview of each card layout
            val trainingClassModel: TrainingClassData = trainingClassDataArrayList[position]
            //holder.trainingClassID.text = trainingClassModel.trainingClassID
            holder.trainingClassName.text = trainingClassModel.trainingClassName
           //holder.trainingClassDetail.text = trainingClassModel.trainingClassDetail
            holder.trainingClassPrice.text = trainingClassModel.trainingClassPrice.toString()
            //holder.trainingClassStatus.text = trainingClassModel.trainingClassStatus
            holder.trainingClassDate.text = trainingClassModel.trainingClassDate
            holder.trainingClassTime.text = trainingClassModel.trainingClassTime

//        // making intent method to go another page
//        holder.itemView.setOnClickListener {
//            val intent = Intent(context, DetailActivity::class.java)
//            intent.putExtra("img", coachModel.coachImage)
//            intent.putExtra("name", coachModel.coachName)
//            intent.putExtra("id", coachModel.coachID)
//            intent.putExtra("experience", coachModel.coachExperience)
//            intent.putExtra("phone", coachModel.coachPhone)
//            intent.putExtra("email", coachModel.coachEmail)
//            intent.putExtra("type", 1)
//            context.startActivity(intent)
//        }
        }

        override fun getItemCount(): Int {
            // this method is used for showing number of card items in recycler view.
            return trainingClassDataArrayList.size
        }

        // View holder class for initializing of your views such as TextView and Imageview.
        class TrainingClassViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val trainingClassName: TextView
            val trainingClassPrice: TextView
            val trainingClassTime: TextView
            val trainingClassDate: TextView
            init {
                trainingClassName = itemView.findViewById(R.id.tvTrainingClassName)
                trainingClassPrice = itemView.findViewById(R.id.tvTrainingClassPrice)
                trainingClassTime = itemView.findViewById(R.id.tvTrainingClassTime)
                trainingClassDate = itemView.findViewById(R.id.tvTrainingClassDate)
            }
        }
}



