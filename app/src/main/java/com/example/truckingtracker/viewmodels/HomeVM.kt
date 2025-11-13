package com.example.truckingtracker.viewmodels

import androidx.lifecycle.ViewModel
import com.example.truckingtracker.repos.HomeRepo

class HomeVM(): ViewModel() {

    val homeRepo = HomeRepo()
    fun getServiceDataList() = homeRepo.getServiceDataList()
}