package com.example.a3track.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.a3track.R
import com.example.a3track.retrofit.models.GetDepartmentsModel
import com.example.a3track.retrofit.models.GetTasksModel
import com.example.a3track.retrofit.models.GetUsersModel
import com.example.a3track.utils.Utils
import com.google.android.material.imageview.ShapeableImageView
import kotlin.properties.Delegates

class MembersAdapter(
    var membersList: List<GetUsersModel>,
    private val departmentName: String): RecyclerView.Adapter<MembersAdapter.MemberViewHolder>() {


    inner class MemberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val memberImageView = itemView.findViewById<ShapeableImageView>(R.id.memberImageView)
        private val memberName = itemView.findViewById<TextView>(R.id.memberName)
        private val memberDepartment = itemView.findViewById<TextView>(R.id.memberDepartment)


        fun bindMember(

            name: String,
            department: String
            ) {

            memberDepartment.text = department
            memberName.text = name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MembersAdapter.MemberViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.members_list_item, parent, false)

        return MemberViewHolder(view)
    }

    // Involves populating data into the item through holder
    override fun onBindViewHolder(holder: MembersAdapter.MemberViewHolder, position: Int) {
        val currentItem = membersList[position]
        holder.bindMember(currentItem.firstName+ " " + currentItem.lastName, departmentName)
        Glide.with(holder.itemView).load(currentItem.image).error(R.drawable.avatar).into(holder.memberImageView)
    }
    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return membersList.size
    }
}
