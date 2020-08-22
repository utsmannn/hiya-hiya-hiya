package com.utsman.hiyahiyahiya.network

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.utsman.hiyahiyahiya.data.ConstantValue
import com.utsman.hiyahiyahiya.model.MessageBody
import com.utsman.hiyahiyahiya.model.ResponseMessage
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NetworkInstance {

    @POST("/fcm/send")
    @Headers("Authorization: key=${ConstantValue.serverKey}", "Content-Type: application/json")
    suspend fun sendMessage(@Body rawBody: NetworkMessage.RawBody): ResponseMessage

    companion object {
        private val gsonBuilder = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setLenient()
            .setPrettyPrinting()
            .create()

        private val retrofit = Retrofit.Builder()
            .baseUrl(ConstantValue.baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder))
            .build()

        fun create(): NetworkInstance = retrofit.create(NetworkInstance::class.java)
    }
}