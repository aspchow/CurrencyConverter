package com.paypay.currencyconverter

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.paypay.currencyconverter.repository.local.getValue
import com.paypay.currencyconverter.repository.local.saveValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class DataStoreTest {
    private val context: Context = ApplicationProvider.getApplicationContext()

    companion object {
        const val TEST_DATA_STORE = "datastoretest"
        const val LMT_KEY = "lmtkey"
    }

    private val testCoroutineDispatcher: TestCoroutineDispatcher =
        TestCoroutineDispatcher()
    private val testCoroutineScope =
        TestCoroutineScope(testCoroutineDispatcher + Job())

    private val testDataStore: androidx.datastore.core.DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            scope = testCoroutineScope,
            produceFile =
            { context.preferencesDataStoreFile(TEST_DATA_STORE) }
        )

    private val lmtPreferenceKey = longPreferencesKey(LMT_KEY)
    private val defaultLmt = 1L

    private val lmtValue = 1000L

    @Before
    fun setup() {
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    @Test
    fun emitsTheDefaultValueWhenNoValueExists() = testCoroutineScope.runBlockingTest {
        val lmt = testDataStore.getValue(key = lmtPreferenceKey, defaultValue = defaultLmt).first()
        Assert.assertEquals(lmt, defaultLmt)
    }


    @Test
    fun emitsTheValueIfValueExists() = testCoroutineScope.runBlockingTest {
        testDataStore.saveValue(lmtPreferenceKey, lmtValue)
        val lmt = testDataStore.getValue(key = lmtPreferenceKey, defaultValue = defaultLmt).first()
        Assert.assertEquals(lmt, lmtValue)
    }


    @After
    fun cleanUp() {
        Dispatchers.resetMain()
        testCoroutineDispatcher.cleanupTestCoroutines()
        testCoroutineScope.runBlockingTest {
            testDataStore.edit { it.clear() }
        }
        testCoroutineScope.cancel()
    }
}