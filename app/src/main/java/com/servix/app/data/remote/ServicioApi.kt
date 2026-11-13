package com.servix.app.data.remote

import com.servix.app.data.model.HealthResponse
import com.servix.app.data.model.LoginRequest
import com.servix.app.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ServicioApi {

    @GET("api/health")
    suspend fun checkServerStatus(): Response<HealthResponse>

    @POST("api/auth/login")
    suspend fun loginUser(@Body request: LoginRequest): Response<LoginResponse>
}