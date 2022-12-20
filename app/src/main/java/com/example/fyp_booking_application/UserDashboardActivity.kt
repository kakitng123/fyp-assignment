package com.example.fyp_booking_application

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.fyp_booking_application.databinding.FragmentTrainingClassBinding
import com.example.fyp_booking_application.frontend.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class UserDashboardActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_dashboard)

        loadFragment(UserHomeFragment()) ////
        val bottomNav = findViewById<BottomNavigationView>(R.id.userBottomNav)
        bottomNav.setOnNavigationItemSelectedListener() { it ->
            when(it.itemId) {
                R.id.home -> loadFragment(UserHomeFragment())
                R.id.training -> loadFragment(CoachDetailFragment())
                R.id.court -> loadFragment(BookingCourtFragment())
                R.id.myWallet -> loadFragment(MyWalletFragment())
                R.id.profile -> loadFragment(UserProfileFragment())
            }
            true
        }
    }
    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.userContainerLayout,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    internal fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.userContainerLayout, fragment)
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
//
    fun replaceFragment(trainingClassFragment: TrainingClassFragment) {
    val fragmentManager = supportFragmentManager
    val fragmentTransaction = fragmentManager.beginTransaction()
    fragmentTransaction.replace(R.id.userContainerLayout, trainingClassFragment)
    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
    fragmentTransaction.addToBackStack(null)
    fragmentTransaction.commit()
}

//    val training: FragmentTrainingClassBinding =
//        (context as AppCompatActivity).supportFragmentManager.beginTransaction()
//    training.replace(R.id.userContainerLayout, TrainingClassFragment()) // Add your fragment class
//
//    training.addToBackStack(null)
//    training.commit()
//    }



}
