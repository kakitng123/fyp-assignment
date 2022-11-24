package com.example.fyp_booking_application

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.widget.*
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap

class UserDashboardActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth //get the shared instance of the FirebaseAuth object
    private lateinit var fstore: FirebaseFirestore //get the shared instance of the FirebaseAuth object
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference

    private lateinit var imgPhoto: ImageView
    val pickImageRequest = 100
    private lateinit var imgUri: Uri
    var filePath: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_dashboard)

        //Declare the variable
        val emailProfile = findViewById<TextView>(R.id.editProEmail)
        val nameProfile = findViewById<TextView>(R.id.editProName)
        val phoneProfile = findViewById<TextView>(R.id.editProPhone)
        val passwordProfile1 = findViewById<TextView>(R.id.editProPass1)
        val passwordProfile2 = findViewById<TextView>(R.id.editProPass2)
        imgPhoto = findViewById<ImageView>(R.id.imgProPhoto)
        val uploadBtn = findViewById<Button>(R.id.btnUpload)
        val browseBtn = findViewById<Button>(R.id.btnBrowse)
        //val browseBtn = findViewById<ImageButton>(R.id.btnBrowse)
        val retrieveBtn = findViewById<Button>(R.id.btnRetrieve)
        val saveBtn = findViewById<Button>(R.id.btnSave)
        val logoutBtn = findViewById<Button>(R.id.btnLogout)

        // Logout Function
        logoutBtn.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // Initialise
        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference
        val userID = auth.currentUser?.uid

        browseBtn.setOnClickListener { launchGallery() }
        uploadBtn.setOnClickListener { uploadImage() }

        //Get Function
        retrieveBtn.setOnClickListener {
            //Retrieve data to the text view
            val retrieveData = fstore.collection("Users").document(userID.toString())
            retrieveData.get()
                .addOnSuccessListener { resultData ->
                    if (resultData.exists()) {
                        Log.d("exits", "Result Document User Data: ${resultData.data}")
                        emailProfile.text = resultData.getString("email")
                        nameProfile.text = resultData.getString("username")
                        phoneProfile.text = resultData.getString("phone")
                        passwordProfile1.text = resultData.getString("password")
                        passwordProfile2.text = resultData.getString("confirmPassword")
                    } else {
                        Log.d("noexits", "No such documents.")
                    }
                    Toast.makeText(applicationContext, "Retrieve Successfully", Toast.LENGTH_SHORT)
                        .show()
                }.addOnFailureListener { exception ->
                    Log.d("noexits", "Error getting documents.", exception)
                    Toast.makeText(applicationContext, "Retrieve Failure", Toast.LENGTH_SHORT)
                        .show()
                }

            // Get a default Photo
            val myPhoto = storageRef.child("profilePicture/logo.png")
            val file = File.createTempFile("temp", "png")
            myPhoto.getFile(file)
                .addOnSuccessListener {
                    val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                    imgPhoto.setImageBitmap(bitmap)
                }
                .addOnFailureListener {
                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
                }
        }

        //Save Updated Data function
        saveBtn.setOnClickListener {
            var email = emailProfile.text.toString()
            var username = nameProfile.text.toString()
            var phone = phoneProfile.text.toString()
            var password = passwordProfile1.text.toString()
            var confirmPassword = passwordProfile2.text.toString()

            ///Validation for all input field and match the pattern
            if (username.isEmpty()) {
                nameProfile.setError("Username is required!")
                nameProfile.requestFocus()
                return@setOnClickListener
            }
            if (phone.isEmpty()) {
                phoneProfile.setError("Phone Number is required!")
                phoneProfile.requestFocus()
                return@setOnClickListener
            } else {
                if (!Patterns.PHONE.matcher(phone).matches()) {
                    phoneProfile.setError("Please provide valid phone!")
                    phoneProfile.requestFocus()
                    return@setOnClickListener
                }
            }

            if(password.isEmpty()){
                passwordProfile1.setError("Password is required!")
                passwordProfile1.requestFocus()
                return@setOnClickListener
            }else{
                if(password.length < 6) {
                    passwordProfile1.setError("Min password length should be 6 characters!")
                    passwordProfile1.requestFocus()
                    return@setOnClickListener
                }
            }
            if(confirmPassword.isEmpty()){
                passwordProfile2.setError("Password is required!")
                passwordProfile2.requestFocus()
                return@setOnClickListener
            }else{
                if(confirmPassword.length < 6) {
                    passwordProfile2.setError("Min password length should be 6 characters!")
                    passwordProfile2.requestFocus()
                    return@setOnClickListener
                }
            }

            val profileUpdates = hashMapOf(
                "email" to email,
                "username" to username,
                "phone" to phone,
                "password" to password,
                "confirmPassword" to confirmPassword,
            )

            //Update Data
            val userID = auth.currentUser?.uid
            val updateData = fstore.collection("Users").document(userID.toString())
            updateData.set(profileUpdates)
                .addOnSuccessListener { editData ->
                    Log.d("exits", "User profile updated.")
                    Toast.makeText(applicationContext, "Save Successfully", Toast.LENGTH_SHORT)
                        .show()
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error adding document", e)
                    Toast.makeText(applicationContext, "Save Failure", Toast.LENGTH_SHORT).show()
                }
        }
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
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                imgPhoto.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    //Upload to Firebase Storage
    private fun addUploadRecordToDb(uri: String) {
        val db = FirebaseFirestore.getInstance()

        val data = HashMap<String, Any>()
        data["imageUrl"] = uri

        db.collection("Profile")
            .add(data)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Saved to DB", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error saving to DB", Toast.LENGTH_LONG).show()
            }
    }

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
                    } else {
                        // Handle failures
                    }
                }?.addOnFailureListener {

                }
        } else {
            Toast.makeText(this, "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }
    }
}

