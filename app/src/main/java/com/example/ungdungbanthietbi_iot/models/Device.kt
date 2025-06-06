package com.example.ungdungbanthietbi_iot.models

import com.google.gson.annotations.SerializedName

data class Device(
    @SerializedName("id") val idDevice: Int,
    val name: String,
    val slug: String,
    val description: String,
    //@SerializedName("description_normal") val descriptionNormal: String,
    @SerializedName("selling_price") val sellingPrice: Double,
    val sold: Int,
    val views: Int,
    val status: Int,
    @SerializedName("is_hide") val isHide: Int,
    @SerializedName("category_id") val categoryId: Int,
    val categories: String,
    @SerializedName("unit_id") val unitId: Int,
    @SerializedName("unit_name") val unitName: String,
    val stock: Int,
    @SerializedName("average_rating") val averageRating: String,
    @SerializedName("total_liked") val totalLiked: String,
    @SerializedName("total_review") val totalReview: String,
    val created_at: String,
    val updated_at: String?,
    val deleted_at: String?,
    val reviews: List<Review>,
    val images: List<Images>,
    val image: String,
    val specifications: List<Specification>
)

data class Specification(
    val id: Int,
    val name: String,
    val attributes: List<Attribute>
)

data class Attribute(
    val id: Int,
    val name: String,
    val value: String
)

data class Images(
    val id: Int,
    val product_id: Int,
    @SerializedName("image") val image: String
)