package com.example.a3track

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.a3track.fragments.ActivitiesFragment
import com.example.a3track.fragments.MyTasksFragment
import com.example.a3track.fragments.ProfileFragment
import com.example.a3track.retrofit.models.GetDepartmentsModel
import com.example.a3track.utils.Constants
import com.example.a3track.viewmodels.DepartmentsViewModel
import com.example.a3track.viewmodels.UsersViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var titleTextView: TextView
    private  var departmentsViewModel: DepartmentsViewModel? = null
    private lateinit var usersViewModel: UsersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        titleTextView = findViewById(R.id.screenTitle)

        usersViewModel = ViewModelProvider(this)[UsersViewModel::class.java]

        departmentsViewModel?.success?.observe(this) {
            supportFragmentManager.beginTransaction().add(R.id.fragmentContainerView2, ActivitiesFragment()).commit()
        }

        departmentsViewModel?.errorMessage?.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }

        getSharedPreferences(Constants.SHAREDPREF, Context.MODE_PRIVATE).getString(Constants.TOKEN, "")!!

        usersViewModel.success.observe(this) {
            departmentsViewModel = ViewModelProvider(this)[DepartmentsViewModel::class.java]
        }

        usersViewModel.errorMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }

        setOnClickListenerForBottomNavigation()

    }

    private fun setOnClickListenerForBottomNavigation(){
        bottomNavigationView.setOnNavigationItemSelectedListener OnNavigationItemSelectedListener@{ item ->
            when (item.itemId) {
                R.id.mytasks -> {
                    titleTextView.text = "My Tasks"
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView2, MyTasksFragment()).commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.profile -> {
                    titleTextView.text = "My Profile"
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView2, ProfileFragment()).commit()
                    return@OnNavigationItemSelectedListener true

                }
            }
            true
        }
    }

}