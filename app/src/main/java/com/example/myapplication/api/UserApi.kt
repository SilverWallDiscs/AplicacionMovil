package com.example.myapplication.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface UserApi {

    @POST("/api/v1/usuario/")
    suspend fun register(@Body usuario: Usuario): Response<Boolean>

    @POST("/api/v1/usuario/login")
    suspend fun login(@Body login: LoginRequest): Response<UsuarioDto>

    @GET("/api/v1/usuario/{id}")
    suspend fun getUser(@Path("id") id: Int): Response<UsuarioDto>
}
