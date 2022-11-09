package com.example.todoapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.adapter.TaskAdapter
import com.example.todoapp.databinding.FragmentHomeBinding
import com.example.todoapp.model.ToDoData
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ktx.database

class HomeFragment : Fragment(), ToDoDialogFragment.OnDialogNextBtnClickListener,
    TaskAdapter.TaskAdapterInterface {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: DatabaseReference
    private var dialog: ToDoDialogFragment? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var authId: String
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var toDoItemList: MutableList<ToDoData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        getTaskFromFirebase()
        binding.addTaskBtn.setOnClickListener {
            if (dialog != null)
                childFragmentManager.beginTransaction().remove(dialog!!).commit()
            dialog = ToDoDialogFragment()
            dialog!!.setListener(this)
            dialog!!.show(childFragmentManager, ToDoDialogFragment.TAG)
        }
    }

    private fun getTaskFromFirebase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                toDoItemList.clear()
                for (taskSnapshot in snapshot.children) {
                    val todoTask = taskSnapshot.key?.let { ToDoData(it, taskSnapshot.value.toString()) }
                    if (todoTask != null) {
                        toDoItemList.add(todoTask)
                    }
                }
                taskAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun init() {
        auth = FirebaseAuth.getInstance()
        authId = auth.currentUser!!.uid
        database = Firebase.database.reference.child("Tasks")
            .child(authId)

        binding.mainRecyclerView.setHasFixedSize(true)
        binding.mainRecyclerView.layoutManager = LinearLayoutManager(context)

        toDoItemList = mutableListOf()
        taskAdapter = TaskAdapter(toDoItemList)
        taskAdapter.setListener(this)
        binding.mainRecyclerView.adapter = taskAdapter
    }

    override fun saveTask(todoTask: String, todoEdit: TextInputEditText) {
        database
            .push().setValue(todoTask)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(context, "Task Added Successfully", Toast.LENGTH_SHORT).show()
                    todoEdit.text = null
                } else {
                    Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        dialog!!.dismiss()
    }

    override fun updateTask(toDoData: ToDoData, todoEdit: TextInputEditText) {
        val map = HashMap<String, Any>()
        map[toDoData.taskId] = toDoData.task
        database.updateChildren(map).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
            dialog!!.dismiss()
        }
    }

    override fun onDeleteItemClicked(toDoData: ToDoData, position: Int) {
        database.child(toDoData.taskId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onEditItemClicked(toDoData: ToDoData, position: Int) {
        if (dialog != null) {
            childFragmentManager.beginTransaction().remove(dialog!!).commit()
        }
        dialog = ToDoDialogFragment.newInstance(toDoData.taskId, toDoData.task)
        dialog!!.setListener(this)
        dialog!!.show(childFragmentManager, ToDoDialogFragment.TAG)
    }



}