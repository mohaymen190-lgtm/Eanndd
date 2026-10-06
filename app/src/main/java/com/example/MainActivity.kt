package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.PremiumActivationScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppDestination
import com.example.ui.viewmodel.ChatViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val chatViewModel: ChatViewModel = viewModel()
                val uiState by chatViewModel.uiState.collectAsState()

                AnimatedContent(
                    targetState = uiState.currentDestination,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "screen_transition"
                ) { destination ->
                    when (destination) {
                        AppDestination.LOGIN -> {
                            LoginScreen(
                                initialEmail = uiState.userSession.email,
                                onLoginSuccess = { email, password ->
                                    chatViewModel.submitLogin(email, password)
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        AppDestination.PREMIUM_GATE -> {
                            PremiumActivationScreen(
                                userSession = uiState.userSession,
                                isVerifying = uiState.isVerifyingPremium,
                                isVerified = uiState.verificationSuccess,
                                errorMessage = uiState.errorBanner,
                                successMessage = uiState.successBanner,
                                onActivatePremium = {
                                    chatViewModel.activatePremium()
                                },
                                onLogout = {
                                    chatViewModel.logout()
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        AppDestination.CHAT -> {
                            ChatScreen(
                                viewModel = chatViewModel,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}
