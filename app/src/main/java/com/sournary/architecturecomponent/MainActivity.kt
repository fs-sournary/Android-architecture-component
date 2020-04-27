package com.sournary.architecturecomponent

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.sournary.architecturecomponent.ui.common.MenuFlowViewModel
import com.sournary.architecturecomponent.util.EdgeToEdge
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val menuFlowViewModel: MenuFlowViewModel by viewModels()

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        EdgeToEdge.setupRoot(main_root)
        setupNavigation()
        setupViewModel()
    }

    private fun setupNavigation() {
        main_nav.setNavigationItemSelectedListener {
            true
        }
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun setupViewModel() {
        menuFlowViewModel.apply {
            lockNavigation.observe(this@MainActivity) { isLock ->
                val mode = when (isLock) {
                    true -> DrawerLayout.LOCK_MODE_LOCKED_CLOSED
                    else -> DrawerLayout.LOCK_MODE_UNLOCKED
                }
                main_root.setDrawerLockMode(mode)
            }
            openNavigation.observe(this@MainActivity) {
                main_root.openDrawer(GravityCompat.START)
            }
        }
    }

}
