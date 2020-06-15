package com.gumaoqi.test.kotlinbaseproject.viewmodel

import androidx.lifecycle.ViewModel
import com.gumaoqi.test.kotlinbaseproject.adapter.OrderAdapter
import com.gumaoqi.test.kotlinbaseproject.adapter.OrderFragmentFoodAdapter
import com.gumaoqi.test.kotlinbaseproject.entity.Result

class RecommendFVM : ViewModel() {
    lateinit var orderFragmentFoodAdapter: OrderFragmentFoodAdapter
    lateinit var orderAdapter: OrderAdapter
    lateinit var foodList: ArrayList<Result>
    lateinit var orderList: ArrayList<Result>
}