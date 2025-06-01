package com.example.ungdungbanthietbi_iot.models

data class CategoryResponse(
    val status_code: Int,
    val data: CategoryData
)

data class CategoryData(
    val categories: List<Category>
)

data class Category(
    val category_id: Int,
    val name: String,
    val slug: String,
    val description: String?,
    val parent_id: Int?,
    val image: String?,
    val is_hide: Boolean,
    val created_at: String?,
    val updated_at: String?,
    val deleted_at: String?,
    val attribute_groups: List<AttributeGroup>,
    val children: List<Category>
)

data class AttributeGroup(
    val group_id: Int,
    val group_name: String,
    val attributes: List<AttributeCATE>
)

data class AttributeCATE(
    val attribute_id: Int,
    val attribute_name: String
)