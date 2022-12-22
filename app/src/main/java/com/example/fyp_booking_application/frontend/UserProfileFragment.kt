package com.example.fyp_booking_application.frontend

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.fyp_booking_application.MainActivity
import com.example.fyp_booking_application.UserDashboardActivity
import com.example.fyp_booking_application.databinding.FragmentUserProfileBinding
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.File
import java.util.*

class UserProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth //get the shared instance of the FirebaseAuth object
    private lateinit var fstore: FirebaseFirestore //get the shared instance of the FirebaseAuth object
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private lateinit var binding : FragmentUserProfileBinding

    private lateinit var imgPhoto: ImageView
    val pickImageRequest = 100
    private lateinit var imgUri: Uri
    var filePath: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialise
        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference
        val userID = auth.currentUser?.uid

        binding = FragmentUserProfileBinding.inflate(layoutInflater)
        val userView = (activity as UserDashboardActivity)
        userView.setTitle("Profile")

        //Logout Function
        binding.logoutBtn.setOnClickListener(View.OnClickListener {
            if (auth.getCurrentUser() != null) auth.signOut()
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        })

        binding.iconBrowse.setOnClickListener { launchGallery() }

        //Display user profile data
        val retrieveProfileData = fstore.collection("Users").document(userID.toString())

        retrieveProfileData.get()
            .addOnCompleteListener { resultData ->
                if (resultData != null) {
                    var emailResult= resultData.result.getString("email").toString()
                    var nameResult = resultData.result.getString("username").toString()
                    var phoneResult = resultData.result.getString("phone").toString()
                    var passResult = resultData.result.getString("password").toString()

                    binding.emailProfile.setText(emailResult)
                    binding.nameProfile.setText(nameResult)
                    binding.phoneProfile.setText(phoneResult)
                    binding.passProfile.setText(passResult)
                } else {
                    Log.d("noexits", "No such documents.")
                }
            }.addOnFailureListener { exception ->
                Log.d("noexits", "Error getting documents.", exception)

            }

        // Get a default Photo
        val myPhoto = storageRef.child("profilePicture/")
        val file = File.createTempFile("temp", "png")
        myPhoto.getFile(file)
            .addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                imgPhoto.setImageBitmap(bitmap)
            }
            .addOnFailureListener {
                Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
            }

        //Save Updated Data function
        binding.saveBtn.setOnClickListener {
            var email: String = binding.emailProfile.text.toString()
            var username: String = binding.nameProfile.text.toString()
            var phone: String = binding.phoneProfile.text.toString()
            var password: String = binding.passProfile.text.toString()
            var gender:String = binding.radioGroup.checkedRadioButtonId.toString()

            //Gender Radio Button
            if(binding.radioBtnMale.isChecked){
                gender = "male"
            }else if(binding.radioBtnFemale.isChecked){
                gender = "female"
            }

            ///Validation for all input field and match the pattern
            if (username.isEmpty()) {
                binding.nameProfile.setError("Username is required!")
                binding.nameProfile.requestFocus()
                return@setOnClickListener
            }
            if (phone.isEmpty()) {
                binding.phoneProfile.setError("Phone Number is required!")
                binding.phoneProfile.requestFocus()
                return@setOnClickListener
            } else {
                if (!Patterns.PHONE.matcher(phone).matches()) {
                    binding.phoneProfile.setError("Please provide valid phone!")
                    binding.phoneProfile.requestFocus()
                    return@setOnClickListener
                }
            }

            if(password.isEmpty()){
                binding.passProfile.setError("Password is required!")
                binding.passProfile.requestFocus()
                return@setOnClickListener
            }else{
                if(password.length < 6) {
                    binding.passProfile.setError("Min password length should be 6 characters!")
                    binding.passProfile.requestFocus()
                    return@setOnClickListener
                }
            }
            val userID = auth.currentUser?.uid

            val profileUpdates = hashMapOf(
                "userID" to userID,
                "email" to email,
                "username" to username,
                "phone" to phone,
                "gender" to gender,
                "password" to password
            )
            uploadImage()

            //Update Data
            val updateData = fstore.collection("Users").document(userID.toString())
            updateData.set(profileUpdates)
                .addOnSuccessListener { editData ->
                    Log.d("exits", "User profile updated.")
                    Toast.makeText(activity, "Save Successfully", Toast.LENGTH_SHORT)
                        .show()
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error adding document", e)
                    Toast.makeText(activity, "Save Failure", Toast.LENGTH_SHORT).show()
                }
        }
        return binding.root
    }

    //Pick image from gallery
    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), pickImageRequest)
    }

    //Checking the file path and bitmap
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pickImageRequest && resultCode == Activity.RESULT_OK) {
            if (data == null || data.data == null) {
                return
            }
            filePath = data.data
        }
    }

    //Upload to Firebase Storage
    private fun addUploadRecordToDb(uri: String) {
        val db = FirebaseFirestore.getInstance()
        val data = HashMap<String, Any>()
        data["imageUrl"] = uri

        db.collection("Profile").add(data)
    }

    //Upload Image
    private fun uploadImage() {
        if (filePath != null) {
            val ref = storageRef?.child("profilePicture/" + UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(filePath!!)

            val urlTask =
                uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    return@Continuation ref.downloadUrl
                })?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        addUploadRecordToDb(downloadUri.toString())
                    }
                }?.addOnFailureListener {

                }
        }
    }
}

