package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BookstoreDao {

  // Cart
  @Query("SELECT * FROM cart_items ORDER BY addedAt DESC")
  fun getCartItems(): Flow<List<CartItemEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertCartItem(item: CartItemEntity): Long

  @Query("SELECT * FROM cart_items WHERE bookId = :bookId AND format = :format LIMIT 1")
  suspend fun findCartItem(bookId: String, format: String): CartItemEntity?

  @Update
  suspend fun updateCartItem(item: CartItemEntity)

  @Query("UPDATE cart_items SET quantity = :quantity WHERE id = :id")
  suspend fun updateCartQuantity(id: Int, quantity: Int)

  @Query("DELETE FROM cart_items WHERE id = :id")
  suspend fun deleteCartItem(id: Int)

  @Query("DELETE FROM cart_items")
  suspend fun clearCart()

  // Wishlist
  @Query("SELECT bookId FROM wishlist_items")
  fun getWishlistBookIds(): Flow<List<String>>

  @Query("SELECT EXISTS(SELECT 1 FROM wishlist_items WHERE bookId = :bookId)")
  fun isBookInWishlist(bookId: String): Flow<Boolean>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun addToWishlist(item: WishlistItemEntity)

  @Query("DELETE FROM wishlist_items WHERE bookId = :bookId")
  suspend fun removeFromWishlist(bookId: String)

  // Orders
  @Query("SELECT * FROM orders ORDER BY createdAt DESC")
  fun getOrders(): Flow<List<OrderEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrder(order: OrderEntity)

  // Reviews
  @Query("SELECT * FROM custom_reviews WHERE bookId = :bookId ORDER BY id DESC")
  fun getReviewsForBook(bookId: String): Flow<List<ReviewEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertReview(review: ReviewEntity)
}
