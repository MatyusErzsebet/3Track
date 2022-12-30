package com.example.a3track.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a3track.R
import com.example.a3track.adapters.MembersAdapter
import com.example.a3track.retrofit.models.GetUsersModel
import com.example.a3track.utils.Constants
import com.example.a3track.viewmodels.DepartmentsViewModel
import com.example.a3track.viewmodels.UsersViewModel

class MembersFragment : Fragment() {

    private lateinit var usersViewModel: UsersViewModel
    private lateinit var departmentsViewModel: DepartmentsViewModel
    private lateinit var recView: RecyclerView
    private lateinit var allMembersButton: Button
    private lateinit var ownersButton: Button
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        usersViewModel = ViewModelProvider(requireActivity())[UsersViewModel::class.java]
        departmentsViewModel = ViewModelProvider(requireActivity())[DepartmentsViewModel::class.java]

        super.onCreate(savedInstanceState)
    }


    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        usersViewModel.getUsers()
        val view =  inflater.inflate(R.layout.fragment_members, container, false)
        searchView = view.findViewById(R.id.searchView)
        recView = view.findViewById(R.id.membRecView)
        val depId = requireArguments().getInt(Constants.DEPARTMENT)
        val list = usersViewModel.userList.filter {
            it.departmentId == depId
        }

        var list1: List<GetUsersModel> = listOf()

        list1 = list

        val depName = departmentsViewModel.departmentList.first {
            it.id == depId
        }.name
        val membAdapter = MembersAdapter(list, depName)

        recView.adapter = membAdapter

        recView.layoutManager = LinearLayoutManager(requireContext())

        allMembersButton = view.findViewById(R.id.allMembersButton)
        ownersButton = view.findViewById(R.id.ownersButton)

        /*
        var currentList = mutableListOf<GetUsersModel>()
        var currentOwnersList = mutableListOf<GetUsersModel>()
        currentList.addAll(list)
        currentOwnersList.addAll(currentList.filter {
            it.type == 0
        })
         */

        allMembersButton.setOnClickListener {
            list1 =  list
            membAdapter.membersList = list1
            membAdapter.notifyDataSetChanged()
        }

        ownersButton.setOnClickListener {
            list1 = list.filter {
                it.type == 0
            }
            membAdapter.membersList = list1
            membAdapter.notifyDataSetChanged()
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {

                val currentList = list1.filter {
                    it.firstName.contains(newText, ignoreCase = true) == true || it.lastName.contains(newText, ignoreCase = true) == true
                }

                membAdapter.membersList = currentList
                membAdapter.notifyDataSetChanged()

                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                // task HERE
                return false
            }

        })

        return view
    }


}