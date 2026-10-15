package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Book
import com.example.model.BookFormat
import com.example.ui.theme.BookAmber
import com.example.ui.theme.StarGold

@Composable
fun BookCard(
  book: Book,
  isWishlisted: Boolean,
  onClick: () -> Unit,
  onWishlistClick: () -> Unit,
  onQuickAddToCart: (BookFormat) -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .testTag("book_card_${book.id}"),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
    ) {
      // Cover + Wishlist Button Box
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .align(Alignment.CenterHorizontally),
        contentAlignment = Alignment.TopCenter
      ) {
        BookCoverView(
          book = book,
          height = 180.dp,
          width = 125.dp
        )

        // Wishlist Icon Button top-right
        Surface(
          shape = CircleShape,
          color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
          shadowElevation = 2.dp,
          modifier = Modifier
            .align(Alignment.TopEnd)
            .size(34.dp)
        ) {
          IconButton(
            onClick = onWishlistClick,
            modifier = Modifier
              .size(34.dp)
              .testTag("wishlist_btn_${book.id}")
          ) {
            Icon(
              imageVector = if (isWishlisted) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
              contentDescription = if (isWishlisted) "Remove from wishlist" else "Add to wishlist",
              tint = if (isWishlisted) Color(0xFFDC2626) else MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(18.dp)
            )
          }
        }

        // Badge indicator if present
        if (book.badge != null) {
          Surface(
            shape = RoundedCornerShape(topStart = 0.dp, bottomEnd = 6.dp, topEnd = 6.dp, bottomStart = 0.dp),
            color = if (book.isStaffPick) BookAmber else MaterialTheme.colorScheme.primary,
            modifier = Modifier.align(Alignment.TopStart)
          ) {
            Text(
              text = book.badge,
              style = MaterialTheme.typography.labelSmall,
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 9.sp,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Rating & Reviews count
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Star,
          contentDescription = null,
          tint = StarGold,
          modifier = Modifier.size(14.dp)
        )
        Text(
          text = "${book.rating}",
          style = MaterialTheme.typography.labelMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Text(
          text = "(${book.reviewCount})",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontSize = 11.sp
        )
      }

      Spacer(modifier = Modifier.height(4.dp))

      // Book Title
      Text(
        text = book.title,
        style = MaterialTheme.typography.titleMedium,
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.SemiBold,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        color = MaterialTheme.colorScheme.onSurface,
        lineHeight = 18.sp
      )

      // Author
      Text(
        text = book.author,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(top = 2.dp)
      )

      Spacer(modifier = Modifier.height(8.dp))

      // Price & Add to Cart Action
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Text(
              text = "$${String.format("%.2f", book.basePrice)}",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.primary
            )

            if (book.originalPrice != null) {
              Text(
                text = "$${String.format("%.2f", book.originalPrice)}",
                style = MaterialTheme.typography.bodySmall,
                textDecoration = TextDecoration.LineThrough,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.sp
              )
            }
          }

          Text(
            text = "Paperback",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 10.sp
          )
        }

        // Quick add to cart button
        FilledTonalIconButton(
          onClick = { onQuickAddToCart(BookFormat.PAPERBACK) },
          colors = IconButtonDefaults.filledTonalIconButtonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
          ),
          modifier = Modifier
            .size(36.dp)
            .testTag("add_to_cart_btn_${book.id}")
        ) {
          Icon(
            imageVector = Icons.Default.ShoppingBag,
            contentDescription = "Add to cart",
            modifier = Modifier.size(18.dp)
          )
        }
      }
    }
  }
}
