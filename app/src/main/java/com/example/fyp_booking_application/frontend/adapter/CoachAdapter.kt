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
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class CoachAdapter(
    private val coachDataArrayList: ArrayList<CoachData>) : RecyclerView.Adapter<CoachAdapter.CoachViewholder>() {
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference

    // to inflate the layout for each item of recycler view.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoachAdapter.CoachViewholder {
        //infate the custom layout
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.coach_card, parent, false)
        //return a new holder instance
        return CoachViewholder(itemView)
    }

    //Involves the populating data into the item through holder
    override fun onBindViewHolder(holder: CoachAdapter.CoachViewholder, position: Int) {
        // Initialise
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference

        //Get the data model based on position
        val coachModel: CoachData = coachDataArrayList[position]

        // to set data to textview and imageview of each card layout
        val img = storageRef.child("images/coachProfile/coach_"+ coachModel.coachName)
        val file = File.createTempFile("temp", "png")

        img.getFile(file).addOnSuccessListener(){
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            holder.coachImage.setImageBitmap(bitmap)
        }

        holder.coachName.text = coachModel.coachName
      //  holder.coachID.text = coachModel.coachID
        holder.coachExperience.text = coachModel.coachExperience
        holder.coachPhone.text = coachModel.coachPhone
        holder.coachEmail.text = coachModel.coachEmail

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

    //Return the total count of items in the list
    override fun getItemCount(): Int {
        // this method is used for showing number of card items in recycler view.
        return coachDataArrayList.size
    }

    // View holder class for initializing of your views such as TextView and Imageview.
    class CoachViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         val coachImage: ImageView
         val coachName: TextView
         val coachID: TextView
         val coachExperience: TextView
         val coachPhone: TextView
         val coachEmail: TextView
         init {
            coachImage = itemView.findViewById(R.id.tvCoachImage)
            coachName = itemView.findViewById(R.id.tvUserProductName)
            coachID = itemView.findViewById(R.id.tvCoachID)
            coachExperience = itemView.findViewById(R.id.tvCoachExperience)
            coachPhone = itemView.findViewById(R.id.tvCoachPhone)
            coachEmail = itemView.findViewById(R.id.tvCoachEmail)
        }
    }
}

