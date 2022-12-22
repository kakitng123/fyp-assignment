package com.example.fyp_booking_application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var fstore: FirebaseFirestore //get the shared instance of the FirebaseAuth object
    private lateinit var auth: FirebaseAuth //get the shared instance of the FirebaseAuth object

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        //Declare Variable
        val emailReset = findViewById<TextView>(R.id.editResetEmail)
        val resetPassBtn = findViewById<Button>(R.id.btnResetPassword)
        val navLogin = findViewById<TextView>(R.id.tvResetLogin)

        //Navigate to Login Page
        navLogin.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // Initialise
        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()

        //Reset Password Function
        resetPassBtn.setOnClickListener {
            //Declare the variable
            val email = emailReset.text.toString()

            //Validation for all input field and match the pattern
            if(email.isEmpty()) {
                emailReset.error = "Email is required!"
                emailReset.requestFocus()
                return@setOnClickListener
            }else{
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailReset.error = "Invalid email!"
                    emailReset.requestFocus()
                    return@setOnClickListener
                }
            }

            //Check the email and password input
            if (email.isNotEmpty()) {
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener {passReset ->
                        //Check is successful
                        if (passReset.isSuccessful) {
                            //Show successfully Toast Message
                            Toast.makeText(this, "Please check your email to reset your password!", Toast.LENGTH_LONG).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                    }.addOnFailureListener {
                        //Show failure Toast Message
                        Toast.makeText(this, "Try again! Something wrong happened!", Toast.LENGTH_LONG).show()
                    }
            }
            else{
                Toast.makeText(this, "Please insert your email!", Toast.LENGTH_LONG).show() //Show failure Toast Message
            }
        }
    }
}