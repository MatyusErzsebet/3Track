package com.example.a3track.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a3track.MainActivity
import com.example.a3track.R
import com.example.a3track.adapters.TasksAdapter
import com.example.a3track.retrofit.models.GetTasksModel
import com.example.a3track.utils.Constants
import com.example.a3track.utils.Utils
import com.example.a3track.viewmodels.DepartmentsViewModel
import com.example.a3track.viewmodels.TasksViewModel
import com.example.a3track.viewmodels.UsersViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MyTasksFragment : Fragment(), TasksAdapter.DetailsButtonClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var tasksViewModel: TasksViewModel
    private lateinit var departmentsViewModel: DepartmentsViewModel
    private lateinit var usersViewModel: UsersViewModel
    private lateinit var addButton: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tasksViewModel = ViewModelProvider(requireActivity())[TasksViewModel::class.java]
        departmentsViewModel = ViewModelProvider(requireActivity())[DepartmentsViewModel::class.java]
        usersViewModel = ViewModelProvider(requireActivity())[UsersViewModel::class.java]
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_my_tasks, container, false)

        recyclerView = view.findViewById(R.id.taskRecView)
        addButton = view.findViewById(R.id.add_fab)

        addButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView2, NewTaskFragment()).addToBackStack(null).commit()
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        observeGetTasksResponse()

        //tasksViewModel.getTasksResponse(requireActivity().getSharedPreferences(Constants.SHAREDPREF, Context.MODE_PRIVATE).getString(Constants.TOKEN,"")!!)

        return view
    }

    @SuppressLint("CommitPrefEdits")
    private fun observeGetTasksResponse(){

        tasksViewModel.tasksResponse.observe(viewLifecycleOwner) { it ->
            it.forEach {
                it.assignerName = usersViewModel.getUserNameById(it.assignerId)
                it.departmentName = departmentsViewModel.getDepartmentNameById(it.departmentId)
                it.assigneeName = usersViewModel.getUserNameById(it.assigneeId)
            }
            recyclerView.adapter = TasksAdapter(it,this)
        }

        tasksViewModel.errorMessage.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            recyclerView.adapter = TasksAdapter(listOf<GetTasksModel>(),this)
        }

    }

    override fun onDetailsClick(position: Int) {
        val task = tasksViewModel.taskList[position]
        val fragment = TaskDetailsFragment()
        val bundle = Bundle()
        fragment.arguments = bundle
        bundle.putString(Constants.TITLE, task.title)
        bundle.putString(Constants.DEPARTMENT, task.departmentName)
        bundle.putString(Constants.ASSIGNER, task.assignerName)
        bundle.putString(Constants.ASSIGNEE, task.assigneeName)
        bundle.putString(Constants.ASSIGNDATE, Utils.getDateString(task.assignDate))
        bundle.putString(Constants.PRIORITY, Utils.getPriorityNameFromNumber(task.priority))
        bundle.putString(Constants.PROGRESS,  if (task.progress != null){ "${task.progress}% Done"} else  "0% Done")
        bundle.putString(Constants.DESCRIPTION, task.description)
        bundle.putString(Constants.DEADLINE, task.deadline.toString())

        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView2, fragment).addToBackStack(null).commit()
    }

}