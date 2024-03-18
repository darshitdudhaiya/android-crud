package com.example.demo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demo.Adapters.ListDisplayAdapter
import com.example.demo.Models.Todos
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var todorc: RecyclerView
    private lateinit var adapter: ListDisplayAdapter

    private lateinit var noTodoText: TextView

    private lateinit var addBtn: FloatingActionButton


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initDatabase()

        noTodoText = findViewById(R.id.noTodoText)

        addBtn = findViewById(R.id.addTodoBtn)

        addBtn.setOnClickListener() {
            addTodo()
        }

        todorc = findViewById(R.id.todo_RcV)

        adapter = ListDisplayAdapter(retriveTodoFromDb())

        todorc.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        todorc.adapter = adapter


    }

     fun refreshData() {
        todorc = findViewById(R.id.todo_RcV)

        adapter = ListDisplayAdapter(retriveTodoFromDb())

        todorc.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        todorc.adapter = adapter
    }

    private fun retriveTodoFromDb(): MutableList<Todos> {
        val todos = mutableListOf<Todos>()

        val database = openOrCreateDatabase("TodoDb", MODE_PRIVATE, null)

        val cursor = database.rawQuery(
            """
            SELECT * FROM todo
        """.trimIndent(), null
        )

        if (cursor.count > 0) {
            while (cursor.moveToNext()) {
                val id = cursor.getInt(0)
                val title = cursor.getString(1)
                val description = cursor.getString(2)

                todos.add(Todos(id, title, description))
            }
        } else {
            todorc.visibility = View.INVISIBLE
            noTodoText.visibility = View.VISIBLE
        }


        cursor.close()
        database.close()
        return todos
    }

    private fun addTodo() {
        val intent = Intent(this, AddTodoActivity::class.java)
        startActivity(intent)
    }

    private fun initDatabase() {
        val database = openOrCreateDatabase("TodoDb", MODE_PRIVATE, null)

        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS todo (
                Id INTEGER PRIMARY KEY AUTOINCREMENT,
                Title VARCHAR(255) NOT NULL,
                Description VARCHAR(255) NOT NULL
            )
        """.trimIndent()
        )

        database.close()
    }
}