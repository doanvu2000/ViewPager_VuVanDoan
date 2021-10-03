package com.example.noteapp_vuvandoan

import `object`.Day
import adapter.MonthAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_one.view.*
import java.time.LocalDateTime
import kotlin.collections.ArrayList

@RequiresApi(Build.VERSION_CODES.O)
class MonthFragment : Fragment() {
    private var weekTitle = mutableListOf("T2", "T3", "T4", "T5", "T6", "T7", "CN")
    private val week = mutableListOf("T2", "T3", "T4", "T5", "T6", "T7", "CN")
    lateinit var time: LocalDateTime
    private val dataList: MutableList<Day> = ArrayList()
    var startDay = ""
    lateinit var adapter: MonthAdapter
    var listTime: MutableList<String> = ArrayList()
    fun newInstance(times: LocalDateTime, startDay: String): MonthFragment {
        val args = Bundle()
        args.putSerializable("time", times)
        args.putString("startDay", startDay)
        val fragment = MonthFragment()
        fragment.arguments = args
        return fragment
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_one, container, false)
        time = arguments?.getSerializable("time") as LocalDateTime
        startDay = arguments?.getString("startDay") as String
        if (startDay == "") {
            startDay = "CN"
        }
        adapter = MonthAdapter(dataList, listTime)
        adapter.context = requireContext()
        initDataList(time, startDay)
        adapter.listTime = listTime
        view.rcvCurrentMonth.adapter = adapter
        view.rcvCurrentMonth.layoutManager =
            GridLayoutManager(context, 7, GridLayoutManager.VERTICAL, false)
        val share = context?.getSharedPreferences("date", Context.MODE_PRIVATE)
        adapter.setItemClick {
            resetState()
            dataList[it].isClick = true
            adapter.day = dataList[it].value
            adapter.notifyDataSetChanged()
            val editor = share?.edit()
//            editor?.putInt("index", it)
//            editor?.putInt("value", dataList[it].value.toInt())
//            editor?.putBoolean("isCurrentMonth", dataList[it].isCurrentMonth)
//            editor?.putInt("month", time.month.value)
//            editor?.putInt("year", time.year)
//            editor?.commit()
            //get time and put to AddNoteActivity

            var month = time.month.value
            var year = time.year
            if (!dataList[it].isCurrentMonth) {
                if (it < 20) // not currentMonth
                    month--
                else month++
            }
            adapter.day = dataList[it].value
            adapter.month = month
            adapter.year = year

        }

        return view
    }

    private fun resetState() {
        for (i in 0 until dataList.size) {
            dataList[i].isClick = false
        }
    }


    private fun addTitleWeek(startDay: String) {
        weekTitle = when (startDay) {
            "T3" -> mutableListOf("T3", "T4", "T5", "T6", "T7", "CN", "T2")
            "T4" -> mutableListOf("T4", "T5", "T6", "T7", "CN", "T2", "T3")
            "T5" -> mutableListOf("T5", "T6", "T7", "CN", "T2", "T3", "T4")
            "T6" -> mutableListOf("T6", "T7", "CN", "T2", "T3", "T4", "T5")
            "T7" -> mutableListOf("T7", "CN", "T2", "T3", "T4", "T5", "T6")
            "CN" -> mutableListOf("CN", "T2", "T3", "T4", "T5", "T6", "T7")
            else -> mutableListOf("T2", "T3", "T4", "T5", "T6", "T7", "CN")
        }
        for (i in 0 until weekTitle.size)
            dataList.add(Day(weekTitle[i], isCurrentMonth = true, isClick = false))
    }

    fun initDataList(timeInit: LocalDateTime, startDay: String) {
        dataList.clear()
        //title
        addTitleWeek(startDay)
        val times = time.withDayOfMonth(1)
        val index = weekTitle.indexOf(week[times.dayOfWeek.value - 1]) + 1
        val dayOfPreviousMonth = time.withDayOfMonth(1).plusDays((-index).toLong()).dayOfMonth
        //Day of previous month
        for (i in 1 until index) {
            dataList.add(
                Day(
                    (dayOfPreviousMonth + i).toString(),
                    isCurrentMonth = false,
                    isClick = false
                )
            )
        }
        //Day of current month
        for (i in 1..time.month.maxLength())
            dataList.add(Day(i.toString(), isCurrentMonth = true, isClick = false))

        //Day of next month
        var nextDay = 0
        while (dataList.size % 7 != 0) {
            nextDay++
            dataList.add(Day(nextDay.toString(), isCurrentMonth = false, isClick = false))
        }
        adapter.month = time.month.value
        adapter.year = time.year
        adapter.notifyDataSetChanged()
    }

}