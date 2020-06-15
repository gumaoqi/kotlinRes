package com.gumaoqi.test.kotlinbaseproject.fragment

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.gumaoqi.test.kotlinbaseproject.R
import com.gumaoqi.test.kotlinbaseproject.adapter.OrderAdapter
import com.gumaoqi.test.kotlinbaseproject.adapter.OrderFragmentFoodAdapter
import com.gumaoqi.test.kotlinbaseproject.base.BaseFragment
import com.gumaoqi.test.kotlinbaseproject.base.GuApplication
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.BUY_ORDER_BACK
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.RECOMMEND_GET_FOOD_BACK
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.RECOMMEND_GET_ORDER_BACK
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.RECOMMEND_GO_TO_ORDER_FOOD
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.RECOMMEND_ORDER_FOOD_BACK
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.SUCCESS
import com.gumaoqi.test.kotlinbaseproject.entity.AddBean
import com.gumaoqi.test.kotlinbaseproject.entity.GetBean
import com.gumaoqi.test.kotlinbaseproject.entity.Result
import com.gumaoqi.test.kotlinbaseproject.entity.UpdateBean
import com.gumaoqi.test.kotlinbaseproject.kt.show
import com.gumaoqi.test.kotlinbaseproject.tool.*
import com.gumaoqi.test.kotlinbaseproject.viewmodel.RecommendFVM
import kotlinx.android.synthetic.main.fragment_discount.*
import kotlinx.android.synthetic.main.fragment_recommend.*

class RecommendFragment : BaseFragment(), TextWatcher {

    private lateinit var gHandler: Handler
    private lateinit var vm: RecommendFVM


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recommend, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vm = ViewModelProviders.of(this).get(RecommendFVM::class.java)
        intData()
        setView()
    }

    override fun intData() {
        super.intData()
        gHandler = Handler(Handler.Callback { msg ->
            if (GuApplication.context == null) {//已经与activity解绑了
                return@Callback false
            }
            when (msg.arg1) {
                SUCCESS -> {
                }
                RECOMMEND_GET_FOOD_BACK -> {
                    val getBean = msg.obj as GetBean
                    vm.foodList = getBean.results as ArrayList<Result>
                    vm.orderFragmentFoodAdapter.setList(vm.foodList)
                    if (getBean.results.isEmpty()) {
                        "暂无菜品数据".show()
                    }
                }
                RECOMMEND_GET_ORDER_BACK -> {
                    val getBean = msg.obj as GetBean
                    vm.orderList = getBean.results as ArrayList<Result>
                    vm.orderAdapter.setList(vm.orderList)
                    if (getBean.results.isEmpty()) {
                        "暂无已点菜品数据".show()
                    }
                }
                RECOMMEND_GO_TO_ORDER_FOOD -> {
                    val result = msg.obj as Result
                    addOrder(result)
                }
                RECOMMEND_ORDER_FOOD_BACK -> {
                    val addBean = msg.obj as AddBean
                    if (addBean.createdAt.isEmpty()) {
                        "点单失败，请稍后重试".show()
                    } else {
                        "点单成功".show()
                        fragment_recommend_number_et.setText("")
                        fragment_recommend_note_et.setText("")
                        getOrderParam()
                    }
                }
                BUY_ORDER_BACK -> {
                    val updateBean = msg.obj as UpdateBean
                    if (updateBean.updatedAt.isEmpty()) {
                        "买单失败，请稍后重试".show()
                    } else {
                        "买单成功".show()
                        setMessageToActivity(HandlerArg.CHANGE_OME, 0)
                    }
                }
            }
            false
        })
        vm.orderFragmentFoodAdapter = OrderFragmentFoodAdapter()
        vm.orderFragmentFoodAdapter.adapterInfo = ""
        vm.orderFragmentFoodAdapter.gHandler = gHandler
        getFoodParam()

        vm.orderAdapter = OrderAdapter()
        vm.orderAdapter.adapterInfo = ""
        vm.orderAdapter.gHandler = gHandler
        getOrderParam()
    }

    override fun setView() {
        super.setView()
        fragment_recommend_one_rv.layoutManager = LinearLayoutManager(GuApplication.context)
        fragment_recommend_one_rv.adapter = vm.orderFragmentFoodAdapter
        fragment_recommend_two_rv.layoutManager = LinearLayoutManager(GuApplication.context)
        fragment_recommend_two_rv.adapter = vm.orderAdapter
        fragment_recommend_et.addTextChangedListener(this)
        fragment_recommend_bt.setOnClickListener {
            Snackbar.make(fragment_recommend_bt, "确认买单吗？", Snackbar.LENGTH_LONG)
                    .setAction("买单") {
                        buyParam()
                    }.show()
        }
    }

    /**
     * onDestroyView中进行解绑操作
     */
    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun addOrder(result: Result) {
        var number = fragment_recommend_number_et.text.toString()
        var note = fragment_recommend_note_et.text.toString()
        if (number.isEmpty()) {
            number = "1"
        }
        if (number.startsWith("0")) {
            "菜品数量不能以0开头".show()
            return
        }
        note = if (note.isEmpty()) {
            "备注：无"
        } else {
            "备注$note"
        }
        Snackbar.make(fragment_recommend_bt, "确认添加\"$number\"份名称为：\"${result.c1}\"," +
                "的菜品吗？", Snackbar.LENGTH_LONG)
                .setAction("添加") {
                    orderParam(result.c1, result.c3, number, note)
                }.show()
    }

    /**
     *添加参数去获取菜品
     */
    private fun getFoodParam() {
        val paramMap = HashMap<String, String>()
        paramMap["c4"] = S.getString("c1")
        paramMap["tablename"] = "res2_food"
        N.getByRetrofit(paramMap, gHandler, "获取所有菜品", HandlerArg.RECOMMEND_GET_FOOD_BACK)
    }

    /**
     *添加参数去获取已点菜品
     */
    private fun getOrderParam() {
        val paramMap = HashMap<String, String>()
        paramMap["c6"] = S.getString("order")
        paramMap["tablename"] = "res2_order"
        N.getByRetrofit(paramMap, gHandler, "获取已点菜品", HandlerArg.RECOMMEND_GET_ORDER_BACK)
    }

    /**
     *添加参数去点餐
     */
    private fun orderParam(name: String, price: String, number: String, note: String) {
        val paramMap = HashMap<String, String>()
        paramMap["c1"] = name
        paramMap["c2"] = price
        paramMap["c3"] = number
        paramMap["c4"] = "" + price.toInt() * number.toInt()
        paramMap["c5"] = S.getString("c1")
        paramMap["c6"] = S.getString("order")
        paramMap["c7"] = Re.getMacFromHardware()
        paramMap["c8"] = note
        paramMap["c9"] = "0"
        paramMap["printid"] = S.getString("c4")
        paramMap["printcontent"] = name + " X " + number +
                "\n" + "下单时间：" + C.longTimeChangeYear(System.currentTimeMillis()) +
                "\n" + "操作员手机编号：" + paramMap["c7"] +
                "\n" + note

        L.i(TAG, "打印机id:" + paramMap["printid"] +
                "\n" + "打印机打印的内容为：" + paramMap["printcontent"])
        paramMap["tablename"] = "res2_order"
        N.addByRetrofit(paramMap, gHandler, "点餐", HandlerArg.RECOMMEND_ORDER_FOOD_BACK)
        addPrintParam(paramMap["printid"]!!, paramMap["printcontent"]!!)
    }

    /**
     *添加参数去买单
     */
    private fun buyParam() {
        val paramMap = HashMap<String, String>()
        paramMap["objectid"] = S.getString("tableId")
        paramMap["c3"] = "0"
        paramMap["printid"] = S.getString("c5")
        var printContent = "餐厅名称：" + S.getString("c3") +
                "\n" + "名称   单价   数量   小计" +
                "\n--------------------"
        var money = 0
        for (o in vm.orderList.sortedBy { it.c1 }) {
            printContent += "\n${o.c1}   ${o.c2}   ${o.c3}   ${o.c4}"
            money += o.c4.toInt()
        }
        printContent += "\n--------------------" +
                "\n" + "合计：$money" +
                "\n" + "时间：${C.longTimeChangeYear(System.currentTimeMillis())}"
        paramMap["printcontent"] = printContent
        L.i(TAG, "打印机id:" + paramMap["printid"] +
                "\n" + "打印机打印的内容为：" + paramMap["printcontent"])
        paramMap["tablename"] = "res2_table"
        N.updateByRetrofit(paramMap, gHandler, "买单", HandlerArg.BUY_ORDER_BACK)
        addPrintParam(paramMap["printid"]!!, paramMap["printcontent"]!!)
    }

    private fun addPrintParam(sn: String, content: String) {
        var UKEY = "9pSMAfbwTFIPYuwa"
        val paramMap = HashMap<String, String>()
        paramMap["user"] = "861469629@qq.com"
        paramMap["stime"] = "" + (System.currentTimeMillis() / 1000)
        paramMap["apiname"] = "Open_printMsg"
        paramMap["sn"] = sn
        paramMap["content"] = content
        val sig = C.sha1(paramMap["user"] + UKEY + paramMap["stime"])
        paramMap["sig"] = sig
        N.addPrintByRetrofit(paramMap, gHandler)
    }

    override fun afterTextChanged(s: Editable?) {
        vm.orderFragmentFoodAdapter.setMyKeyWord(s.toString())
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}