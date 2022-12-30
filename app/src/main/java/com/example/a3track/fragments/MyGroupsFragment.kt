package com.example.a3track.fragments

import android.os.Bundle
import android.os.Debug
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a3track.R
import com.example.a3track.adapters.DepartmentsAdapter
import com.example.a3track.utils.Constants
import com.example.a3track.viewmodels.DepartmentsViewModel

class MyGroupsFragment : Fragment(), DepartmentsAdapter.DepartmentOnClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var departmentsViewModel: DepartmentsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        departmentsViewModel = ViewModelProvider(requireActivity())[DepartmentsViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_groups, container, false)

        recyclerView = view.findViewById(R.id.depRecView)
        recyclerView.adapter = DepartmentsAdapter(departmentsViewModel.departmentList, this)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        return view
    }

    override fun onDepartmentClick(position: Int) {
        val fragment = MembersFragment()
        val bundle = Bundle()
        bundle.putInt(Constants.DEPARTMENT, (recyclerView.adapter as DepartmentsAdapter).departmentList[position].id)
        fragment.arguments = bundle
        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView2, fragment).addToBackStack(null).commit()
    }

}