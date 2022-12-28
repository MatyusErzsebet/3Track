package com.example.a3track.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.a3track.R
import com.example.a3track.adapters.TasksAdapter
import com.example.a3track.retrofit.Proxy
import com.example.a3track.retrofit.models.CreateTaskModel
import com.example.a3track.retrofit.models.CreateTaskResponse
import com.example.a3track.retrofit.models.ForgetPasswordModel
import com.example.a3track.retrofit.models.GetTasksModel
import com.example.a3track.utils.Constants
import com.example.a3track.viewmodels.DepartmentsViewModel
import com.example.a3track.viewmodels.TasksViewModel
import com.example.a3track.viewmodels.UsersViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*


class NewTaskFragment : Fragment() {

    private lateinit var departmentSpinner: Spinner
    private lateinit var assigneeSpinner: Spinner
    private lateinit var title: TextInputEditText
    private lateinit var description: TextInputEditText
    private lateinit var prioritySpinner: Spinner
    private lateinit var createButton: Button
    private lateinit var datePicker: DatePicker
    private lateinit var usersViewModel: UsersViewModel
    private lateinit var tasksViewModel: TasksViewModel
    private lateinit var departmentsViewModel: DepartmentsViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private var checkedDep = 0
    private var checkedAssign = 0
    private var checkedPrio = 0
    private val errorMessage = MutableLiveData<String>()
    private val tasksResponse = MutableLiveData<CreateTaskResponse>()
    private var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val loading = MutableLiveData<Boolean>()

    fun getCreateTaskResponse(token: String, title: String, description: String, assigneeId: Int, priority: Int, deadline: String, departmentId: Int, status: Int) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val createTaskModel = CreateTaskModel(title = title, description = description, assigneeId = assigneeId, priority = priority, deadLine = deadline, departmentId= departmentId, status = 0)
            val response = Proxy.createTask(token, createTaskModel)
            withContext(Dispatchers.Main) {
                Log.d("asd", createTaskModel.toString())
                if (response.isSuccessful) {
                    val responseBody = response.body()!! as CreateTaskResponse
                    tasksResponse.postValue(responseBody)
                    loading.value = false

                } else {
                    onError("Error when creating task")
                }
            }
        }

    }

    private fun onError(message: String) {
        errorMessage.postValue(message)
        loading.postValue(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        usersViewModel = ViewModelProvider(requireActivity())[UsersViewModel::class.java]
        tasksViewModel = ViewModelProvider(requireActivity())[TasksViewModel::class.java]
        departmentsViewModel = ViewModelProvider(requireActivity())[DepartmentsViewModel::class.java]
        sharedPreferences = requireActivity().getSharedPreferences(Constants.SHAREDPREF, Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        requireActivity().findViewById<TextView>(R.id.screenTitle).text = "Create new task"
        val view = inflater.inflate(R.layout.fragment_new_task, container, false)

        departmentSpinner = view.findViewById(R.id.departmentSpinner)
        assigneeSpinner = view.findViewById(R.id.assigneeSpinner)
        title = view.findViewById(R.id.newTaskTitleEditText)
        description = view.findViewById(R.id.newTaskDescription)
        prioritySpinner = view.findViewById(R.id.prioritySpinner)
        createButton = view.findViewById(R.id.createTaskButton)
        datePicker = view.findViewById(R.id.deadlineDatePicker)

        setAdaptersToSpinners()

        tasksResponse.observe(viewLifecycleOwner) { it ->
            Toast.makeText(requireContext(), "Task created", Toast.LENGTH_LONG).show()
            tasksViewModel.getTasks()
            Handler().postDelayed(Runnable {
                requireActivity().supportFragmentManager.beginTransaction().
                replace(R.id.fragmentContainerView2, MyTasksFragment())
                    .commit()
            }, 3000)

        }

        errorMessage.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }

        createButton.setOnClickListener {
            val day = datePicker.dayOfMonth
            val month = datePicker.month + 1
            val year = datePicker.year

            val dateFormatter = SimpleDateFormat("MM-dd-yyyy")
            val date = dateFormatter.parse("$month-$day-$year") as Date
            val dateInTimeStamp = date.time
            if(checkedAssign > 1 && checkedDep > 1 && checkedPrio > 1 && title.text?.length!! > 0 && description.text?.length!! > 0 ){
                getCreateTaskResponse(sharedPreferences.getString(Constants.TOKEN,"")!!, title.text.toString(), description.text.toString(), usersViewModel.userList[assigneeSpinner.selectedItemPosition].id, prioritySpinner.selectedItemPosition, year.toString(), departmentsViewModel.departmentList[departmentSpinner.selectedItemPosition].id, 0)
            }

            else
                Toast.makeText(requireContext(), "Please fill everything and choose a deadline later than current date", Toast.LENGTH_SHORT).show()

        }


        return view
    }


    private fun setAdaptersToSpinners(){

        val chsDep = "Choose department"
        val departments = mutableListOf(chsDep)
        departments.addAll(departmentsViewModel.departmentList.map {
            it.name
        })
        val departmentsArrayAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, departments )
        departmentsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        departmentSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (checkedDep == 1) {
                    departmentsArrayAdapter.remove(chsDep)
                    departmentsArrayAdapter.notifyDataSetChanged()
                    departmentSpinner.setSelection(position-1)
                }
                checkedDep++
            }


        }

        departmentSpinner.adapter =departmentsArrayAdapter


        val chsAss = "Choose assignee"
        val assignees = mutableListOf(chsAss)
        assignees.addAll(usersViewModel.userList.map {
            it.firstName + " " + it.lastName
        })

        val assigneesArrayAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, assignees)
        assigneesArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        assigneeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                if (checkedAssign == 1) {
                    assigneesArrayAdapter.remove(chsAss)
                    assigneesArrayAdapter.notifyDataSetChanged()
                    assigneeSpinner.setSelection(position - 1)
                }
                checkedAssign++
            }
        }

        assigneeSpinner.adapter = assigneesArrayAdapter

        val chspr = "Choose priority"

        val priorities = mutableListOf(chspr)
        priorities.addAll(mutableListOf("Low", "Medium", "High"))
        val priorityArrayAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, priorities)
        priorityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        prioritySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (checkedPrio == 1) {
                    priorityArrayAdapter.remove(chspr)
                    priorityArrayAdapter.notifyDataSetChanged()
                    prioritySpinner.setSelection(position - 1)
                }
                checkedPrio++
            }

        }

        prioritySpinner.adapter = priorityArrayAdapter
    }




}