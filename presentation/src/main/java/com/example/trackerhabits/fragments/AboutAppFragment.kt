package com.example.trackerhabits.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.trackerhabits.R

class AboutAppFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        val view =  inflater.inflate(R.layout.fragment_about_app, container, false)

        return view
    }

}
