package adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp_vuvandoan.Note
import com.example.noteapp_vuvandoan.R
import kotlinx.android.synthetic.main.item_note_layout.view.*

class NoteAdapter(var noteList: MutableList<Note>) :
    RecyclerView.Adapter<NoteAdapter.ViewHolder>() {
    private var onCLickItem: ((Note) -> Unit)? = null
    fun setOnClickItem(callback: (Note) -> Unit) {
        onCLickItem = callback
    }

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        fun bindData(note: Note) {
            itemView.tvTitleNote.text = note.title
            itemView.tvTimeNote.text = note.timeNote
            itemView.tvContentNote.text = note.content
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_note_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(noteList[position])
        holder.itemView.setOnClickListener { onCLickItem?.invoke(noteList[position]) }
    }

    override fun getItemCount(): Int {
        return noteList.size
    }
}