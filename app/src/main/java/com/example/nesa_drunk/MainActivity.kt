package com.example.nesa_drunk

import android.content.Intent
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

        handleIntent(intent)

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> replaceFragment(HomeFragment())
                R.id.nav_info -> replaceFragment(InfoFragment())
                R.id.nav_news -> replaceFragment(NewsFragment())
                R.id.nav_agenda -> replaceFragment(AgendaFragment())
                R.id.nav_profile -> replaceFragment(ProfileFragment())
            }
            true
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        val openFragment = intent?.getStringExtra("OPEN_FRAGMENT")
        if (openFragment == "AGENDA") {
            replaceFragment(AgendaFragment())
            binding.bottomNavigation.selectedItemId = R.id.nav_agenda
        } else if (supportFragmentManager.findFragmentById(R.id.fragment_container) == null) {
            replaceFragment(HomeFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
