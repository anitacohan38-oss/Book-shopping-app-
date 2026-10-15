package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.CartItemWithBook
import com.example.ui.theme.BookAmber
import com.example.ui.theme.BookNavy
import com.example.ui.viewmodel.CheckoutState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutSheet(
  cartItems: List<CartItemWithBook>,
  state: CheckoutState,
  onAddressChange: (String) -> Unit,
  onDeliverySpeedChange: (String, Double) -> Unit,
  onPaymentMethodChange: (String) -> Unit,
  onApplyPromo: (String) -> Unit,
  onPlaceOrder: (items: List<CartItemWithBook>, total: Double) -> Unit,
  onDismiss: () -> Unit
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  var promoInput by remember { mutableStateOf("") }
  var isEditingAddress by remember { mutableStateOf(false) }

  // Calculation
  val itemsSubtotal = cartItems.sumOf { it.totalPrice }
  val discount = if (state.appliedDiscountPercent > 0) {
    itemsSubtotal * state.appliedDiscountPercent
  } else {
    state.appliedDiscountFixed.coerceAtMost(itemsSubtotal)
  }
  val discountedSubtotal = (itemsSubtotal - discount).coerceAtLeast(0.0)
  val shippingCost = if (itemsSubtotal >= 35.0 || state.deliverySpeedCost == 0.0) 0.0 else state.deliverySpeedCost
  val estTax = discountedSubtotal * 0.08
  val finalTotal = discountedSubtotal + shippingCost + estTax

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = MaterialTheme.colorScheme.surface,
    modifier = Modifier.testTag("checkout_modal_sheet")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.9f)
        .padding(horizontal = 20.dp)
        .verticalScroll(rememberScrollState())
        .padding(bottom = 36.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Text(
            text = "Checkout",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "${cartItems.sumOf { it.entity.quantity }} books in your order",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = null,
            tint = Color(0xFF059669),
            modifier = Modifier.size(16.dp)
          )
          Text(
            text = "256-bit Secure",
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF059669),
            fontWeight = FontWeight.Bold
          )
        }
      }

      HorizontalDivider()

      // Shipping Address Section
      Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Icon(Icons.Default.LocationOn, contentDescription = null, tint = BookAmber, modifier = Modifier.size(18.dp))
            Text("Shipping Address", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
          }

          Text(
            text = if (isEditingAddress) "Done" else "Change",
            style = MaterialTheme.typography.labelMedium,
            color = BookAmber,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
              .clickable { isEditingAddress = !isEditingAddress }
              .padding(4.dp)
          )
        }

        if (isEditingAddress) {
          OutlinedTextField(
            value = state.shippingAddress,
            onValueChange = onAddressChange,
            label = { Text("Street Address, City, State & Zip") },
            modifier = Modifier.fillMaxWidth().testTag("shipping_address_input")
          )
        } else {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = state.shippingAddress,
              style = MaterialTheme.typography.bodyMedium,
              modifier = Modifier.padding(12.dp)
            )
          }
        }
      }

      // Delivery Speed
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Icon(Icons.Default.LocalShipping, contentDescription = null, tint = BookAmber, modifier = Modifier.size(18.dp))
          Text("Delivery Method", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        }

        // Standard Delivery Card
        val isStandard = state.deliverySpeed.startsWith("Standard")
        Surface(
          shape = RoundedCornerShape(8.dp),
          border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isStandard) BookAmber else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
          ),
          color = if (isStandard) BookAmber.copy(alpha = 0.08f) else Color.Transparent,
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onDeliverySpeedChange("Standard (3-5 days) - Free", 0.0) }
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text("Standard Delivery (3-5 business days)", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
              Text("Carbon neutral bookstore delivery", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text("FREE", fontWeight = FontWeight.Bold, color = Color(0xFF059669))
          }
        }

        // Express Delivery Card
        val isExpress = state.deliverySpeed.startsWith("Express")
        Surface(
          shape = RoundedCornerShape(8.dp),
          border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isExpress) BookAmber else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
          ),
          color = if (isExpress) BookAmber.copy(alpha = 0.08f) else Color.Transparent,
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onDeliverySpeedChange("Express (1-2 days) - $5.99", 5.99) }
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text("Express Delivery (1-2 business days)", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
              Text("Priority courier packaging", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text("$5.99", fontWeight = FontWeight.Bold)
          }
        }
      }

      // Payment Selection
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Icon(Icons.Default.CreditCard, contentDescription = null, tint = BookAmber, modifier = Modifier.size(18.dp))
          Text("Payment Method", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        }

        listOf("Credit Card (•••• 4242)", "Google Pay", "Apple Pay").forEach { method ->
          val isSelected = state.paymentMethod == method
          Surface(
            shape = RoundedCornerShape(8.dp),
            border = androidx.compose.foundation.BorderStroke(
              1.dp,
              if (isSelected) BookNavy else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            ),
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onPaymentMethodChange(method) }
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              RadioButton(selected = isSelected, onClick = { onPaymentMethodChange(method) })
              Spacer(modifier = Modifier.width(8.dp))
              Text(method, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
            }
          }
        }
      }

      // Promo Code Section
      Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Icon(Icons.Default.Sell, contentDescription = null, tint = BookAmber, modifier = Modifier.size(18.dp))
          Text("Promotional Code", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          OutlinedTextField(
            value = promoInput,
            onValueChange = { promoInput = it },
            placeholder = { Text("e.g. BOOKWORM15") },
            singleLine = true,
            modifier = Modifier.weight(1f).testTag("promo_input")
          )
          Button(
            onClick = { onApplyPromo(promoInput) },
            colors = ButtonDefaults.buttonColors(containerColor = BookAmber),
            modifier = Modifier.testTag("apply_promo_btn")
          ) {
            Text("Apply")
          }
        }

        if (state.promoMessage != null) {
          Text(
            text = state.promoMessage,
            style = MaterialTheme.typography.bodySmall,
            color = if (state.promoMessage.contains("applied")) Color(0xFF059669) else MaterialTheme.colorScheme.error,
            fontWeight = FontWeight.SemiBold
          )
        }
      }

      HorizontalDivider()

      // Order Summary Breakdown
      Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
          Text("Items Subtotal", color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text("$${String.format("%.2f", itemsSubtotal)}")
        }

        if (discount > 0.0) {
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Discount (${state.promoCode})", color = Color(0xFF059669), fontWeight = FontWeight.Medium)
            Text("-$${String.format("%.2f", discount)}", color = Color(0xFF059669), fontWeight = FontWeight.Bold)
          }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
          Text("Shipping", color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text(if (shippingCost == 0.0) "FREE" else "$${String.format("%.2f", shippingCost)}")
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
          Text("Estimated Tax (8%)", color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text("$${String.format("%.2f", estTax)}")
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("Order Total", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
          Text(
            "$${String.format("%.2f", finalTotal)}",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = BookNavy
          )
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      // Place Order Button
      Button(
        onClick = { onPlaceOrder(cartItems, finalTotal) },
        enabled = !state.isPlacingOrder && cartItems.isNotEmpty(),
        colors = ButtonDefaults.buttonColors(containerColor = BookNavy),
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .testTag("confirm_place_order_btn"),
        shape = RoundedCornerShape(12.dp)
      ) {
        if (state.isPlacingOrder) {
          CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text("Placing Order...")
        } else {
          Text("Place Order • $${String.format("%.2f", finalTotal)}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}
