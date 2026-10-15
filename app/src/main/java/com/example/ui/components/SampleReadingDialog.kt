package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Book
import com.example.ui.theme.WarmPaper
import com.example.ui.theme.WarmPaperVariant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SampleReadingDialog(
  book: Book,
  fontSizeSp: Int,
  onFontSizeChange: (Int) -> Unit,
  onDismiss: () -> Unit
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = Color(0xFFFBF8F3), // Warm reading parchment paper
    contentColor = Color(0xFF1E1B18),
    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
    modifier = Modifier.testTag("sample_reader_sheet")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.88f)
        .padding(horizontal = 20.dp)
    ) {
      // Top header with title and Font Size toggles
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Icon(
            imageVector = Icons.Default.AutoStories,
            contentDescription = null,
            tint = Color(0xFF854D0E),
            modifier = Modifier.size(20.dp)
          )
          Column {
            Text(
              text = "Sample Excerpt",
              style = MaterialTheme.typography.labelSmall,
              color = Color(0xFF854D0E),
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp
            )
            Text(
              text = book.title,
              style = MaterialTheme.typography.titleSmall,
              fontFamily = FontFamily.Serif,
              fontWeight = FontWeight.Bold,
              maxLines = 1
            )
          }
        }

        // Font size control & Close button
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          // Smaller font
          IconButton(
            onClick = { if (fontSizeSp > 13) onFontSizeChange(fontSizeSp - 2) },
            modifier = Modifier.size(36.dp)
          ) {
            Text("A-", fontWeight = FontWeight.Bold, fontSize = 13.sp)
          }

          // Larger font
          IconButton(
            onClick = { if (fontSizeSp < 24) onFontSizeChange(fontSizeSp + 2) },
            modifier = Modifier.size(36.dp)
          ) {
            Text("A+", fontWeight = FontWeight.Bold, fontSize = 16.sp)
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.size(36.dp)
          ) {
            Icon(Icons.Default.Close, contentDescription = "Close preview")
          }
        }
      }

      HorizontalDivider(
        modifier = Modifier.padding(vertical = 12.dp),
        color = Color(0xFFE2D9CE)
      )

      // Book sample content with scroll
      val scrollState = rememberScrollState()
      Column(
        modifier = Modifier
          .fillMaxSize()
          .verticalScroll(scrollState)
          .padding(bottom = 32.dp)
      ) {
        // Author & Chapter title
        Text(
          text = "By " + book.author,
          style = MaterialTheme.typography.bodyMedium,
          fontStyle = FontStyle.Italic,
          color = Color(0xFF78716C),
          modifier = Modifier.fillMaxWidth(),
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        Surface(
          shape = RoundedCornerShape(8.dp),
          color = WarmPaperVariant,
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = book.sampleChapterTitle,
            style = MaterialTheme.typography.titleMedium,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF451A03),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp)
          )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Excerpt text
        Text(
          text = book.sampleExcerpt,
          style = MaterialTheme.typography.bodyLarge.copy(
            fontSize = fontSizeSp.sp,
            lineHeight = (fontSizeSp * 1.6f).sp
          ),
          fontFamily = FontFamily.Serif,
          color = Color(0xFF292524),
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(30.dp))

        // End of excerpt note
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFEF3C7), RoundedCornerShape(8.dp))
            .padding(14.dp)
        ) {
          Text(
            text = "✨ You have reached the end of this free sample. Order ${book.title} to read the complete ${book.pages}-page edition!",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF92400E),
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
          )
        }
      }
    }
  }
}
