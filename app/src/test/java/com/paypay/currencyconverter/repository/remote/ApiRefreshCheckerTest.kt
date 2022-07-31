package com.paypay.currencyconverter.repository.remote

import com.paypay.currencyconverter.repository.remote.ApiRefreshChecker
import org.junit.Assert
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

class ApiRefreshCheckerTest {

    private var apiRefreshChecker = ApiRefreshChecker()


    @Test
    fun returnsFalseWhenTheTimeNotExpires() {

        val currentTime = System.currentTimeMillis()

        Assert.assertFalse(apiRefreshChecker.checkIfTimePassed(currentTime))

        Assert.assertFalse(apiRefreshChecker.checkIfTimePassed(currentTime + ApiRefreshChecker.API_CACHE_MILLISECONDS))

    }


    @Test
    fun returnsTrueWhenTheTimeExpires() {

        val currentTime = System.currentTimeMillis()

        Assert.assertTrue(apiRefreshChecker.checkIfTimePassed(-1))

        Assert.assertTrue(apiRefreshChecker.checkIfTimePassed(currentTime - ApiRefreshChecker.API_CACHE_MILLISECONDS ))

    }
}