package com.gumaoqi.test.kotlinbaseproject.viewmodel

import androidx.lifecycle.ViewModel
import com.gumaoqi.test.kotlinbaseproject.adapter.TableAdapter
import com.gumaoqi.test.kotlinbaseproject.entity.Result

class ActionFVM : ViewModel() {
    lateinit var tableAdapter: TableAdapter
    lateinit var tableList: ArrayList<Result>
}