package com.example.calendar_vuvandoan

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime
import java.util.logging.Handler

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val adapterViewPager = AdapterViewPager(supportFragmentManager, 1)
        var time = LocalDateTime.now()
//        val mHandler = Handler(Looper.getMainLooper())
        adapterViewPager.fragmentList.add(FragmentOne().newInstance(time.plusMonths(-1).withDayOfMonth(1)))
        adapterViewPager.fragmentList.add(FragmentOne().newInstance(time))
        adapterViewPager.fragmentList.add(FragmentOne().newInstance(time.plusMonths(1).withDayOfMonth(1)))
        viewPager.adapter = adapterViewPager
        viewPager.currentItem = 1
    }

    class AdapterViewPager(manager: FragmentManager, behavior: Int) :
        FragmentPagerAdapter(manager, behavior) {
        val fragmentList: MutableList<Fragment> = ArrayList()
        override fun getCount(): Int {
            return fragmentList.size
        }

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

    }
}