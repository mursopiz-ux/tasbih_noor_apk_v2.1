package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.database.WiridModuleTypes
import com.example.data.model.SessionEntity
import com.example.ui.NoorViewModel
import com.example.ui.theme.AmiriFontFamily
import com.example.ui.theme.CinzelFontFamily
import com.example.ui.theme.NoorChampagneGold
import com.example.ui.theme.NoorGold
import com.example.ui.theme.NoorGoldBright
import com.example.ui.theme.NoorSoftGold
import com.example.util.DateUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
    viewModel: NoorViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val allSessions by viewModel.allSessions.collectAsState()
    val stats by viewModel.stats.collectAsState()

    var selectedFilter by remember { mutableStateOf("Semua") }
    var sessionToDelete by remember { mutableStateOf<SessionEntity?>(null) }

    val filterOptions = listOf("Semua", "Hari Ini", "7 Hari", "Selesai", "Al-Ghazali", "Tasbih")

    val filteredSessions = remember(allSessions, selectedFilter) {
        val startOfToday = DateUtils.getStartOfTodayMillis()
        val sevenDaysAgo = DateUtils.getStartOfWeekMillis()

        when (selectedFilter) {
            "Hari Ini" -> allSessions.filter { it.timestamp >= startOfToday }
            "7 Hari" -> allSessions.filter { it.timestamp >= sevenDaysAgo }
            "Selesai" -> allSessions.filter { it.isCompleted }
            "Al-Ghazali" -> allSessions.filter { it.moduleType == WiridModuleTypes.AL_GHAZALI }
            "Tasbih" -> allSessions.filter { it.moduleType == "TASBIH" || it.moduleType == WiridModuleTypes.GENERAL }
            else -> allSessions
        }
    }

    // Aggregate counts per YYYY-MM-DD for Consistency Calendar & 7-Day Progress Chart
    val dailyTotalsMap = remember(allSessions) {
        val map = mutableMapOf<String, Int>()
        for (s in allSessions) {
            val key = DateUtils.timestampToDateKey(s.timestamp)
            map[key] = (map[key] ?: 0) + s.count
        }
        map
    }

    val last7Days = remember(dailyTotalsMap) {
        DateUtils.getLastNDaysKeys(7).map { (key, label) ->
            Triple(key, label, dailyTotalsMap[key] ?: 0)
        }
    }

    val last28Days = remember(dailyTotalsMap) {
        DateUtils.getLastNDaysKeys(28).map { (key, _) ->
            key to (dailyTotalsMap[key] ?: 0)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("history_screen")
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "RIWAYAT & STATISTIK",
                            fontFamily = CinzelFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 21.sp,
                            color = NoorGold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Konsistensi & catatan ibadah harian",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(
                        onClick = {
                            val summaryText = buildString {
                                appendLine("✨ TASBIH NOOR V2.01 — Ringkasan Dzikir ✨")
                                appendLine("Hari Ini: ${stats.todayDhikr}")
                                appendLine("Minggu Ini: ${stats.weeklyDhikr}")
                                appendLine("Bulan Ini: ${stats.monthlyDhikr}")
                                appendLine("Total Sepanjang Waktu: ${stats.totalDhikr}")
                                appendLine("Sesi / Wirid Selesai: ${stats.completedSessions}")
                                appendLine("\nA Peaceful Digital Dhikr Experience.")
                            }
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, "Statistik Dzikir TASBIH NOOR")
                                putExtra(Intent.EXTRA_TEXT, summaryText)
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Bagikan Statistik Dzikir"))
                        },
                        modifier = Modifier.testTag("share_history_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Bagikan Ringkasan",
                            tint = NoorGold
                        )
                    }
                }
            }

            // 4 Statistics Cards: Harian, Mingguan, Bulanan, Total
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatCard(
                            title = "Harian",
                            value = "${stats.todayDhikr}",
                            subtitle = "Hari Ini",
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "Mingguan",
                            value = "${stats.weeklyDhikr}",
                            subtitle = "7 Hari Terakhir",
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatCard(
                            title = "Bulanan",
                            value = "${stats.monthlyDhikr}",
                            subtitle = "Bulan Ini",
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "Total Bacaan",
                            value = "${stats.totalDhikr}",
                            subtitle = "${stats.completedSessions} Wirid/Sesi Selesai",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // 7-Day Progress Chart
            item {
                ProgressBarChartCard(last7Days = last7Days)
            }

            // 28-Day Consistency Calendar
            item {
                ConsistencyCalendarCard(last28Days = last28Days)
            }

            // Filter Chips
            item {
                Column {
                    Text(
                        text = "RIWAYAT WIRID & SESI",
                        fontFamily = CinzelFontFamily,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = NoorGold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filterOptions) { filter ->
                            val isSelected = selectedFilter == filter
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedFilter = filter },
                                label = { Text(filter, fontSize = 12.sp) },
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
                }
            }

            // Session History Items or Empty State
            if (filteredSessions.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, NoorGold.copy(alpha = 0.18f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Belum ada riwayat tersimpan",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Mulailah berdzikir di Digital Tasbih atau Modul Wirid untuk mencatat progres Anda.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(filteredSessions, key = { it.id }) { session ->
                    SessionItemCard(
                        session = session,
                        onDelete = { sessionToDelete = session }
                    )
                }
            }
        }

        // Delete Session Confirmation Dialog
        sessionToDelete?.let { session ->
            AlertDialog(
                onDismissRequest = { sessionToDelete = null },
                title = {
                    Text(text = "Hapus Riwayat Sesi?", fontFamily = CinzelFontFamily, color = NoorGold)
                },
                text = {
                    Text(
                        text = "Apakah Anda yakin ingin menghapus catatan dzikir '${session.dhikrName}' (${session.count}x)?",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.deleteSession(session)
                            sessionToDelete = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier.testTag("confirm_delete_session_button")
                    ) {
                        Text("Hapus", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { sessionToDelete = null }) {
                        Text("Batal", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            )
        }
    }
}

@Composable
private fun ProgressBarChartCard(
    last7Days: List<Triple<String, String, Int>>
) {
    val maxCount = (last7Days.maxOfOrNull { it.third } ?: 0).coerceAtLeast(10)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("progress_chart_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, NoorGold.copy(alpha = 0.22f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Grafik Progres 7 Hari",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Maks: $maxCount",
                    style = MaterialTheme.typography.labelSmall,
                    color = NoorSoftGold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
            ) {
                val barCount = last7Days.size.coerceAtLeast(1)
                val slotWidth = size.width / barCount
                val barWidth = (slotWidth * 0.48f).coerceAtMost(28.dp.toPx())

                // Subtle horizontal baseline
                drawLine(
                    color = NoorGold.copy(alpha = 0.2f),
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 1.dp.toPx()
                )

                last7Days.forEachIndexed { index, (_, _, count) ->
                    val ratio = (count.toFloat() / maxCount.toFloat()).coerceIn(0f, 1f)
                    val barHeight = if (count > 0) (size.height * ratio).coerceAtLeast(6.dp.toPx()) else 4.dp.toPx()
                    val left = index * slotWidth + (slotWidth - barWidth) / 2f
                    val top = size.height - barHeight

                    drawRoundRect(
                        brush = Brush.verticalGradient(
                            colors = if (count > 0) {
                                listOf(NoorGoldBright, NoorChampagneGold)
                            } else {
                                listOf(NoorGold.copy(alpha = 0.16f), NoorGold.copy(alpha = 0.1f))
                            }
                        ),
                        topLeft = Offset(left, top),
                        size = Size(barWidth, barHeight),
                        cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                last7Days.forEach { (_, label, count) ->
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (count > 0) "$count" else "0",
                            fontSize = 10.sp,
                            fontWeight = if (count > 0) FontWeight.Bold else FontWeight.Normal,
                            color = if (count > 0) NoorGoldBright else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = label,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ConsistencyCalendarCard(
    last28Days: List<Pair<String, Int>>
) {
    val activeDays = last28Days.count { it.second > 0 }
    val maxDayCount = (last28Days.maxOfOrNull { it.second } ?: 1).coerceAtLeast(1)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("consistency_calendar_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, NoorGold.copy(alpha = 0.22f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Kalender Konsistensi",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "28 hari terakhir",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    color = NoorGold.copy(alpha = 0.18f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "$activeDays / 28 Hari Aktif",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = NoorGoldBright,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 4 rows x 7 columns grid
            val weeks = last28Days.chunked(7)
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                weeks.forEach { week ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        week.forEach { (dateKey, count) ->
                            val dayLabel = dateKey.takeLast(2)
                            val alpha = when {
                                count <= 0 -> 0.12f
                                count < maxDayCount * 0.35f -> 0.45f
                                count < maxDayCount * 0.7f -> 0.72f
                                else -> 1f
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1.15f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (count > 0) NoorChampagneGold.copy(alpha = alpha)
                                        else MaterialTheme.colorScheme.surfaceVariant
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = dayLabel,
                                    fontSize = 11.sp,
                                    fontWeight = if (count > 0) FontWeight.Bold else FontWeight.Normal,
                                    color = if (count > 0) Color(0xFF07152B) else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, NoorGold.copy(alpha = 0.22f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontFamily = CinzelFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = NoorGoldBright
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = NoorSoftGold.copy(alpha = 0.8f),
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun SessionItemCard(
    session: SessionEntity,
    onDelete: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID")) }
    val formattedDate = remember(session.timestamp) { dateFormat.format(Date(session.timestamp)) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("session_card_${session.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, NoorGold.copy(alpha = 0.18f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = session.dhikrName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (session.arabic.isNotBlank()) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = session.arabic,
                            fontFamily = AmiriFontFamily,
                            fontSize = 16.sp,
                            color = NoorSoftGold,
                            maxLines = 1
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = if (session.isCompleted) NoorGold.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(
                        0.8.dp,
                        if (session.isCompleted) NoorGold else NoorGold.copy(alpha = 0.2f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (session.isCompleted) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Selesai",
                                tint = NoorGoldBright,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        Text(
                            text = "${session.count} / ${session.target}",
                            fontFamily = CinzelFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (session.isCompleted) NoorGoldBright else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Hapus Riwayat",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
