package adapter

import `object`.Day
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.calendar_vuvandoan.R
import kotlinx.android.synthetic.main.item_day.view.*
import java.util.*


class MonthAdapter(private val dataList: MutableList<Day>, private val today: String) :
    RecyclerView.Adapter<MonthAdapter.ViewHolder>() {
    lateinit var onItemClick: (index: Int) -> Unit
    var check = 0
    var indexCLick: Int = -1
    fun setItemClick(event: (index: Int) -> Unit) {
        onItemClick = event
    }

    private val TAG = "MonthAdapter"

    @SuppressLint("ClickableViewAccessibility")
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(color: String) {
            val item = dataList[adapterPosition]
            itemView.tvDay.text = item.value
            itemView.tvDay.setTextColor(Color.parseColor(color))
            if (dataList[adapterPosition].isClick) {
                if (adapterPosition > 6)
                    if (check == 1)
                        itemView.setBackgroundColor(Color.parseColor("#339900"))
                    else if (check > 1) {
                        val ran = Random()
                        val color = Color.argb(
                            255, ran.nextInt(256), ran.nextInt(256), ran.nextInt(256)
                        )
                        itemView.setBackgroundColor(color)
                    }

            } else {
                itemView.setBackgroundColor(Color.parseColor("#FFFFFF"))
            }
        }


        init {
            itemView.setOnClickListener {
                onItemClick.invoke(adapterPosition)
                indexCLick = adapterPosition
            }

            itemView.setOnTouchListener { _, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                    check++
                    Handler(Looper.getMainLooper()).postDelayed({
                        Log.d(TAG, "check = $check")
                        check = 0
                    }, 500)
                }
                false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_day, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position <= 6 || dataList[position].isCurrentMonth) {
            holder.bindData("#000000")//currentMonth or title
        } else holder.bindData("#C0C0C0")//previousMonth
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}