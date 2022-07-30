package com.paypay.currencyconverter.repository.remote

import com.paypay.currencyconverter.repository.model.CurrencyRate
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyService {

    @GET("/api/latest.json")
    suspend fun getCurrencyRate(@Query("app_id") apiKey : String): Response<CurrencyRate>
}