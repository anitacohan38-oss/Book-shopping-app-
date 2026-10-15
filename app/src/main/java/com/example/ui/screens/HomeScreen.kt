package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.Book
import com.example.model.BookFormat
import com.example.ui.components.BookCard
import com.example.ui.theme.BookAmber
import com.example.ui.theme.BookNavy
import com.example.ui.theme.BookTerracotta
import com.example.ui.viewmodel.BookstoreViewModel
import com.example.ui.viewmodel.Screen

@Composable
fun HomeScreen(
  viewModel: BookstoreViewModel,
  cartItemCount: Int,
  wishlistIds: List<String>,
  onNavigateToCatalog: () -> Unit,
  onNavigateToCart: () -> Unit,
  onNavigateToBook: (String) -> Unit
) {
  val scrollState = rememberScrollState()
  val genres = listOf("All", "Fiction", "Sci-Fi", "Fantasy", "Mystery", "Self-Help", "Biography")

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(scrollState)
      .padding(bottom = 80.dp)
  ) {
    // Top Bar with Brand & Cart Icon
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 14.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Text(
            text = "BOOKVERSE",
            style = MaterialTheme.typography.titleLarge,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp,
            color = BookNavy
          )
          Surface(
            shape = RoundedCornerShape(4.dp),
            color = BookAmber.copy(alpha = 0.15f)
          ) {
            Text(
              text = "EST. 2026",
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold,
              fontSize = 9.sp,
              color = BookAmber,
              modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )
          }
        }
        Text(
          text = "Fine books, stories & audio editions",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      // Cart Badge Button
      BadgedBox(
        badge = {
          if (cartItemCount > 0) {
            Badge(
              containerColor = BookTerracotta,
              contentColor = Color.White
            ) {
              Text("$cartItemCount")
            }
          }
        }
      ) {
        Surface(
          shape = CircleShape,
          color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
          modifier = Modifier.size(42.dp)
        ) {
          IconButton(
            onClick = onNavigateToCart,
            modifier = Modifier.testTag("home_cart_btn")
          ) {
            Icon(
              imageVector = Icons.Default.ShoppingBag,
              contentDescription = "View Cart",
              tint = BookNavy,
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }
    }

    // Search Bar
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 6.dp)
    ) {
      OutlinedTextField(
        value = viewModel.searchQuery.value,
        onValueChange = {
          viewModel.searchQuery.value = it
          if (it.isNotBlank()) {
            onNavigateToCatalog()
          }
        },
        placeholder = { Text("Search by title, author, or genre...") },
        leadingIcon = {
          Icon(Icons.Default.Search, contentDescription = null, tint = BookAmber)
        },
        trailingIcon = {
          if (viewModel.searchQuery.value.isNotEmpty()) {
            IconButton(onClick = { viewModel.searchQuery.value = "" }) {
              Icon(Icons.Default.Clear, contentDescription = "Clear search")
            }
          }
        },
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = MaterialTheme.colorScheme.surface,
          unfocusedContainerColor = MaterialTheme.colorScheme.surface,
          focusedBorderColor = BookAmber,
          unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        ),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("home_search_field")
      )
    }

    // Hero Promo Banner
    Card(
      shape = RoundedCornerShape(16.dp),
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 12.dp)
        .testTag("hero_banner_card"),
      elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(185.dp)
      ) {
        Image(
          painter = painterResource(id = R.drawable.banner_book_festival),
          contentDescription = "Cozy Bookstore Banner",
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )

        // Gradient overlay
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.horizontalGradient(
                colors = listOf(
                  Color.Black.copy(alpha = 0.82f),
                  Color.Black.copy(alpha = 0.55f),
                  Color.Transparent
                )
              )
            )
        )

        // Banner Content
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
          verticalArrangement = Arrangement.SpaceBetween
        ) {
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = BookAmber
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp),
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
              Icon(Icons.Default.LocalOffer, contentDescription = null, tint = Color.Black, modifier = Modifier.size(12.dp))
              Text(
                text = "SPECIAL CODE: BOOKWORM15",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
              )
            }
          }

          Column {
            Text(
              text = "Autumn Book Gala",
              style = MaterialTheme.typography.headlineMedium,
              fontFamily = FontFamily.Serif,
              color = Color.White,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "Take 15% off all hardcovers & discover new worlds.",
              style = MaterialTheme.typography.bodySmall,
              color = Color.White.copy(alpha = 0.85f),
              modifier = Modifier.padding(top = 2.dp)
            )
          }

          Button(
            onClick = onNavigateToCatalog,
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
            modifier = Modifier.height(34.dp).testTag("shop_now_btn")
          ) {
            Text("Explore Titles", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(4.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(14.dp))
          }
        }
      }
    }

    // Genre Filter Chips
    LazyRow(
      contentPadding = PaddingValues(horizontal = 20.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier.padding(vertical = 4.dp)
    ) {
      items(genres) { genre ->
        val isSelected = viewModel.selectedGenre.value == genre
        FilterChip(
          selected = isSelected,
          onClick = {
            viewModel.selectedGenre.value = genre
            onNavigateToCatalog()
          },
          label = { Text(genre) },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = BookNavy,
            selectedLabelColor = Color.White
          ),
          border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = isSelected,
            borderColor = if (isSelected) BookNavy else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
          )
        )
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Section 1: Staff Picks & Literary Gems (Horizontal Carousel)
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = BookAmber, modifier = Modifier.size(18.dp))
        Text(
          text = "Staff Recommendations",
          style = MaterialTheme.typography.titleLarge,
          fontFamily = FontFamily.Serif,
          fontWeight = FontWeight.Bold
        )
      }

      Text(
        text = "See All",
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        color = BookAmber,
        modifier = Modifier
          .clickable { onNavigateToCatalog() }
          .padding(4.dp)
      )
    }

    Spacer(modifier = Modifier.height(10.dp))

    LazyRow(
      contentPadding = PaddingValues(horizontal = 20.dp),
      horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      items(viewModel.featuredBooks) { book ->
        BookCard(
          book = book,
          isWishlisted = wishlistIds.contains(book.id),
          onClick = { onNavigateToBook(book.id) },
          onWishlistClick = { viewModel.toggleWishlist(book.id) },
          onQuickAddToCart = { format -> viewModel.addToCart(book, format) },
          modifier = Modifier.width(165.dp)
        )
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Literary Quote of the Day Box
    Surface(
      shape = RoundedCornerShape(12.dp),
      color = Color(0xFFFEF3C7).copy(alpha = 0.7f),
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp)
    ) {
      Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Bookmark,
          contentDescription = null,
          tint = BookAmber,
          modifier = Modifier.size(28.dp)
        )
        Column {
          Text(
            text = "“A reader lives a thousand lives before he dies. The man who never reads lives only one.”",
            style = MaterialTheme.typography.bodyMedium,
            fontFamily = FontFamily.Serif,
            fontStyle = FontStyle.Italic,
            color = Color(0xFF78350F)
          )
          Text(
            text = "— George R.R. Martin",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF92400E),
            modifier = Modifier.padding(top = 4.dp)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Section 2: Reader Bestsellers
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "Top Bestsellers",
        style = MaterialTheme.typography.titleLarge,
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold
      )

      Text(
        text = "View Catalog",
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        color = BookAmber,
        modifier = Modifier
          .clickable { onNavigateToCatalog() }
          .padding(4.dp)
      )
    }

    Spacer(modifier = Modifier.height(10.dp))

    LazyRow(
      contentPadding = PaddingValues(horizontal = 20.dp),
      horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      items(viewModel.bestsellers) { book ->
        BookCard(
          book = book,
          isWishlisted = wishlistIds.contains(book.id),
          onClick = { onNavigateToBook(book.id) },
          onWishlistClick = { viewModel.toggleWishlist(book.id) },
          onQuickAddToCart = { format -> viewModel.addToCart(book, format) },
          modifier = Modifier.width(165.dp)
        )
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Bookverse Perks Banner
    Surface(
      shape = RoundedCornerShape(12.dp),
      color = BookNavy,
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp)
    ) {
      Row(
        modifier = Modifier.padding(18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "Free Shipping on Orders $35+",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "Plus complimentary collectible bookmark in every package.",
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.8f),
            modifier = Modifier.padding(top = 2.dp)
          )
        }
      }
    }
  }
}
