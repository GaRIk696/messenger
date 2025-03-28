package com.dpp.messenger.root.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dpp.messenger.R
import com.dpp.messenger.databinding.ActivityRootBinding
import com.dpp.messenger.libs.ThemeManager
import com.dpp.messenger.libs.TokenManager

class RootActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController

        if (savedInstanceState == null && TokenManager.getToken(this) != null){
            navController.navigate(R.id.action_authFragment_to_chatsFragment)
        }

        if (savedInstanceState == null){
            if (ThemeManager.getTheme(this)){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding.bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, navDestination: NavDestination, _ ->
            if (navDestination.id == R.id.authFragment){
                binding.bottomNavigationView.isVisible = false
            } else if (navDestination.id == R.id.chatFragment){
                binding.bottomNavigationView.isVisible = false
            } else {
                binding.bottomNavigationView.isVisible = true
            }

        }
    }
}