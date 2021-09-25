package com.android.diepdao1708.todo4.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.android.diepdao1708.todo4.R
import com.android.diepdao1708.todo4.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var navController : NavController
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = findViewById(R.id.drawerLayout)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        toggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController
        findViewById<NavigationView>(R.id.nav_view)
            .setupWithNavController(navController)

        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_ghichu -> {
                    setToolbarTitle("Ghi Chú")
                    navController.navigate(R.id.move_ghichu)
                    drawerLayout.close()
                    true
                }
                R.id.nav_loinhac -> {
                    setToolbarTitle("Lời Nhắc")
                    navController.navigate(R.id.move_loinhac)
                    drawerLayout.close()
                    true
                }
                R.id.nav_nhan -> {
                    setToolbarTitle("Nhãn")
                    navController.navigate(R.id.move_nhan)
                    drawerLayout.close()
                    true
                }
                R.id.nav_thungrac -> {
                    setToolbarTitle("Thùng Rác")
                    navController.navigate(R.id.move_thungrac)
                    drawerLayout.close()
                    true
                }
            }
            true
        }

        setToolbarTitle("Ghi Chú")

    }

    private fun setToolbarTitle(title: String){
        supportActionBar?.title = title
    }
}