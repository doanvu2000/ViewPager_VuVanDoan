package com.example.noteapp_vuvandoan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_note.*

class AddNoteActivity : AppCompatActivity() {
    lateinit var sqliteHelper: NoteDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        sqliteHelper = NoteDatabase(this)
        btnAdd.setOnClickListener {
            addNote()
        }
    }

    private fun addNote() {
        val title = inputTitleNote.text.toString()
        val timeNote = inputTimeNote.text.toString()
        val contentNote = inputContentNote.text.toString()
        if (title.isEmpty() || timeNote.isEmpty() || contentNote.isEmpty()) {
            Toast.makeText(
                this,
                "Bạn phải nhập đủ thông tin 3 trường", Toast.LENGTH_SHORT
            ).show()
        } else {
            val status = sqliteHelper.insertNote(Note(title, timeNote, contentNote))
            if (status > -1) {
                Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show()
                clearEditext()
            } else {
                Toast.makeText(this, "Không thêm được", Toast.LENGTH_SHORT).show()
            }
            val intent = Intent(this, NoteHomeScreen::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

    }

    private fun clearEditext() {
        inputTitleNote.setText("")
        inputTimeNote.setText("")
        inputContentNote.setText("")
        inputTitleNote.requestFocus()
    }
}