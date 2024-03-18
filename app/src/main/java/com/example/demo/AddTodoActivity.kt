package com.example.demo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class AddTodoActivity : AppCompatActivity() {

    private lateinit var title: TextInputEditText
    private lateinit var description: TextInputEditText

    private lateinit var addBtn: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_todo)

        title = findViewById(R.id.title)
        description = findViewById(R.id.description)
        addBtn = findViewById(R.id.addBtn)

        val id = intent.getIntExtra("id", 0)
        if (id != 0) {

            addBtn.text = "Edit"
            addBtn.setOnClickListener() {
                EditTodoDetails()
            }
            getTodoDetails(id)

        } else {
            addBtn.setOnClickListener() {
                addTodoInDatabase()
            }
        }
    }

    private fun addTodoInDatabase() {
        val database = openOrCreateDatabase("TodoDb", MODE_PRIVATE, null)

        val title = title.text.toString()
        val description = description.text.toString()

        database.execSQL(
            """
            INSERT INTO todo (Title,Description) VALUES(?,?)
        """.trimIndent(), arrayOf(title, description)
        )

        database.close()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun getTodoDetails(id: Int) {
        val database = openOrCreateDatabase("TodoDb", MODE_PRIVATE, null)

        val cursor = database.rawQuery(
            """
            SELECT * FROM todo WHERE Id = ?
        """.trimIndent(), arrayOf(id.toString())
        )

        if (cursor != null && cursor.moveToFirst()) {
            try {
                val titleFromDb = cursor.getString(1)
                val descriptionFromDb = cursor.getString(2)

                title.setText(titleFromDb)
                description.setText(descriptionFromDb)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                cursor.close()
                database.close()
            }
        } else {
            Toast.makeText(this, "No data found for ID: $id", Toast.LENGTH_SHORT).show()
        }
    }

    private fun EditTodoDetails() {
            val title = title.text.toString()
            val description = description.text.toString()
            val database = openOrCreateDatabase("TodoDb", MODE_PRIVATE, null)
            database.execSQL(
                "UPDATE todo SET Title = ?,Description = ? WHERE Id = ?",
                arrayOf(title, description, intent.getIntExtra("id", 0))
            )

            database.close()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
    }
}