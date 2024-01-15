package com.example.listfirebase.data.retrofit

import com.example.listfirebase.Constants
import com.example.listfirebase.data.firebasedata.additemsapi.AddItemsData
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET(Constants.API_ITEMS_ENDPOINT)
    suspend fun getItems(): List<AddItemsData>

    @GET("${Constants.API_ITEMS_ENDPOINT}/{title}")
    suspend fun getItem(@Path("title") title: String): AddItemsData
}