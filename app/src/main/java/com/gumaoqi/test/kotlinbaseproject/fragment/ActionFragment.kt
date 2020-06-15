package com.gumaoqi.test.kotlinbaseproject.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.accessibility.AccessibilityEventCompat.setAction
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.gumaoqi.test.kotlinbaseproject.R
import com.gumaoqi.test.kotlinbaseproject.adapter.TableAdapter
import com.gumaoqi.test.kotlinbaseproject.base.BaseFragment
import com.gumaoqi.test.kotlinbaseproject.base.GuApplication
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.ADD_TABLE_BACK
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.DELETE_TABLE_BACK
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.GET_TABLE_BACK
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.SUCCESS
import com.gumaoqi.test.kotlinbaseproject.entity.AddBean
import com.gumaoqi.test.kotlinbaseproject.entity.DeleteBean
import com.gumaoqi.test.kotlinbaseproject.entity.GetBean
import com.gumaoqi.test.kotlinbaseproject.entity.Result
import com.gumaoqi.test.kotlinbaseproject.kt.show
import com.gumaoqi.test.kotlinbaseproject.tool.N
import com.gumaoqi.test.kotlinbaseproject.tool.N.addByRetrofit
import com.gumaoqi.test.kotlinbaseproject.tool.S
import com.gumaoqi.test.kotlinbaseproject.viewmodel.ActionFVM
import kotlinx.android.synthetic.main.fragment_action.*

class ActionFragment : BaseFragment() {

    private lateinit var gHandler: Handler
    private lateinit var vm: ActionFVM


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_action, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vm = ViewModelProviders.of(this).get(ActionFVM::class.java)
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
                ADD_TABLE_BACK -> {
                    val addBean = msg.obj as AddBean
                    if (addBean.createdAt.isEmpty()) {
                        "添加桌子失败".show()
                    } else {
                        "添加桌子成功".show()
                        getTableParam()
                    }
                }
                GET_TABLE_BACK -> {
                    val getBean = msg.obj as GetBean
                    vm.tableList = getBean.results as ArrayList<Result>
                    vm.tableAdapter.setList(vm.tableList)
                    if (getBean.results.isEmpty()) {
                        "暂无桌子数据".show()
                    }
                }
                DELETE_TABLE_BACK -> {
                    val deleteBean = msg.obj as DeleteBean
                    if (deleteBean.msg == "ok") {
                        "删除桌子成功".show()
                        getTableParam()
                    }else{
                        "删除桌子失败，请稍后重试".show()
                    }
                }
                HandlerArg.GO_TO_DELETE_TABLE -> {
                    val result = msg.obj as Result
                    Snackbar.make(fragment_action_bt, "确认删除名称为：\"${result.c1}\"的桌子吗？", Snackbar.LENGTH_LONG)
                            .setAction("删除") {
                                deleteTableParam(result.objectId)
                            }.show()
                }
            }
            false
        })
        vm.tableAdapter = TableAdapter()
        vm.tableAdapter.adapterInfo = ""
        vm.tableAdapter.gHandler = gHandler
        getTableParam()
    }

    override fun setView() {
        super.setView()
        fragment_action_rv.layoutManager = LinearLayoutManager(GuApplication.context)
        fragment_action_rv.adapter = vm.tableAdapter
        fragment_action_bt.setOnClickListener { addTable() }
    }

    /**
     * onDestroyView中进行解绑操作
     */
    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun addTable() {
        val tableName = fragment_action_et.text.toString()
        if (tableName.isEmpty()) {
            "请输入桌子名称".show()
            return
        }
        addTableParam(tableName)
    }

    /**
     *添加参数去添加桌子
     */
    private fun addTableParam(tablaName: String) {
        val paramMap = HashMap<String, String>()
        paramMap["c1"] = tablaName
        paramMap["c2"] = S.getString("c1")
        paramMap["tablename"] = "res2_table"
        N.addByRetrofit(paramMap, gHandler, "添加桌子", HandlerArg.ADD_TABLE_BACK)
    }

    /**
     *添加参数去获取桌子
     */
    private fun getTableParam() {
        val paramMap = HashMap<String, String>()
        paramMap["c2"] = S.getString("c1")
        paramMap["tablename"] = "res2_table"
        N.getByRetrofit(paramMap, gHandler, "获取桌子", HandlerArg.GET_TABLE_BACK)
    }

    /**
     *添加参数去获取桌子
     */
    private fun deleteTableParam(objectId: String) {
        val paramMap = HashMap<String, String>()
        paramMap["objectid"] = objectId
        paramMap["tablename"] = "res2_table"
        N.deleteByRetrofit(paramMap, gHandler, "删除桌子", HandlerArg.DELETE_TABLE_BACK)
    }

}