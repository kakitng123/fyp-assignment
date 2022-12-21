package com.example.fyp_booking_application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.fyp_booking_application.databinding.ActivityUserDashboardBinding
import com.example.fyp_booking_application.frontend.*

class UserDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(UserProfileFragment())

        binding.apply {
            userBottomNav.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.home -> replaceFragment(BookingCourtHistoryFragment())
                    R.id.training -> replaceFragment(CoachFragment())
                    R.id.court -> replaceFragment(BookingCourtFragment())
                    R.id.myWallet -> replaceFragment(MyWalletFragment())
                    R.id.profile -> replaceFragment(UserProfileFragment())
                }
                true
            }
        }


//
//        val bottomNav = findViewById<BottomNavigationView>(R.id.userBottomNav)
//        bottomNav.setOnNavigationItemSelectedListener() { it ->
//            when(it.itemId) {
//                R.id.home -> loadFragment(UserHomeFragment())
//                R.id.training -> loadFragment(CoachDetailFragment())
//                R.id.court -> loadFragment(BookingCourtFragment())
//                R.id.myWallet -> loadFragment(MyWalletFragment())
//                R.id.profile -> loadFragment(UserProfileFragment())
//            }
//            true
//        }
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

    internal fun setTitle(title: String){
        supportActionBar?.title = title
    }
}
