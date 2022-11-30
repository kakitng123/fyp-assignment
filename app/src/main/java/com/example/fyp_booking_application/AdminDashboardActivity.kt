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
        replaceFragment(AdminHomeFragment())

        binding.apply {
            toggle = ActionBarDrawerToggle(this@AdminDashboardActivity, mainDrawer, R.string.open, R.string.close)
            mainDrawer.setDrawerListener(toggle)
            toggle.syncState()

            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            adminNav.setNavigationItemSelectedListener {
                mainDrawer.closeDrawers()
                when(it.itemId){
                    R.id.nav_admin -> replaceFragment(AdminHomeFragment())
                    R.id.nav_coach -> replaceFragment(CoachAdminFragment())
                    R.id.nav_class -> replaceFragment(ClassAdminFragment())
                    R.id.nav_court -> replaceFragment(CourtAdminFragment())
                    R.id.nav_product -> replaceFragment(ProductAdminFragment())
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

    internal fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.adminLayout, fragment)
        fragmentTransaction.setTransition(TRANSIT_FRAGMENT_OPEN)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
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