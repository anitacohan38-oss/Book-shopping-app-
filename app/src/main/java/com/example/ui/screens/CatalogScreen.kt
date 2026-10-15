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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Book
import com.example.model.BookFormat
import com.example.ui.components.BookCard
import com.example.ui.theme.BookAmber
import com.example.ui.theme.BookNavy
import com.example.ui.viewmodel.BookstoreViewModel
import com.example.ui.viewmodel.SortOption

@Composable
fun CatalogScreen(
  viewModel: BookstoreViewModel,
  books: List<Book>,
  wishlistIds: List<String>,
  onNavigateToBook: (String) -> Unit
) {
  var isSortMenuExpanded by remember { mutableStateOf(false) }
  val genres = listOf("All", "Fiction", "Sci-Fi", "Fantasy", "Mystery", "Self-Help", "Biography")
  val formats = listOf<BookFormat?>(null, BookFormat.HARDCOVER, BookFormat.PAPERBACK, BookFormat.AUDIOBOOK, BookFormat.EBOOK)

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(bottom = 80.dp)
  ) {
    // Top Bar
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
      Text(
        text = "Book Catalog",
        style = MaterialTheme.typography.headlineLarge,
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        color = BookNavy
      )
      Text(
        text = "Browse our full bookstore collection",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Search bar
      OutlinedTextField(
        value = viewModel.searchQuery.value,
        onValueChange = { viewModel.searchQuery.value = it },
        placeholder = { Text("Search title, author, or keyword...") },
        leadingIcon = {
          Icon(Icons.Default.Search, contentDescription = null, tint = BookAmber)
        },
        trailingIcon = {
          if (viewModel.searchQuery.value.isNotEmpty()) {
            IconButton(onClick = { viewModel.searchQuery.value = "" }) {
              Icon(Icons.Default.Clear, contentDescription = "Clear search")
            }
          }
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = MaterialTheme.colorScheme.surface,
          unfocusedContainerColor = MaterialTheme.colorScheme.surface,
          focusedBorderColor = BookAmber
        ),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("catalog_search_field")
      )
    }

    // Genre Filter Row
    LazyRow(
      contentPadding = PaddingValues(horizontal = 20.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier.padding(bottom = 6.dp)
    ) {
      items(genres) { genre ->
        val isSelected = viewModel.selectedGenre.value == genre
        FilterChip(
          selected = isSelected,
          onClick = { viewModel.selectedGenre.value = genre },
          label = { Text(genre) },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = BookNavy,
            selectedLabelColor = Color.White
          )
        )
      }
    }

    // Format & Sort Row
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 4.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Books Count
      Text(
        text = "${books.size} books found",
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      // Sort Dropdown Button
      Box {
        OutlinedButton(
          onClick = { isSortMenuExpanded = true },
          shape = RoundedCornerShape(8.dp),
          contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
          modifier = Modifier.height(34.dp).testTag("sort_menu_btn")
        ) {
          Icon(Icons.Default.Sort, contentDescription = null, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(viewModel.selectedSort.value.label, style = MaterialTheme.typography.labelSmall)
        }

        DropdownMenu(
          expanded = isSortMenuExpanded,
          onDismissRequest = { isSortMenuExpanded = false }
        ) {
          SortOption.entries.forEach { option ->
            DropdownMenuItem(
              text = { Text(option.label) },
              onClick = {
                viewModel.selectedSort.value = option
                isSortMenuExpanded = false
              }
            )
          }
        }
      }
    }

    // Format Filter Row
    LazyRow(
      contentPadding = PaddingValues(horizontal = 20.dp),
      horizontalArrangement = Arrangement.spacedBy(6.dp),
      modifier = Modifier.padding(bottom = 10.dp)
    ) {
      items(formats) { format ->
        val isSelected = viewModel.selectedFormatFilter.value == format
        val label = format?.label ?: "All Formats"
        Surface(
          shape = RoundedCornerShape(16.dp),
          color = if (isSelected) BookAmber.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
          border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isSelected) BookAmber else Color.Transparent
          ),
          modifier = Modifier
            .clickable { viewModel.selectedFormatFilter.value = format }
        ) {
          Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) BookAmber else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
          )
        }
      }
    }

    // Book Grid or Empty State
    if (books.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(32.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Icon(
            imageVector = Icons.Default.FilterList,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outline,
            modifier = Modifier.size(48.dp)
          )
          Text(
            text = "No books found",
            style = MaterialTheme.typography.titleLarge,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "Try clearing your search query or choosing a different genre filter.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
          )
          Button(
            onClick = {
              viewModel.searchQuery.value = ""
              viewModel.selectedGenre.value = "All"
              viewModel.selectedFormatFilter.value = null
            },
            colors = ButtonDefaults.buttonColors(containerColor = BookNavy)
          ) {
            Text("Reset All Filters")
          }
        }
      }
    } else {
      LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize().testTag("books_catalog_grid")
      ) {
        items(books) { book ->
          BookCard(
            book = book,
            isWishlisted = wishlistIds.contains(book.id),
            onClick = { onNavigateToBook(book.id) },
            onWishlistClick = { viewModel.toggleWishlist(book.id) },
            onQuickAddToCart = { format -> viewModel.addToCart(book, format) }
          )
        }
      }
    }
  }
}
