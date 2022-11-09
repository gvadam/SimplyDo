package com.example.todoapp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.databinding.EachTodoItemBinding
import com.example.todoapp.model.ToDoData
import java.util.*

class TaskAdapter(private val list: MutableList<ToDoData>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private var listener:TaskAdapterInterface? = null
    fun setListener(listener:TaskAdapterInterface){
        this.listener = listener
    }
    class TaskViewHolder(val binding: EachTodoItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding =
            EachTodoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        with(holder) {
            with(list[position]) {
                binding.taskName.text = this.task
                if(position % 3 == 0){
                    binding.taskContainer.setBackgroundColor(Color.parseColor("#ffc8dd")) //pink
                } else if (position % 2 == 0){
                    binding.taskContainer.setBackgroundColor(Color.parseColor("#bde0fe")) //blue
                } else {
                    binding.taskContainer.setBackgroundColor(Color.parseColor("#ffffff")) //white
                }

                binding.editBtn.setOnClickListener {
                    listener?.onEditItemClicked(this , position)
                }
                binding.deleteBtn.setOnClickListener {
                    listener?.onDeleteItemClicked(this , position)
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface TaskAdapterInterface{
        fun onDeleteItemClicked(toDoData: ToDoData , position : Int)
        fun onEditItemClicked(toDoData: ToDoData , position: Int)
    }

}