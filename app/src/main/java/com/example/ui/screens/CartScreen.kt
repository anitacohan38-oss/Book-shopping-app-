package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.repository.CartItemWithBook
import com.example.ui.components.BookCoverView
import com.example.ui.theme.BookAmber
import com.example.ui.theme.BookNavy
import com.example.ui.theme.BookTerracotta
import com.example.ui.viewmodel.BookstoreViewModel
import com.example.ui.viewmodel.CheckoutState

@Composable
fun CartScreen(
  viewModel: BookstoreViewModel,
  cartItems: List<CartItemWithBook>,
  checkoutState: CheckoutState,
  onNavigateToCatalog: () -> Unit,
  onNavigateToBook: (String) -> Unit
) {
  if (cartItems.isEmpty()) {
    // Empty State with Cozy Illustration
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(24.dp)
        .padding(bottom = 70.dp),
      contentAlignment = Alignment.Center
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Surface(
          shape = RoundedCornerShape(20.dp),
          color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
          modifier = Modifier.size(220.dp)
        ) {
          Image(
            painter = painterResource(id = R.drawable.illustration_book_lover),
            contentDescription = "Empty Book Cart",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(20.dp))
          )
        }

        Text(
          text = "Your Book Cart is Empty",
          style = MaterialTheme.typography.headlineMedium,
          fontFamily = FontFamily.Serif,
          fontWeight = FontWeight.Bold,
          textAlign = TextAlign.Center
        )

        Text(
          text = "No stories waiting in your bag yet. Discover bestsellers, staff recommendations, and rare editions.",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          textAlign = TextAlign.Center,
          modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Button(
          onClick = onNavigateToCatalog,
          colors = ButtonDefaults.buttonColors(containerColor = BookNavy),
          shape = RoundedCornerShape(12.dp),
          contentPadding = PaddingValues(horizontal = 24.dp, vertical = 14.dp),
          modifier = Modifier.testTag("cart_explore_btn")
        ) {
          Icon(Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text("Browse Bestsellers", fontWeight = FontWeight.Bold)
        }
      }
    }
  } else {
    val itemsSubtotal = cartItems.sumOf { it.totalPrice }
    val discount = if (checkoutState.appliedDiscountPercent > 0) {
      itemsSubtotal * checkoutState.appliedDiscountPercent
    } else {
      checkoutState.appliedDiscountFixed.coerceAtMost(itemsSubtotal)
    }
    val discountedSubtotal = (itemsSubtotal - discount).coerceAtLeast(0.0)
    val shippingCost = if (itemsSubtotal >= 35.0 || checkoutState.deliverySpeedCost == 0.0) 0.0 else 4.99
    val finalTotal = discountedSubtotal + shippingCost + (discountedSubtotal * 0.08)

    Box(modifier = Modifier.fillMaxSize()) {
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(bottom = 90.dp), // space for bottom checkout button
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // Top Header
        item {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "Shopping Cart",
                style = MaterialTheme.typography.headlineLarge,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = BookNavy
              )
              Text(
                text = "${cartItems.sumOf { it.entity.quantity }} books selected",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            Text(
              text = "Clear All",
              style = MaterialTheme.typography.labelMedium,
              color = Color(0xFFDC2626),
              fontWeight = FontWeight.SemiBold,
              modifier = Modifier
                .clickable { viewModel.clearCart() }
                .padding(4.dp)
                .testTag("clear_cart_btn")
            )
          }
        }

        // Free Shipping Progress
        item {
          val progress = (itemsSubtotal / 35.0).coerceIn(0.0, 1.0)
          val remaining = (35.0 - itemsSubtotal).coerceAtLeast(0.0)

          Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (progress >= 1.0) Color(0xFFD1FAE5) else Color(0xFFFEF3C7),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Text(
                text = if (progress >= 1.0) "🎉 You've unlocked FREE Bookstore Delivery!" else "Add $${String.format("%.2f", remaining)} more for FREE shipping",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = if (progress >= 1.0) Color(0xFF065F46) else Color(0xFF92400E)
              )
            }
          }
        }

        // Cart Items
        items(cartItems, key = { it.entity.id }) { item ->
          Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("cart_item_${item.entity.id}")
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
              horizontalArrangement = Arrangement.spacedBy(12.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              // Mini Cover
              Box(
                modifier = Modifier.clickable { onNavigateToBook(item.book.id) }
              ) {
                BookCoverView(
                  book = item.book,
                  height = 95.dp,
                  width = 65.dp,
                  elevation = 3.dp
                )
              }

              // Details & Controls
              Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Text(
                  text = item.book.title,
                  style = MaterialTheme.typography.titleMedium,
                  fontFamily = FontFamily.Serif,
                  fontWeight = FontWeight.Bold,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis
                )

                Text(
                  text = item.book.author,
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Surface(
                  shape = RoundedCornerShape(4.dp),
                  color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                  Text(
                    text = item.format.label,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }

                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = "$${String.format("%.2f", item.totalPrice)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = BookNavy
                  )

                  // Quantity increment / decrement
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                  ) {
                    Surface(
                      shape = CircleShape,
                      color = MaterialTheme.colorScheme.surfaceVariant,
                      modifier = Modifier.size(28.dp)
                    ) {
                      IconButton(
                        onClick = {
                          viewModel.updateCartItemQuantity(item.entity.id, item.entity.quantity - 1)
                        },
                        modifier = Modifier.size(28.dp)
                      ) {
                        Icon(Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(14.dp))
                      }
                    }

                    Text(
                      text = "${item.entity.quantity}",
                      style = MaterialTheme.typography.titleSmall,
                      fontWeight = FontWeight.Bold,
                      modifier = Modifier.padding(horizontal = 6.dp)
                    )

                    Surface(
                      shape = CircleShape,
                      color = MaterialTheme.colorScheme.surfaceVariant,
                      modifier = Modifier.size(28.dp)
                    ) {
                      IconButton(
                        onClick = {
                          viewModel.updateCartItemQuantity(item.entity.id, item.entity.quantity + 1)
                        },
                        modifier = Modifier.size(28.dp)
                      ) {
                        Icon(Icons.Default.Add, contentDescription = "Increase", modifier = Modifier.size(14.dp))
                      }
                    }

                    IconButton(
                      onClick = { viewModel.removeCartItem(item.entity.id) },
                      modifier = Modifier
                        .size(28.dp)
                        .padding(start = 4.dp)
                    ) {
                      Icon(Icons.Default.DeleteOutline, contentDescription = "Remove", tint = Color(0xFFDC2626), modifier = Modifier.size(18.dp))
                    }
                  }
                }
              }
            }
          }
        }

        // Quick Promo Coupons Pill Row
        item {
          Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
              text = "Tap to apply bookstore coupon:",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              items(listOf("BOOKWORM15", "FREESHIP", "FIRSTREAD")) { promo ->
                val isCurrent = checkoutState.promoCode == promo
                Surface(
                  shape = RoundedCornerShape(14.dp),
                  color = if (isCurrent) Color(0xFFD1FAE5) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                  border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isCurrent) Color(0xFF059669) else Color.Transparent
                  ),
                  modifier = Modifier.clickable { viewModel.applyPromoCode(promo) }
                ) {
                  Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                  ) {
                    Icon(Icons.Default.LocalOffer, contentDescription = null, tint = BookAmber, modifier = Modifier.size(12.dp))
                    Text(
                      text = promo,
                      style = MaterialTheme.typography.labelSmall,
                      fontWeight = FontWeight.Bold,
                      color = if (isCurrent) Color(0xFF065F46) else MaterialTheme.colorScheme.onSurface
                    )
                  }
                }
              }
            }
          }
        }

        // Summary Card
        item {
          Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier.padding(16.dp),
              verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Text(
                text = "Order Calculation",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
              )

              HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

              Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Subtotal", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("$${String.format("%.2f", itemsSubtotal)}")
              }

              if (discount > 0.0) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                  Text("Promo (${checkoutState.promoCode})", color = Color(0xFF059669), fontWeight = FontWeight.Medium)
                  Text("-$${String.format("%.2f", discount)}", color = Color(0xFF059669), fontWeight = FontWeight.Bold)
                }
              }

              Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Estimated Shipping", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(if (shippingCost == 0.0) "FREE" else "$${String.format("%.2f", shippingCost)}")
              }

              Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Estimated Tax (8%)", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("$${String.format("%.2f", discountedSubtotal * 0.08)}")
              }

              HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text("Estimated Total", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(
                  "$${String.format("%.2f", finalTotal)}",
                  style = MaterialTheme.typography.titleLarge,
                  fontWeight = FontWeight.Bold,
                  color = BookNavy
                )
              }
            }
          }
        }
      }

      // Sticky Checkout Action Bar
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
          Column {
            Text(
              text = "$${String.format("%.2f", finalTotal)}",
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold,
              color = BookNavy
            )
            Text(
              text = "Taxes & shipping calculated",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          Button(
            onClick = { viewModel.openCheckout() },
            colors = ButtonDefaults.buttonColors(containerColor = BookNavy),
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
            modifier = Modifier.testTag("proceed_to_checkout_btn")
          ) {
            Text("Proceed to Checkout", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(6.dp))
            Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
          }
        }
      }
    }
  }
}
