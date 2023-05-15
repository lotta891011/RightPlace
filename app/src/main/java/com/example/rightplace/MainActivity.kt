package com.example.rightplace

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.rightplace.architecture.DocumentTypeViewModel
import com.example.rightplace.architecture.DocumentViewModel
import com.example.rightplace.architecture.SpaceViewModel
import com.example.rightplace.database.AppDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        supportActionBar?.setBackgroundDrawable(getDrawable(R.drawable.ic_launcher_foreground))

        val viewModel: DocumentViewModel by viewModels()
        viewModel.init(AppDatabase.getDatabase(this))

        val documentTypeViewModel: DocumentTypeViewModel by viewModels()
        documentTypeViewModel.init(AppDatabase.getDatabase(this))

        val spaceViewModel: SpaceViewModel by viewModels()
        spaceViewModel.init(AppDatabase.getDatabase(this))

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.cameraFragment, R.id.spaceFragment, R.id.documentTypeFragment)
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        setupWithNavController(
            findViewById<BottomNavigationView>(R.id.bottomNavigationView),
            navHostFragment.navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)||super.onSupportNavigateUp()
    }
}