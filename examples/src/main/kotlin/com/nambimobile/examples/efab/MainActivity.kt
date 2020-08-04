package com.nambimobile.examples.efab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var navigationDrawer: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.navigation_drawer)
        setSupportActionBar(findViewById(R.id.toolbar))

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navigation_host) as NavHostFragment
        val navController = navHostFragment.navController
        navigationDrawer = findViewById(R.id.navigation_drawer)

        findViewById<NavigationView>(R.id.navigation_view).apply {
            setCheckedItem(R.id.default_configuration_xml_fragment)
            setupWithNavController(navController)
            // Since the navigationView is hooked into the navController, whenever a menu item is
            // clicked whose id matches a destination in our navigation graph, that fragment is
            // auto loaded into the NavHost (see main_activity.xml layout file) and the menu item
            // is highlighted. Magic, ladies & gents.
        }

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
            setupActionBarWithNavController(navController, navigationDrawer)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                navigationDrawer.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        when {
            navigationDrawer.isDrawerOpen(GravityCompat.START) ->
                navigationDrawer.closeDrawer(GravityCompat.START)
            else -> super.onBackPressed()
        }
    }
}
