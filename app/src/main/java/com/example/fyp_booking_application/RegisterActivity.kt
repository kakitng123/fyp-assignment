package com.example.fyp_booking_application

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var fstore: FirebaseFirestore //get the shared instance of the FirebaseAuth object
    private lateinit var auth: FirebaseAuth //get the shared instance of the FirebaseAuth object

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //Declare the variable
        val emailRegister = findViewById<TextView>(R.id.editRegEmail)
        val usernameRegister = findViewById<TextView>(R.id.editRegName)
        val passwordRegister1 = findViewById<TextView>(R.id.editRegPass1)
        val passwordRegister2 = findViewById<TextView>(R.id.editRegPass2)
        val radioGroupReg = findViewById<RadioGroup>(R.id.radioGroup)
        val maleRegBtn = findViewById<RadioButton>(R.id.radioBtnStudent)
        val femaleRegBtn = findViewById<RadioButton>(R.id.radioBtnTeacher)
        val registerBtn = findViewById<Button>(R.id.btnRegSignUp)
        val navLogin = findViewById<TextView>(R.id.linkSignIn)


        //Navigate to Login Page
        navLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
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
            var radioBtn = radioGroupReg.checkedRadioButtonId
            val maleRadio = maleRegBtn
            val femaleRadio = femaleRegBtn


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

//            if(!password.equals(confirmPassword)){
//                passwordRegister2.setError("Password would not be matched!")
//                passwordRegister2.requestFocus()
//                return@setOnClickListener
//            }else{
//                passwordRegister2.setError("Password Matched!")
//                passwordRegister2.requestFocus()
//                return@setOnClickListener
//
//            }

//            if (maleRadio.isChecked() || femaleRadio.isChecked()) {
//                radioBtn.setError("Min confirm password length should be 6 characters!")
//                radioBtn.requestFocus()
//                return@setOnClickListener
//
//                Log.d("QAOD", "Gender is Selected");
//            } else {
//                passwordRegister2.setError("Please select Gender")
//                passwordRegister2.requestFocus()
//                return@setOnClickListener
//            }

            //Check the email and password input
            if (email.isNotEmpty() && password.isNotEmpty()){
                // create user data in Authentication by using this method
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            //Verify the email
                            auth.currentUser!!.sendEmailVerification()
                                .addOnCompleteListener { verify ->
                                    if (verify.isSuccessful) {
                                        Log.d(ContentValues.TAG, "Email sent.")
                                        Toast.makeText(this, "Register successfully. Please verify your email address!", Toast.LENGTH_SHORT).show()
                                    }
                                }

                            //Create a new user with email, username, phone, password field
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
                                .set(user)
                                .addOnSuccessListener { createData ->
                                    Log.d(ContentValues.TAG, "Data added with ID: ")
                                }
                                .addOnFailureListener { e ->
                                    Log.w(ContentValues.TAG, "Error adding document", e)
                                }
                        } else {
                            // If sign up fails, display a message to the user.
                            Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(baseContext, "Email has been used by others accounts!", Toast.LENGTH_SHORT).show()
                        }
                        startActivity(Intent(this, LoginActivity::class.java))
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
