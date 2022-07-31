package com.paypay.currencyconverter.repository.local


import com.paypay.currencyconverter.repository.local.DataStore
import com.paypay.currencyconverter.repository.local.LocalDataSource
import com.paypay.currencyconverter.repository.model.Rate
import io.mockk.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class LocalDataSourceTest {

    private val dataStore: DataStore = mockk()

    private val rateFlowFromDataSource: Flow<Rate> = mockk()

    private val rateLmtFromDataSource: Flow<Long> = mockk()

    private val rate: Rate = mockk()

    @Before
    fun setUp() {
        clearAllMocks()
    }


    @Test
    fun callTheDataStoreForTheRateFlow() = runTest {
        val localDataSource = getTheLocalDataStoreToGetRate()
        localDataSource.getRate()
        coVerify { dataStore.getRate() }
    }

    @Test
    fun getTheRateFromDataStoreAndEmit() = runTest {
        val localDataSource = getTheLocalDataStoreToGetRate()
        val rateFlow = localDataSource.getRate()
        Assert.assertEquals(rateFlow, rateFlowFromDataSource)
    }


    @Test
    fun callTheDataStoreForTheRateLmtFlow() = runTest {
        val localDataSource = getTheLocalDataStoreToGetRateLmt()
        localDataSource.getLmtForRate()
        coVerify { dataStore.getLmtOFRate() }
    }

    @Test
    fun getTheRateLmtFromDataStoreAndEmit() = runTest {
        val localDataSource = getTheLocalDataStoreToGetRate()
        val rateFlow = localDataSource.getRate()
        Assert.assertEquals(rateFlow, rateFlowFromDataSource)
    }

    @Test
    fun saveTheRateInTheDataStore() = runTest {
        val localDataSource = getTheLocalDataStoreToUpdateRateLmt()
        localDataSource.saveRate(rate = rate)
        coVerify {
            dataStore.saveRate(rate = rate)
        }
    }


    private fun getTheLocalDataStoreToGetRate(): LocalDataSource {
        every { dataStore.getRate() } returns rateFlowFromDataSource
        return LocalDataSource(dataStore = dataStore)
    }

    private fun getTheLocalDataStoreToGetRateLmt(): LocalDataSource {
        every { dataStore.getLmtOFRate() } returns rateLmtFromDataSource
        return LocalDataSource(dataStore = dataStore)
    }

    private fun getTheLocalDataStoreToUpdateRateLmt(): LocalDataSource {
        coEvery { dataStore.saveRate(rate = rate) } returns Unit
        return LocalDataSource(dataStore = dataStore)
    }

}