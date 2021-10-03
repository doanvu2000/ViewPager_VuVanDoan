package com.example.noteapp_vuvandoan

import adapter.NoteAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_note_home_screen.*
import java.io.*
import java.lang.Exception
import java.nio.charset.Charset

class NoteHomeScreen : AppCompatActivity() {
    var noteList: MutableList<Note> = ArrayList()
    var searchNoteList: MutableList<Note> = ArrayList()
    lateinit var noteDatabase: NoteDatabase
    lateinit var noteAdapter: NoteAdapter
    val fileName = "notes.csv"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_home_screen)

        //initData
        //  initDataList()
        noteDatabase = NoteDatabase(this)
        fetchAllNoteFromDatabase()
        noteAdapter = NoteAdapter(noteList)
        noteAdapter.setOnClickItem {
            Toast.makeText(this, it.title, Toast.LENGTH_SHORT).show()
            val intent = Intent(this, DetailNoteActivity::class.java)
            intent.putExtra("title", it.title)
            intent.putExtra("timeNote", it.timeNote)
            intent.putExtra("content", it.content)
            startActivity(intent)
        }
//        searchNoteList = noteList
        rcv_notes.adapter = noteAdapter
        rcv_notes.layoutManager = LinearLayoutManager(this)
        rcv_notes.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        search_bar.setOnQueryTextListener(object : OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                search_bar.clearFocus()
                //click submit string search
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    searchNoteList = searchNote(newText)
                    sortNoteList(searchNoteList)
                    noteAdapter.noteList = searchNoteList
                    noteAdapter.notifyDataSetChanged()
                }
                return true
            }
        })
        btnAddNote.setOnClickListener {
            startActivity(Intent(this, AddNoteActivity::class.java))
        }

    }

    private fun fetchAllNoteFromDatabase() {
        noteList = noteDatabase.getAllNote()
        sortNoteList(noteList)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.read_from_file -> {//read note from file csv
                readFromFile()
                noteAdapter.notifyDataSetChanged()

            }
            R.id.write_to_file -> {//write noteList to file csv
                writeToFile()
            }
            R.id.view_calendar -> {//intent to Calendar
                startActivity(Intent(this, MainActivity::class.java))
            }
            R.id.delete_all_notes -> {
                deleteAllNotes()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllNotes() {
        noteList.clear()
        noteAdapter.notifyDataSetChanged()
        //clear from database
        noteDatabase.deleteAllNote()
        Toast.makeText(this, "Đã xóa hết các ghi chú", Toast.LENGTH_SHORT).show()
    }

    private fun writeToFile() {
        var stringToFile = ""
        for (item in noteList) {
            stringToFile += item.title + ","
            stringToFile += item.timeNote + ","
            stringToFile += "\"" + item.content + "\"" + "\n"
        }
        try {
            //write stringToFile to fileName
            val writer = FileOutputStream(File(filesDir, fileName))
            writer.write(stringToFile.toByteArray())
            writer.close()
            Toast.makeText(this, "Ghi vào file thành công!", Toast.LENGTH_SHORT)
                .show()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun readFromFile() {
        noteList.clear()
        val input = FileInputStream(File(filesDir, fileName))
        val inputReader = InputStreamReader(input)
        val reader = BufferedReader(inputReader)
        var line = ""
        try {
            while (reader.readLine().also { line = it } != null) {
                //split by ","

                val tokens = line.split(",")
                //Read data
                val title = tokens[0]
                val timeNote = tokens[1]
                val content = line.substring(line.indexOf("\""))
                noteList.add(Note(title, timeNote, content))
            }
        } catch (e: Exception) {
        }
        sortNoteList(noteList)
        for (item in noteList) {
            noteDatabase.insertNote(item)
        }
    }

    private fun sortNoteList(list: MutableList<Note>) {
        for (i in 0 until list.size-2) {
            for (j in i..list.size-1) {
                val timeNote1 = list[i].timeNote.split("/")
                val timeNote2 = list[j].timeNote.split("/")
                if (timeNote2[2] > timeNote1[2]) {//year
                    list[i] = list[j].also { list[j] = list[i] }
                } else if (timeNote2[1] > timeNote1[1]) {//month
                    list[i] = list[j].also { list[j] = list[i] }
                } else if (timeNote2[0] > timeNote1[0]) {//day
                    list[i] = list[j].also { list[j] = list[i] }
                }
            }
        }
    }

    private fun searchNote(s: String): MutableList<Note> =
        noteList.filter { note ->
            note.content.lowercase().contains(s.lowercase())
        } as MutableList<Note>
}