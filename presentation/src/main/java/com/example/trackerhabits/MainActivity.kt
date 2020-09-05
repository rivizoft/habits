package com.example.trackerhabits

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.domain.usecases.HabitIntercept
import com.example.trackerhabits.di.AppComponent
import com.example.trackerhabits.di.ContextModule
import com.example.trackerhabits.di.DaggerAppComponent
import com.example.trackerhabits.fragments.AddOrEditHabitFragment
import com.example.trackerhabits.viewModels.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet.view.*
import javax.inject.Inject

class MainActivity : FragmentActivity() {

    lateinit var floatingButton: FloatingActionButton
    lateinit var drawerLayout: DrawerLayout
    lateinit var drawerNavView: NavigationView
    lateinit var navController: NavController
    lateinit var mModel: MainViewModel
    lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    @Inject
    lateinit var habitIntercept: HabitIntercept

    //Setting Dagger set context
    lateinit var appComponent: AppComponent

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        floatingButton = findViewById(R.id.fab_main)
        drawerLayout = findViewById(R.id.drawer_layout)
        drawerNavView = findViewById(R.id.navigation_view_main)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_main_controller) as NavHostFragment
        navController = navHostFragment.navController

        //Setting Dagger
        appComponent = DaggerAppComponent.builder()
        .contextModule(ContextModule(applicationContext))
        .build()

        //Inject
        appComponent.inject(this)

        //Setting FAB
        fab_main.setOnClickListener()
        {
            val fragment = AddOrEditHabitFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        //Setting FAB hide/show
        supportFragmentManager.addOnBackStackChangedListener()
        {
            when (supportFragmentManager.backStackEntryCount)
            {
                0 ->
                {
                    fab_main.show()
                    bottom_sheet_layout.visibility = View.VISIBLE
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                }
                else ->
                {
                    fab_main.hide()
                    bottom_sheet_layout.visibility = View.INVISIBLE
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }
            }
        }

        //Setting Bottom Sheet
        val bottomSheet = findViewById<LinearLayout>(R.id.bottom_sheet_layout)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottom_sheet_layout.setOnClickListener {
                bottomSheetBehavior.state =
                    if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED)
                        BottomSheetBehavior.STATE_EXPANDED
                    else
                        BottomSheetBehavior.STATE_COLLAPSED
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback()
        {
            override fun onStateChanged(bottomSheet: View, newState: Int) {}

            override fun onSlide(bottomSheet: View, slideOffset: Float)
            {
                bottom_sheet_layout.bottom_sheet_arrow.rotation = slideOffset * 180
            }

        })

        //Setting Drawer Navigation View
        drawerNavView.setNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId)
            {
                R.id.about_window ->
                {
                    navController.navigate(R.id.aboutAppFragment)
                    bottom_sheet_layout.visibility = View.GONE
                    fab_main.hide()
                }
                R.id.main_window ->
                {
                    navController.navigate(R.id.mainHabitsFragment)
                    bottom_sheet_layout.visibility = View.VISIBLE
                    fab_main.show()
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        //Setting ViewModel
        mModel = ViewModelProvider(this, object : ViewModelProvider.Factory
        {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MainViewModel(application, habitIntercept) as T
            }

        }).get(MainViewModel::class.java)

        mModel.toastMessages.observe(this)
        {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }

        mModel.syncLocalHabitsWithServer()

        //Setting logo
            Glide.with(this)
                .load("https://rivizoft.ru/src/logo.png")
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.ic_launcher_background)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(drawerNavView.getHeaderView(0).findViewById(R.id.header_logo) as ImageView)
    }

    override fun onBackPressed()
    {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }
}
