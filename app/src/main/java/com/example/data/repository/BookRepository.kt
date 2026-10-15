package com.example.data.repository

import com.example.data.local.BookstoreDao
import com.example.data.local.CartItemEntity
import com.example.data.local.OrderEntity
import com.example.data.local.ReviewEntity
import com.example.data.local.WishlistItemEntity
import com.example.model.Book
import com.example.model.BookFormat
import com.example.model.Review
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.util.UUID

data class CartItemWithBook(
  val entity: CartItemEntity,
  val book: Book,
  val format: BookFormat
) {
  val totalPrice: Double get() = entity.price * entity.quantity
}

class BookRepository(private val dao: BookstoreDao) {

  val allBooks: List<Book> = listOf(
    Book(
      id = "b1",
      title = "The Midnight Library",
      author = "Matt Haig",
      coverColorHex = 0xFF1E3A8A, // Deep midnight blue
      rating = 4.8f,
      reviewCount = 1420,
      basePrice = 18.99,
      originalPrice = 24.99,
      genre = "Fiction",
      formatPrices = mapOf(
        BookFormat.HARDCOVER to 24.99,
        BookFormat.PAPERBACK to 18.99,
        BookFormat.EBOOK to 9.99,
        BookFormat.AUDIOBOOK to 21.50
      ),
      description = "Between life and death there is a library, and within that library, the shelves go on forever. Every book provides a chance to try another life you could have lived. To see how things would be if you had made other choices... Would you have done anything different, if you had the chance to undo your regrets?",
      sampleChapterTitle = "Chapter 1: A Conversation About Rain",
      sampleExcerpt = "Nineteen years before she decided to die, Nora Seed sat in the warmth of the small library at Hazeldene School in the town of Bedford.\n\nShe stared down at the chessboard on the low table between them. 'Mrs. Elm,' she said, 'is it possible to make a move that ruins everything before the game has even begun?'\n\nThe librarian looked up through her spectacles, an enigmatic half-smile playing across her lips. 'Every pawn carries a queen inside it, Nora. The only fatal move is refusing to touch the board at all.' Outside, rain began to tap insistently against the leaded glass pane.",
      pages = 304,
      publisher = "Canongate Books",
      isbn = "978-0525559474",
      publishYear = 2020,
      badge = "Bestseller",
      isStaffPick = true,
      audioDurationHours = 8.5f
    ),
    Book(
      id = "b2",
      title = "Project Hail Mary",
      author = "Andy Weir",
      coverColorHex = 0xFFB45309, // Amber cosmic flare
      rating = 4.9f,
      reviewCount = 2850,
      basePrice = 22.50,
      originalPrice = 28.00,
      genre = "Sci-Fi",
      formatPrices = mapOf(
        BookFormat.HARDCOVER to 28.00,
        BookFormat.PAPERBACK to 22.50,
        BookFormat.EBOOK to 12.99,
        BookFormat.AUDIOBOOK to 26.99
      ),
      description = "Ryland Grace is the sole survivor on a desperate, last-chance mission—and if he fails, humanity and the earth itself are doomed. Except right now, he doesn't know that. He can't even remember his own name, let alone the nature of his assignment or how to complete it.",
      sampleChapterTitle = "Chapter 1: Awakening in the Dark",
      sampleExcerpt = "'What's two plus two?'\n\nThat's the first question. I don't know who's asking it. I don't know why. The voice is calm, mechanical, and slightly filtered through a speaker.\n\n'Four,' I manage to croak. My throat feels like sandpaper that's been baked in an oven. The room is blindingly white, sterile, and hums with the low vibrational pulse of ionic cooling systems. I look down at my hands. They tremble against medical restraints. There is a whole solar system fading, and my memories are locked in a forgotten safe.",
      pages = 496,
      publisher = "Ballantine Books",
      isbn = "978-0593135204",
      publishYear = 2021,
      badge = "Staff Pick",
      isStaffPick = true,
      audioDurationHours = 16.2f
    ),
    Book(
      id = "b3",
      title = "Atomic Habits",
      author = "James Clear",
      coverColorHex = 0xFF047857, // Deep Emerald
      rating = 4.9f,
      reviewCount = 5120,
      basePrice = 16.20,
      originalPrice = 27.00,
      genre = "Self-Help",
      formatPrices = mapOf(
        BookFormat.HARDCOVER to 27.00,
        BookFormat.PAPERBACK to 16.20,
        BookFormat.EBOOK to 11.99,
        BookFormat.AUDIOBOOK to 19.99
      ),
      description = "An easy and proven way to build good habits and break bad ones. No matter your goals, Atomic Habits offers a proven framework for improving—every day. James Clear, one of the world's leading experts on habit formation, reveals practical strategies that will teach you exactly how to form good habits, break bad ones, and master the tiny behaviors that lead to remarkable results.",
      sampleChapterTitle = "The Fundamentals: Why Tiny Changes Make a Big Difference",
      sampleExcerpt = "The fate of British Cycling changed one day in 2003. The organization, which was the governing body for professional cycling in Great Britain, had recently hired Dave Brailsford as its new performance director.\n\nAt the time, professional cyclists in Great Britain had endured nearly one hundred years of mediocrity. Brailsford had been hired to put British Cycling on a new trajectory. What made him different from previous coaches was his relentless commitment to a strategy that he referred to as 'the aggregation of marginal gains.' The whole principle came from the idea that if you broke down everything you could think of that goes into riding a bike, and then improve it by 1 percent, you will get a significant increase when you put them all together.",
      pages = 320,
      publisher = "Avery / Penguin",
      isbn = "978-0735211292",
      publishYear = 2018,
      badge = "#1 Bestseller",
      isStaffPick = false,
      audioDurationHours = 5.5f
    ),
    Book(
      id = "b4",
      title = "Tomorrow, and Tomorrow, and Tomorrow",
      author = "Gabrielle Zevin",
      coverColorHex = 0xFF0284C7, // Ocean Azure
      rating = 4.7f,
      reviewCount = 980,
      basePrice = 19.99,
      originalPrice = 28.00,
      genre = "Fiction",
      formatPrices = mapOf(
        BookFormat.HARDCOVER to 28.00,
        BookFormat.PAPERBACK to 19.99,
        BookFormat.EBOOK to 10.99,
        BookFormat.AUDIOBOOK to 22.00
      ),
      description = "On a bitter-cold day in the December of his junior year at Harvard, Sam Masur exits a subway car and sees, amid the throngs of people upon the platform, Sadie Green. He calls her name. For a moment, she pretends not to hear him, but then she turns, and a game begins: a legendary collaboration that will launch them to stardom.",
      sampleChapterTitle = "Act I: The Platform at Park Street",
      sampleExcerpt = "Before Mazer invented Ichigo, he was just Sam Masur, a boy with an injured foot and a stubborn habit of looking past the crowd.\n\nThe commuter train hissed steam into the frigid Boston afternoon air. Sadie was wearing a navy duffle coat with wooden toggle buttons, clutching a spiral notebook filled with hand-drawn level maps. When their eyes met across the platform, ten years of childhood memories collapsed into a single heartbeat.",
      pages = 416,
      publisher = "Knopf",
      isbn = "978-0593321201",
      publishYear = 2022,
      badge = "Award Winner",
      isStaffPick = true,
      audioDurationHours = 14.0f
    ),
    Book(
      id = "b5",
      title = "The Silent Patient",
      author = "Alex Michaelides",
      coverColorHex = 0xFF7F1D1D, // Crimson Noir
      rating = 4.6f,
      reviewCount = 3410,
      basePrice = 15.99,
      originalPrice = 18.99,
      genre = "Mystery",
      formatPrices = mapOf(
        BookFormat.HARDCOVER to 24.00,
        BookFormat.PAPERBACK to 15.99,
        BookFormat.EBOOK to 8.99,
        BookFormat.AUDIOBOOK to 18.00
      ),
      description = "Alicia Berenson’s life is seemingly perfect. A famous painter married to an in-demand fashion photographer, she lives in a grand house overlooking a park in one of London’s most desirable areas. One evening her husband returns home late from a fashion shoot, and Alicia shoots him five times in the face, and then never speaks another word.",
      sampleChapterTitle = "Prologue: Alicia Berenson's Diary",
      sampleExcerpt = "July 14.\n\nI don't know why I am writing this. That's not true. Perhaps I do. I want to tell the truth. But Gabriel says that journaling can be dangerous if you aren't honest with the mirror.\n\nTheo Faber is convinced he can make me speak. He sits in the psychiatric ward across from me, his notebook balanced on his knees, watching my silence as if it were a physical door he could unlock with gentle phrases.",
      pages = 336,
      publisher = "Celadon Books",
      isbn = "978-1250301696",
      publishYear = 2019,
      badge = "Thriller",
      isStaffPick = false,
      audioDurationHours = 8.7f
    ),
    Book(
      id = "b6",
      title = "Dune",
      author = "Frank Herbert",
      coverColorHex = 0xFF92400E, // Dune Spice Orange
      rating = 4.8f,
      reviewCount = 4200,
      basePrice = 21.00,
      originalPrice = 26.00,
      genre = "Sci-Fi",
      formatPrices = mapOf(
        BookFormat.HARDCOVER to 35.00,
        BookFormat.PAPERBACK to 21.00,
        BookFormat.EBOOK to 9.99,
        BookFormat.AUDIOBOOK to 29.99
      ),
      description = "Set on the desert planet Arrakis, Dune is the story of the boy Paul Atreides, heir to a noble family tasked with ruling an inhospitable world where the only thing of value is the 'spice' melange, a drug capable of extending life and enhancing consciousness.",
      sampleChapterTitle = "Part One: Dune",
      sampleExcerpt = "A beginning is the time for taking the most delicate care that the balances are correct. This every sister of the Bene Gesserit knows.\n\nTo begin your study of the life of Muad'Dib, then, take care that you first place him in his time: born in the 57th year of the Padishah Emperor, Shaddam IV. And take the most special care that you locate him in his place: the planet Arrakis. Do not be deceived by the fact that he was born on Caladan and lived his first fifteen years there. Arrakis, the planet known as Dune, is forever his place.",
      pages = 688,
      publisher = "Ace Books",
      isbn = "978-0441013593",
      publishYear = 1965,
      badge = "Sci-Fi Classic",
      isStaffPick = true,
      audioDurationHours = 21.3f
    ),
    Book(
      id = "b7",
      title = "The Song of Achilles",
      author = "Madeline Miller",
      coverColorHex = 0xFF7C2D12, // Warm Bronze
      rating = 4.8f,
      reviewCount = 2190,
      basePrice = 17.50,
      originalPrice = 22.00,
      genre = "Fantasy",
      formatPrices = mapOf(
        BookFormat.HARDCOVER to 26.00,
        BookFormat.PAPERBACK to 17.50,
        BookFormat.EBOOK to 9.99,
        BookFormat.AUDIOBOOK to 19.50
      ),
      description = "A thrilling, profound and breathtaking retelling of the Trojan War and the legendary love between Achilles and Patroclus. A tale of gods, kings, immortal fame, and the human heart.",
      sampleChapterTitle = "Chapter 1: The Prince of Opus",
      sampleExcerpt = "My father was a king and the son of kings. He was a short man, as most of us were, and built like a bull, all shoulders.\n\nHe married my mother when she was fourteen and sworn to produce him heirs. It was a good match, for her dowry was enormous. But my father never forgave her for producing me—a simple, small boy with neither the silver tongue of a diplomat nor the fierce sword-arm of a warrior.",
      pages = 416,
      publisher = "Ecco Press",
      isbn = "978-0062060624",
      publishYear = 2011,
      badge = "Book Club Favorite",
      isStaffPick = false,
      audioDurationHours = 11.2f
    ),
    Book(
      id = "b8",
      title = "Steve Jobs",
      author = "Walter Isaacson",
      coverColorHex = 0xFF334155, // Minimalist Slate
      rating = 4.7f,
      reviewCount = 3100,
      basePrice = 24.99,
      originalPrice = 35.00,
      genre = "Biography",
      formatPrices = mapOf(
        BookFormat.HARDCOVER to 35.00,
        BookFormat.PAPERBACK to 24.99,
        BookFormat.EBOOK to 14.99,
        BookFormat.AUDIOBOOK to 28.00
      ),
      description = "Based on more than forty interviews with Steve Jobs conducted over two years—as well as interviews with more than a hundred family members, friends, adversaries, competitors, and colleagues—Walter Isaacson has written an enthralling story of the roller-coaster life and searingly intense personality of a creative entrepreneur whose passion for perfection and ferocious drive revolutionized six industries.",
      sampleChapterTitle = "Chapter 1: Childhood: Abandoned and Chosen",
      sampleExcerpt = "Paul and Clara Jobs loved their adopted son fiercely. When Steve was six, he sat on the lawn with his adoptive father in Mountain View, California, working on the engine of a restored car.\n\n'Steve,' Paul told him, running his calloused hand over the mahogany wooden fence in the backyard, 'you have to make the back of the fence just as beautiful as the front. Nobody else will see it, but you will know.' That obsessive standard of craftsmanship shaped modern technology forever.",
      pages = 656,
      publisher = "Simon & Schuster",
      isbn = "978-1451648539",
      publishYear = 2011,
      badge = "Bestselling Bio",
      isStaffPick = false,
      audioDurationHours = 25.1f
    ),
    Book(
      id = "b9",
      title = "Yellowface",
      author = "R.F. Kuang",
      coverColorHex = 0xFFCA8A04, // Canary Yellow
      rating = 4.5f,
      reviewCount = 1840,
      basePrice = 19.00,
      originalPrice = 26.99,
      genre = "Fiction",
      formatPrices = mapOf(
        BookFormat.HARDCOVER to 26.99,
        BookFormat.PAPERBACK to 19.00,
        BookFormat.EBOOK to 11.99,
        BookFormat.AUDIOBOOK to 21.00
      ),
      description = "What's the harm in taking a dead friend's manuscript if it means the world gets to read it? June Hayward and Athena Liu were supposed to be twin rising stars. But Athena's a literary darling, and June's first novel barely made a whisper. When June witnesses Athena's sudden death in a freak accident, she makes a split-second decision: she steals Athena's finished masterpiece.",
      sampleChapterTitle = "Chapter One: The Rooftop at Dupont",
      sampleExcerpt = "Athena Liu has everything. She's gorgeous, effortlessly brilliant, and Netflix bought her second novel before the ink had even dried on the manuscript galleys.\n\nI was sitting in her kitchen drinking her expensive sake, pretending that watching her receive the literary world's adulation didn't feel like swallowing battery acid. And then she choked on a pancake. Ten minutes later, I was alone in the quiet apartment with the only printed draft of 'The Last Front'.",
      pages = 336,
      publisher = "William Morrow",
      isbn = "978-0063250833",
      publishYear = 2023,
      badge = "Trending",
      isStaffPick = true,
      audioDurationHours = 9.8f
    ),
    Book(
      id = "b10",
      title = "The Psychology of Money",
      author = "Morgan Housel",
      coverColorHex = 0xFF0F766E, // Teal Mint
      rating = 4.8f,
      reviewCount = 2950,
      basePrice = 16.99,
      originalPrice = 21.99,
      genre = "Self-Help",
      formatPrices = mapOf(
        BookFormat.HARDCOVER to 24.99,
        BookFormat.PAPERBACK to 16.99,
        BookFormat.EBOOK to 8.99,
        BookFormat.AUDIOBOOK to 18.00
      ),
      description = "Timeless lessons on wealth, greed, and happiness. Doing well with money isn't necessarily about what you know. It's about how you behave. And behavior is hard to teach, even to really smart people.",
      sampleChapterTitle = "Introduction: The Greatest Show On Earth",
      sampleExcerpt = "Ronald James Read was an American philanthropist, investor, janitor, and gas station attendant. He grew up in rural Vermont, the first person in his family to graduate from high school.\n\nWhen he died in 2014 at age 92, the humble rural janitor made international headlines: he left $2 million to his stepchildren and more than $6 million to his local hospital and library. How did he do it? No lottery ticket. Just steady, quiet compounding over five uninterrupted decades.",
      pages = 256,
      publisher = "Harriman House",
      isbn = "978-0857197689",
      publishYear = 2020,
      badge = "Staff Pick",
      isStaffPick = true,
      audioDurationHours = 5.8f
    ),
    Book(
      id = "b11",
      title = "Fourth Wing",
      author = "Rebecca Yarros",
      coverColorHex = 0xFF581C87, // Imperial Violet
      rating = 4.9f,
      reviewCount = 6120,
      basePrice = 23.99,
      originalPrice = 29.99,
      genre = "Fantasy",
      formatPrices = mapOf(
        BookFormat.HARDCOVER to 29.99,
        BookFormat.PAPERBACK to 23.99,
        BookFormat.EBOOK to 13.99,
        BookFormat.AUDIOBOOK to 27.50
      ),
      description = "Twenty-year-old Violet Sorrengail was supposed to enter the Scribe Quadrant, living a quiet life among books and history. Now, the commanding general—also known as her tough-as-talons mother—has ordered Violet to join the hundreds of candidates striving to become the elite of Navarre: dragon riders.",
      sampleChapterTitle = "Chapter One: The Parapet",
      sampleExcerpt = "A dragon without its rider is a tragedy. A rider without their dragon is dead.\n\nWelcome to the fourth wing of Basgiath War College. Below the narrow limestone parapet lies a thousand-foot sheer drop into jagged rocks. Rain slicks the stone under my boots, and the wind is trying to shove me into oblivion. Behind me stands Xaden Riorson, watching me with eyes that could cut through armor.",
      pages = 528,
      publisher = "Red Tower Books",
      isbn = "978-1649374042",
      publishYear = 2023,
      badge = "#1 Fantasy",
      isStaffPick = false,
      audioDurationHours = 20.8f
    ),
    Book(
      id = "b12",
      title = "A Man Called Ove",
      author = "Fredrik Backman",
      coverColorHex = 0xFF3B82F6, // Nordic Sky
      rating = 4.8f,
      reviewCount = 3890,
      basePrice = 16.50,
      originalPrice = 20.00,
      genre = "Fiction",
      formatPrices = mapOf(
        BookFormat.HARDCOVER to 24.00,
        BookFormat.PAPERBACK to 16.50,
        BookFormat.EBOOK to 9.99,
        BookFormat.AUDIOBOOK to 17.50
      ),
      description = "A heartwarming, quirky tale about a curmudgeonly grump who finds his solitary life turned upside down by a boisterous young family that moves in next door. A testament to how unexpected kindness can reawaken a broken heart.",
      sampleChapterTitle = "Chapter 1: A Man Called Ove Buys a Computer That Is Not a Computer",
      sampleExcerpt = "Ove is fifty-nine. He drives a Saab. He is the kind of man who points at people he dislikes as if they were burglars outside his bedroom window.\n\nHe has firm principles, strict routines, and an unwavering conviction that the world was a considerably better place when people knew how to change a radiator fan without asking the internet.",
      pages = 368,
      publisher = "Atria Books",
      isbn = "978-1476738024",
      publishYear = 2012,
      badge = "Feel Good",
      isStaffPick = true,
      audioDurationHours = 9.1f
    )
  )

  fun getBookById(id: String): Book? = allBooks.find { it.id == id }

  fun getFeaturedBooks(): List<Book> = allBooks.filter { it.isStaffPick || it.rating >= 4.8f }

  fun getBestsellers(): List<Book> = allBooks.filter { it.badge?.contains("Bestseller") == true || it.reviewCount > 2500 }

  // Reactive Cart stream
  val cartItemsWithDetails: Flow<List<CartItemWithBook>> = dao.getCartItems().combine(dao.getWishlistBookIds()) { cartEntities, _ ->
    cartEntities.mapNotNull { entity ->
      val book = getBookById(entity.bookId) ?: return@mapNotNull null
      val format = BookFormat.entries.find { it.name == entity.format } ?: BookFormat.PAPERBACK
      CartItemWithBook(entity, book, format)
    }
  }

  suspend fun addToCart(bookId: String, format: BookFormat, quantity: Int = 1) {
    val book = getBookById(bookId) ?: return
    val existing = dao.findCartItem(bookId, format.name)
    val price = book.priceFor(format)
    if (existing != null) {
      dao.updateCartItem(existing.copy(quantity = existing.quantity + quantity))
    } else {
      dao.insertCartItem(
        CartItemEntity(
          bookId = bookId,
          format = format.name,
          quantity = quantity,
          price = price
        )
      )
    }
  }

  suspend fun updateCartQuantity(cartItemId: Int, newQuantity: Int) {
    if (newQuantity <= 0) {
      dao.deleteCartItem(cartItemId)
    } else {
      dao.updateCartQuantity(cartItemId, newQuantity)
    }
  }

  suspend fun removeCartItem(cartItemId: Int) {
    dao.deleteCartItem(cartItemId)
  }

  suspend fun clearCart() {
    dao.clearCart()
  }

  // Wishlist
  fun getWishlistIds(): Flow<List<String>> = dao.getWishlistBookIds()

  suspend fun toggleWishlist(bookId: String, isInWishlist: Boolean) {
    if (isInWishlist) {
      dao.removeFromWishlist(bookId)
    } else {
      dao.addToWishlist(WishlistItemEntity(bookId = bookId))
    }
  }

  // Orders
  fun getOrders(): Flow<List<OrderEntity>> = dao.getOrders()

  suspend fun createOrder(
    items: List<CartItemWithBook>,
    total: Double,
    address: String,
    deliverySpeed: String
  ): String {
    val orderId = "BV-" + (10000..99999).random()
    val trackingCode = "TRK-" + UUID.randomUUID().toString().take(8).uppercase()
    val itemsSummary = items.joinToString("; ") { "${it.book.title} (${it.format.label} x${it.entity.quantity})" }
    val order = OrderEntity(
      orderId = orderId,
      totalAmount = total,
      status = "Processing",
      itemsJson = itemsSummary,
      itemCount = items.sumOf { it.entity.quantity },
      shippingAddress = address,
      deliverySpeed = deliverySpeed,
      trackingNumber = trackingCode
    )
    dao.insertOrder(order)
    dao.clearCart()
    return orderId
  }

  // Reviews
  fun getReviewsForBook(bookId: String): Flow<List<ReviewEntity>> = dao.getReviewsForBook(bookId)

  suspend fun submitReview(bookId: String, userName: String, rating: Int, comment: String) {
    val review = ReviewEntity(
      id = UUID.randomUUID().toString(),
      bookId = bookId,
      userName = userName.ifBlank { "Avid Reader" },
      rating = rating,
      date = "Just now",
      comment = comment
    )
    dao.insertReview(review)
  }

  val defaultSeedReviews: List<Review> = listOf(
    Review("r1", "b1", "Elena Vance", 5, "2 days ago", "This book made me weep and appreciate every single breath I take. Masterpiece of contemporary fiction."),
    Review("r2", "b1", "Marcus Reid", 5, "1 week ago", "Beautiful concept executed with raw vulnerability. Must read for anyone grappling with 'what ifs'."),
    Review("r3", "b2", "Dr. David Thorne", 5, "3 days ago", "Scientific rigor meets relentless human optimism. The audiobook narration by Ray Porter is also phenomenal!"),
    Review("r4", "b3", "Sarah Jenkins", 5, "May 2026", "Small compounding habits literally transformed my productivity routine. Keep this on your desk."),
    Review("r5", "b5", "Chloe Bennett", 4, "June 2026", "The twist completely blindsided me. I finished the last hundred pages at 2 AM without blinking.")
  )
}
