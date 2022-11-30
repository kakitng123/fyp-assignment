package com.example.fyp_booking_application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.fyp_booking_application.backend.courtAdminFragment
import com.example.fyp_booking_application.backend.productAdminFragment
import com.example.fyp_booking_application.databinding.ActivityUserDashboardBinding
import com.example.fyp_booking_application.frontend.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class UserDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_dashboard)
        loadFragment(UserHomeFragment()) ////
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(UserHomeFragment())
                    return@setOnNavigationItemReselectedListener
                }
                R.id.training -> {
                    loadFragment(TrainingClassFragment())
                    return@setOnNavigationItemReselectedListener
                }
                R.id.court -> {
                    loadFragment(BookingCourtFragment())
                    return@setOnNavigationItemReselectedListener
                }
                R.id.myWallet -> {
                    loadFragment(MyWalletFragment())
                    return@setOnNavigationItemReselectedListener
                }
                R.id.profile-> {
                    loadFragment(UserProfileFragment())
                    return@setOnNavigationItemReselectedListener
                }
            }
        }
    }
    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
