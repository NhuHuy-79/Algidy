package com.nhuhuy.algidy.utils

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow


class BiometricHandler(private val activity: FragmentActivity) {
    fun authenticate(): Flow<BiometricResult> = channelFlow {
        trySend(BiometricResult.Idle)

        val authenticators = BIOMETRIC_STRONG or
                DEVICE_CREDENTIAL
        val executor = ContextCompat.getMainExecutor(activity)
        val manager = BiometricManager.from(activity)

        when (manager.canAuthenticate(authenticators)) {
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                trySend(BiometricResult.Error.Unavailable)
                channel.close()
                return@channelFlow
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                trySend(BiometricResult.Error.NotEnroll)
                channel.close()
                return@channelFlow
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                trySend(BiometricResult.Error.NoHardware)
                channel.close()
                return@channelFlow
            }

            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
                trySend(BiometricResult.Error.NoFeature)
                channel.close()
                return@channelFlow
            }

            BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {
                trySend(BiometricResult.Error.Unknown)
                channel.close()
                return@channelFlow
            }

            else -> Unit
        }

        val biometricPrompt = BiometricPrompt(
            activity, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    trySend(BiometricResult.Success)
                    channel.close()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    trySend(BiometricResult.Error.HasError(error = errString.toString()))
                    channel.close()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    trySend(BiometricResult.Failed)
                }
            }
        )

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Xác thực Algidy")
            .setSubtitle("Quét vân tay để tiếp tục")
            .setAllowedAuthenticators(authenticators)
            .build()

        biometricPrompt.authenticate(promptInfo)


        awaitClose {
            biometricPrompt.cancelAuthentication()
        }
    }
}


sealed interface BiometricResult {
    data object Idle : BiometricResult
    data object Success : BiometricResult
    sealed interface Error : BiometricResult {
        data class HasError(val error: String) : Error
        data object NoFeature : Error
        data object NoHardware : Error
        data object Unavailable : Error
        data object NotEnroll : Error
        data object Unknown : Error
    }

    data object Failed : BiometricResult
}