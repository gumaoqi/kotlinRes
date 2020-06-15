package com.gumaoqi.test.kotlinbaseproject.service

import com.gumaoqi.test.kotlinbaseproject.entity.AddBean
import com.gumaoqi.test.kotlinbaseproject.entity.FeiEYunBean
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.util.HashMap

interface AddPrintService {
    @FormUrlEncoded
    @POST("Api/Open/")
    fun addPrint(@FieldMap paramMap: HashMap<String, String>): Call<FeiEYunBean>
}