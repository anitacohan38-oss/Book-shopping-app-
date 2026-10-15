package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.OrderEntity
import com.example.ui.theme.BookAmber
import com.example.ui.theme.BookNavy

@Composable
fun OrderSuccessDialog(
  order: OrderEntity,
  onDismiss: () -> Unit,
  onViewOrders: () -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    text = {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // Success Circle
        Box(
          modifier = Modifier
            .size(64.dp)
            .background(Color(0xFF10B981).copy(alpha = 0.15f), CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Order Confirmed",
            tint = Color(0xFF059669),
            modifier = Modifier.size(36.dp)
          )
        }

        Text(
          text = "Thank You for Your Order!",
          style = MaterialTheme.typography.titleLarge,
          fontFamily = FontFamily.Serif,
          fontWeight = FontWeight.Bold,
          textAlign = TextAlign.Center
        )

        Text(
          text = "We're packaging your literary treasures with care. An order confirmation has been logged to your Bookverse account.",
          style = MaterialTheme.typography.bodyMedium,
          textAlign = TextAlign.Center,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Surface(
          shape = RoundedCornerShape(12.dp),
          color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Order Number", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text(order.orderId, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
            }

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Tracking ID", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text(order.trackingNumber, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = BookAmber)
            }

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Estimated Delivery", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text("3 business days", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 2.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Total Paid", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
              Text("$${String.format("%.2f", order.totalAmount)}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = BookNavy)
            }
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = onViewOrders,
        colors = ButtonDefaults.buttonColors(containerColor = BookNavy),
        modifier = Modifier.testTag("view_orders_btn")
      ) {
        Text("View in Orders")
      }
    },
    dismissButton = {
      OutlinedButton(onClick = onDismiss) {
        Text("Keep Reading")
      }
    },
    shape = RoundedCornerShape(20.dp)
  )
}
