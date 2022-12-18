package com.example.fyp_booking_application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN
import com.example.fyp_booking_application.backend.*
import com.example.fyp_booking_application.databinding.ActivityAdminDashboardBinding

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAdminDashboardBinding
    private lateinit var toggle : ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(AdminHomeFragment(), R.id.adminLayout)

        binding.apply {
            toggle = ActionBarDrawerToggle(this@AdminDashboardActivity, mainDrawer, R.string.open, R.string.close)
            mainDrawer.setDrawerListener(toggle)
            toggle.syncState()

            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            adminNav.setNavigationItemSelectedListener {
                mainDrawer.closeDrawers()
                when(it.itemId){
                    R.id.nav_admin -> replaceFragment(AdminHomeFragment(), R.id.adminLayout)
                    R.id.nav_coach -> replaceFragment(AdminCoachFragment(), R.id.adminLayout)
                    R.id.nav_class -> replaceFragment(AdminClassFragment(), R.id.adminLayout)
                    R.id.nav_court -> replaceFragment(AdminCourtFragment(), R.id.adminLayout)
                    R.id.nav_product -> replaceFragment(AdminProductFragment(), R.id.adminLayout)
                    R.id.nav_notification -> replaceFragment(AdminNotifFragment(), R.id.adminLayout)
                    R.id.nav_booking -> replaceFragment(AdminBookingFragment(), R.id.adminLayout)
                    R.id.nav_purchase -> replaceFragment(AdminPurchaseFragment(), R.id.adminLayout)
                    R.id.nav_settings -> replaceFragment(AdminUserFragment(), R.id.adminLayout)
                    R.id.nav_signout -> {
                        startActivity(Intent(this@AdminDashboardActivity, MainActivity::class.java))
                        finish()
                    }
                }
                true
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            true
        }
        return super.onOptionsItemSelected(item)
    }

    internal fun replaceFragment(fragment: Fragment, layout: Int){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(layout, fragment)
        fragmentTransaction.setTransition(TRANSIT_FRAGMENT_OPEN)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}