package com.smic.conjugadorit

import com.smic.domain.usecase.GetVerb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify


/**
 * @author Smogevscih Yuri
 * 07.07.2022
 */
class SharedViewModelTest {
    private lateinit var subject: SharedViewModel
    private lateinit var getVerb: GetVerb

    @Before
    fun setUp() {
        getVerb = mock(GetVerb::class.java)
        subject = SharedViewModel()
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @Test
    fun muTest() {
        val infinitivo = "comer"
        subject.setSelectedVerb(infinitivo)
        verify(getVerb).invoke(infinitivo)

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}