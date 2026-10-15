package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.model.Book
import com.example.ui.theme.BookNavy
import com.example.ui.theme.StarGold

@Composable
fun ReviewDialog(
  book: Book,
  onDismiss: () -> Unit,
  onSubmit: (name: String, rating: Int, comment: String) -> Unit
) {
  var rating by remember { mutableIntStateOf(5) }
  var reviewerName by remember { mutableStateOf("") }
  var reviewText by remember { mutableStateOf("") }
  var errorText by remember { mutableStateOf<String?>(null) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = "Write a Review",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold
      )
    },
    text = {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Text(
          text = "Reviewing: ${book.title}",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Rating Stars
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically
        ) {
          (1..5).forEach { star ->
            Icon(
              imageVector = if (star <= rating) Icons.Filled.Star else Icons.Outlined.StarBorder,
              contentDescription = "$star stars",
              tint = if (star <= rating) StarGold else MaterialTheme.colorScheme.outline,
              modifier = Modifier
                .size(36.dp)
                .clickable { rating = star }
                .padding(2.dp)
                .testTag("star_rate_$star")
            )
          }
        }

        OutlinedTextField(
          value = reviewerName,
          onValueChange = { reviewerName = it },
          label = { Text("Your Name (or Nickname)") },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("review_name_input")
        )

        OutlinedTextField(
          value = reviewText,
          onValueChange = {
            reviewText = it
            errorText = null
          },
          label = { Text("What did you think of the book?") },
          minLines = 3,
          maxLines = 5,
          isError = errorText != null,
          supportingText = errorText?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("review_comment_input")
        )
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (reviewText.trim().length < 5) {
            errorText = "Please write at least 5 characters."
          } else {
            onSubmit(reviewerName.ifBlank { "Avid Reader" }, rating, reviewText.trim())
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = BookNavy),
        modifier = Modifier.testTag("submit_review_btn")
      ) {
        Text("Submit Review")
      }
    },
    dismissButton = {
      OutlinedButton(onClick = onDismiss) {
        Text("Cancel")
      }
    },
    shape = RoundedCornerShape(16.dp)
  )
}
