package com.gumaoqi.test.kotlinbaseproject.fragment

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.gumaoqi.test.kotlinbaseproject.R
import com.gumaoqi.test.kotlinbaseproject.adapter.HomeTableAdapter
import com.gumaoqi.test.kotlinbaseproject.adapter.TableAdapter
import com.gumaoqi.test.kotlinbaseproject.base.BaseFragment
import com.gumaoqi.test.kotlinbaseproject.base.GuApplication
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.CHECK_TABLE_STATE_BACK
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.GET_TABLE_HOME_BACK
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.GO_TO_ORDER_FOOD
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.SUCCESS
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.UPDATE_TABLE_STATE_BACK
import com.gumaoqi.test.kotlinbaseproject.entity.GetBean
import com.gumaoqi.test.kotlinbaseproject.entity.Result
import com.gumaoqi.test.kotlinbaseproject.entity.UpdateBean
import com.gumaoqi.test.kotlinbaseproject.kt.show
import com.gumaoqi.test.kotlinbaseproject.tool.N
import com.gumaoqi.test.kotlinbaseproject.tool.S
import com.gumaoqi.test.kotlinbaseproject.viewmodel.HomeFVM
import kotlinx.android.synthetic.main.fragment_action.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment() {

    private lateinit var gHandler: Handler
    private lateinit var vm: HomeFVM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vm = ViewModelProviders.of(this).get(HomeFVM::class.java)
        intData()
        setView()
    }

    override fun intData() {
        super.intData()
        gHandler = Handler(Handler.Callback { msg ->
            if (activity == null) {//已经与activity解绑了
                return@Callback false
            }
            when (msg.arg1) {
                SUCCESS -> {
                }
                GET_TABLE_HOME_BACK -> {
                    val getBean = msg.obj as GetBean
                    vm.tableList = getBean.results as ArrayList<Result>
                    vm.tableHomeAdapter.setList(vm.tableList)
                    if (getBean.results.isEmpty()) {
                        "暂无桌子数据".show()
                    }
                }
                GO_TO_ORDER_FOOD -> {
                    val result = msg.obj as Result
                    checkTableState(result.objectId, result.c3)
                }
                CHECK_TABLE_STATE_BACK -> {
                    val getBean = msg.obj as GetBean
                    if (getBean.results.isEmpty()) {
                        "桌子状态改变，正在刷新数据".show()
                        getTableParam()
                    } else {
                        val result = getBean.results[0]
                        if (result.c3 == "0") {//改变桌子状态为用餐中
                            updateTableState(getBean.results[0].objectId)
                        } else {
                            S.setString("order", result.objectId + result.c3)
                            S.setString("tableId", result.objectId)
                            S.setString("tableName", result.c1)
                            setMessageToActivity(HandlerArg.CHANGE_FOUR, 0)
                        }
                    }
                }
                UPDATE_TABLE_STATE_BACK -> {
                    val updateBean = msg.obj as UpdateBean
                    if (updateBean.updatedAt.isEmpty()) {
                        "改变桌子状态失败，请稍后重试".show()
                    } else {
                        "改变桌子状态成功，请再次点击进入点餐页面".show()
                        getTableParam()
                    }
                }
            }
            false
        })
        vm.tableHomeAdapter = HomeTableAdapter()
        vm.tableHomeAdapter.adapterInfo = ""
        vm.tableHomeAdapter.gHandler = gHandler
        getTableParam()
    }

    override fun setView() {
        super.setView()
        fragment_home_rv.layoutManager = LinearLayoutManager(GuApplication.context)
        fragment_home_rv.adapter = vm.tableHomeAdapter
    }

    /**
     * onDestroyView中进行解绑操作
     */
    override fun onDestroyView() {
        super.onDestroyView()
    }

    /**
     *添加参数去获取桌子
     */
    private fun getTableParam() {
        val paramMap = HashMap<String, String>()
        paramMap["c2"] = S.getString("c1")
        paramMap["tablename"] = "res2_table"
        N.getByRetrofit(paramMap, gHandler, "获取桌子", HandlerArg.GET_TABLE_HOME_BACK)
    }

    /**
     *添加参数去检查桌子状态
     */
    private fun checkTableState(objectId: String, c3: String) {
        val paramMap = HashMap<String, String>()
        paramMap["objectid"] = objectId
        paramMap["c3"] = c3
        paramMap["tablename"] = "res2_table"
        N.getByRetrofit(paramMap, gHandler, "检查桌子状态", HandlerArg.CHECK_TABLE_STATE_BACK)
    }

    /**
     *添加参数去修改桌子状态
     */
    private fun updateTableState(objectId: String) {
        val paramMap = HashMap<String, String>()
        paramMap["objectid"] = objectId
        paramMap["c3"] = System.currentTimeMillis().toString()
        paramMap["tablename"] = "res2_table"
        N.updateByRetrofit(paramMap, gHandler, "修改桌子状态", HandlerArg.UPDATE_TABLE_STATE_BACK)
    }
}