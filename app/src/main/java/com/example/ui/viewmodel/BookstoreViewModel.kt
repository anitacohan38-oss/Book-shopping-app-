package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.OrderEntity
import com.example.data.local.ReviewEntity
import com.example.data.repository.BookRepository
import com.example.data.repository.CartItemWithBook
import com.example.model.Book
import com.example.model.BookFormat
import com.example.model.Review
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class Screen {
  data object Home : Screen()
  data object Catalog : Screen()
  data object Wishlist : Screen()
  data object Cart : Screen()
  data object Profile : Screen()
  data class BookDetail(val bookId: String) : Screen()
}

enum class SortOption(val label: String) {
  POPULAR("Most Popular"),
  RATING("Highest Rated"),
  PRICE_LOW("Price: Low to High"),
  PRICE_HIGH("Price: High to Low")
}

data class CheckoutState(
  val isSheetOpen: Boolean = false,
  val shippingAddress: String = "742 Evergreen Terrace, Springfield, OR 97477",
  val deliverySpeed: String = "Standard (3-5 days) - Free",
  val deliverySpeedCost: Double = 0.0,
  val paymentMethod: String = "Credit Card (•••• 4242)",
  val promoCode: String = "",
  val appliedDiscountPercent: Double = 0.0,
  val appliedDiscountFixed: Double = 0.0,
  val promoMessage: String? = null,
  val isPlacingOrder: Boolean = false,
  val lastOrderPlaced: OrderEntity? = null
)

class BookstoreViewModel(private val repository: BookRepository) : ViewModel() {

  private val _currentScreen = MutableStateFlow<Screen>(Screen.Home)
  val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

  // Navigation history stack for BackHandler
  private val screenStack = mutableListOf<Screen>(Screen.Home)

  // Search & Catalog Filter State
  val searchQuery = MutableStateFlow("")
  val selectedGenre = MutableStateFlow("All")
  val selectedSort = MutableStateFlow(SortOption.POPULAR)
  val selectedFormatFilter = MutableStateFlow<BookFormat?>(null)

  // Detail screen state
  private val _selectedBook = MutableStateFlow<Book?>(null)
  val selectedBook: StateFlow<Book?> = _selectedBook.asStateFlow()

  val selectedBookFormat = MutableStateFlow(BookFormat.PAPERBACK)
  val selectedQuantity = MutableStateFlow(1)

  // Sample Reader Dialog
  val isSampleReaderOpen = MutableStateFlow(false)
  val readerFontSizeSp = MutableStateFlow(16)

  // Add Review Dialog
  val isReviewDialogOpen = MutableStateFlow(false)

  // Checkout State
  private val _checkoutState = MutableStateFlow(CheckoutState())
  val checkoutState: StateFlow<CheckoutState> = _checkoutState.asStateFlow()

  // Toast / User Notifications
  private val _toastEvent = MutableSharedFlow<String>()
  val toastEvent: SharedFlow<String> = _toastEvent.asSharedFlow()

  // Repository data streams
  val cartItems: StateFlow<List<CartItemWithBook>> = repository.cartItemsWithDetails
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val wishlistIds: StateFlow<List<String>> = repository.getWishlistIds()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val orders: StateFlow<List<OrderEntity>> = repository.getOrders()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Reviews for the currently selected book (seed + custom)
  private val _selectedBookReviews = MutableStateFlow<List<Review>>(emptyList())
  val selectedBookReviews: StateFlow<List<Review>> = _selectedBookReviews.asStateFlow()

  init {
    // Observe custom reviews when selected book changes
    viewModelScope.launch {
      _selectedBook.collect { book ->
        if (book != null) {
          repository.getReviewsForBook(book.id).collect { customReviews ->
            val seeds = repository.defaultSeedReviews.filter { it.bookId == book.id }
            val mappedCustom = customReviews.map {
              Review(
                id = it.id,
                bookId = it.bookId,
                userName = it.userName,
                rating = it.rating,
                date = it.date,
                comment = it.comment,
                helpfulVotes = 0,
                verifiedPurchase = true
              )
            }
            _selectedBookReviews.value = mappedCustom + seeds
          }
        }
      }
    }
  }

  // Filtered books stream
  val filteredBooks: StateFlow<List<Book>> = combine(
    searchQuery,
    selectedGenre,
    selectedSort,
    selectedFormatFilter
  ) { query, genre, sort, formatFilter ->
    var list = repository.allBooks

    if (query.isNotBlank()) {
      val trimmed = query.trim().lowercase()
      list = list.filter {
        it.title.lowercase().contains(trimmed) ||
          it.author.lowercase().contains(trimmed) ||
          it.genre.lowercase().contains(trimmed) ||
          it.description.lowercase().contains(trimmed)
      }
    }

    if (genre != "All") {
      list = list.filter { it.genre.equals(genre, ignoreCase = true) }
    }

    if (formatFilter != null) {
      list = list.filter { it.formatPrices.containsKey(formatFilter) }
    }

    when (sort) {
      SortOption.POPULAR -> list.sortedByDescending { it.reviewCount }
      SortOption.RATING -> list.sortedByDescending { it.rating }
      SortOption.PRICE_LOW -> list.sortedBy { it.basePrice }
      SortOption.PRICE_HIGH -> list.sortedByDescending { it.basePrice }
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), repository.allBooks)

  val featuredBooks: List<Book> get() = repository.getFeaturedBooks()
  val bestsellers: List<Book> get() = repository.getBestsellers()

  fun navigateTo(screen: Screen) {
    if (_currentScreen.value != screen) {
      screenStack.add(screen)
      _currentScreen.value = screen
    }
  }

  fun navigateToBook(bookId: String) {
    val book = repository.getBookById(bookId)
    if (book != null) {
      _selectedBook.value = book
      selectedBookFormat.value = BookFormat.PAPERBACK
      selectedQuantity.value = 1
      navigateTo(Screen.BookDetail(bookId))
    }
  }

  fun handleBack(): Boolean {
    if (isSampleReaderOpen.value) {
      isSampleReaderOpen.value = false
      return true
    }
    if (_checkoutState.value.isSheetOpen) {
      _checkoutState.value = _checkoutState.value.copy(isSheetOpen = false)
      return true
    }
    if (_checkoutState.value.lastOrderPlaced != null) {
      _checkoutState.value = _checkoutState.value.copy(lastOrderPlaced = null)
      return true
    }
    if (screenStack.size > 1) {
      screenStack.removeAt(screenStack.size - 1)
      val previous = screenStack.last()
      _currentScreen.value = previous
      if (previous is Screen.BookDetail) {
        _selectedBook.value = repository.getBookById(previous.bookId)
      }
      return true
    }
    return false
  }

  fun toggleWishlist(bookId: String) {
    viewModelScope.launch {
      val isSaved = wishlistIds.value.contains(bookId)
      repository.toggleWishlist(bookId, isSaved)
      _toastEvent.emit(if (isSaved) "Removed from Wishlist" else "Saved to Wishlist")
    }
  }

  fun addToCart(book: Book, format: BookFormat, quantity: Int = 1) {
    viewModelScope.launch {
      repository.addToCart(book.id, format, quantity)
      _toastEvent.emit("Added ${book.title} (${format.label}) to cart!")
    }
  }

  fun updateCartItemQuantity(cartItemId: Int, quantity: Int) {
    viewModelScope.launch {
      repository.updateCartQuantity(cartItemId, quantity)
    }
  }

  fun removeCartItem(cartItemId: Int) {
    viewModelScope.launch {
      repository.removeCartItem(cartItemId)
      _toastEvent.emit("Item removed from cart")
    }
  }

  fun clearCart() {
    viewModelScope.launch {
      repository.clearCart()
      _toastEvent.emit("Cart cleared")
    }
  }

  fun applyPromoCode(code: String) {
    val clean = code.trim().uppercase()
    when (clean) {
      "BOOKWORM15" -> {
        _checkoutState.value = _checkoutState.value.copy(
          promoCode = clean,
          appliedDiscountPercent = 0.15,
          appliedDiscountFixed = 0.0,
          promoMessage = "15% off applied!"
        )
      }
      "FREESHIP" -> {
        _checkoutState.value = _checkoutState.value.copy(
          promoCode = clean,
          appliedDiscountPercent = 0.0,
          appliedDiscountFixed = 4.99,
          deliverySpeedCost = 0.0,
          promoMessage = "Free shipping applied!"
        )
      }
      "FIRSTREAD" -> {
        _checkoutState.value = _checkoutState.value.copy(
          promoCode = clean,
          appliedDiscountPercent = 0.0,
          appliedDiscountFixed = 5.0,
          promoMessage = "$5.00 discount applied!"
        )
      }
      else -> {
        _checkoutState.value = _checkoutState.value.copy(
          promoMessage = "Invalid coupon code. Try BOOKWORM15"
        )
      }
    }
  }

  fun openCheckout() {
    _checkoutState.value = _checkoutState.value.copy(isSheetOpen = true)
  }

  fun closeCheckout() {
    _checkoutState.value = _checkoutState.value.copy(isSheetOpen = false)
  }

  fun setShippingAddress(address: String) {
    _checkoutState.value = _checkoutState.value.copy(shippingAddress = address)
  }

  fun setDeliverySpeed(speedName: String, cost: Double) {
    _checkoutState.value = _checkoutState.value.copy(deliverySpeed = speedName, deliverySpeedCost = cost)
  }

  fun setPaymentMethod(method: String) {
    _checkoutState.value = _checkoutState.value.copy(paymentMethod = method)
  }

  fun placeOrder(items: List<CartItemWithBook>, finalTotal: Double) {
    viewModelScope.launch {
      _checkoutState.value = _checkoutState.value.copy(isPlacingOrder = true)
      val orderId = repository.createOrder(
        items = items,
        total = finalTotal,
        address = _checkoutState.value.shippingAddress,
        deliverySpeed = _checkoutState.value.deliverySpeed
      )
      val placed = OrderEntity(
        orderId = orderId,
        createdAt = System.currentTimeMillis(),
        totalAmount = finalTotal,
        status = "Processing",
        itemsJson = items.joinToString("; ") { "${it.book.title} (${it.format.label})" },
        itemCount = items.sumOf { it.entity.quantity },
        shippingAddress = _checkoutState.value.shippingAddress,
        deliverySpeed = _checkoutState.value.deliverySpeed,
        trackingNumber = "BV-TRK-" + (100000..999999).random()
      )
      _checkoutState.value = _checkoutState.value.copy(
        isPlacingOrder = false,
        isSheetOpen = false,
        lastOrderPlaced = placed
      )
      _toastEvent.emit("Order placed successfully! Order #${placed.orderId}")
    }
  }

  fun dismissOrderSuccess() {
    _checkoutState.value = _checkoutState.value.copy(lastOrderPlaced = null)
  }

  fun submitReview(bookId: String, name: String, rating: Int, text: String) {
    viewModelScope.launch {
      repository.submitReview(bookId, name, rating, text)
      isReviewDialogOpen.value = false
      _toastEvent.emit("Thank you for your review!")
    }
  }
}
