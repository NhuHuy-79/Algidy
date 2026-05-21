package com.nhuhuy.algidy.feature.settings.presentation.viewmodel

import app.cash.turbine.test
import com.nhuhuy.algidy.core.model.setting.AppFont
import com.nhuhuy.algidy.core.model.setting.AppLanguage
import com.nhuhuy.algidy.core.model.setting.DarkMode
import com.nhuhuy.algidy.feature.settings.domain.model.SettingData
import com.nhuhuy.algidy.feature.settings.domain.usecase.ManageDataUseCase
import com.nhuhuy.algidy.feature.settings.domain.usecase.ObserveSettingStateUseCase
import com.nhuhuy.algidy.feature.settings.domain.usecase.SelectSettingUseCase
import com.nhuhuy.algidy.feature.settings.domain.usecase.SetToggleSettingUseCase
import com.nhuhuy.algidy.feature.settings.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var observeSettingStateUseCase: ObserveSettingStateUseCase
    private lateinit var setToggleSettingUseCase: SetToggleSettingUseCase
    private lateinit var selectSettingUseCase: SelectSettingUseCase
    private lateinit var manageDataUseCase: ManageDataUseCase
    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setUp() {
        observeSettingStateUseCase = mockk()
        setToggleSettingUseCase = mockk()
        selectSettingUseCase = mockk()
        manageDataUseCase = mockk()

        every { observeSettingStateUseCase() } returns flowOf(SettingData())

        viewModel = SettingsViewModel(
            observeSettingStateUseCase,
            setToggleSettingUseCase,
            selectSettingUseCase,
            manageDataUseCase
        )
    }

    @Test
    fun `init should observe setting state and update uiState`() = runTest {
        val settingData = SettingData(
            darkMode = DarkMode.DARK,
            language = AppLanguage.VIETNAMESE,
            font = AppFont.POPPINS
        )
        every { observeSettingStateUseCase() } returns flowOf(settingData)

        // Re-init to trigger init block
        viewModel = SettingsViewModel(
            observeSettingStateUseCase,
            setToggleSettingUseCase,
            selectSettingUseCase,
            manageDataUseCase
        )

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(DarkMode.DARK, state.darkMode)
            assertEquals(AppLanguage.VIETNAMESE, state.language)
            assertEquals(AppFont.POPPINS, state.font)
        }
    }


    @Test
    fun `onAction SetDarkMode should call selectSettingUseCase`() = runTest {
        val darkMode = DarkMode.LIGHT
        coEvery { selectSettingUseCase.selectDarkMode(darkMode) } returns Unit

        viewModel.onAction(SettingsAction.SetDarkMode(darkMode))

        coVerify(exactly = 1) { selectSettingUseCase.selectDarkMode(darkMode) }
    }

    @Test
    fun `onAction ChangeLanguage should call selectSettingUseCase`() = runTest {
        val language = AppLanguage.VIETNAMESE
        coEvery { selectSettingUseCase.selectAppLanguage(language) } returns Unit

        viewModel.onAction(SettingsAction.ChangeLanguage(language))

        coVerify(exactly = 1) { selectSettingUseCase.selectAppLanguage(language) }
    }
}
