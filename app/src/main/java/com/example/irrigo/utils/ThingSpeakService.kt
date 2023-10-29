package com.example.irrigo.utils

import com.example.irrigo.data.MyData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ThingSpeakService {
    @GET("channels/2287370/fields/1.json?api_key=UM1M1WJPVIQ6NV5N&results=2")
    suspend fun getChannelData(): MyData
}

