package com.paypay.currencyconverter.viewmodel

import com.paypay.currencyconverter.repository.Repository
import com.paypay.currencyconverter.repository.model.Currency
import com.paypay.currencyconverter.repository.model.Rate
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CurrencyConverterViewModelTest {


    private val repository: Repository = mockk()

    private val rateFlowFromRepository: Flow<Rate> = mockk()

    val apiResult: Result<Unit> = mockk()

    private val rateApiResponse: Flow<Result<Unit>> = flowOf(apiResult)

    @Before
    fun setUp() {
        clearAllMocks()
    }

    @Test
    fun requestTheRepositoryForRateFlow() {
        val viewModel = getViewModelForTheRateFlow()
        viewModel.getCurrencyRate()
        verify { repository.getRate() }
    }

    @Test
    fun emitsTheRateFlowObservedFromRepository() {
        val viewModel = getViewModelForTheRateFlow()
        val rateFlow = viewModel.getCurrencyRate()
        Assert.assertEquals(rateFlow, rateFlowFromRepository)
    }

    @Test
    fun requestsTheCurrencyRateFromServer() {
        val viewModel = getViewModelForTheRateFromServer()
        viewModel.getCurrencyRateFromServer()
        verify { repository.getCurrencyRateFromServer() }
    }

    @Test
    fun emitsTheCurrencyRateFromServer() = runTest {
        val viewModel = getViewModelForTheRateFromServer()
        val response = viewModel.getCurrencyRateFromServer()
        Assert.assertEquals(response.first(), rateApiResponse.first())
    }

    @Test
    fun returnAllTheCurrenciesIfSearchIsEmpty() = runTest {
        val viewModel = getViewModelForAvailableCurrencies()
        viewModel.search("")
        Assert.assertEquals(
            Currency.values().filter { it != viewModel.selectedCurrency.value },
            viewModel.availableCurrencies.first()
        )
    }

    @Test
    fun filtersTheCurrenciesWithSearchQueryIfNotEmpty() = runTest {
        val viewModel = getViewModelForAvailableCurrencies()
        val search = "INR"
        viewModel.search(search)
        Assert.assertEquals(
            Currency.values().filter {
                it.name.contentEquals(
                    search,
                    true
                ) && it != viewModel.selectedCurrency.value
            },
            viewModel.availableCurrencies.first()
        )
    }

    @Test
    fun theSelectedCurrencyShouldNotContainsInAvailableCurrency() = runTest {
        val viewModel = getViewModelForAvailableCurrencies()
        viewModel.setTheCurrency(Currency.INR)
        Assert.assertFalse(viewModel.availableCurrencies.first().contains(Currency.INR))
    }

    private fun getViewModelForAvailableCurrencies(): CurrencyConverterViewModel {
        return CurrencyConverterViewModel(repository = repository)
    }


    private fun getViewModelForTheRateFromServer(): CurrencyConverterViewModel {
        every { repository.getCurrencyRateFromServer() } returns rateApiResponse
        return CurrencyConverterViewModel(repository = repository)

    }

    private fun getViewModelForTheRateFlow(): CurrencyConverterViewModel {
        every { repository.getRate() } returns rateFlowFromRepository
        return CurrencyConverterViewModel(repository = repository)
    }


}