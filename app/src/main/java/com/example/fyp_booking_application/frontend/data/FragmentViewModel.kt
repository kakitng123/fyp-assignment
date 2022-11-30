package com.example.fyp_booking_application.frontend.data

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fyp_booking_application.R

class FragmentViewModel : ViewModel() {
    val data = MutableLiveData<String>()

    fun setData(newData: String){
        data.value = newData
    }
}
