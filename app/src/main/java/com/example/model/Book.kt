package com.example.model

enum class BookFormat(val label: String, val badge: String) {
  HARDCOVER("Hardcover", "Premium Edition"),
  PAPERBACK("Paperback", "Popular"),
  EBOOK("eBook", "Instant Read"),
  AUDIOBOOK("Audiobook", "Narrated")
}

data class Book(
  val id: String,
  val title: String,
  val author: String,
  val coverColorHex: Long,
  val rating: Float,
  val reviewCount: Int,
  val basePrice: Double,
  val originalPrice: Double? = null,
  val genre: String,
  val formatPrices: Map<BookFormat, Double>,
  val description: String,
  val sampleChapterTitle: String,
  val sampleExcerpt: String,
  val pages: Int,
  val language: String = "English",
  val publisher: String,
  val isbn: String,
  val publishYear: Int,
  val badge: String? = null,
  val isStaffPick: Boolean = false,
  val audioDurationHours: Float? = null
) {
  fun priceFor(format: BookFormat): Double = formatPrices[format] ?: basePrice
}

data class Review(
  val id: String,
  val bookId: String,
  val userName: String,
  val rating: Int,
  val date: String,
  val comment: String,
  val helpfulVotes: Int = 0,
  val verifiedPurchase: Boolean = true
)
