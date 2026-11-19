package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Brightness3
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.database.WiridModuleTypes
import com.example.ui.NoorViewModel
import com.example.ui.components.IslamicGeometricDeco
import com.example.ui.components.TasbihNoorLogoBadge
import com.example.ui.theme.CinzelFontFamily
import com.example.ui.theme.NoorChampagneGold
import com.example.ui.theme.NoorDeepRoyalBlue
import com.example.ui.theme.NoorGold
import com.example.ui.theme.NoorGoldBright
import com.example.ui.theme.NoorMidnightNavy
import com.example.ui.theme.NoorSoftGold
import com.example.util.DateUtils

@Composable
fun DashboardScreen(
    viewModel: NoorViewModel,
    onOpenTasbih: () -> Unit,
    onOpenWiridModule: (String) -> Unit,
    onOpenWiridHub: () -> Unit,
    onOpenHistory: () -> Unit,
    onOpenLibrary: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val counterState by viewModel.counterState.collectAsState()
    val todayDhikr by viewModel.todayDhikr.collectAsState()
    val completedSessions by viewModel.completedSessionsCount.collectAsState()
    val wiridProgressMap by viewModel.todayWiridProgressMap.collectAsState()
    val allDhikrs by viewModel.allDhikrs.collectAsState()

    val hijriDate = remember { DateUtils.getFormattedHijriDate() }
    val gregorianDate = remember { DateUtils.getFormattedGregorianDate() }

    val alGhazaliItems = remember(allDhikrs) {
        allDhikrs.filter { it.moduleType == WiridModuleTypes.AL_GHAZALI }
    }
    val alGhazaliCompletedCount = remember(alGhazaliItems, wiridProgressMap) {
        alGhazaliItems.count { wiridProgressMap[it.id]?.isCompleted == true }
    }

    val liveTodayTotal = todayDhikr + counterState.count
    val dailyGoal = 100
    val dailyProgress = (liveTodayTotal.toFloat() / dailyGoal.toFloat()).coerceIn(0f, 1f)

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("dashboard_screen")
    ) {
        IslamicGeometricDeco(
            modifier = Modifier
                .size(280.dp)
                .align(Alignment.TopEnd)
                .offset(x = 60.dp, y = (-20).dp),
            color = NoorGold.copy(alpha = 0.06f)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(14.dp))

            // 1. Logo TASBIH NOOR + Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TasbihNoorLogoBadge(size = 48.dp, withGlow = true)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "TASBIH NOOR",
                            fontFamily = CinzelFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            letterSpacing = 1.6.sp,
                            color = NoorGold
                        )
                        Text(
                            text = "A Peaceful Digital Dhikr Experience.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(
                    onClick = onOpenSettings,
                    modifier = Modifier.testTag("dashboard_settings_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Pengaturan",
                        tint = NoorGold
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 2. Assalamu'alaikum + 3. Tanggal Hijriah dan Masehi
            Text(
                text = "Assalamu'alaikum",
                fontFamily = CinzelFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(6.dp))

            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, NoorGold.copy(alpha = 0.22f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = NoorGold,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$hijriDate  •  $gregorianDate",
                        style = MaterialTheme.typography.bodySmall,
                        color = NoorSoftGold,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Ringkasan Zikir Hari Ini
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("dashboard_today_summary_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, NoorGold.copy(alpha = 0.25f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Ringkasan Zikir Hari Ini",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "$completedSessions sesi selesai tercatat",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "$liveTodayTotal",
                            fontFamily = CinzelFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp,
                            color = NoorGoldBright
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { dailyProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = NoorChampagneGold,
                        trackColor = NoorChampagneGold.copy(alpha = 0.16f)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Bacaan aktif: ${counterState.activeDhikr?.name ?: "Subhanallah"} (${counterState.count}/${counterState.target})",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 5. Tombol Digital Tasbih (Primary Prominent CTA)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenTasbih() }
                    .testTag("dashboard_tasbih_button"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                border = BorderStroke(1.5.dp, NoorChampagneGold)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    NoorDeepRoyalBlue,
                                    NoorChampagneGold.copy(alpha = 0.25f)
                                )
                            )
                        )
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = NoorChampagneGold,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.RadioButtonChecked,
                                        contentDescription = null,
                                        tint = NoorMidnightNavy,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = "Digital Tasbih",
                                    fontFamily = CinzelFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Color.White
                                )
                                Text(
                                    text = "Mulai berdzikir dengan Noor Light Counter",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = NoorSoftGold
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Buka Digital Tasbih",
                            tint = NoorGoldBright
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 6. Menu Wirid Imam Al-Ghazali (Featured Card)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenWiridModule(WiridModuleTypes.AL_GHAZALI) }
                    .testTag("dashboard_al_ghazali_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, NoorGold.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = NoorGold.copy(alpha = 0.16f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.size(42.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.MenuBook,
                                    contentDescription = null,
                                    tint = NoorGold,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Wirid Imam Al-Ghazali",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = NoorGoldBright
                            )
                            Text(
                                text = "10 Wirid Harian • Kitab Bidāyatul Hidāyah",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Surface(
                        color = NoorGold.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "$alGhazaliCompletedCount/10",
                            fontFamily = CinzelFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = NoorGoldBright,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 7. Wirid Pagi & 8. Wirid Petang
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DashboardQuickTile(
                    title = "Wirid Pagi",
                    subtitle = "Sunnah Pagi",
                    icon = Icons.Default.WbSunny,
                    onClick = { onOpenWiridModule(WiridModuleTypes.WIRID_PAGI) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("dashboard_wirid_pagi_card")
                )
                DashboardQuickTile(
                    title = "Wirid Petang",
                    subtitle = "Sunnah Petang",
                    icon = Icons.Default.WbTwilight,
                    onClick = { onOpenWiridModule(WiridModuleTypes.WIRID_PETANG) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("dashboard_wirid_petang_card")
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 9. Setelah Salat & 10. Riwayat dan Statistik
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DashboardQuickTile(
                    title = "Setelah Salat",
                    subtitle = "Dzikir Fardu",
                    icon = Icons.Default.Mosque,
                    onClick = { onOpenWiridModule(WiridModuleTypes.SETELAH_SALAT) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("dashboard_setelah_salat_card")
                )
                DashboardQuickTile(
                    title = "Riwayat & Statistik",
                    subtitle = "Grafik & Kalender",
                    icon = Icons.Default.BarChart,
                    onClick = onOpenHistory,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("dashboard_history_card")
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Additional Quick Shortcuts: All Wirid Modules & Custom Library
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onOpenWiridHub,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = NoorGold
                    ),
                    border = BorderStroke(1.dp, NoorGold.copy(alpha = 0.25f)),
                    shape = RoundedCornerShape(14.dp),
                    contentPadding = PaddingValues(vertical = 12.dp, horizontal = 12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Semua Wirid", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = onOpenLibrary,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = NoorGold
                    ),
                    border = BorderStroke(1.dp, NoorGold.copy(alpha = 0.25f)),
                    shape = RoundedCornerShape(14.dp),
                    contentPadding = PaddingValues(vertical = 12.dp, horizontal = 12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.BookmarkBorder, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Pustaka & Kustom", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun DashboardQuickTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, NoorGold.copy(alpha = 0.22f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Surface(
                color = NoorGold.copy(alpha = 0.14f),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = NoorGold,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
