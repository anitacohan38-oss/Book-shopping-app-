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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Book

@Composable
fun BookCoverView(
  book: Book,
  modifier: Modifier = Modifier,
  height: Dp = 190.dp,
  width: Dp = 130.dp,
  elevation: Dp = 6.dp,
  showSpineEffect: Boolean = true
) {
  val baseColor = Color(book.coverColorHex)
  val darkSpine = baseColor.copy(alpha = 0.85f)
  val highlight = Color.White.copy(alpha = 0.15f)

  Surface(
    modifier = modifier
      .width(width)
      .height(height)
      .shadow(elevation, RoundedCornerShape(topEnd = 6.dp, bottomEnd = 6.dp, topStart = 2.dp, bottomStart = 2.dp)),
    shape = RoundedCornerShape(topEnd = 6.dp, bottomEnd = 6.dp, topStart = 2.dp, bottomStart = 2.dp),
    color = baseColor
  ) {
    Box(modifier = Modifier.fillMaxSize()) {
      // Book cloth gradient texture
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(
            Brush.linearGradient(
              colors = listOf(
                baseColor,
                baseColor.copy(alpha = 0.9f),
                baseColor.copy(red = (baseColor.red * 0.75f).coerceAtLeast(0f))
              )
            )
          )
      )

      // Spine lighting shadow along the left edge
      if (showSpineEffect) {
        Row(modifier = Modifier.fillMaxHeight()) {
          Box(
            modifier = Modifier
              .width(7.dp)
              .fillMaxHeight()
              .background(
                Brush.horizontalGradient(
                  colors = listOf(
                    Color.Black.copy(alpha = 0.45f),
                    Color.Black.copy(alpha = 0.15f),
                    Color.White.copy(alpha = 0.25f),
                    Color.Transparent
                  )
                )
              )
          )
        }
      }

      // Elegant golden bookmark ribbon on top right
      Box(
        modifier = Modifier
          .align(Alignment.TopEnd)
          .padding(end = 12.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Bookmark,
          contentDescription = null,
          tint = Color(0xFFF59E0B),
          modifier = Modifier
            .width(18.dp)
            .height(26.dp)
        )
      }

      // Book cover typography (Embossed look)
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(
            start = if (showSpineEffect) 14.dp else 10.dp,
            end = 10.dp,
            top = 16.dp,
            bottom = 12.dp
          ),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Genre pill
        Surface(
          shape = RoundedCornerShape(4.dp),
          color = Color.White.copy(alpha = 0.2f)
        ) {
          Text(
            text = book.genre.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = Color.White,
            letterSpacing = 1.sp,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }

        // Center Title & Icon
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.fillMaxWidth()
        ) {
          Icon(
            imageVector = Icons.Default.AutoStories,
            contentDescription = null,
            tint = Color(0xFFFDE68A).copy(alpha = 0.8f),
            modifier = Modifier
              .width(22.dp)
              .height(22.dp)
              .padding(bottom = 6.dp)
          )

          Text(
            text = book.title,
            style = MaterialTheme.typography.titleSmall,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 16.sp
          )
        }

        // Author name
        Text(
          text = book.author,
          style = MaterialTheme.typography.labelSmall,
          color = Color(0xFFFDE68A),
          textAlign = TextAlign.Center,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          fontWeight = FontWeight.Medium,
          fontSize = 11.sp
        )
      }
    }
  }
}
