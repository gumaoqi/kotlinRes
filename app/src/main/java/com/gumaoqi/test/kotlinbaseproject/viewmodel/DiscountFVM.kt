package com.gumaoqi.test.kotlinbaseproject.viewmodel

import androidx.lifecycle.ViewModel
import com.gumaoqi.test.kotlinbaseproject.adapter.FoodAdapter
import com.gumaoqi.test.kotlinbaseproject.entity.Result

class DiscountFVM : ViewModel() {
    lateinit var foodAdapter: FoodAdapter
    lateinit var foodList: ArrayList<Result>
}