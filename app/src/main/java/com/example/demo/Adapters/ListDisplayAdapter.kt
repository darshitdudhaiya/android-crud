package com.example.demo.Adapters

import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.demo.AddTodoActivity
import com.example.demo.MainActivity
import com.example.demo.Models.Todos
import com.example.demo.R
import com.google.android.material.card.MaterialCardView

class ListDisplayAdapter(private val todos: MutableList<Todos>) : RecyclerView.Adapter<TodoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)

        val viewHolder = TodoViewHolder(view)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return todos.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todo = todos[position]
        holder.bind(todo)
    }
}

class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val titleView: TextView = itemView.findViewById<TextView>(R.id.txtTitle)
    private val descView: TextView = itemView.findViewById<TextView>(R.id.txtDesc)

    fun bind(todos: Todos) {
        val card: MaterialCardView = itemView.findViewById(R.id.itemCard)
        val deletebtn: ImageButton = itemView.findViewById(R.id.deleteNote)

        titleView.text = todos.title
        descView.text = todos.description

        card.setOnClickListener() {
            val context = itemView.context
            val intent = Intent(context, AddTodoActivity::class.java)
            intent.putExtra("id", todos.id)
            context.startActivity(intent)
            (context as? Activity)?.finish()
        }

        deletebtn.setOnClickListener() {
            val context = itemView.context

            context.run {
                val database = openOrCreateDatabase("TodoDb", MODE_PRIVATE, null)

                database.execSQL("DELETE FROM todo WHERE Id = ?", arrayOf(todos.id.toString()))

                val mainActivity = context as MainActivity
                mainActivity.refreshData()
            }
        }
    }
}