package adapter

import `object`.Day
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp_vuvandoan.R
import kotlinx.android.synthetic.main.item_day.view.*


class MonthAdapter(private val dataList: MutableList<Day>, var indexItemClick: Int) :
    RecyclerView.Adapter<MonthAdapter.ViewHolder>() {
    var month = 0
    var year = 0
    lateinit var context: Context
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
                    if (check == 1) {
                        itemView.setBackgroundColor(Color.parseColor("#7FFFD4"))
                    } else if (check > 1) {
                        itemView.setBackgroundColor(Color.parseColor("#F4A460"))
                    }

            } else {
                itemView.setBackgroundColor(Color.parseColor("#FFFFFF"))
                val share = context.getSharedPreferences("date", Context.MODE_PRIVATE)
                if (month == share.getInt("month", 0) && year == share.getInt("year", 0)
                    && adapterPosition > 6
                    && dataList[adapterPosition].value.toInt() == share.getInt("value", 0)
                    && dataList[adapterPosition].isCurrentMonth == share.getBoolean(
                        "isCurrentMonth",
                        false
                    )
                ) {//set clicked
                    Log.d(TAG, "bindData: $adapterPosition - ${share.getInt("index", 0)}")
                    itemView.setBackgroundColor(Color.parseColor("#7FFFD4"))
                }
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