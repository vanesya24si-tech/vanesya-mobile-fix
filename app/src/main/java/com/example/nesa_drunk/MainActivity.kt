package com.example.nesa_drunk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.nesa_drunk.databinding.ActivityMainBinding
import com.example.nesa_drunk.ui.home.HomeFragment
import com.example.nesa_drunk.ui.home.InfoFragment
import com.example.nesa_drunk.ui.news.NewsFragment
import com.example.nesa_drunk.ui.agenda.AgendaFragment
import com.example.nesa_drunk.ui.profile.ProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load HomeFragment by default
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.nav_info -> {
                    replaceFragment(InfoFragment())
                    true
                }
                R.id.nav_news -> {
                    replaceFragment(NewsFragment())
                    true
                }
                R.id.nav_agenda -> {
                    replaceFragment(AgendaFragment())
                    true
                }
                R.id.nav_profile -> {
                    replaceFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(
            android.R.anim.fade_in,
            android.R.anim.fade_out,
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }
}
