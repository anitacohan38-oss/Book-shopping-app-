package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.BookstoreDatabase
import com.example.data.repository.BookRepository
import com.example.ui.components.CheckoutSheet
import com.example.ui.components.OrderSuccessDialog
import com.example.ui.components.ReviewDialog
import com.example.ui.components.SampleReadingDialog
import com.example.ui.screens.BookDetailScreen
import com.example.ui.screens.CatalogScreen
import com.example.ui.screens.CartScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.WishlistScreen
import com.example.ui.theme.BookAmber
import com.example.ui.theme.BookNavy
import com.example.ui.theme.BookTerracotta
import com.example.ui.theme.BookverseTheme
import com.example.ui.viewmodel.BookstoreViewModel
import com.example.ui.viewmodel.Screen

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    val database = BookstoreDatabase.getDatabase(applicationContext)
    val repository = BookRepository(database.bookstoreDao())

    setContent {
      BookverseTheme {
        val viewModel: BookstoreViewModel = viewModel(
          factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
              return BookstoreViewModel(repository) as T
            }
          }
        )

        BookverseApp(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun BookverseApp(viewModel: BookstoreViewModel) {
  val currentScreen by viewModel.currentScreen.collectAsState()
  val cartItems by viewModel.cartItems.collectAsState()
  val wishlistIds by viewModel.wishlistIds.collectAsState()
  val orders by viewModel.orders.collectAsState()
  val filteredBooks by viewModel.filteredBooks.collectAsState()
  val selectedBook by viewModel.selectedBook.collectAsState()
  val checkoutState by viewModel.checkoutState.collectAsState()

  val isSampleReaderOpen by viewModel.isSampleReaderOpen.collectAsState()
  val readerFontSize by viewModel.readerFontSizeSp.collectAsState()
  val isReviewDialogOpen by viewModel.isReviewDialogOpen.collectAsState()

  val snackbarHostState = remember { SnackbarHostState() }

  // Handle system back navigation
  BackHandler(enabled = true) {
    if (!viewModel.handleBack()) {
      // If at home, let default finish activity
    }
  }

  // Observe toast events from ViewModel
  LaunchedEffect(Unit) {
    viewModel.toastEvent.collect { message ->
      snackbarHostState.showSnackbar(message)
    }
  }

  val totalCartItemCount = cartItems.sumOf { it.entity.quantity }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    snackbarHost = { SnackbarHost(snackbarHostState) },
    bottomBar = {
      // Show NavigationBar on all primary top-level tabs
      NavigationBar(
        containerColor = Color.White,
        tonalElevation = 6.dp,
        modifier = Modifier.testTag("main_bottom_nav")
      ) {
        val isHome = currentScreen is Screen.Home
        NavigationBarItem(
          selected = isHome,
          onClick = { viewModel.navigateTo(Screen.Home) },
          icon = {
            Icon(
              imageVector = if (isHome) Icons.Filled.Home else Icons.Outlined.Home,
              contentDescription = "Home"
            )
          },
          label = { Text("Home") },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = BookNavy,
            indicatorColor = BookAmber.copy(alpha = 0.2f)
          ),
          modifier = Modifier.testTag("nav_item_home")
        )

        val isCatalog = currentScreen is Screen.Catalog
        NavigationBarItem(
          selected = isCatalog,
          onClick = { viewModel.navigateTo(Screen.Catalog) },
          icon = {
            Icon(
              imageVector = if (isCatalog) Icons.Filled.MenuBook else Icons.Outlined.MenuBook,
              contentDescription = "Catalog"
            )
          },
          label = { Text("Books") },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = BookNavy,
            indicatorColor = BookAmber.copy(alpha = 0.2f)
          ),
          modifier = Modifier.testTag("nav_item_catalog")
        )

        val isWishlist = currentScreen is Screen.Wishlist
        NavigationBarItem(
          selected = isWishlist,
          onClick = { viewModel.navigateTo(Screen.Wishlist) },
          icon = {
            BadgedBox(
              badge = {
                if (wishlistIds.isNotEmpty()) {
                  Badge(containerColor = BookAmber) {
                    Text("${wishlistIds.size}")
                  }
                }
              }
            ) {
              Icon(
                imageVector = if (isWishlist) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Wishlist"
              )
            }
          },
          label = { Text("Wishlist") },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = BookNavy,
            indicatorColor = BookAmber.copy(alpha = 0.2f)
          ),
          modifier = Modifier.testTag("nav_item_wishlist")
        )

        val isCart = currentScreen is Screen.Cart
        NavigationBarItem(
          selected = isCart,
          onClick = { viewModel.navigateTo(Screen.Cart) },
          icon = {
            BadgedBox(
              badge = {
                if (totalCartItemCount > 0) {
                  Badge(containerColor = BookTerracotta) {
                    Text("$totalCartItemCount")
                  }
                }
              }
            ) {
              Icon(
                imageVector = if (isCart) Icons.Filled.ShoppingBag else Icons.Outlined.ShoppingBag,
                contentDescription = "Cart"
              )
            }
          },
          label = { Text("Cart") },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = BookNavy,
            indicatorColor = BookAmber.copy(alpha = 0.2f)
          ),
          modifier = Modifier.testTag("nav_item_cart")
        )

        val isProfile = currentScreen is Screen.Profile
        NavigationBarItem(
          selected = isProfile,
          onClick = { viewModel.navigateTo(Screen.Profile) },
          icon = {
            Icon(
              imageVector = if (isProfile) Icons.Filled.Person else Icons.Outlined.Person,
              contentDescription = "Profile"
            )
          },
          label = { Text("Profile") },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = BookNavy,
            indicatorColor = BookAmber.copy(alpha = 0.2f)
          ),
          modifier = Modifier.testTag("nav_item_profile")
        )
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      when (val screen = currentScreen) {
        is Screen.Home -> {
          HomeScreen(
            viewModel = viewModel,
            cartItemCount = totalCartItemCount,
            wishlistIds = wishlistIds,
            onNavigateToCatalog = { viewModel.navigateTo(Screen.Catalog) },
            onNavigateToCart = { viewModel.navigateTo(Screen.Cart) },
            onNavigateToBook = { bookId -> viewModel.navigateToBook(bookId) }
          )
        }
        is Screen.Catalog -> {
          CatalogScreen(
            viewModel = viewModel,
            books = filteredBooks,
            wishlistIds = wishlistIds,
            onNavigateToBook = { bookId -> viewModel.navigateToBook(bookId) }
          )
        }
        is Screen.Wishlist -> {
          val wishlistBooks = remember(wishlistIds) {
            wishlistIds.mapNotNull { viewModel.featuredBooks.find { b -> b.id == it } ?: filteredBooks.find { b -> b.id == it } }
          }
          WishlistScreen(
            viewModel = viewModel,
            wishlistBooks = wishlistBooks,
            onNavigateToCatalog = { viewModel.navigateTo(Screen.Catalog) },
            onNavigateToBook = { bookId -> viewModel.navigateToBook(bookId) }
          )
        }
        is Screen.Cart -> {
          CartScreen(
            viewModel = viewModel,
            cartItems = cartItems,
            checkoutState = checkoutState,
            onNavigateToCatalog = { viewModel.navigateTo(Screen.Catalog) },
            onNavigateToBook = { bookId -> viewModel.navigateToBook(bookId) }
          )
        }
        is Screen.Profile -> {
          ProfileScreen(
            viewModel = viewModel,
            orders = orders
          )
        }
        is Screen.BookDetail -> {
          val book = selectedBook ?: viewModel.featuredBooks.first()
          BookDetailScreen(
            book = book,
            viewModel = viewModel,
            onBack = { viewModel.handleBack() }
          )
        }
      }

      // Sample Chapter Reader Sheet
      if (isSampleReaderOpen && selectedBook != null) {
        SampleReadingDialog(
          book = selectedBook!!,
          fontSizeSp = readerFontSize,
          onFontSizeChange = { viewModel.readerFontSizeSp.value = it },
          onDismiss = { viewModel.isSampleReaderOpen.value = false }
        )
      }

      // Write Review Dialog
      if (isReviewDialogOpen && selectedBook != null) {
        ReviewDialog(
          book = selectedBook!!,
          onDismiss = { viewModel.isReviewDialogOpen.value = false },
          onSubmit = { name, rating, comment ->
            viewModel.submitReview(selectedBook!!.id, name, rating, comment)
          }
        )
      }

      // Checkout Bottom Sheet
      if (checkoutState.isSheetOpen) {
        CheckoutSheet(
          cartItems = cartItems,
          state = checkoutState,
          onAddressChange = { viewModel.setShippingAddress(it) },
          onDeliverySpeedChange = { speed, cost -> viewModel.setDeliverySpeed(speed, cost) },
          onPaymentMethodChange = { viewModel.setPaymentMethod(it) },
          onApplyPromo = { viewModel.applyPromoCode(it) },
          onPlaceOrder = { items, total -> viewModel.placeOrder(items, total) },
          onDismiss = { viewModel.closeCheckout() }
        )
      }

      // Order Success Dialog
      if (checkoutState.lastOrderPlaced != null) {
        OrderSuccessDialog(
          order = checkoutState.lastOrderPlaced!!,
          onDismiss = { viewModel.dismissOrderSuccess() },
          onViewOrders = {
            viewModel.dismissOrderSuccess()
            viewModel.navigateTo(Screen.Profile)
          }
        )
      }
    }
  }
}
