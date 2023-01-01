package com.example.a3track.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.a3track.R
import com.example.a3track.retrofit.models.GetUsersModel
import com.example.a3track.utils.Constants
import com.example.a3track.viewmodels.UsersViewModel
import com.google.android.material.imageview.ShapeableImageView


class ProfileFragment : Fragment() {

    private lateinit var profileImageView: ShapeableImageView
    private lateinit var profileName: TextView
    private lateinit var profileDepartment: TextView

    private lateinit var mentorImageView: ShapeableImageView
    private lateinit var mentorName: TextView
    private lateinit var mentorDepartment: TextView

    private lateinit var email: TextView
    private lateinit var phone: TextView
    private lateinit var location: TextView

    private lateinit var usersViewModel: UsersViewModel
    private lateinit var shared: SharedPreferences

    private lateinit var updateProfile: ImageView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().viewModelStore.clear()
        usersViewModel = ViewModelProvider(requireActivity())[UsersViewModel::class.java]
        shared = requireActivity().getSharedPreferences(Constants.SHAREDPREF, Context.MODE_PRIVATE)
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {


        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        profileImageView = view.findViewById(R.id.profileImageView)
        profileName = view.findViewById(R.id.profileName)
        profileDepartment = view.findViewById(R.id.profileDepartment)
        mentorImageView = view.findViewById(R.id.mentorImageView)
        mentorName = view.findViewById(R.id.mentorName)
        mentorDepartment = view.findViewById(R.id.mentorDepartment)
        email = view.findViewById(R.id.profileEmail)
        phone = view.findViewById(R.id.profilePhone)
        location = view.findViewById(R.id.profileLocation)

        updateProfile = view.findViewById(R.id.editProfile)

        updateProfile.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView2, UpdateProfileFragment()).addToBackStack(null).commit()
        }

        val mDefaultBackground = resources.getDrawable(R.drawable.avatar)

        usersViewModel.getUsers()

        usersViewModel.success.observe(viewLifecycleOwner, Observer{
            Glide.with(requireContext()).load(usersViewModel.myUser.image).error(mDefaultBackground).into(profileImageView)
            profileName.text = usersViewModel.myUser.firstName + " " + usersViewModel.myUser.lastName
            profileDepartment.text = "Software Developer"

            Glide.with(requireContext()).load(usersViewModel.myMentor.image).error(mDefaultBackground).into(mentorImageView)
            mentorName.text = usersViewModel.myMentor.firstName + " " + usersViewModel.myMentor.lastName
            mentorDepartment.text = usersViewModel.myUser.firstName + " " + usersViewModel.myUser.lastName +" 's mentor"

            email.text = shared.getString(Constants.EMAIL, "")
            phone.text = usersViewModel.myUser.phoneNumber
            location.text = usersViewModel.myUser.location
        })


        return view
    }

}