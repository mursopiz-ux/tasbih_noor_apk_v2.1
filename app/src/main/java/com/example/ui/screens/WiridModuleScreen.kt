package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.database.WiridModuleTypes
import com.example.data.database.WiridSourceTypes
import com.example.data.model.DhikrEntity
import com.example.data.model.WiridProgressEntity
import com.example.ui.NoorViewModel
import com.example.ui.theme.AmiriFontFamily
import com.example.ui.theme.CinzelFontFamily
import com.example.ui.theme.NoorChampagneGold
import com.example.ui.theme.NoorGold
import com.example.ui.theme.NoorGoldBright
import com.example.ui.theme.NoorMidnightNavy
import com.example.ui.theme.NoorSoftGold
import com.example.ui.theme.NoorSuccessGreen

@Composable
fun WiridModuleScreen(
    moduleType: String,
    viewModel: NoorViewModel,
    onBack: () -> Unit,
    onOpenInTasbih: (DhikrEntity, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val allDhikrs by viewModel.allDhikrs.collectAsState()
    val progressMap by viewModel.todayWiridProgressMap.collectAsState()
    val settings by viewModel.settings.collectAsState()

    val moduleMeta = remember(moduleType) {
        WIRID_MODULES_META.find { it.moduleType == moduleType }
            ?: WIRID_MODULES_META.first()
    }
    val isAlGhazali = moduleType == WiridModuleTypes.AL_GHAZALI

    val moduleDhikrs = remember(allDhikrs, moduleType) {
        allDhikrs.filter { it.moduleType == moduleType }.sortedBy { it.sortOrder }
    }

    val completedCount = remember(moduleDhikrs, progressMap) {
        moduleDhikrs.count { progressMap[it.id]?.isCompleted == true }
    }
    val moduleProgress = if (moduleDhikrs.isNotEmpty()) {
        completedCount.toFloat() / moduleDhikrs.size.toFloat()
    } else 0f

    var showResetModuleDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .testTag("wirid_module_screen_$moduleType")
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("wirid_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali",
                        tint = NoorGold
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(
                        text = moduleMeta.title,
                        fontFamily = CinzelFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = NoorGold
                    )
                    Text(
                        text = moduleMeta.sourceBadge,
                        style = MaterialTheme.typography.labelSmall,
                        color = NoorSoftGold
                    )
                }
            }

            IconButton(
                onClick = { showResetModuleDialog = true },
                modifier = Modifier.testTag("reset_module_wirid_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Reset Modul",
                    tint = NoorGold
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Overall Module Progress Summary
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, NoorGold.copy(alpha = 0.25f))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isAlGhazali) {
                            "Progres 10 Wirid Imam Al-Ghazali"
                        } else {
                            "Progres Hari Ini"
                        },
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "$completedCount / ${moduleDhikrs.size} Selesai",
                        fontFamily = CinzelFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = NoorGoldBright
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { moduleProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = NoorChampagneGold,
                    trackColor = NoorChampagneGold.copy(alpha = 0.16f)
                )

                // Al-Ghazali Target Switcher: 10, 70, 100 kali
                if (isAlGhazali) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Target Pengulangan (Bidayatul Hidayah):",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(10, 70, 100).forEach { targetOption ->
                            val isSelected = settings.alGhazaliTarget == targetOption
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.setAlGhazaliTarget(targetOption) },
                                label = {
                                    Text(
                                        text = "$targetOption kali",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = NoorGold.copy(alpha = 0.22f),
                                    selectedLabelColor = NoorGoldBright
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = isSelected,
                                    borderColor = if (isSelected) NoorGold else NoorGold.copy(alpha = 0.25f)
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("ghazali_target_$targetOption")
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // List of Wirid Cards
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            items(moduleDhikrs, key = { it.id }) { wirid ->
                val effectiveTarget = if (isAlGhazali) settings.alGhazaliTarget else wirid.target
                val progress = progressMap[wirid.id]
                WiridItemCard(
                    dhikr = wirid,
                    effectiveTarget = effectiveTarget,
                    progress = progress,
                    onTapIncrement = { viewModel.incrementWirid(wirid, effectiveTarget) },
                    onReset = { viewModel.resetSingleWirid(wirid, effectiveTarget) },
                    onOpenTasbih = { onOpenInTasbih(wirid, effectiveTarget) }
                )
            }
        }
    }

    if (showResetModuleDialog) {
        AlertDialog(
            onDismissRequest = { showResetModuleDialog = false },
            title = {
                Text("Reset Progres Modul?", fontFamily = CinzelFontFamily, color = NoorGold)
            },
            text = {
                Text(
                    text = "Atur ulang seluruh hitungan hari ini untuk ${moduleMeta.title}? Catatan sesi yang sudah selesai di Riwayat akan tetap tersimpan.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resetModuleWirids(moduleType)
                        showResetModuleDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NoorGold)
                ) {
                    Text("Ya, Reset", color = NoorMidnightNavy, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetModuleDialog = false }) {
                    Text("Batal", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        )
    }
}

@Composable
private fun WiridItemCard(
    dhikr: DhikrEntity,
    effectiveTarget: Int,
    progress: WiridProgressEntity?,
    onTapIncrement: () -> Unit,
    onReset: () -> Unit,
    onOpenTasbih: () -> Unit
) {
    val currentCount = progress?.currentCount ?: 0
    val isCompleted = currentCount >= effectiveTarget && effectiveTarget > 0
    val progressRatio = if (effectiveTarget > 0) {
        (currentCount.toFloat() / effectiveTarget.toFloat()).coerceIn(0f, 1f)
    } else 0f

    val sourceCategoryLabel = when (dhikr.sourceType) {
        WiridSourceTypes.BIDAYATUL_HIDAYAH -> "Bidāyatul Hidāyah"
        WiridSourceTypes.HADIS -> "Hadis"
        WiridSourceTypes.AMALAN_TAMBAHAN -> "Amalan Tambahan"
        else -> "Umum"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("wirid_item_${dhikr.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCompleted) NoorGold.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            if (isCompleted) 1.5.dp else 1.dp,
            if (isCompleted) NoorGold else NoorGold.copy(alpha = 0.22f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header badges: Source Type + Completion Marker
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = NoorGold.copy(alpha = 0.16f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = sourceCategoryLabel,
                            style = MaterialTheme.typography.labelSmall,
                            color = NoorGold,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${effectiveTarget}x",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                if (isCompleted) {
                    Surface(
                        color = NoorSuccessGreen.copy(alpha = 0.18f),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, NoorSuccessGreen.copy(alpha = 0.6f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Selesai",
                                tint = NoorSuccessGreen,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Selesai",
                                style = MaterialTheme.typography.labelSmall,
                                color = NoorSuccessGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = dhikr.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Full Harakat Arabic Text (RTL)
            Text(
                text = dhikr.arabic,
                fontFamily = AmiriFontFamily,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = NoorGoldBright,
                lineHeight = 40.sp,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (dhikr.transliteration.isNotBlank()) {
                Text(
                    text = dhikr.transliteration,
                    style = MaterialTheme.typography.bodyMedium,
                    color = NoorSoftGold,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            if (dhikr.translation.isNotBlank()) {
                Text(
                    text = "Artinya: “${dhikr.translation}”",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )
            }

            if (dhikr.source.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Sumber: ${dhikr.source}",
                    style = MaterialTheme.typography.labelSmall,
                    color = NoorGold.copy(alpha = 0.85f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress Bar + Numeric Indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LinearProgressIndicator(
                    progress = { progressRatio },
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = if (isCompleted) NoorSuccessGreen else NoorChampagneGold,
                    trackColor = NoorChampagneGold.copy(alpha = 0.16f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "$currentCount / $effectiveTarget",
                    fontFamily = CinzelFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = if (isCompleted) NoorSuccessGreen else NoorGoldBright
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Interactive Counter, Reset & Open in Full Tasbih Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onTapIncrement,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCompleted) NoorGold.copy(alpha = 0.3f) else NoorChampagneGold
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("wirid_tap_button_${dhikr.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = if (isCompleted) NoorGoldBright else NoorMidnightNavy,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isCompleted) "Tambah (+1)" else "Hitung (+1)",
                        color = if (isCompleted) NoorGoldBright else NoorMidnightNavy,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = onOpenTasbih,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, NoorGold.copy(alpha = 0.35f)),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    modifier = Modifier.testTag("wirid_open_tasbih_${dhikr.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.RadioButtonChecked,
                        contentDescription = "Buka di Tasbih",
                        tint = NoorGold,
                        modifier = Modifier.size(18.dp)
                    )
                }

                OutlinedButton(
                    onClick = onReset,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, NoorGold.copy(alpha = 0.35f)),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    enabled = currentCount > 0,
                    modifier = Modifier.testTag("wirid_reset_${dhikr.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reset",
                        tint = if (currentCount > 0) NoorGold else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
