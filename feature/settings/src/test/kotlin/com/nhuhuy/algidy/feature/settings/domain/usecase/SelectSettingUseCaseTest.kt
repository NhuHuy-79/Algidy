package com.nhuhuy.algidy.feature.settings.domain.usecase

import com.nhuhuy.algidy.core.datastore.SettingsDataStore
import com.nhuhuy.algidy.core.model.setting.AppFont
import com.nhuhuy.algidy.core.model.setting.AppLanguage
import com.nhuhuy.algidy.core.model.setting.DarkMode
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SelectSettingUseCaseTest {

    private lateinit var settingsDataStore: SettingsDataStore
    private lateinit var selectSettingUseCase: SelectSettingUseCase

    @Before
    fun setUp() {
        settingsDataStore = mockk()
        selectSettingUseCase = SelectSettingUseCase(settingsDataStore)
    }

    @Test
    fun `selectDarkMode should call setDarkMode on dataStore`() = runTest {
        val darkMode = DarkMode.DARK
        coEvery { settingsDataStore.setDarkMode(darkMode) } returns Unit

        selectSettingUseCase.selectDarkMode(darkMode)

        coVerify(exactly = 1) { settingsDataStore.setDarkMode(darkMode) }
    }

    @Test
    fun `selectAppFont should call setFont on dataStore`() = runTest {
        val appFont = AppFont.INTER
        coEvery { settingsDataStore.setFont(appFont) } returns Unit

        selectSettingUseCase.selectAppFont(appFont)

        coVerify(exactly = 1) { settingsDataStore.setFont(appFont) }
    }

    @Test
    fun `selectAppLanguage should call setLanguage on dataStore`() = runTest {
        val language = AppLanguage.VIETNAMESE
        coEvery { settingsDataStore.setLanguage(language) } returns Unit

        selectSettingUseCase.selectAppLanguage(language)

        coVerify(exactly = 1) { settingsDataStore.setLanguage(language) }
    }
}
