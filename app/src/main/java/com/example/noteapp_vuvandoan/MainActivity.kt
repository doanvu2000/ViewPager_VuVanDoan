package com.example.noteapp_vuvandoan

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {
    val adapterViewPager = AdapterViewPager(supportFragmentManager, 1)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var times = LocalDateTime.now()
        tvTitle.text = "Tháng ${times.month.value} năm ${times.year}"
        adapterViewPager.setView(times, "CN", adapterViewPager)
        viewPager.adapter = adapterViewPager
        viewPager.currentItem = 1
        var currentItem = 0
        var startDay = ""
        btnChangeDay.setOnClickListener {
            val arrItems = arrayOf("Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ nhật")
            fun setAdapter() {
                var dateTime = adapterViewPager.fragmentList[1].time
                adapterViewPager.updateFragment(dateTime, startDay)
                Toast.makeText(this, "Lịch đã được cập nhật lại!", Toast.LENGTH_SHORT).show()
            }

            val dialog: AlertDialog.Builder? =
                AlertDialog.Builder(this).setTitle("Chọn ngày bắt đầu của tháng")
                    .setItems(arrItems) { _, position ->
                        startDay = when (position) {
                            0 -> "T2"
                            1 -> "T3"
                            2 -> "T4"
                            3 -> "T5"
                            4 -> "T6"
                            5 -> "T7"
                            else -> "CN"
                        }
                        setAdapter()
                    }
            dialog?.show()
        }

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                currentItem = position
                var dateTime = adapterViewPager.fragmentList[currentItem].time
                tvTitle.text = "Tháng ${dateTime.month.value} năm ${dateTime.year}"
            }

            override fun onPageScrollStateChanged(state: Int) {

                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    val start = adapterViewPager.fragmentList[1].startDay
                    var dateTime = adapterViewPager.fragmentList[1].time
                    if (currentItem > 1) {//next month
                        dateTime = dateTime.plusMonths(1)
                    } else if (currentItem < 1) { //previous month
                        dateTime = dateTime.minusMonths(1)
                    }
                    tvTitle.text = "Tháng ${dateTime.month.value} năm ${dateTime.year}"
                    adapterViewPager.updateFragment(dateTime, start)
                    viewPager.setCurrentItem(1, false)
                    viewPager.offscreenPageLimit = 2
                }
            }
        })
        btnAddNoteCalendar.setOnClickListener {

        }
    }

    class AdapterViewPager(manager: FragmentManager, behavior: Int) :
        FragmentPagerAdapter(manager, behavior) {
        val fragmentList: MutableList<FragmentOne> = ArrayList()
        override fun getCount(): Int {
            return fragmentList.size
        }

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun setView(time: LocalDateTime, startDay: String, adapterViewPager: AdapterViewPager) {
            adapterViewPager.fragmentList.add(
                FragmentOne().newInstance(time.minusMonths(1).withDayOfMonth(1), startDay)
            )
            adapterViewPager.fragmentList.add(FragmentOne().newInstance(time, startDay))
            adapterViewPager.fragmentList.add(
                FragmentOne().newInstance(time.plusMonths(1).withDayOfMonth(1), startDay)
            )
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun updateFragment(time: LocalDateTime, startDay: String) {
            val previousMonth = time.minusMonths(1)
            val nextMonth = time.plusMonths(1)
            fragmentList[0].time = previousMonth
            fragmentList[1].time = time
            fragmentList[2].time = nextMonth
            fragmentList[0].startDay = startDay
            fragmentList[1].startDay = startDay
            fragmentList[2].startDay = startDay
            fragmentList[0].initDataList(previousMonth, startDay)
            fragmentList[1].initDataList(time, startDay)
            fragmentList[2].initDataList(nextMonth, startDay)
        }
    }
}