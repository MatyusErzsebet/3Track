package com.example.a3track.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.a3track.R
import com.example.a3track.retrofit.Proxy
import com.example.a3track.retrofit.models.GetDepartmentsModel
import com.example.a3track.retrofit.models.GetUsersModel
import com.example.a3track.retrofit.models.UpdateProfileModel
import com.example.a3track.utils.Constants
import com.example.a3track.viewmodels.UsersViewModel
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.*


class UpdateProfileFragment : Fragment() {

    private lateinit var profile: ShapeableImageView
    private lateinit var firstName: EditText
    private lateinit var lastName: EditText
    private lateinit var phone: EditText
    private lateinit var location:EditText
    private lateinit var shared: SharedPreferences
    private lateinit var userViewModel: UsersViewModel
    private lateinit var myUser: GetUsersModel
    private lateinit var updateButton: Button

    val errorMessage = MutableLiveData<String>()
    var job: Job? = null
    val success = MutableLiveData<Boolean>()
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val loading = MutableLiveData<Boolean>()


    private fun onError(message: String) {
        errorMessage.postValue(message)
        loading.postValue(false)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shared = requireActivity().getSharedPreferences(Constants.SHAREDPREF, Context.MODE_PRIVATE)
        userViewModel = ViewModelProvider(requireActivity())[UsersViewModel::class.java]
        myUser = userViewModel.userList.first {
            it.id == shared.getInt(Constants.USERID, 0)
        }

    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_update_profile, container, false)

        profile = view.findViewById(R.id.shapeableImageView2)
        firstName = view.findViewById(R.id.updatefirstName)
        lastName = view.findViewById(R.id.updateLastName)
        phone = view.findViewById(R.id.updatePhone)
        location = view.findViewById(R.id.updateLocation)

        Glide.with(requireContext()).load(myUser.image).error(R.drawable.avatar).into(profile)
        firstName.setText(myUser.firstName)
        lastName.setText(myUser.lastName)
        phone.setText(myUser.phoneNumber)
        location.setText(myUser.location)

        updateButton = view.findViewById(R.id.updateProfileButton)


        val save = userViewModel.userList
        userViewModel.success.postValue(null)


        updateButton.setOnClickListener {
            if(lastName.text.isNotEmpty() && firstName.text.isNotEmpty() && phone.text.isNotEmpty() && location.text.isNotEmpty()){

                success.observe(viewLifecycleOwner, Observer {
                    if(it == true){
                        userViewModel.success.postValue(false)
                        Toast.makeText(requireContext(), "Profile updated", Toast.LENGTH_LONG).show()
                        Log.d("MYUSER", userViewModel.myUser.lastName)
                        Handler().postDelayed(Runnable {
                            requireActivity().supportFragmentManager.beginTransaction().
                            replace(R.id.fragmentContainerView2, ProfileFragment())
                                .commit()
                        }, 3000)
                    }
                    else
                        Toast.makeText(requireContext(), "Couldn't update profile", Toast.LENGTH_LONG).show()

                })

                errorMessage.observe(viewLifecycleOwner, Observer {
                    Toast.makeText(requireContext(), "Couldn't update profile", Toast.LENGTH_LONG).show()
                })

                getUpdateResponse()

            }
            else
                Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show()
        }



        return view
    }

    private fun getUpdateResponse(){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val response = Proxy.updateProfile(shared.getString(Constants.TOKEN,"")!!, UpdateProfileModel(firstName = firstName.text.toString(), lastName = lastName.text.toString(), phoneNumber = phone.text.toString(), location = location.text.toString(), imageUrl = myUser.image))
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val responseBody = response.body()!!
                    if(responseBody.message.compareTo("Success") == 0)
                        success.postValue(true)
                    else
                        success.postValue(false)
                    loading.value = false

                } else {
                    onError("Can not get departments!")
                }
            }
        }
    }


}