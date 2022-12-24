package com.example.fyp_booking_application.frontend.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_booking_application.CoachData
import com.example.fyp_booking_application.R
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class UserCoachAdapter(
    private val coachDataArrayList: ArrayList<CoachData>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<UserCoachAdapter.CoachViewholder>() {
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference

    //Inflate the layout for each item of recycler view.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserCoachAdapter.CoachViewholder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_coach_card, parent, false) //Inflate the custom layout
        return CoachViewholder(itemView) //Return a new holder instance
    }

    //Involves the populating data into the item through holder
    override fun onBindViewHolder(holder: UserCoachAdapter.CoachViewholder, position: Int) {
        //Initialise
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        val coachModel: CoachData = coachDataArrayList[position] //Get the data model based on position

        //Set data to textview and imageview of each card layout
        val img = storageRef.child("images/coachProfile/coach_"+ coachModel.coachName)
        val file = File.createTempFile("temp", "png")

        img.getFile(file).addOnSuccessListener(){
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            holder.coachImage.setImageBitmap(bitmap)
        }

        holder.coachName.text = coachModel.coachName
        holder.coachExperience.text = coachModel.coachExp
        holder.coachPhone.text = coachModel.coachPhone
        holder.coachEmail.text = coachModel.coachEmail
    }

    //Return the total count of items in the list
    override fun getItemCount(): Int {
        return coachDataArrayList.size
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    //View holder class for initializing of your views such as TextView and Imageview.
    inner class CoachViewholder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val coachImage: ImageView
        val coachName: TextView
        val coachExperience: TextView
        val coachPhone: TextView
        val coachEmail: TextView
        init {
            coachImage = itemView.findViewById(R.id.tvCoachImage)
            coachName = itemView.findViewById(R.id.tvCoachName)
            coachExperience = itemView.findViewById(R.id.tvCoachExperience)
            coachPhone = itemView.findViewById(R.id.tvCoachPhone)
            coachEmail = itemView.findViewById(R.id.tvCoachEmail)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (adapterPosition != RecyclerView.NO_POSITION){
                listener.onItemClick(adapterPosition)
            }
        }
    }
}

