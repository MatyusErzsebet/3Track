package com.example.a3track.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a3track.R
import com.example.a3track.retrofit.models.GetDepartmentsModel
import com.example.a3track.retrofit.models.GetTasksModel
import com.example.a3track.utils.Utils
import kotlin.properties.Delegates

class DepartmentsAdapter(
    val departmentList: List<GetDepartmentsModel>,
    private val departmentOnClickListener: DepartmentOnClickListener) : RecyclerView.Adapter<DepartmentsAdapter.DepartmentViewHolder>() {


    interface DepartmentOnClickListener {
        fun onDepartmentClick(position: Int)
    }

    inner class DepartmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val departmentTextView =
            itemView.findViewById<TextView>(R.id.depTitle)
        private var departmentId by Delegates.notNull<Int>()

        init {
            itemView.setOnClickListener {
                val position = layoutPosition
                departmentOnClickListener.onDepartmentClick(position)
            }
        }

        fun bindDepartment(
            id:Int,
            departmentName: String,

        ) {
            departmentId = id
            departmentTextView.text = departmentName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepartmentsAdapter.DepartmentViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.department_list_item, parent, false)
        return DepartmentViewHolder(view)
    }

    // Involves populating data into the item through holder
    override fun onBindViewHolder(holder: DepartmentsAdapter.DepartmentViewHolder, position: Int) {
        val currentItem = departmentList[position]
        holder.bindDepartment(currentItem.id,currentItem.name)
    }


    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return departmentList.size
    }
}
