package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Book
import com.example.model.BookFormat
import com.example.ui.components.BookCoverView
import com.example.ui.theme.BookAmber
import com.example.ui.theme.BookNavy
import com.example.ui.theme.BookTerracotta
import com.example.ui.theme.StarGold
import com.example.ui.viewmodel.BookstoreViewModel

@Composable
fun BookDetailScreen(
  book: Book,
  viewModel: BookstoreViewModel,
  onBack: () -> Unit
) {
  val scrollState = rememberScrollState()
  val wishlistIds by viewModel.wishlistIds.collectAsState()
  val isWishlisted = wishlistIds.contains(book.id)
  val reviews by viewModel.selectedBookReviews.collectAsState()
  val selectedFormat by viewModel.selectedBookFormat.collectAsState()
  val quantity by viewModel.selectedQuantity.collectAsState()
  var isDescriptionExpanded by remember { mutableStateOf(false) }

  Box(modifier = Modifier.fillMaxSize()) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)
        .padding(bottom = 90.dp) // space for sticky bottom bar
    ) {
      // Top Navigation Bar
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = onBack,
          modifier = Modifier.testTag("detail_back_btn")
        ) {
          Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
          ) {
            IconButton(
              onClick = { viewModel.toggleWishlist(book.id) },
              modifier = Modifier.testTag("detail_wishlist_toggle")
            ) {
              Icon(
                imageVector = if (isWishlisted) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Save to Wishlist",
                tint = if (isWishlisted) Color(0xFFDC2626) else MaterialTheme.colorScheme.onSurface
              )
            }
          }
        }
      }

      // Large Tactile Book Cover Presentation
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
      ) {
        BookCoverView(
          book = book,
          height = 270.dp,
          width = 185.dp,
          elevation = 10.dp
        )
      }

      // Title & Author & Ratings
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        if (book.badge != null) {
          Surface(
            shape = RoundedCornerShape(16.dp),
            color = if (book.isStaffPick) BookAmber else BookNavy
          ) {
            Text(
              text = book.badge,
              style = MaterialTheme.typography.labelSmall,
              color = Color.White,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )
          }
          Spacer(modifier = Modifier.height(8.dp))
        }

        Text(
          text = book.title,
          style = MaterialTheme.typography.headlineLarge,
          fontFamily = FontFamily.Serif,
          fontWeight = FontWeight.Bold,
          textAlign = androidx.compose.ui.text.style.TextAlign.Center,
          color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = "By ${book.author}",
          style = MaterialTheme.typography.titleMedium,
          color = BookAmber,
          fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Rating row
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Icon(Icons.Filled.Star, contentDescription = null, tint = StarGold, modifier = Modifier.size(18.dp))
          Text(
            text = "${book.rating}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "• ${book.reviewCount} customer reviews",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Format Selector
      Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text(
          text = "Select Edition / Format",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          book.formatPrices.forEach { (format, price) ->
            val isSelected = selectedFormat == format
            Surface(
              shape = RoundedCornerShape(10.dp),
              border = androidx.compose.foundation.BorderStroke(
                1.5.dp,
                if (isSelected) BookAmber else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
              ),
              color = if (isSelected) BookAmber.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface,
              modifier = Modifier
                .weight(1f)
                .clickable { viewModel.selectedBookFormat.value = format }
                .testTag("format_chip_${format.name}")
            ) {
              Column(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Text(
                  text = format.label,
                  style = MaterialTheme.typography.labelSmall,
                  fontWeight = FontWeight.Bold,
                  color = if (isSelected) BookAmber else MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = "$${String.format("%.2f", price)}",
                  style = MaterialTheme.typography.titleSmall,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.padding(top = 2.dp)
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Sample Read & Audio Info Buttons
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        OutlinedButton(
          onClick = { viewModel.isSampleReaderOpen.value = true },
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier
            .weight(1f)
            .height(44.dp)
            .testTag("read_sample_btn")
        ) {
          Icon(Icons.Default.AutoStories, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Look Inside (Sample)")
        }

        if (book.audioDurationHours != null) {
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.height(44.dp)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 12.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Icon(Icons.Default.Headphones, contentDescription = null, tint = BookAmber, modifier = Modifier.size(18.dp))
              Text(
                text = "${book.audioDurationHours}h Audio",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Synopsis Section
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp)
      ) {
        Text(
          text = "About this Book",
          style = MaterialTheme.typography.titleMedium,
          fontFamily = FontFamily.Serif,
          fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = book.description,
          style = MaterialTheme.typography.bodyMedium,
          lineHeight = 22.sp,
          maxLines = if (isDescriptionExpanded) Int.MAX_VALUE else 4,
          color = MaterialTheme.colorScheme.onSurface
        )

        Text(
          text = if (isDescriptionExpanded) "Show Less" else "Read More...",
          style = MaterialTheme.typography.labelMedium,
          color = BookAmber,
          fontWeight = FontWeight.Bold,
          modifier = Modifier
            .clickable { isDescriptionExpanded = !isDescriptionExpanded }
            .padding(top = 4.dp)
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Book Details Table (Specifications)
      Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)),
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp)
      ) {
        Column(
          modifier = Modifier.padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Text(
            text = "Product Details",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
          )

          HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Publisher", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(book.publisher, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
          }
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Publication Year", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("${book.publishYear}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
          }
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Print Length", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("${book.pages} pages", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
          }
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Language", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(book.language, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
          }
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("ISBN-13", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(book.isbn, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Reader Reviews Section
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Reader Reviews",
            style = MaterialTheme.typography.titleLarge,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold
          )

          OutlinedButton(
            onClick = { viewModel.isReviewDialogOpen.value = true },
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
            modifier = Modifier.height(34.dp).testTag("write_review_btn")
          ) {
            Icon(Icons.Default.RateReview, contentDescription = null, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Write a Review", style = MaterialTheme.typography.labelSmall)
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (reviews.isEmpty()) {
          Text(
            text = "Be the first verified reader to review this title!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        } else {
          Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            reviews.forEach { review ->
              Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                Column(
                  modifier = Modifier.padding(12.dp),
                  verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Row(
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                      Text(
                        text = review.userName,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                      )
                      if (review.verifiedPurchase) {
                        Icon(
                          imageVector = Icons.Default.Verified,
                          contentDescription = "Verified Purchase",
                          tint = Color(0xFF059669),
                          modifier = Modifier.size(14.dp)
                        )
                      }
                    }

                    Text(
                      text = review.date,
                      style = MaterialTheme.typography.labelSmall,
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  }

                  // Stars
                  Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    (1..5).forEach { star ->
                      Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = if (star <= review.rating) StarGold else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        modifier = Modifier.size(14.dp)
                      )
                    }
                  }

                  Text(
                    text = review.comment,
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 20.sp
                  )
                }
              }
            }
          }
        }
      }
    }

    // Sticky Bottom Bar with Price, Quantity & Add to Cart
    Surface(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .fillMaxWidth(),
      color = MaterialTheme.colorScheme.surface,
      shadowElevation = 12.dp
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Price for chosen format
        Column {
          val price = book.priceFor(selectedFormat)
          Text(
            text = "$${String.format("%.2f", price * quantity)}",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = BookNavy
          )
          Text(
            text = "${selectedFormat.label} • $quantity item${if (quantity > 1) "s" else ""}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        // Quantity controls
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.size(32.dp)
          ) {
            IconButton(
              onClick = { if (quantity > 1) viewModel.selectedQuantity.value = quantity - 1 },
              modifier = Modifier.testTag("qty_minus_btn")
            ) {
              Icon(Icons.Default.Remove, contentDescription = "Decrease quantity", modifier = Modifier.size(16.dp))
            }
          }

          Text(
            text = "$quantity",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp)
          )

          Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.size(32.dp)
          ) {
            IconButton(
              onClick = { if (quantity < 10) viewModel.selectedQuantity.value = quantity + 1 },
              modifier = Modifier.testTag("qty_plus_btn")
            ) {
              Icon(Icons.Default.Add, contentDescription = "Increase quantity", modifier = Modifier.size(16.dp))
            }
          }
        }

        // Add to Cart Button
        Button(
          onClick = {
            viewModel.addToCart(book, selectedFormat, quantity)
          },
          colors = ButtonDefaults.buttonColors(containerColor = BookNavy),
          shape = RoundedCornerShape(12.dp),
          contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
          modifier = Modifier.testTag("detail_add_to_cart_btn")
        ) {
          Icon(Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text("Add to Cart", fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}
