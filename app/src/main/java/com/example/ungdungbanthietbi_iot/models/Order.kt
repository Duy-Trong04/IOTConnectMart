package com.example.ungdungbanthietbi_iot.models

import com.google.gson.annotations.SerializedName

data class Order(
    @SerializedName("id") val id: String,
    val order_number: Int?,
    @SerializedName("order_id") val order_id: String?,
    @SerializedName("customer_id") val idCustomer:String,
    val saler_id: String,
    val shipper_id: String,
    val export_date: String?,
    val total_import_money: Double?,
    @SerializedName("total_money") val total_money: Double,
    val shipping_fee: String?,
    @SerializedName("prepaid") val prepaid: Double,
    @SerializedName("remaining") val remaining: Double,
    val profit: Double?,
    @SerializedName("discount") val discount:Double,
    @SerializedName("vat") val vat: Double,
    @SerializedName("amount") val totalAmount:Double,
    @SerializedName("payment_method") val paymentMethod:String?,
    @SerializedName("payment_account") val accountNumber:String?,
    val phone: String,
    @SerializedName("name_recipient") val nameRecipient:String?,
    @SerializedName("platform_order") val platformOrder:String?,
    val note: String,
    val status: Int,
    @SerializedName("address") val address:String,
    val created_at:String,
    val updated_at:String?,
    val deleted_at: String?,
    val details: List<DetailsOrders>,
    val count_product: Int
)

data class DetailsOrders(
    val product_id: Int,
    val product_name: String,
    val image: String,
    val quantity: Int,
    val price: Double
)

data class CheckoutRequest(
    val shipping: Shipping,
    val payment: Payment,
    val products: List<Product>,
    val order: OrderRequest
)

data class Shipping(
    val addressType: String,
    val savedAddressId: String,
    val fullName: String,
    val phone: String,
    val email: String,
    val address: String,
    val city: String,
    val district: String,
    val ward: String,
    val shippingMethod: String,
    val note: String
)

data class Payment(
    val paymentMethod: String,
    val sameAsShipping: Boolean,
    val cardNumber: String,
    val cardName: String,
    val cardExpiry: String,
    val cardCvc: String
)

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val quantity: Int,
    val selected: Boolean
)

data class OrderRequest(
    val customer_id: String,
    val export_date: String,
    val total_money: Int,
    val discount: Int,
    val vat: Int,
    val amount: Int,
    val status: Int
)