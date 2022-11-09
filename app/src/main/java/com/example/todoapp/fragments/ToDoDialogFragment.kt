package com.example.todoapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.todoapp.databinding.FragmentToDoDialogBinding
import com.example.todoapp.model.ToDoData
import com.google.android.material.textfield.TextInputEditText


class ToDoDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentToDoDialogBinding
    private var listener : OnDialogNextBtnClickListener? = null
    private var toDoData: ToDoData? = null

    fun setListener(listener: OnDialogNextBtnClickListener) {
        this.listener = listener
    }

    companion object {
        const val TAG = "DialogFragment"
        @JvmStatic
        fun newInstance(taskId: String, task: String) =
            ToDoDialogFragment().apply {
                arguments = Bundle().apply {
                    putString("taskId", taskId)
                    putString("task", task)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentToDoDialogBinding.inflate(inflater , container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null){
            toDoData = ToDoData(
                arguments?.getString("taskId").toString(),
                arguments?.getString("task").toString()
            )
            binding.taskEdit.setText(toDoData?.task)
        }
        binding.closeBtn.setOnClickListener {
            dismiss()
        }
        binding.addTaskBtn.setOnClickListener {
            val todoTask = binding.taskEdit.text.toString()
            if (todoTask.isNotEmpty()){
                if (toDoData == null){
                    listener?.saveTask(todoTask , binding.taskEdit)
                }else{
                    toDoData!!.task = todoTask
                    listener?.updateTask(toDoData!!, binding.taskEdit)
                }

            }
        }
    }

    interface OnDialogNextBtnClickListener{
        fun saveTask(todoTask:String , todoEdit: TextInputEditText)
        fun updateTask(toDoData: ToDoData , todoEdit: TextInputEditText)
    }


}