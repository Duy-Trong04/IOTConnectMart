package com.example.ungdungbanthietbi_iot.models

import com.google.gson.annotations.SerializedName

data class Review(
    val idReview:Int,
    val idCustomer:String,
    val idEmployee:String,
    val idDevice:Int,
    val comment:String,
    val rating:Int,
    val response:String,
    val note:String,
    val created_at:String,
    val updated_at:String,
    val status:Int
)
data class Reviews(
    @SerializedName("id") val idReview:Int,
    @SerializedName("customer_id") val idCustomer:String,
    @SerializedName("product_id") val idDevice:Int,
    val comment:String?,
    val image: String?,
    val rating:Int,
    val response:String?,
    val note:String?,
    val surname: String?,
    val lastname: String,
    val customer_image: String?,
    val created_at:String,
    val updated_at:String?,
    val deleted_at:String?
)

data class ReviewDetail(
    @SerializedName("id") val idReview:Int,
    @SerializedName("customer_id") val idCustomer:String,
    @SerializedName("product_id") val idDevice:Int,
    val comment:String?,
    val image: String?,
    val rating:Int,
    val response:String?,
    val note:String?,
    val surname: String?,
    val lastname: String,
    val customer_image: String?,
    val created_at:String,
    val updated_at:String?,
    val deleted_at:String?,
    val customer: CustomerReview,
    val product: ProductReview
)

data class CustomerReview(
    val surname: String,
    val lastname: String,
    val image: String?,
)
data class ProductReview(
    val name: String,
    val image: String?,
)