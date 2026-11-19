package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.NoorViewModel
import com.example.ui.theme.CinzelFontFamily
import com.example.ui.theme.NoorGold
import com.example.ui.theme.NoorMidnightNavy
import kotlinx.coroutines.launch

@Composable
fun BackupRestoreDialog(
    viewModel: NoorViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var selectedTab by remember { mutableIntStateOf(0) }

    var exportedJson by remember { mutableStateOf("") }
    var importInputJson by remember { mutableStateOf("") }
    var isLoadingExport by remember { mutableStateOf(false) }
    var restoreStatusMessage by remember { mutableStateOf<String?>(null) }
    var isError by remember { mutableStateOf(false) }

    LaunchedEffect(selectedTab) {
        if (selectedTab == 0 && exportedJson.isEmpty()) {
            isLoadingExport = true
            try {
                exportedJson = viewModel.exportBackupJson()
            } catch (e: Exception) {
                exportedJson = "Gagal membuat cadangan: ${e.message}"
            } finally {
                isLoadingExport = false
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Cadangan & Pemulihan Data",
                fontFamily = CinzelFontFamily,
                color = NoorGold,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.Transparent,
                    contentColor = NoorGold
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Ekspor", fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Pulihkan", fontWeight = FontWeight.Bold) }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (selectedTab == 0) {
                    Text(
                        text = "Simpan data dzikir kustom, progres wirid, riwayat sesi, dan pengaturan Anda dalam format JSON mandiri.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    if (isLoadingExport) {
                        CircularProgressIndicator(
                            color = NoorGold,
                            modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp)
                        )
                    } else {
                        OutlinedTextField(
                            value = exportedJson,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Data Cadangan JSON") },
                            maxLines = 8,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("exported_json_field")
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            OutlinedButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Tasbih Noor Backup", exportedJson)
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "Cadangan disalin ke papan klip", Toast.LENGTH_SHORT).show()
                                },
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, NoorGold.copy(alpha = 0.4f)),
                                modifier = Modifier.testTag("copy_backup_button")
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = null, tint = NoorGold, modifier = Modifier.height(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Salin", color = NoorGold, fontSize = 12.sp)
                            }

                            Button(
                                onClick = {
                                    val sendIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, exportedJson)
                                        type = "text/plain"
                                    }
                                    val shareIntent = Intent.createChooser(sendIntent, "Bagikan Cadangan TASBIH NOOR")
                                    context.startActivity(shareIntent)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = NoorGold),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("share_backup_button")
                            ) {
                                Icon(Icons.Default.Share, contentDescription = null, tint = NoorMidnightNavy, modifier = Modifier.height(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Bagikan", color = NoorMidnightNavy, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                } else {
                    Text(
                        text = "Tempel teks cadangan JSON TASBIH NOOR atau NOOR COUNTER untuk memulihkan data Anda.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = importInputJson,
                        onValueChange = {
                            importInputJson = it
                            restoreStatusMessage = null
                        },
                        placeholder = { Text("Tempel data JSON di sini...") },
                        maxLines = 8,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("import_json_field")
                    )

                    restoreStatusMessage?.let { msg ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = msg,
                            color = if (isError) MaterialTheme.colorScheme.error else NoorGold,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clipData = clipboard.primaryClip
                                if (clipData != null && clipData.itemCount > 0) {
                                    importInputJson = clipData.getItemAt(0).text.toString()
                                } else {
                                    Toast.makeText(context, "Papan klip kosong", Toast.LENGTH_SHORT).show()
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, NoorGold.copy(alpha = 0.4f))
                        ) {
                            Text("Tempel", color = NoorGold, fontSize = 12.sp)
                        }

                        Button(
                            onClick = {
                                if (importInputJson.isBlank()) {
                                    restoreStatusMessage = "Silakan tempel data JSON terlebih dahulu"
                                    isError = true
                                    return@Button
                                }
                                coroutineScope.launch {
                                    val result = viewModel.restoreBackupJson(importInputJson)
                                    if (result.isSuccess) {
                                        restoreStatusMessage = "Berhasil memulihkan ${result.getOrNull() ?: 0} data!"
                                        isError = false
                                    } else {
                                        restoreStatusMessage = "Gagal memulihkan: ${result.exceptionOrNull()?.message}"
                                        isError = true
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NoorGold),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("apply_restore_button")
                        ) {
                            Icon(Icons.Default.FileUpload, contentDescription = null, tint = NoorMidnightNavy, modifier = Modifier.height(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Pulihkan", color = NoorMidnightNavy, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Tutup", color = NoorGold)
            }
        }
    )
}
