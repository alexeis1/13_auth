package ru.netology.nmedia.ui.login

import ru.netology.nmedia.dto.Media
import ru.netology.nmedia.dto.MediaUpload

interface RegisterRepositoryInterface {

    suspend fun registerNoPhoto(
        login: String,
        pass: String,
        name: String
    ): AuthPair

    suspend fun registerWithPhoto(
        login: String,
        pass: String,
        name: String,
        media: MediaUpload
    ): AuthPair

    suspend fun login(username: String, password: String): AuthPair

    suspend fun upload(upload: MediaUpload): Media
}
