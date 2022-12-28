package com.example.a3track.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a3track.R
import com.example.a3track.retrofit.models.GetTasksModel
import com.example.a3track.utils.Constants
import com.example.a3track.utils.Utils

class TasksAdapter (private val taskList: List<GetTasksModel>,
                    private val detailsButtonClickListener: DetailsButtonClickListener) : RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {


    interface DetailsButtonClickListener {
        fun onDetailsClick(position: Int)
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val departmentTextView =
            itemView.findViewById<TextView>(R.id.departmentTitle)
        private val titleTextView =
            itemView.findViewById<TextView>(R.id.taskTitle)
        private val assignerTextView =
            itemView.findViewById<TextView>(R.id.assigner)
        private val deadlineTextView =
            itemView.findViewById<TextView>(R.id.deadline)
        private val priorityTextView =
            itemView.findViewById<TextView>(R.id.priority)
        private val descriptionTextView =
            itemView.findViewById<TextView>(R.id.taskDescription)
        private val progressTextView =
            itemView.findViewById<TextView>(R.id.taskProgress)
        private val assignDateTextView = itemView.findViewById<TextView>(R.id.assignDate)

        init{
            itemView.setOnClickListener{
                val position = layoutPosition
                detailsButtonClickListener.onDetailsClick(position)
            }
        }

        fun bindTask(
            department: String,
            title: String,
            assigner: String,
            deadline: Long,
            priority: Int,
            description: String,
            progress: Int,
            createdTime: Long
        ) {
            departmentTextView.text = department
            titleTextView.text = title
            assignerTextView.text = assigner
            deadlineTextView.text = deadline.toString()
            priorityTextView.text = Utils.getPriorityNameFromNumber(priority)
            descriptionTextView.text = description
            progressTextView.text = "$progress% Done"
            assignDateTextView.text = Utils.getDateString(createdTime).substringBeforeLast(':')
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksAdapter.TaskViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val view = inflater.inflate(R.layout.task_list_item, parent, false)
        // Return a new holder instance
        return TaskViewHolder(view)
    }

    // Involves populating data into the item through holder
    override fun onBindViewHolder(holder: TasksAdapter.TaskViewHolder, position: Int) {
        val currentItem = taskList[position]
        holder.bindTask(currentItem.departmentName,
            currentItem.title,
            currentItem.assignerName,
            currentItem.deadline,
            currentItem.priority,
            currentItem.description,
            currentItem.progress ?: 0,
        currentItem.assignDate)
    }


    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return taskList.size
    }
}