package com.sournary.architecturecomponent.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.updatePadding
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.sournary.architecturecomponent.R
import com.sournary.architecturecomponent.databinding.ActivityMainBinding
import com.sournary.architecturecomponent.ext.observeEvent
import com.sournary.architecturecomponent.util.EdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        setupWindow()
        setupNavigation()
        setupViewModel()
    }

    private fun setupWindow() {
        EdgeToEdge.setupRoot(binding.mainRoot)
        binding.root.setOnApplyWindowInsetsListener { v, insets ->
            v.updatePadding(
                left = insets.systemWindowInsetLeft,
                right = insets.systemWindowInsetRight
            )
            insets
        }
    }

    private fun setupNavigation() {
        binding.mainNav.setNavigationItemSelectedListener {
            binding.mainRoot.closeDrawers()
            true
        }
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun setupViewModel() {
        mainViewModel.apply {
            lockNavigation.observe(this@MainActivity) { isLock ->
                val mode = when (isLock) {
                    true -> DrawerLayout.LOCK_MODE_LOCKED_CLOSED
                    else -> DrawerLayout.LOCK_MODE_UNLOCKED
                }
                binding.mainRoot.setDrawerLockMode(mode)
            }
            openNavigation.observeEvent(this@MainActivity) {
                binding.mainRoot.openDrawer(GravityCompat.START)
            }
        }
    }
}
