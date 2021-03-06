package com.gumaoqi.test.kotlinbaseproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gumaoqi.test.kotlinbaseproject.R
import com.gumaoqi.test.kotlinbaseproject.base.BaseAdapter
import com.gumaoqi.test.kotlinbaseproject.base.GuApplication
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg
import com.gumaoqi.test.kotlinbaseproject.entity.Result
import com.gumaoqi.test.kotlinbaseproject.tool.C

class HomeTableAdapter : BaseAdapter() {
    lateinit var adapterList: List<Any>

    fun getList(): List<Any>? {
        return adapterList
    }

    fun setList(list: List<Any>) {
        this.adapterList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            when (viewType) {
                0 -> {
                    val view = LayoutInflater.from(GuApplication.context).inflate(R.layout.item_item, viewGroup, false)
                    val viewHolder = ContentViewHolder(view)
                    viewHolder
                }
                else -> {
                    val view = LayoutInflater.from(GuApplication.context).inflate(R.layout.item_load_more, viewGroup, false)
                    val viewHolder = LoadMoreViewHolder(view)
                    viewHolder
                }
            }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder) {
            is ContentViewHolder -> {
                val result = adapterList[position] as Result
                val state =
                        if (result.c3.isEmpty() || result.c3 == "0") {
                            "空闲"
                        } else {
                            "用餐" + ((System.currentTimeMillis() - result.c3.toLong()) / 1000 / 60) + "分钟"
                        }
                viewHolder.itemItemOneTv.visibility = View.GONE
                viewHolder.itemItemTwoTv.text = "桌子名称：${result.c1}"
                viewHolder.itemItemThreeTv.text = "当前状态：$state"
                viewHolder.itemItemFourTv.visibility = View.GONE
                viewHolder.itemItemFiveTv.visibility = View.GONE
                viewHolder.itemItemSixTv.visibility = View.GONE
                viewHolder.itemItemSevenTv.visibility = View.GONE
                viewHolder.itemItemLl.setOnClickListener {
                    val message = gHandler.obtainMessage()
                    message.arg1 = HandlerArg.GO_TO_ORDER_FOOD
                    message.obj = result
                    gHandler.sendMessageDelayed(message, 100)
                }
            }
            is LoadMoreViewHolder -> {
                viewHolder.itemLoadMoreTv.text = adapterInfo
                viewHolder.itemLoadMoreRootLl.setOnClickListener {
                    setMessageToActivity(HandlerArg.MINE, 0, adapterInfo)
                }
            }
        }
    }

    override fun getItemCount(): Int =
            if (::adapterList.isInitialized) {
                adapterList.size + 1
            } else {
                1
            }

    override fun getItemViewType(position: Int): Int =
            if (::adapterList.isInitialized) {
                if (position > adapterList.size - 1) {
                    1
                } else {
                    0
                }
            } else {
                1
            }

    /**
     * 普通item的view
     */
    inner class ContentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemItemOneTv: TextView = view.findViewById(R.id.item_item_one_tv)
        val itemItemTwoTv: TextView = view.findViewById(R.id.item_item_two_tv)
        val itemItemThreeTv: TextView = view.findViewById(R.id.item_item_three_tv)
        val itemItemFourTv: TextView = view.findViewById(R.id.item_item_four_tv)
        val itemItemFiveTv: TextView = view.findViewById(R.id.item_item_five_tv)
        val itemItemSixTv: TextView = view.findViewById(R.id.item_item_six_tv)
        val itemItemSevenTv: TextView = view.findViewById(R.id.item_item_seven_tv)
        val itemItemLl: LinearLayout = view.findViewById(R.id.item_item_ll)
    }
}