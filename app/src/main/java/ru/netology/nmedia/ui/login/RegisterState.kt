package ru.netology.nmedia.ui.login

data class RegisterState(
    val isRegistering: Boolean = false,
    val error : Boolean = false
)

data class LoginState(
    val isLogin: Boolean = false,
    val error : Boolean = false
)
