package com.sih2026.artisancatalog.data.remote.api

import com.sih2026.artisancatalog.data.remote.dto.AnalyzeRequest
import com.sih2026.artisancatalog.data.remote.dto.AnalyzeResponse
import com.sih2026.artisancatalog.data.remote.dto.ContentRequest
import com.sih2026.artisancatalog.data.remote.dto.ContentResponse
import com.sih2026.artisancatalog.data.remote.dto.HealthResponse
import com.sih2026.artisancatalog.data.remote.dto.ImageGenRequest
import com.sih2026.artisancatalog.data.remote.dto.ImageGenResponse
import com.sih2026.artisancatalog.data.remote.dto.PhotoshootRequest
import com.sih2026.artisancatalog.data.remote.dto.PhotoshootResponse
import com.sih2026.artisancatalog.data.remote.dto.TranscribeRequest
import com.sih2026.artisancatalog.data.remote.dto.TranscribeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface KalakritiApiService {

    @GET("api/health")
    suspend fun checkHealth(): Response<HealthResponse>

    @POST("api/product-sessions/{id}/analyze")
    suspend fun analyzeProduct(
        @Path("id") sessionId: String,
        @Body request: AnalyzeRequest
    ): Response<AnalyzeResponse>

    @POST("api/product-sessions/{id}/generate-content")
    suspend fun generateContent(
        @Path("id") sessionId: String,
        @Body request: ContentRequest
    ): Response<ContentResponse>

    @POST("api/product-sessions/{id}/photoshoot")
    suspend fun generatePhotoshootPrompts(
        @Path("id") sessionId: String,
        @Body request: PhotoshootRequest
    ): Response<PhotoshootResponse>

    @POST("api/product-sessions/{id}/generate-single-image")
    suspend fun generateSingleImage(
        @Path("id") sessionId: String,
        @Body request: ImageGenRequest
    ): Response<ImageGenResponse>

    @POST("api/product-sessions/{id}/transcribe-voice")
    suspend fun transcribeVoice(
        @Path("id") sessionId: String,
        @Body request: TranscribeRequest
    ): Response<TranscribeResponse>
}
