package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Int = 0,
  val bookId: String,
  val format: String,
  val quantity: Int,
  val price: Double,
  val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "wishlist_items")
data class WishlistItemEntity(
  @PrimaryKey
  val bookId: String,
  val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "orders")
data class OrderEntity(
  @PrimaryKey
  val orderId: String,
  val createdAt: Long = System.currentTimeMillis(),
  val totalAmount: Double,
  val status: String, // "Processing", "Shipped", "Delivered"
  val itemsJson: String, // summary of books and formats
  val itemCount: Int,
  val shippingAddress: String,
  val deliverySpeed: String,
  val trackingNumber: String
)

@Entity(tableName = "custom_reviews")
data class ReviewEntity(
  @PrimaryKey
  val id: String,
  val bookId: String,
  val userName: String,
  val rating: Int,
  val date: String,
  val comment: String
)
