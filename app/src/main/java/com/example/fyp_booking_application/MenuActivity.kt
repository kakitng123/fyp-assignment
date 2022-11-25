package com.example.fyp_booking_application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.fyp_booking_application.databinding.ActivityMainBinding
import com.example.fyp_booking_application.databinding.ActivityMenuBinding
import com.example.fyp_booking_application.frontend.bookingFragment
import com.example.fyp_booking_application.frontend.homeFragment
import com.example.fyp_booking_application.frontend.profileFragment

class MenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(homeFragment())

        binding.mainNavView.setOnItemSelectedListener { it ->
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
