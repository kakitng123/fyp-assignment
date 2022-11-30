package com.example.fyp_booking_application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.fyp_booking_application.frontend.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class UserDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_dashboard)
        loadFragment(UserHomeFragment())
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
