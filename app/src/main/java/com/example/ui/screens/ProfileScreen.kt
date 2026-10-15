package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.OrderEntity
import com.example.ui.theme.BookAmber
import com.example.ui.theme.BookNavy
import com.example.ui.theme.BookNavyDark
import com.example.ui.theme.BookSage
import com.example.ui.viewmodel.BookstoreViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProfileScreen(
  viewModel: BookstoreViewModel,
  orders: List<OrderEntity>
) {
  val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(bottom = 80.dp),
    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Header
    item {
      Text(
        text = "Reader Profile",
        style = MaterialTheme.typography.headlineLarge,
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        color = BookNavy
      )
      Text(
        text = "Your library, orders, and Bookverse Club membership",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }

    // Member VIP Card
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth().testTag("member_vip_card"),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .background(
              Brush.linearGradient(
                colors = listOf(BookNavyDark, BookNavy, Color(0xFF1E3A8A))
              )
            )
            .padding(20.dp)
        ) {
          Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                Surface(
                  shape = CircleShape,
                  color = BookAmber.copy(alpha = 0.25f),
                  modifier = Modifier.size(44.dp)
                ) {
                  Box(contentAlignment = Alignment.Center) {
                    Icon(
                      imageVector = Icons.Default.Person,
                      contentDescription = null,
                      tint = BookAmber,
                      modifier = Modifier.size(24.dp)
                    )
                  }
                }
                Column {
                  Text("Anita Cohan", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                  Text("Member since 2024", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.7f))
                }
              }

              Surface(
                shape = RoundedCornerShape(8.dp),
                color = BookAmber
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp),
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                  Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.Black, modifier = Modifier.size(12.dp))
                  Text("GOLD TIER", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color.Black)
                }
              }
            }

            HorizontalDivider(color = Color.White.copy(alpha = 0.2f))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Column {
                Text("Bookverse Points", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.7f))
                Text("1,450 pts", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = BookAmber)
              }

              Column(horizontalAlignment = Alignment.End) {
                Text("Perks Active", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.7f))
                Text("Free Shipping + 10% Off", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = Color.White)
              }
            }
          }
        }
      }
    }

    // 2026 Reading Goal Challenge
    item {
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Icon(Icons.Default.MenuBook, contentDescription = null, tint = BookAmber, modifier = Modifier.size(20.dp))
              Text("2026 Reading Challenge", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            Text("18 / 25 Books", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = BookAmber)
          }

          LinearProgressIndicator(
            progress = { 18f / 25f },
            modifier = Modifier.fillMaxWidth().height(8.dp),
            color = BookAmber,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
          )

          Text(
            text = "You are 3 books ahead of schedule! Keep reading to reach your annual goal.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }

    // Orders History Section Header
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Icon(Icons.Default.Receipt, contentDescription = null, tint = BookNavy, modifier = Modifier.size(20.dp))
          Text(
            text = "Order History",
            style = MaterialTheme.typography.titleLarge,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold
          )
        }

        Text(
          text = "${orders.size} orders",
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    if (orders.isEmpty()) {
      item {
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Icon(Icons.Default.LocalShipping, contentDescription = null, tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(36.dp))
            Text("No Orders Yet", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
            Text(
              "When you purchase books, your order receipts and live tracking details will appear here.",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
          }
        }
      }
    } else {
      items(orders, key = { it.orderId }) { order ->
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier.fillMaxWidth().testTag("order_item_${order.orderId}")
        ) {
          Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = "Order #${order.orderId}",
                  style = MaterialTheme.typography.titleSmall,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = dateFormat.format(Date(order.createdAt)),
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }

              Surface(
                shape = RoundedCornerShape(6.dp),
                color = if (order.status == "Delivered") Color(0xFFD1FAE5) else Color(0xFFFEF3C7)
              ) {
                Text(
                  text = order.status.uppercase(),
                  style = MaterialTheme.typography.labelSmall,
                  fontWeight = FontWeight.Bold,
                  fontSize = 10.sp,
                  color = if (order.status == "Delivered") Color(0xFF065F46) else Color(0xFF92400E),
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
              }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 2.dp))

            Text(
              text = order.itemsJson,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurface
            )

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Tracking: ${order.trackingNumber}",
                style = MaterialTheme.typography.bodySmall,
                color = BookAmber,
                fontWeight = FontWeight.SemiBold
              )

              Text(
                text = "$${String.format("%.2f", order.totalAmount)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = BookNavy
              )
            }
          }
        }
      }
    }

    // Bookstore Guarantee Box
    item {
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Icon(Icons.Default.Shield, contentDescription = null, tint = BookSage, modifier = Modifier.size(24.dp))
          Column {
            Text("100% Genuine Print Guarantee", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
            Text(
              "Direct publisher partnerships with acid-free archival quality paper and sturdy bookcloth bindings.",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }
  }
}
