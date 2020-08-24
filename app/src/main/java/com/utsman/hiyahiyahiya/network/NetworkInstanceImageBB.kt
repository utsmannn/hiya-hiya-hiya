package com.utsman.hiyahiyahiya.network

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.utsman.hiyahiyahiya.data.ConstantValue
import com.utsman.hiyahiyahiya.model.ImageBB
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query

interface NetworkInstanceImageBB {

    @FormUrlEncoded
    @POST("/1/upload")
    suspend fun postToImageBB(
        @Query("key") key: String,
        @Field("image") image: String?
    ): ImageBB

    companion object {
        private val gsonBuilder = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setLenient()
            .setPrettyPrinting()
            .create()

        private val retrofit = Retrofit.Builder()
            .baseUrl(ConstantValue.baseUrlImageBB)
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder))
            .build()

        fun create(): NetworkInstanceImageBB = retrofit.create(NetworkInstanceImageBB::class.java)
    }
}