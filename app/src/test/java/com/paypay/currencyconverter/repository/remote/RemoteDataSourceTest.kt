package com.paypay.currencyconverter.repository.remote

import com.paypay.currencyconverter.BuildConfig
import com.paypay.currencyconverter.repository.model.CurrencyRate
import com.paypay.currencyconverter.repository.remote.CurrencyService
import com.paypay.currencyconverter.repository.remote.RemoteDataSource
import io.mockk.*
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class RemoteDataSourceTest {


    private val currencyRateResponse: Response<CurrencyRate> = mockk()

    private lateinit var currencyService: CurrencyService

    @Before
    fun setUp() {
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun emitsTheSameResponseFromApiService() = runTest {
        val remoteDataSource = setTheApiCallForSuccessCase()
        val response = remoteDataSource.getCurrencyRate()
        assertEquals(response, currencyRateResponse)
    }

    @Test
    fun callsTheApiCallWhenGetCurrencyRateIsRequested() = runTest {
        val remoteDataSource = setTheApiCallForSuccessCase()
        remoteDataSource.getCurrencyRate()
        coVerify { currencyService.getCurrencyRate(BuildConfig.API_KEY) }
    }

    private fun setTheApiCallForSuccessCase(): RemoteDataSource {
        currencyService = mockk()
        coEvery { currencyService.getCurrencyRate(BuildConfig.API_KEY) } returns currencyRateResponse
        return RemoteDataSource(currencyService)
    }

}