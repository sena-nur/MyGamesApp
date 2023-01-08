package com.sena.mygamesapp.Activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sena.mygamesapp.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navController = findNavController(R.id.nav_host_fragment)
        //Defining a navController to be able to use the navigation component
        setupBottomNavMenu(navController)

    }
    private fun setupBottomNavMenu(navController: NavController) {
        //Setting BottomNavigationView
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.setOnItemSelectedListener{
            when (it.itemId) {
                R.id.games_dest -> {
                    navController.popBackStack(R.id.navigation,true)
                    navController.navigate(R.id.games_dest)
                    return@setOnItemSelectedListener true
                }
                R.id.favourites_dest -> {
                    navController.popBackStack(R.id.navigation,true)
                    navController.navigate(R.id.favourites_dest)
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //set the page transition of the selected menu item
        return item.onNavDestinationSelected(findNavController(R.id.nav_host_fragment)) || super.onOptionsItemSelected(item)
    }
}

