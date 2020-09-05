package com.example.trackerhabits.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.example.domain.HabitType

import com.example.trackerhabits.R
import com.example.trackerhabits.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout

class MainHabitsFragment : Fragment() {

    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager
    lateinit var toolBar: Toolbar
    lateinit var drawerLayout: DrawerLayout

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_main_habits, container, false)

        tabLayout = view.findViewById(R.id.tab_types_habit)
        toolBar = view.findViewById(R.id.toolbar_main)
        viewPager = view.findViewById(R.id.view_pager)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Setting Tab Layout
        val viewPagerAdapter = ViewPagerAdapter(childFragmentManager)
        viewPagerAdapter.addFragment(HabitsListFragment.getInstance(com.example.domain.HabitType.GOOD), "Хорошие")
        viewPagerAdapter.addFragment(HabitsListFragment.getInstance(com.example.domain.HabitType.BAD), "Плохие")
        viewPager.adapter = viewPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        drawerLayout = activity?.findViewById(R.id.drawer_layout)!!

        //Setting ToolBar
        val toggle = ActionBarDrawerToggle(activity, drawerLayout, toolBar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }
}
