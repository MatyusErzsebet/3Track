package com.example.a3track.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.a3track.R
import com.example.a3track.utils.Constants

class TaskDetailsFragment : Fragment() {

    private lateinit var detailTitle: TextView
    private lateinit var detailDepartment: TextView
    private lateinit var detailAssigner: TextView
    private lateinit var detailAssignDate: TextView
    private lateinit var detailAssignee: TextView
    private lateinit var detailPriority: TextView
    private lateinit var detailProgress: TextView
    private lateinit var detailDescription: TextView
    private lateinit var detailDeadline: TextView


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_task_details, container, false)
        requireActivity().findViewById<TextView>(R.id.screenTitle).text = "Task Description"
        detailTitle = view.findViewById(R.id.taskDetailTitle)
        detailDepartment = view.findViewById(R.id.departmentDetail)
        detailAssigner = view.findViewById(R.id.assignerDetail)
        detailAssignDate = view.findViewById(R.id.assignDateDetail)
        detailAssignee = view.findViewById(R.id.assigneeDetail)
        detailPriority = view.findViewById(R.id.priorityDetail)
        detailProgress = view.findViewById(R.id.progressDetail)
        detailDescription = view.findViewById(R.id.descriptionDetail)
        detailDeadline = view.findViewById(R.id.deadlineDetail)

        detailTitle.text = requireArguments().getString(Constants.TITLE)
        detailDepartment.text = requireArguments().getString(Constants.DEPARTMENT)
        detailAssigner.text = requireArguments().getString(Constants.ASSIGNER)
        detailAssignDate.text = requireArguments().getString(Constants.ASSIGNDATE)
        detailAssignee.text = requireArguments().getString(Constants.ASSIGNEE)
        detailPriority.text = requireArguments().getString(Constants.PRIORITY)
        detailProgress.text = requireArguments().getString(Constants.PROGRESS)
        detailDescription.text = requireArguments().getString(Constants.DESCRIPTION)
        detailDeadline.text = requireArguments().getString(Constants.DEADLINE)

        return view
    }

}