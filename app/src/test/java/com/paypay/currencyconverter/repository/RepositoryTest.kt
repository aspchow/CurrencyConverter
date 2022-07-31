package com.paypay.currencyconverter.repository


import androidx.compose.runtime.collectAsState
import com.paypay.currencyconverter.repository.local.LocalDataSource
import com.paypay.currencyconverter.repository.model.Currency
import com.paypay.currencyconverter.repository.model.CurrencyRate
import com.paypay.currencyconverter.repository.model.Rate
import com.paypay.currencyconverter.repository.remote.ApiRefreshChecker
import com.paypay.currencyconverter.repository.remote.RemoteDataSource
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class RepositoryTest {

    private val localDataSource: LocalDataSource = mockk()
    private val remoteDataSource: RemoteDataSource = mockk()
    private val apiRefreshChecker: ApiRefreshChecker = mockk()
    private val rateFlowFromLocalDataSource: Flow<Rate> = mockk()
    private val lmtValue: Long = 1L
    private val lmtFlowFromLocalDataSource: Flow<Long> = flowOf(lmtValue)
    private val apiResponse: Response<CurrencyRate> = mockk()
    private val currencyRate: CurrencyRate = mockk()
    private val rate: Rate = mockk()

    @Before
    fun setUp() {
        clearAllMocks()
        unmockkAll()
    }


    @Test
    fun requestsLocalDatastoreForRate() {
        val repository = getRepositoryForEmittingRate()
        repository.getRate()
        verify { localDataSource.getRate() }
    }

    @Test
    fun returnsRateFlowFromLocalDataStore() {
        val repository = getRepositoryForEmittingRate()
        val rateFlow = repository.getRate()
        Assert.assertEquals(rateFlow, rateFlowFromLocalDataSource)
    }


    @Test
    fun shouldNotCallApiWhenTimeIsNotPassed() = runTest {
        val repository = getRepositoryForGettingRateFromServerWhenTimeIsNotPassed()
        repository.getCurrencyRateFromServer().first()
        coVerify { remoteDataSource.getCurrencyRate() wasNot called }
    }

    @Test
    fun shouldNotCallApiWhenTimeIsPassed() = runTest {
        val repository = getRepositoryForGettingRateFromServerWhenTimeIsPassed()
        repository.getCurrencyRateFromServer().first()
        coVerify { remoteDataSource.getCurrencyRate() }
    }

    @Test
    fun saveTheRateInTheLocalDatasourceWhenApiIsSuccess() = runTest {
        val repository = getRepositoryWhenApiIsSuccessFull()
        repository.getCurrencyRateFromServer().first()
        coVerify { localDataSource.saveRate(rate = rate) }
    }

    @Test
    fun emitsSuccessWhenEveryThingIsSuccess() = runTest {
        val repository = getRepositoryWhenApiIsSuccessFull()
        val result = repository.getCurrencyRateFromServer().first()
        Assert.assertTrue(result.isSuccess)
    }

    @Test
    fun emitsFailureWhenApiIsFailure() = runTest {
        val repository = getRepositoryWhenApiIsFailure()
        val result = repository.getCurrencyRateFromServer().first()
        Assert.assertFalse(result.isSuccess)
    }


    @Test
    fun emitsFailureWhenSomeExceptionHappened() = runTest {
        val repository = getRepositoryWhenLocalDatasourceThrowsException()
        val result = repository.getCurrencyRateFromServer().first()
        Assert.assertFalse(result.isSuccess)
    }


    private fun getRepositoryWhenLocalDatasourceThrowsException(): Repository {
        every { localDataSource.getLmtForRate() } returns lmtFlowFromLocalDataSource
        coEvery { localDataSource.saveRate(rate = rate) } returns Unit
        every { apiRefreshChecker.checkIfTimePassed(lmtValue) } returns true
        coEvery { remoteDataSource.getCurrencyRate() } throws Exception("")
        every { apiResponse.isSuccessful } returns false
        every { apiResponse.body() } returns currencyRate
        every { currencyRate.rates } returns rate
        return Repository(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource,
            apiRefreshChecker = apiRefreshChecker
        )
    }

    private fun getRepositoryWhenApiIsFailure(): Repository {
        every { localDataSource.getLmtForRate() } returns lmtFlowFromLocalDataSource
        coEvery { localDataSource.saveRate(rate = rate) } returns Unit
        every { apiRefreshChecker.checkIfTimePassed(lmtValue) } returns true
        coEvery { remoteDataSource.getCurrencyRate() } returns apiResponse
        every { apiResponse.isSuccessful } returns false
        every { apiResponse.body() } returns currencyRate
        every { currencyRate.rates } returns rate
        return Repository(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource,
            apiRefreshChecker = apiRefreshChecker
        )
    }

    private fun getRepositoryWhenApiIsSuccessFull(): Repository {
        every { localDataSource.getLmtForRate() } returns lmtFlowFromLocalDataSource
        coEvery { localDataSource.saveRate(rate = rate) } returns Unit
        every { apiRefreshChecker.checkIfTimePassed(lmtValue) } returns true
        coEvery { remoteDataSource.getCurrencyRate() } returns apiResponse
        every { apiResponse.isSuccessful } returns true
        every { apiResponse.body() } returns currencyRate
        every { currencyRate.rates } returns rate
        return Repository(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource,
            apiRefreshChecker = apiRefreshChecker
        )
    }

    private fun getRepositoryForGettingRateFromServerWhenTimeIsPassed(): Repository {
        every { localDataSource.getLmtForRate() } returns lmtFlowFromLocalDataSource
        every { apiRefreshChecker.checkIfTimePassed(lmtValue) } returns true
        return Repository(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource,
            apiRefreshChecker = apiRefreshChecker
        )
    }

    private fun getRepositoryForGettingRateFromServerWhenTimeIsNotPassed(): Repository {
        every { localDataSource.getLmtForRate() } returns lmtFlowFromLocalDataSource
        every { apiRefreshChecker.checkIfTimePassed(lmtValue) } returns false
        return Repository(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource,
            apiRefreshChecker = apiRefreshChecker
        )
    }


    private fun getRepositoryForEmittingRate(): Repository {
        every { localDataSource.getRate() } returns rateFlowFromLocalDataSource
        return Repository(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource,
            apiRefreshChecker = apiRefreshChecker
        )
    }
}