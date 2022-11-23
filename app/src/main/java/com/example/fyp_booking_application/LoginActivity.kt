package com.example.fyp_booking_application

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    private lateinit var fstore: FirebaseFirestore //get the shared instance of the FirebaseAuth object
    private lateinit var auth: FirebaseAuth //get the shared instance of the FirebaseAuth object
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Declare the variable
        val emailLogin = findViewById<TextView>(R.id.editLogEmail)
        val passwordLogin = findViewById<TextView>(R.id.editLogPass)
        val signInBtn = findViewById<Button>(R.id.btnSignIn)
     //   val navForgotPass = findViewById<TextView>(R.id.tvForgotPassword)
        val navRegister = findViewById<TextView>(R.id.linkSignUp)

        //Navigate to Forgot Password Page
//        navForgotPass.setOnClickListener {
//            startActivity(Intent(this, ForgotPasswordActivity::class.java))
//            finish()
//        }

        //Navigate to Register Page
        navRegister.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // Initialise
        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()

        //Sign In Function
        signInBtn.setOnClickListener {
            //Declare the variable
            val email = emailLogin.text.toString()
            val password = passwordLogin.text.toString()

            //Validation for all input field and match the pattern
            if(email.isEmpty()) {
                emailLogin.setError("Email is required!")
                emailLogin.requestFocus()
                return@setOnClickListener
            }else{
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailLogin.setError("Invalid email!")
                    emailLogin.requestFocus()
                    return@setOnClickListener
                }
            }
            if(password.isEmpty()){
                passwordLogin.setError("Password is required!")
                passwordLogin.requestFocus()
                return@setOnClickListener
            }else{
                if(password.length < 6) {
                    passwordLogin.setError("Min password length should be 6 characters!")
                    passwordLogin.requestFocus()
                    return@setOnClickListener
                }
            }

            if (email.isNotEmpty() && password.isNotEmpty()) { //Check the email and password input
                auth.signInWithEmailAndPassword(email, password) //Sign in as user
                    .addOnCompleteListener(this) { signIn ->
                        if (signIn.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")
                            Toast.makeText(this, "Login successfully", Toast.LENGTH_SHORT).show() //Show successfully Toast Message
                            startActivity(Intent(applicationContext, UserDashboardActivity::class.java))
                            finish()
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", signIn.exception)
                            Toast.makeText(this, "Does not have this account. Please register!", Toast.LENGTH_SHORT).show()
                        }
                    }

            }else{
                Toast.makeText(applicationContext, "Please insert your information!", Toast.LENGTH_SHORT).show() //Show failure Toast Message
            }
        }
    }
}
//
//    //Check if the user is currently signed in (havent logout, still in userDasboardUI)
//      override fun onStart(){
//        super.onStart()
//        if(auth.currentUser != null){
//            //navigate to UserDashboard Page
//            startActivity(Intent(this, UserDashboardActivity::class.java))
//        }
//    }