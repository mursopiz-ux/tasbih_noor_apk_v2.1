package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DhikrEntity
import com.example.ui.NoorViewModel
import com.example.ui.theme.AmiriFontFamily
import com.example.ui.theme.CinzelFontFamily
import com.example.ui.theme.NoorGold
import com.example.ui.theme.NoorGoldBright
import com.example.ui.theme.NoorMidnightNavy
import com.example.ui.theme.NoorSoftGold

@Composable
fun DhikrLibraryScreen(
    viewModel: NoorViewModel,
    onDhikrSelected: (DhikrEntity) -> Unit,
    onBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val allDhikrs by viewModel.allDhikrs.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val counterState by viewModel.counterState.collectAsState()

    var showCustomDialog by remember { mutableStateOf(false) }
    var dhikrToEdit by remember { mutableStateOf<DhikrEntity?>(null) }
    var dhikrToDelete by remember { mutableStateOf<DhikrEntity?>(null) }

    val categories = listOf(
        "Semua", "Favorit", "Kustom", "Wirid Al-Ghazali", "Tasbih", "Tahmid", "Takbir",
        "Istighfar", "Tahlil", "Shalawat", "Doa & Dzikir"
    )

    val filteredDhikrs = allDhikrs.filter { dhikr ->
        val matchesQuery = if (searchQuery.isBlank()) {
            true
        } else {
            val q = searchQuery.trim().lowercase()
            dhikr.name.lowercase().contains(q) ||
                dhikr.transliteration.lowercase().contains(q) ||
                dhikr.translation.lowercase().contains(q) ||
                dhikr.arabic.contains(q) ||
                dhikr.source.lowercase().contains(q)
        }

        val matchesCategory = when (selectedCategory) {
            "Semua" -> true
            "Favorit" -> dhikr.isFavorite
            "Kustom" -> dhikr.isCustom
            else -> dhikr.category.equals(selectedCategory, ignoreCase = true)
        }

        matchesQuery && matchesCategory
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("dhikr_library_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    if (onBack != null) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier.testTag("library_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Kembali",
                                tint = NoorGold
                            )
                        }
                    }
                    Column {
                        Text(
                            text = "PUSTAKA DZIKIR",
                            fontFamily = CinzelFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = NoorGold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Pilih, cari, atau buat dzikir kustom",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Button(
                    onClick = {
                        dhikrToEdit = null
                        showCustomDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NoorGold),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("add_custom_dhikr_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = NoorMidnightNavy,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Kustom",
                        color = NoorMidnightNavy,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = { Text("Cari dzikir, arti, atau sumber...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Cari",
                        tint = NoorGold
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setSearchQuery("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Hapus",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                shape = RoundedCornerShape(14.dp),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("dhikr_search_field")
            )

            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { cat ->
                    val isSelected = selectedCategory == cat
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.setSelectedCategory(cat) },
                        label = { Text(cat, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NoorGold.copy(alpha = 0.2f),
                            selectedLabelColor = NoorGold
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = if (isSelected) NoorGold else NoorGold.copy(alpha = 0.2f)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (filteredDhikrs.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Tidak ada dzikir ditemukan",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Coba kata kunci lain atau tambahkan dzikir kustom baru.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 90.dp)
                ) {
                    items(filteredDhikrs, key = { it.id }) { dhikr ->
                        val isActive = counterState.activeDhikr?.id == dhikr.id
                        DhikrItemCard(
                            dhikr = dhikr,
                            isActive = isActive,
                            onSelect = {
                                viewModel.selectDhikr(dhikr)
                                onDhikrSelected(dhikr)
                            },
                            onToggleFavorite = { viewModel.toggleFavorite(dhikr) },
                            onEdit = {
                                dhikrToEdit = dhikr
                                showCustomDialog = true
                            },
                            onDelete = { dhikrToDelete = dhikr }
                        )
                    }
                }
            }
        }

        if (showCustomDialog) {
            CustomDhikrDialog(
                initialDhikr = dhikrToEdit,
                onDismiss = {
                    showCustomDialog = false
                    dhikrToEdit = null
                },
                onSave = { id, name, arabic, translit, trans, cat, target, isFav ->
                    viewModel.saveCustomDhikr(
                        id = id,
                        name = name,
                        arabic = arabic,
                        transliteration = translit,
                        translation = trans,
                        category = cat,
                        target = target,
                        isFavorite = isFav
                    )
                    showCustomDialog = false
                    dhikrToEdit = null
                }
            )
        }

        dhikrToDelete?.let { dhikr ->
            AlertDialog(
                onDismissRequest = { dhikrToDelete = null },
                title = {
                    Text(
                        text = "Hapus Dzikir Kustom?",
                        fontFamily = CinzelFontFamily,
                        color = NoorGold
                    )
                },
                text = {
                    Text(
                        text = "Apakah Anda yakin ingin menghapus '${dhikr.name}'? Tindakan ini tidak dapat dibatalkan.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.deleteCustomDhikr(dhikr)
                            dhikrToDelete = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier.testTag("confirm_delete_dhikr_button")
                    ) {
                        Text("Hapus", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { dhikrToDelete = null }) {
                        Text("Batal", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            )
        }
    }
}

@Composable
fun DhikrItemCard(
    dhikr: DhikrEntity,
    isActive: Boolean,
    onSelect: () -> Unit,
    onToggleFavorite: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .testTag("dhikr_card_${dhikr.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) NoorGold.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            if (isActive) 1.5.dp else 1.dp,
            if (isActive) NoorGold else NoorGold.copy(alpha = 0.2f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = NoorGold.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = dhikr.category,
                            style = MaterialTheme.typography.labelSmall,
                            color = NoorGold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                    if (dhikr.isCustom) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = NoorSoftGold.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Kustom",
                                style = MaterialTheme.typography.labelSmall,
                                color = NoorSoftGold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Target: ${dhikr.target}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }

                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (dhikr.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = "Favorit",
                            tint = if (dhikr.isFavorite) NoorGold else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (dhikr.arabic.isNotBlank()) {
                Text(
                    text = dhikr.arabic,
                    fontFamily = AmiriFontFamily,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    color = NoorGoldBright,
                    lineHeight = 34.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            Text(
                text = dhikr.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (dhikr.transliteration.isNotBlank() && dhikr.transliteration != dhikr.name) {
                Text(
                    text = dhikr.transliteration,
                    style = MaterialTheme.typography.bodySmall,
                    color = NoorSoftGold
                )
            }

            if (dhikr.translation.isNotBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "“${dhikr.translation}”",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (dhikr.source.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Sumber: ${dhikr.source}",
                    style = MaterialTheme.typography.labelSmall,
                    color = NoorGold.copy(alpha = 0.8f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (dhikr.isCustom) {
                    Row {
                        IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = NoorGold,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Hapus",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                if (isActive) {
                    Surface(
                        color = NoorGold.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, NoorGold)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = NoorGold,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Sedang Digunakan",
                                style = MaterialTheme.typography.labelSmall,
                                color = NoorGold,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                } else {
                    OutlinedButton(
                        onClick = onSelect,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, NoorGold.copy(alpha = 0.4f)),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Gunakan",
                            style = MaterialTheme.typography.labelSmall,
                            color = NoorGold,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
