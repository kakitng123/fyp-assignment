package com.example.fyp_booking_application

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var fstore: FirebaseFirestore //get the shared instance of the FirebaseAuth object
    private lateinit var auth: FirebaseAuth //get the shared instance of the FirebaseAuth object

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //Declare Variable
        val emailRegister = findViewById<TextView>(R.id.editRegEmail)
        val usernameRegister = findViewById<TextView>(R.id.editRegName)
        val passwordRegister1 = findViewById<TextView>(R.id.editRegPass1)
        val passwordRegister2 = findViewById<TextView>(R.id.editRegPass2)
        val radioRoleReg = findViewById<RadioGroup>(R.id.radioGroupRole)
        val playerRegBtn = findViewById<RadioButton>(R.id.isUser)
        val coachRegBtn = findViewById<RadioButton>(R.id.isAdmin)
        val registerBtn = findViewById<Button>(R.id.btnRegSignUp)
        val navLogin = findViewById<TextView>(R.id.linkSignIn)

        //Navigate to Login Page
        navLogin.setOnClickListener {
            Toast.makeText(this, "Login successfully", Toast.LENGTH_SHORT).show() //Show successfully Toast Message
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // Initialise
        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()

        //On-click Register
        registerBtn.setOnClickListener {
            //Input Data
            var email = emailRegister.text.toString()
            var username = usernameRegister.text.toString()
            var password = passwordRegister1.text.toString()
            var confirmPassword = passwordRegister2.text.toString()
            var radioGroup = radioRoleReg.checkedRadioButtonId.toString()
            var isPlayer = playerRegBtn
            var isCoach = coachRegBtn

            //Validation for all input field and match the pattern
            if(email.isEmpty()) {
                emailRegister.setError("Email is required!")
                emailRegister.requestFocus()
                return@setOnClickListener
            }else{
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailRegister.setError("Invalid email!")
                    emailRegister.requestFocus()
                    return@setOnClickListener
                }
            }
            if(username.isEmpty()){
                usernameRegister.setError("Username is required!")
                usernameRegister.requestFocus()
                return@setOnClickListener
            }
            if(password.isEmpty()){
                passwordRegister1.setError("Password is required!")
                passwordRegister1.requestFocus()
                return@setOnClickListener
            }else{
                if(password.length < 6) {
                    passwordRegister1.setError("Min password length should be 6 characters!")
                    passwordRegister1.requestFocus()
                    return@setOnClickListener
                }
            }

            if(confirmPassword.isEmpty()){
                passwordRegister2.setError("Password is required!")
                passwordRegister2.requestFocus()
                return@setOnClickListener
            }else{
                if(confirmPassword.length < 6) {
                    passwordRegister2.setError("Min password length should be 6 characters!")
                    passwordRegister2.requestFocus()
                    return@setOnClickListener
                }
            }

            if(password!=confirmPassword){
                passwordRegister2.setError("Password does not match!")
                passwordRegister2.requestFocus()
                return@setOnClickListener
            }

            if(!(isPlayer.isChecked() || isCoach.isChecked())){
                Toast.makeText(this, "Please select the role type", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //Check the email and password input
            if (email.isNotEmpty() && password.isNotEmpty()) {
                //Create user data in Authentication by using Password-based Accounts
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            //Verify the email address
                            auth.currentUser!!.sendEmailVerification()
                                .addOnCompleteListener { verify ->
                                    if (verify.isSuccessful) {
                                        Log.d(ContentValues.TAG, "Email sent.")
                                        Toast.makeText(this, "Register successfully. Please verify your email address!",
                                            Toast.LENGTH_SHORT).show()
                                    }
                                    //Verify isPlayer (User)
                                    if (isPlayer.isChecked()) {
                                        val user = hashMapOf(
                                            "email" to email,
                                            "username" to username,
                                            "password" to password,
                                            "confirmPassword" to confirmPassword,
                                            "isUser" to "1",
                                        )
                                        // Add a new document with a generated ID
                                        val userID = auth.currentUser?.uid
                                        fstore.collection("Users").document(userID.toString())
                                            .set(user).addOnSuccessListener { createData ->
                                                Log.d(ContentValues.TAG, "Data added with ID: ")
                                            }.addOnFailureListener { e ->
                                                Log.w(ContentValues.TAG, "Error adding document", e)
                                            }
                                    }
                                    //Verify isCoach (Admin)
                                    if (isCoach.isChecked()) {
                                        val admin = hashMapOf(
                                            "email" to email,
                                            "username" to username,
                                            "password" to password,
                                            "confirmPassword" to confirmPassword,
                                            "isAdmin" to "1",
                                        )
                                        // Add a new document with a generated ID
                                        val adminID = auth.currentUser?.uid
                                        fstore.collection("Users").document(adminID.toString())
                                            .set(admin).addOnSuccessListener { createData ->
                                                Log.d(ContentValues.TAG, "Data added with ID: ")
                                            }.addOnFailureListener { e ->
                                                Log.w(ContentValues.TAG, "Error adding document", e)
                                            }
                                    }
                                }
                        } else {
                            //If sign up fails, display a message to the user.
                            Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(baseContext, "Email has been used by others accounts!", Toast.LENGTH_SHORT).show()
                        }
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }.addOnFailureListener {
                        //Show failure Toast Message
                        Toast.makeText(this, "Registered Failure", Toast.LENGTH_SHORT).show()
                    }
            }
            else{
                Toast.makeText(this, "Please insert your information!", Toast.LENGTH_SHORT).show() //Show failure Toast Message
            }
        }
    }
}