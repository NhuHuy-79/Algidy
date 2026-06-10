package com.nhuhuy.algidy.utils

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.nhuhuy.algidy.core.presentation.R
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow


class BiometricHandler(private val activity: FragmentActivity) {
    // Hàm này gọi TRƯỚC khi hiện nút "Đăng nhập bằng vân tay"
    fun isBiometricAvailable(): Boolean {
        val manager = BiometricManager.from(activity)
        val authenticators = BIOMETRIC_STRONG or DEVICE_CREDENTIAL
        return manager.canAuthenticate(authenticators) == BiometricManager.BIOMETRIC_SUCCESS
    }

    // Hàm này gọi KHI người dùng bấm vào nút "Đăng nhập"
    fun authenticate(): Flow<BiometricResult> = channelFlow {
        val authenticators = BIOMETRIC_STRONG or DEVICE_CREDENTIAL
        val manager = BiometricManager.from(activity)

        // Kiểm tra nhanh trước khi bắt đầu
        when (manager.canAuthenticate(authenticators)) {
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                trySend(BiometricResult.Error.NotEnrolled)
                channel.close(); return@channelFlow
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE,
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE,
            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
                trySend(BiometricResult.Error.NotSupported)
                channel.close(); return@channelFlow
            }
        }

        val biometricPrompt = BiometricPrompt(
            activity, ContextCompat.getMainExecutor(activity),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    trySend(BiometricResult.Success)
                    channel.close()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    val error = when (errorCode) {
                        BiometricPrompt.ERROR_LOCKOUT,
                        BiometricPrompt.ERROR_LOCKOUT_PERMANENT -> BiometricResult.Error.LockedOut

                        BiometricPrompt.ERROR_USER_CANCELED -> BiometricResult.Idle
                        else -> BiometricResult.Error.HasError(errString.toString())
                    }
                    trySend(error)
                    channel.close()
                }

                override fun onAuthenticationFailed() {
                    trySend(BiometricResult.Failed)
                }
            }
        )

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(activity.getString(R.string.biometric_title))
            .setSubtitle(activity.getString(R.string.biometric_subtitle))
            .setAllowedAuthenticators(authenticators)
            .build()

        biometricPrompt.authenticate(promptInfo)
        awaitClose { biometricPrompt.cancelAuthentication() }
    }
}


sealed interface BiometricResult {
    data object Idle : BiometricResult
    data object Success : BiometricResult
    data object Failed : BiometricResult // Vân tay sai

    sealed interface Error : BiometricResult {
        data object NotSupported : Error
        data object NotEnrolled : Error
        data object LockedOut : Error
        data class HasError(val message: String) : Error
    }
}