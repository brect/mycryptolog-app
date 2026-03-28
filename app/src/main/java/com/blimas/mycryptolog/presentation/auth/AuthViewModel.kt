package com.blimas.mycryptolog.presentation.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blimas.mycryptolog.domain.model.AuthState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState.LOADING)
    val authState: StateFlow<AuthState> = _authState

    init {
        viewModelScope.launch {
            auth.addAuthStateListener {
                if (it.currentUser != null) {
                    _authState.value = AuthState.AUTHENTICATED
                } else {
                    _authState.value = AuthState.UNAUTHENTICATED
                }
            }
        }
    }

    fun login(email: String, password: String, onComplete: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }

    fun signUp(email: String, password: String, onComplete: (Boolean) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.user?.sendEmailVerification()?.addOnCompleteListener {
                        Log.d("AuthViewModel", "Verification email sent.")
                    }
                    onComplete(true)
                } else {
                    onComplete(false)
                }
            }
    }

    fun sendPasswordResetEmail(email: String, onComplete: (Boolean) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }

    fun signOut() {
        auth.signOut()
    }
}