package com.gumaoqi.test.kotlinbaseproject.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.gumaoqi.test.kotlinbaseproject.R
import com.gumaoqi.test.kotlinbaseproject.adapter.FoodAdapter
import com.gumaoqi.test.kotlinbaseproject.base.BaseFragment
import com.gumaoqi.test.kotlinbaseproject.base.GuApplication
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.SUCCESS
import com.gumaoqi.test.kotlinbaseproject.entity.AddBean
import com.gumaoqi.test.kotlinbaseproject.entity.DeleteBean
import com.gumaoqi.test.kotlinbaseproject.entity.GetBean
import com.gumaoqi.test.kotlinbaseproject.entity.Result
import com.gumaoqi.test.kotlinbaseproject.kt.show
import com.gumaoqi.test.kotlinbaseproject.tool.N
import com.gumaoqi.test.kotlinbaseproject.tool.S
import com.gumaoqi.test.kotlinbaseproject.viewmodel.DiscountFVM
import kotlinx.android.synthetic.main.fragment_discount.*

class DisCountFragment : BaseFragment() {

    private lateinit var gHandler: Handler
    private lateinit var vm: DiscountFVM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_discount, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vm = ViewModelProviders.of(this).get(DiscountFVM::class.java)
        intData()
        setView()
    }

    override fun intData() {
        gHandler = Handler(Handler.Callback { msg ->
            if (activity == null) {//已经与activity解绑了
                return@Callback false
            }
            when (msg.arg1) {
                SUCCESS -> {
                }
                HandlerArg.ADD_FOOD_BACK -> {
                    val addBean = msg.obj as AddBean
                    if (addBean.createdAt.isEmpty()) {
                        "添加菜品失败".show()
                    } else {
                        "添加菜品成功".show()
                        getFoodParam()
                    }
                }
                HandlerArg.GET_FOOD_BACK -> {
                    val getBean = msg.obj as GetBean
                    vm.foodList = getBean.results as ArrayList<Result>
                    vm.foodAdapter.setList(vm.foodList)
                    if (getBean.results.isEmpty()) {
                        "暂无菜品数据".show()
                    }
                }
                HandlerArg.DELETE_FOOD_BACK -> {
                    val deleteBean = msg.obj as DeleteBean
                    if (deleteBean.msg == "ok") {
                        "删除菜品成功".show()
                        getFoodParam()
                    } else {
                        "删除菜品失败，请稍后重试".show()
                    }
                }
                HandlerArg.GO_TO_DELETE_FOOD -> {
                    val result = msg.obj as Result
                    Snackbar.make(fragment_discount_three_bt, "确认删除名称为：\"${result.c1}\"的菜品吗？", Snackbar.LENGTH_LONG)
                            .setAction("删除") {
                                deleteFoodParam(result.objectId)
                            }.show()
                }
            }
            false
        })
        vm.foodAdapter = FoodAdapter()
        vm.foodAdapter.adapterInfo = ""
        vm.foodAdapter.gHandler = gHandler
        getFoodParam()
    }

    override fun setView() {
        super.setView()
        fragment_discount_rv.layoutManager = LinearLayoutManager(GuApplication.context)
        fragment_discount_rv.adapter = vm.foodAdapter
        fragment_discount_three_bt.setOnClickListener { addFood() }
    }

    /**
     * onDestroyView中进行解绑操作
     */
    override fun onDestroyView() {
        super.onDestroyView()

    }

    private fun addFood() {
        val foodName = fragment_discount_one_et.text.toString()
        val foodTab = fragment_discount_two_et.text.toString()
        val foodPrice = fragment_discount_three_et.text.toString()
        if (foodName.isEmpty()) {
            "请输入菜品名称".show()
            return
        }
        if (foodTab.isEmpty()) {
            "请输入菜品标签".show()
            return
        }
        if (foodPrice.isEmpty()) {
            "请输入菜品价格".show()
            return
        }
        if (foodPrice.startsWith("0")) {
            "菜品价格不能以\"0\"开头".show()
            return
        }
        addFoodParam(foodName, foodTab, foodPrice)
    }

    /**
     *添加参数去添加菜品
     */
    private fun addFoodParam(foodName: String, foodTab: String, foodPrice: String) {
        val paramMap = HashMap<String, String>()
        paramMap["c1"] = foodName
        paramMap["c2"] = foodTab
        paramMap["c3"] = foodPrice
        paramMap["c4"] = S.getString("c1")
        paramMap["tablename"] = "res2_food"
        N.addByRetrofit(paramMap, gHandler, "添加菜品", HandlerArg.ADD_FOOD_BACK)
    }

    /**
     *添加参数去获取菜品
     */
    private fun getFoodParam() {
        val paramMap = HashMap<String, String>()
        paramMap["c4"] = S.getString("c1")
        paramMap["tablename"] = "res2_food"
        N.getByRetrofit(paramMap, gHandler, "获取菜品", HandlerArg.GET_FOOD_BACK)
    }

    /**
     *添加参数去获取菜品
     */
    private fun deleteFoodParam(objectId: String) {
        val paramMap = HashMap<String, String>()
        paramMap["objectid"] = objectId
        paramMap["tablename"] = "res2_food"
        N.deleteByRetrofit(paramMap, gHandler, "删除菜品", HandlerArg.DELETE_FOOD_BACK)
    }

}