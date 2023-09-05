package com.example.diettracker

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.ui.*
import com.bumptech.glide.Glide
import com.example.diettracker.databinding.ActivityNavDrawerBinding

class NavDrawer : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityNavDrawerBinding
    private lateinit var navView: NavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNavDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarNavDrawer.toolbar)

        updateName()
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navController = findNavController(R.id.nav_host_fragment_content_nav_drawer)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_calorie, R.id.nav_food, R.id.nav_profile
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)



        navController.addOnDestinationChangedListener(object : NavController.OnDestinationChangedListener {
            override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
                // your code here

                // Displays user's image:
                // Gets the profile image from shared Preferences
                val sharedPref = getPreferences(Context.MODE_PRIVATE)
                val imageUri = sharedPref?.getString("profileImageUri", null)
                val navImage = findViewById<ImageView>(R.id.imgProfileHeader)

                if(imageUri!= null && navImage != null){
                    val uri = Uri.parse(imageUri)
                    Glide.with(this@NavDrawer).load(uri).into(navImage)
                }

            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.nav_drawer, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_nav_drawer)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onResume() {
        super.onResume()
        updateName()



    }

    // Updates the user's name that's stored in sharedPreferences
    private fun updateName() {

        navView = binding.navView
        val txtNavHeaderName =
            navView.getHeaderView(0).findViewById<TextView>(R.id.txtNavHeaderName)
        val sharedPreferences = getSharedPreferences("MainActivity", Context.MODE_PRIVATE)

        val name = sharedPreferences?.getString("name", "default_value")
        txtNavHeaderName.text = name

    }
}