package com.gumaoqi.test.kotlinbaseproject.widget

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow

import com.bumptech.glide.Glide
import com.gumaoqi.test.kotlinbaseproject.R
import com.gumaoqi.test.kotlinbaseproject.base.GuApplication
import com.gumaoqi.test.kotlinbaseproject.base.GuApplication.Companion.context
import kotlinx.android.synthetic.main.pop_check_mac_fail.view.*


/**
 * Copyright (C), 2010-2019, 四川源润通信有限公司
 * FileName: CheckMacFailPopupWindow
 * Author: MaoQi Gu
 * Date: 2019/3/4 17:29
 * Description: 检查mac地址的PopupWindown
 * History:
 * UpdateTime: 2019/3/4
 */
class CheckMacFailPopupWindow() : PopupWindow() {

    init {
        val inflater = GuApplication.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.pop_check_mac_fail, null)
        this.contentView = view//设置PopupWindow的view
        this.width = ViewGroup.LayoutParams.MATCH_PARENT
        this.height = ViewGroup.LayoutParams.MATCH_PARENT
        this.isFocusable = true
        view.isFocusable = true//设置该view可以被点击
        Glide.with(context).load(R.mipmap.loading2).into(view.pop_check_mac_fail_iv)
    }
}