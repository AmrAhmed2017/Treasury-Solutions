package com.example.core.base

import com.example.network.NetworkManager
import com.example.sharedpreference.SharedPrefManager
import javax.inject.Inject


open class BaseRepo @Inject constructor() {


    val networkManager = NetworkManager()

    @Inject
    lateinit var sharedPrefManager: SharedPrefManager
}