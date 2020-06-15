package com.gumaoqi.test.kotlinbaseproject.viewmodel

import androidx.lifecycle.ViewModel
import com.gumaoqi.test.kotlinbaseproject.adapter.HomeTableAdapter
import com.gumaoqi.test.kotlinbaseproject.entity.Result

class HomeFVM : ViewModel() {
    lateinit var tableHomeAdapter: HomeTableAdapter
    lateinit var tableList: ArrayList<Result>
}