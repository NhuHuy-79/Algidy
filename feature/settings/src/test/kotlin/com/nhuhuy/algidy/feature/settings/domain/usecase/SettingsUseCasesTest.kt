package com.nhuhuy.algidy.feature.settings.domain.usecase

import app.cash.turbine.test
import com.nhuhuy.algidy.core.datastore.SettingsDataStore
import com.nhuhuy.algidy.core.model.setting.AppFont
import com.nhuhuy.algidy.core.model.setting.AppLanguage
import com.nhuhuy.algidy.core.model.setting.DarkMode
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SettingsUseCasesTest {

    private lateinit var settingsDataStore: SettingsDataStore

    @Before
    fun setUp() {
        settingsDataStore = mockk()
    }

    @Test
    fun `SetToggleSettingUseCase should call correct methods on dataStore`() = runTest {
        val useCase = SetToggleSettingUseCase(settingsDataStore)
        coEvery { settingsDataStore.setBiometricLock(any()) } returns Unit
        coEvery { settingsDataStore.setNotificationsEnabled(any()) } returns Unit
        coEvery { settingsDataStore.setDynamicColor(any()) } returns Unit

        useCase.toggleBiometricLock(true)
        useCase.toggleNotifications(false)
        useCase.toggleDynamicColor(true)

        coVerify { settingsDataStore.setBiometricLock(true) }
        coVerify { settingsDataStore.setNotificationsEnabled(false) }
        coVerify { settingsDataStore.setDynamicColor(true) }
    }

    @Test
    fun `ObserveSettingStateUseCase should combine flows from dataStore`() = runTest {
        val useCase = ObserveSettingStateUseCase(settingsDataStore)
        every { settingsDataStore.darkModeFlow } returns flowOf(DarkMode.DARK)
        every { settingsDataStore.notificationsEnabledFlow } returns flowOf(true)
        every { settingsDataStore.dynamicColorFlow } returns flowOf(false)
        every { settingsDataStore.biometricLockFlow } returns flowOf(true)
        every { settingsDataStore.appLanguageFlow } returns flowOf(AppLanguage.ENGLISH)
        every { settingsDataStore.appFontFlow } returns flowOf(AppFont.DEFAULT)

        useCase().test {
            val result = awaitItem()
            assertEquals(DarkMode.DARK, result.darkMode)
            assertEquals(true, result.enableNotifications)
            assertEquals(true, result.enableBiometricsLock)
            awaitComplete()
        }
    }
}
