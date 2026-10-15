package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
  entities = [
    CartItemEntity::class,
    WishlistItemEntity::class,
    OrderEntity::class,
    ReviewEntity::class
  ],
  version = 1,
  exportSchema = false
)
abstract class BookstoreDatabase : RoomDatabase() {
  abstract fun bookstoreDao(): BookstoreDao

  companion object {
    @Volatile
    private var INSTANCE: BookstoreDatabase? = null

    fun getDatabase(context: Context): BookstoreDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          BookstoreDatabase::class.java,
          "bookverse_database"
        ).fallbackToDestructiveMigration().build()
        INSTANCE = instance
        instance
      }
    }
  }
}
