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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.database.WiridModuleTypes
import com.example.ui.NoorViewModel
import com.example.ui.theme.CinzelFontFamily
import com.example.ui.theme.NoorGold
import com.example.ui.theme.NoorGoldBright
import com.example.ui.theme.NoorSoftGold

data class WiridModuleMeta(
    val moduleType: String,
    val title: String,
    val subtitle: String,
    val sourceBadge: String,
    val icon: ImageVector
)

val WIRID_MODULES_META = listOf(
    WiridModuleMeta(
        moduleType = WiridModuleTypes.AL_GHAZALI,
        title = "Wirid Harian Imam Al-Ghazali",
        subtitle = "10 Kalimat Zikir • Target 10 / 70 / 100 kali",
        sourceBadge = "Kitab Bidāyatul Hidāyah",
        icon = Icons.Default.MenuBook
    ),
    WiridModuleMeta(
        moduleType = WiridModuleTypes.WIRID_PAGI,
        title = "Wirid Pagi",
        subtitle = "Penjagaan & syukur di awal hari",
        sourceBadge = "Hadis Shahih",
        icon = Icons.Default.WbSunny
    ),
    WiridModuleMeta(
        moduleType = WiridModuleTypes.WIRID_PETANG,
        title = "Wirid Petang",
        subtitle = "Ketenangan & perlindungan sore hari",
        sourceBadge = "Hadis Shahih",
        icon = Icons.Default.WbTwilight
    ),
    WiridModuleMeta(
        moduleType = WiridModuleTypes.SETELAH_SALAT,
        title = "Zikir Setelah Salat Fardu",
        subtitle = "Istighfar, Tasbih 33x, Tahmid 33x, Takbir 33x",
        sourceBadge = "Hadis Shahih",
        icon = Icons.Default.Mosque
    ),
    WiridModuleMeta(
        moduleType = WiridModuleTypes.ISTIGHFAR,
        title = "Istighfar",
        subtitle = "Mohon ampunan & penyucian hati",
        sourceBadge = "Hadis Shahih",
        icon = Icons.Default.Spa
    ),
    WiridModuleMeta(
        moduleType = WiridModuleTypes.SELAWAT,
        title = "Selawat",
        subtitle = "Shalawat Ibrahimiyah & shalawat ringkas",
        sourceBadge = "Hadis & Amalan Ulama",
        icon = Icons.Default.Favorite
    ),
    WiridModuleMeta(
        moduleType = WiridModuleTypes.TASBIH_TAHMID_TAKBIR,
        title = "Tasbih, Tahmid, Takbir",
        subtitle = "Bacaan pemuliaan utama 33 / 33 / 34",
        sourceBadge = "Hadis Shahih",
        icon = Icons.Default.Repeat
    ),
    WiridModuleMeta(
        moduleType = WiridModuleTypes.WIRID_MALAM,
        title = "Wirid Malam",
        subtitle = "Dzikir terjaga malam & ketenteraman",
        sourceBadge = "Hadis & Amalan Tambahan",
        icon = Icons.Default.NightsStay
    ),
    WiridModuleMeta(
        moduleType = WiridModuleTypes.DOA_TIDUR,
        title = "Doa Sebelum Tidur",
        subtitle = "Doa tidur, perlindungan & Tasbih Fathimah",
        sourceBadge = "Hadis Shahih",
        icon = Icons.Default.Bedtime
    )
)

@Composable
fun WiridHubScreen(
    viewModel: NoorViewModel,
    onOpenModule: (String) -> Unit,
    onOpenLibrary: () -> Unit,
    modifier: Modifier = Modifier
) {
    val allDhikrs by viewModel.allDhikrs.collectAsState()
    val progressMap by viewModel.todayWiridProgressMap.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .testTag("wirid_hub_screen")
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "MODUL WIRID & DZIKIR",
                    fontFamily = CinzelFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = NoorGold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Kitab Bidāyatul Hidāyah, Hadis & Amalan Harian",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            OutlinedButton(
                onClick = onOpenLibrary,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, NoorGold.copy(alpha = 0.4f)),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                modifier = Modifier.testTag("wirid_to_library_button")
            ) {
                Icon(Icons.Default.BookmarkBorder, contentDescription = null, tint = NoorGold, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Kustom", color = NoorGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            items(WIRID_MODULES_META, key = { it.moduleType }) { module ->
                val itemsInModule = remember(allDhikrs, module.moduleType) {
                    allDhikrs.filter { it.moduleType == module.moduleType }
                }
                val completedInModule = remember(itemsInModule, progressMap) {
                    itemsInModule.count { progressMap[it.id]?.isCompleted == true }
                }
                val isFeatured = module.moduleType == WiridModuleTypes.AL_GHAZALI

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenModule(module.moduleType) }
                        .testTag("wirid_module_card_${module.moduleType}"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isFeatured) NoorGold.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(
                        if (isFeatured) 1.5.dp else 1.dp,
                        if (isFeatured) NoorGold else NoorGold.copy(alpha = 0.22f)
                    )
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
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = module.icon,
                                        contentDescription = module.title,
                                        tint = NoorGoldBright,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Surface(
                                    color = NoorGold.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = module.sourceBadge,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 10.sp,
                                        color = NoorSoftGold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = module.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = module.subtitle,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "$completedInModule/${itemsInModule.size}",
                                fontFamily = CinzelFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = NoorGoldBright
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Buka Modul",
                                tint = NoorGold,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
