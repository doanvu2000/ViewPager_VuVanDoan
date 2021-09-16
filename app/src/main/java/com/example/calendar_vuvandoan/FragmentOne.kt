package com.example.calendar_vuvandoan

import `object`.Day
import adapter.MonthAdapter
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_one.view.*
import java.time.LocalDateTime
import kotlin.collections.ArrayList

@RequiresApi(Build.VERSION_CODES.O)
class FragmentOne : Fragment() {
    private var weekTitle = mutableListOf("T2", "T3", "T4", "T5", "T6", "T7", "CN")
    private val week = mutableListOf("T2", "T3", "T4", "T5", "T6", "T7", "CN")
    private var time = LocalDateTime.now()
    private val dataList: MutableList<Day> = ArrayList()

    fun newInstance(times: LocalDateTime): FragmentOne {
        val args = Bundle()
        args.putSerializable("time", times)
        val fragment = FragmentOne()
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
        val month = time.monthValue
        val year = time.year
        val day = time.dayOfMonth

        view.tvTimeNow.text = "Ngày $day tháng $month năm $year"
        initDataList("CN")
        val adapter = MonthAdapter(dataList, day.toString())
        view.rcvCurrentMonth.adapter = adapter
        view.rcvCurrentMonth.layoutManager =
            GridLayoutManager(context, 7, GridLayoutManager.VERTICAL, false)
        adapter.setItemClick {
            resetState()
            dataList[it].isClick = true
            adapter.notifyDataSetChanged()
        }
        var startDay = ""
        view.btnSetting.setOnClickListener {
            val arrItems = arrayOf("Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ nhật")
            fun setAdapter() {
                dataList.clear()
                initDataList(startDay)
                adapter.notifyDataSetChanged()
                Toast.makeText(context, "Lịch đã được cập nhật lại!", Toast.LENGTH_SHORT).show()
            }
            MaterialAlertDialogBuilder(requireContext()).setTitle("Chọn ngày bắt đầu của tháng")
                .setItems(arrItems) { _, which ->
                    startDay = when (which) {
                        0 -> "T2"
                        1 -> "T3"
                        2 -> "T4"
                        3 -> "T5"
                        4 -> "T6"
                        5 -> "T7"
                        else -> "CN"
                    }
                    setAdapter()
                }.show()
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

    private fun initDataList(startDay: String) {
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
    }

}