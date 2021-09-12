package com.android.diepdao1708.todo4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.android.diepdao1708.todo4.fragments.*
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController : NavController
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerLayout)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
//        setEventMenuToolbar(toolbar)




        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        toggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController
        findViewById<NavigationView>(R.id.nav_view)
            .setupWithNavController(navController)

        setupNavigationMenu(navController)

        setToolbarTitle("Ghi Chú")

    }

    private fun setupNavigationMenu(navController: NavController){
        val slideNavView = findViewById<NavigationView>(R.id.nav_view)
        slideNavView?.setupWithNavController(navController)

        setEventDrawer(slideNavView)
    }

//    // set lai su kien ->>>
//    private fun setEventMenuToolbar(toolbar: Toolbar){
//        toolbar.setOnMenuItemClickListener {
//            if(it.itemId == R.id.action_menu){
//                drawerLayout.openDrawer(GravityCompat.START)
//                return@setOnMenuItemClickListener true
//            }
//            return@setOnMenuItemClickListener onOptionsItemSelected(it)
//        }
//    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.toolbar_menu,menu)
//        return true
//    }

    // Co chuc nang goi lai id cua fragment dang chay
    private fun checkId(): Int{
        return navHostFragment.childFragmentManager.fragments[0].id
    }

    // https://developer.android.com/guide/navigation/navigation-global-action
    // Vi minh ko the check tat ca truong hop nen lam local action
    private fun setEventDrawer(slideNavView : NavigationView){
        slideNavView.setNavigationItemSelectedListener {
            when (it.itemId){
                R.id.nav_ghichu -> {

                    if(checkId() != R.id.ghiChuFragment) {
                        setToolbarTitle("Ghi Chú")
                        navController.navigate(R.id.move_ghichu)
                    }
                    drawerLayout.close()
                    true
                }
                R.id.nav_loinhac -> {
                    setToolbarTitle("Lời Nhắc")
                    if(checkId() != R.id.loiNhacFragment) navController.navigate(R.id.move_loinhac)
                    drawerLayout.close()
                    true
                }
                R.id.nav_nhan -> {
                    setToolbarTitle("Nhãn")
                    if(checkId() != R.id.nhanFragment) navController.navigate(R.id.move_nhan)
                    drawerLayout.close()
                    true
                }
                R.id.nav_thungrac -> {
                    setToolbarTitle("Thùng Rác")
                    if(checkId() != R.id.thungRacFragment) navController.navigate(R.id.move_thungrac)
                    drawerLayout.close()
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    private fun setToolbarTitle(title: String){
        supportActionBar?.title = title
    }
}