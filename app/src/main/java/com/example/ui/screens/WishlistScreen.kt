package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Book
import com.example.model.BookFormat
import com.example.ui.components.BookCoverView
import com.example.ui.theme.BookAmber
import com.example.ui.theme.BookNavy
import com.example.ui.theme.StarGold
import com.example.ui.viewmodel.BookstoreViewModel

@Composable
fun WishlistScreen(
  viewModel: BookstoreViewModel,
  wishlistBooks: List<Book>,
  onNavigateToCatalog: () -> Unit,
  onNavigateToBook: (String) -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(bottom = 80.dp)
  ) {
    // Header
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
      Text(
        text = "Your Wishlist",
        style = MaterialTheme.typography.headlineLarge,
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        color = BookNavy
      )
      Text(
        text = "${wishlistBooks.size} saved titles to read later",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }

    if (wishlistBooks.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(32.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = null,
            tint = Color(0xFFFCA5A5),
            modifier = Modifier.size(54.dp)
          )

          Text(
            text = "Your Wishlist is Empty",
            style = MaterialTheme.typography.titleLarge,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold
          )

          Text(
            text = "Tap the heart icon on any book to curate your personal reading bucket list.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
          )

          Spacer(modifier = Modifier.height(4.dp))

          Button(
            onClick = onNavigateToCatalog,
            colors = ButtonDefaults.buttonColors(containerColor = BookNavy),
            shape = RoundedCornerShape(12.dp)
          ) {
            Text("Explore Bestsellers")
          }
        }
      }
    } else {
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        items(wishlistBooks, key = { it.id }) { book ->
          Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth().testTag("wishlist_item_${book.id}")
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
              horizontalArrangement = Arrangement.spacedBy(12.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(modifier = Modifier.clickable { onNavigateToBook(book.id) }) {
                BookCoverView(
                  book = book,
                  height = 95.dp,
                  width = 65.dp,
                  elevation = 3.dp
                )
              }

              Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Text(
                  text = book.title,
                  style = MaterialTheme.typography.titleMedium,
                  fontFamily = FontFamily.Serif,
                  fontWeight = FontWeight.Bold,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis
                )

                Text(
                  text = book.author,
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                  Icon(Icons.Default.Star, contentDescription = null, tint = StarGold, modifier = Modifier.size(14.dp))
                  Text("${book.rating}", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                  Text("(${book.reviewCount})", style = MaterialTheme.typography.bodySmall, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = "$${String.format("%.2f", book.basePrice)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = BookNavy
                  )

                  Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Button(
                      onClick = { viewModel.addToCart(book, BookFormat.PAPERBACK) },
                      colors = ButtonDefaults.buttonColors(containerColor = BookAmber),
                      contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                      shape = RoundedCornerShape(8.dp),
                      modifier = Modifier.height(32.dp)
                    ) {
                      Icon(Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.size(14.dp))
                      Spacer(modifier = Modifier.width(4.dp))
                      Text("Move to Cart", style = MaterialTheme.typography.labelSmall)
                    }

                    IconButton(
                      onClick = { viewModel.toggleWishlist(book.id) },
                      modifier = Modifier.size(32.dp)
                    ) {
                      Icon(Icons.Default.DeleteOutline, contentDescription = "Remove", tint = Color(0xFFDC2626), modifier = Modifier.size(18.dp))
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
