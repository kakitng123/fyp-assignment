package com.example.fyp_booking_application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.fyp_booking_application.databinding.ActivityMainBinding
import com.example.fyp_booking_application.frontend.bookingFragment
import com.example.fyp_booking_application.frontend.homeFragment
import com.example.fyp_booking_application.frontend.profileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_login)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(homeFragment())

        binding.mainNavView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.navBar_home -> replaceFragment(homeFragment())
                R.id.navBar_booking -> replaceFragment(bookingFragment())
                R.id.navBar_profile -> replaceFragment(profileFragment())
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainLayout, fragment)
        fragmentTransaction.commit()

    }
}
/* PUT YOUR (ENTONG OR ME WATEVER) MAINACTIVITY


 */