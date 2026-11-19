package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.CounterUiState
import com.example.ui.NoorViewModel
import com.example.ui.components.IslamicGeometricDeco
import com.example.ui.components.NoorLightCounter
import com.example.ui.components.TasbihNoorLogoBadge
import com.example.ui.theme.AmiriFontFamily
import com.example.ui.theme.CinzelFontFamily
import com.example.ui.theme.NoorGold
import com.example.ui.theme.NoorGoldBright
import com.example.ui.theme.NoorMidnightNavy
import com.example.ui.theme.NoorSoftGold

@Composable
fun MainCounterScreen(
    viewModel: NoorViewModel,
    counterState: CounterUiState,
    animationIntensity: String,
    keepScreenOn: Boolean,
    onNavigateToSettings: () -> Unit,
    onNavigateToLibrary: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val view = LocalView.current

    // Mode layar tetap menyala while on Digital Tasbih when enabled
    DisposableEffect(keepScreenOn) {
        view.keepScreenOn = keepScreenOn
        onDispose {
            view.keepScreenOn = false
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("main_counter_screen")
    ) {
        IslamicGeometricDeco(
            modifier = Modifier
                .size(340.dp)
                .align(Alignment.TopEnd)
                .offset(x = 50.dp, y = 10.dp),
            color = NoorGold.copy(alpha = 0.05f)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // A. Header: Official TASBIH NOOR Logo, Arabic identity, Keep Screen On, Shortcuts
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TasbihNoorLogoBadge(size = 38.dp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "TASBIH NOOR",
                            fontFamily = CinzelFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            letterSpacing = 1.5.sp,
                            color = NoorGold
                        )
                        Text(
                            text = "Digital Tasbih",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Keep Screen On Toggle
                    Surface(
                        color = if (keepScreenOn) NoorGold.copy(alpha = 0.22f) else Color.Transparent,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(
                            1.dp,
                            if (keepScreenOn) NoorGold else NoorGold.copy(alpha = 0.25f)
                        ),
                        modifier = Modifier
                            .clickable { viewModel.updateKeepScreenOn(!keepScreenOn) }
                            .testTag("keep_screen_on_toggle")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (keepScreenOn) Icons.Filled.LightMode else Icons.Outlined.LightMode,
                                contentDescription = "Layar Tetap Menyala",
                                tint = if (keepScreenOn) NoorGoldBright else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (keepScreenOn) "On" else "Layar",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (keepScreenOn) NoorGoldBright else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    IconButton(
                        onClick = onNavigateToLibrary,
                        modifier = Modifier.testTag("library_shortcut_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.BookmarkBorder,
                            contentDescription = "Pustaka Dzikir",
                            tint = NoorGold
                        )
                    }
                    IconButton(
                        onClick = onNavigateToSettings,
                        modifier = Modifier.testTag("settings_shortcut_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Pengaturan",
                            tint = NoorGold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // B. Active Dhikr Card
            val active = counterState.activeDhikr
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToLibrary() }
                    .testTag("active_dhikr_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, NoorGold.copy(alpha = 0.24f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = NoorGold.copy(alpha = 0.16f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = active?.category ?: "Tasbih",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = NoorGold,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                            if (!active?.source.isNullOrBlank()) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = active?.source ?: "",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1
                                )
                            }
                        }

                        IconButton(
                            onClick = { active?.let { viewModel.toggleFavorite(it) } },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = if (active?.isFavorite == true) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = "Favorit",
                                tint = if (active?.isFavorite == true) NoorGold else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = active?.arabic ?: "سُبْحَانَ ٱللَّٰهِ",
                        fontFamily = AmiriFontFamily,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = NoorGoldBright,
                        textAlign = TextAlign.Center,
                        lineHeight = 44.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = active?.transliteration?.ifBlank { active.name } ?: active?.name ?: "Subhanallah",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )

                    if (!active?.translation.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "“${active?.translation}”",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // C. Noor Light Counter
            NoorLightCounter(
                count = counterState.count,
                target = counterState.target,
                pulseTrigger = counterState.pulseTrigger,
                animationIntensity = animationIntensity
            )

            AnimatedVisibility(
                visible = counterState.isTargetReached,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Surface(
                    color = NoorGold.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, NoorGold),
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = NoorGoldBright,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Target Dzikir Tercapai",
                            style = MaterialTheme.typography.labelMedium,
                            color = NoorGoldBright,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // D. Main Action: Large Circular TAP Button
            val tapInteractionSource = remember { MutableInteractionSource() }
            Box(
                modifier = Modifier
                    .size(116.dp)
                    .shadow(elevation = 14.dp, shape = CircleShape, spotColor = NoorGold)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                NoorGoldBright,
                                NoorGold,
                                NoorSoftGold.copy(alpha = 0.9f)
                            )
                        )
                    )
                    .clickable(
                        interactionSource = tapInteractionSource,
                        indication = ripple(bounded = true, color = Color.White)
                    ) {
                        viewModel.increment()
                    }
                    .testTag("main_tap_button"),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "اضْغَطْ",
                        fontFamily = AmiriFontFamily,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = NoorMidnightNavy
                    )
                    Text(
                        text = "TAP",
                        fontFamily = CinzelFontFamily,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        color = NoorMidnightNavy
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // E. Secondary Controls: Undo, Target, Reset, Save
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = { viewModel.undo() },
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, NoorGold.copy(alpha = 0.35f)),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    enabled = counterState.count > 0,
                    modifier = Modifier.testTag("undo_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Undo,
                        contentDescription = "Urungkan",
                        modifier = Modifier.size(18.dp),
                        tint = if (counterState.count > 0) NoorGold else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "-1",
                        fontWeight = FontWeight.Bold,
                        color = if (counterState.count > 0) NoorGold else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                    )
                }

                OutlinedButton(
                    onClick = { viewModel.showChangeTargetDialog(true) },
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, NoorGold.copy(alpha = 0.35f)),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    modifier = Modifier.testTag("change_target_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Flag,
                        contentDescription = "Target",
                        modifier = Modifier.size(16.dp),
                        tint = NoorGold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${counterState.target}",
                        fontWeight = FontWeight.Bold,
                        color = NoorGold
                    )
                }

                OutlinedButton(
                    onClick = { viewModel.showResetDialog(true) },
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, NoorGold.copy(alpha = 0.35f)),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    modifier = Modifier.testTag("reset_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Atur Ulang",
                        modifier = Modifier.size(18.dp),
                        tint = NoorGold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Reset", color = NoorGold)
                }

                OutlinedButton(
                    onClick = { viewModel.saveCurrentSessionAndContinue(restartCount = true) },
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, NoorGold.copy(alpha = 0.35f)),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    enabled = counterState.count > 0,
                    modifier = Modifier.testTag("save_session_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = "Simpan",
                        modifier = Modifier.size(18.dp),
                        tint = if (counterState.count > 0) NoorGold else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // F. Next Dhikr Navigation Button
            OutlinedButton(
                onClick = { viewModel.nextDhikr() },
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, NoorGold.copy(alpha = 0.35f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .testTag("next_dhikr_button")
            ) {
                Text(
                    text = "Dzikir Berikutnya",
                    color = NoorSoftGold,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                    contentDescription = "Dzikir Berikutnya",
                    tint = NoorGold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Dialog: Reset Confirmation
        if (counterState.showResetConfirmation) {
            AlertDialog(
                onDismissRequest = { viewModel.showResetDialog(false) },
                title = {
                    Text(text = "Atur Ulang Hitungan?", fontFamily = CinzelFontFamily, color = NoorGold)
                },
                text = {
                    Text(
                        text = "Hitungan saat ini (${counterState.count}) akan disimpan ke riwayat dan penghitung kembali ke 0.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                confirmButton = {
                    Button(
                        onClick = { viewModel.confirmReset() },
                        colors = ButtonDefaults.buttonColors(containerColor = NoorGold),
                        modifier = Modifier.testTag("confirm_reset_button")
                    ) {
                        Text("Reset & Simpan", color = NoorMidnightNavy, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.showResetDialog(false) }) {
                        Text("Batal", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            )
        }

        // Dialog: Target Reached Celebration
        if (counterState.showTargetReachedDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissTargetReachedDialog() },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = NoorGoldBright)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Target Dzikir Tercapai", fontFamily = CinzelFontFamily, color = NoorGoldBright)
                    }
                },
                text = {
                    Column {
                        Text(
                            text = "Alhamdulillah! Anda telah menyelesaikan ${counterState.target} kali dzikir ${counterState.activeDhikr?.name ?: ""}.",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Pilih apakah ingin menyimpan sesi ini, melanjutkan dzikir, atau berpindah ke dzikir berikutnya.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { viewModel.saveCurrentSessionAndContinue(restartCount = true) },
                        colors = ButtonDefaults.buttonColors(containerColor = NoorGold),
                        modifier = Modifier.testTag("save_and_finish_target_button")
                    ) {
                        Text("Simpan & Selesai", color = NoorMidnightNavy, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    Row {
                        TextButton(
                            onClick = { viewModel.saveCurrentSessionAndContinue(restartCount = false) }
                        ) {
                            Text("Lanjut", color = NoorSoftGold)
                        }
                        TextButton(
                            onClick = {
                                viewModel.dismissTargetReachedDialog()
                                viewModel.nextDhikr()
                            }
                        ) {
                            Text("Berikutnya", color = NoorGold)
                        }
                    }
                }
            )
        }

        // Dialog: Change Target
        if (counterState.showChangeTargetDialog) {
            ChangeTargetDialog(
                currentTarget = counterState.target,
                onDismiss = { viewModel.showChangeTargetDialog(false) },
                onSelectTarget = { viewModel.setTarget(it) }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChangeTargetDialog(
    currentTarget: Int,
    onDismiss: () -> Unit,
    onSelectTarget: (Int) -> Unit
) {
    var customInput by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Pilih Target Dzikir", fontFamily = CinzelFontFamily, color = NoorGold)
        },
        text = {
            Column {
                Text(
                    text = "Hitungan dzikir saat ini akan tetap dipertahankan.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(14.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(10, 33, 70, 99, 100).forEach { targetOption ->
                        val isSelected = currentTarget == targetOption && customInput.isBlank()
                        OutlinedButton(
                            onClick = { onSelectTarget(targetOption) },
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(
                                if (isSelected) 2.dp else 1.dp,
                                if (isSelected) NoorGold else NoorGold.copy(alpha = 0.3f)
                            ),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isSelected) NoorGold.copy(alpha = 0.15f) else Color.Transparent
                            ),
                            modifier = Modifier.testTag("target_option_$targetOption")
                        ) {
                            Text(
                                text = "$targetOption",
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) NoorGold else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Atau tentukan target kustom:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = customInput,
                    onValueChange = { customInput = it.filter { char -> char.isDigit() }.take(5) },
                    label = { Text("Target Kustom (cth: 500)") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("custom_target_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val parsed = customInput.toIntOrNull()
                    if (parsed != null && parsed > 0) {
                        onSelectTarget(parsed)
                    } else {
                        onDismiss()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = NoorGold),
                modifier = Modifier.testTag("apply_target_button")
            ) {
                Text("Terapkan", color = NoorMidnightNavy, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    )
}
