package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.DhikrEntity
import com.example.ui.theme.CinzelFontFamily
import com.example.ui.theme.NoorGold
import com.example.ui.theme.NoorMidnightNavy

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CustomDhikrDialog(
    initialDhikr: DhikrEntity? = null,
    onDismiss: () -> Unit,
    onSave: (
        id: String?,
        name: String,
        arabic: String,
        transliteration: String,
        translation: String,
        category: String,
        target: Int,
        isFavorite: Boolean
    ) -> Unit
) {
    var name by remember { mutableStateOf(initialDhikr?.name ?: "") }
    var arabic by remember { mutableStateOf(initialDhikr?.arabic ?: "") }
    var transliteration by remember { mutableStateOf(initialDhikr?.transliteration ?: "") }
    var translation by remember { mutableStateOf(initialDhikr?.translation ?: "") }
    var category by remember { mutableStateOf(initialDhikr?.category ?: "Kustom") }
    var targetText by remember { mutableStateOf(initialDhikr?.target?.toString() ?: "33") }
    var isFavorite by remember { mutableStateOf(initialDhikr?.isFavorite ?: false) }

    var nameError by remember { mutableStateOf(false) }
    var targetError by remember { mutableStateOf(false) }

    val categories = listOf("Kustom", "Tasbih", "Tahmid", "Takbir", "Istighfar", "Tahlil", "Shalawat", "Doa & Dzikir")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (initialDhikr == null) "Tambah Dzikir Kustom" else "Edit Dzikir",
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
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = it.isBlank()
                    },
                    label = { Text("Nama Dzikir *") },
                    isError = nameError,
                    supportingText = { if (nameError) Text("Nama dzikir wajib diisi") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("custom_dhikr_name_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = arabic,
                    onValueChange = { arabic = it },
                    label = { Text("Teks Arab (Opsional, cth: سُبْحَانَ ٱللَّٰهِ)") },
                    singleLine = false,
                    maxLines = 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("custom_dhikr_arabic_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = transliteration,
                    onValueChange = { transliteration = it },
                    label = { Text("Transliterasi Latin (Opsional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = translation,
                    onValueChange = { translation = it },
                    label = { Text("Arti / Terjemahan (Opsional)") },
                    singleLine = false,
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Kategori",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.forEach { cat ->
                        val isSelected = category == cat
                        FilterChip(
                            selected = isSelected,
                            onClick = { category = cat },
                            label = { Text(cat, style = MaterialTheme.typography.bodySmall) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = NoorGold.copy(alpha = 0.2f),
                                selectedLabelColor = NoorGold
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = if (isSelected) NoorGold else NoorGold.copy(alpha = 0.25f)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = targetText,
                        onValueChange = {
                            val digits = it.filter { c -> c.isDigit() }.take(5)
                            targetText = digits
                            targetError = (digits.toIntOrNull() ?: 0) <= 0
                        },
                        label = { Text("Target Hitungan") },
                        isError = targetError,
                        supportingText = { if (targetError) Text("Harus lebih dari 0") },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("custom_dhikr_target_input")
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Favorit",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        IconToggleButton(
                            checked = isFavorite,
                            onCheckedChange = { isFavorite = it }
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = "Tandai Favorit",
                                tint = if (isFavorite) NoorGold else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val validTarget = targetText.toIntOrNull() ?: 33
                    if (name.isBlank()) {
                        nameError = true
                        return@Button
                    }
                    if (validTarget <= 0) {
                        targetError = true
                        return@Button
                    }
                    onSave(
                        initialDhikr?.id,
                        name,
                        arabic,
                        transliteration,
                        translation,
                        category,
                        validTarget,
                        isFavorite
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = NoorGold),
                modifier = Modifier.testTag("save_custom_dhikr_button")
            ) {
                Text("Simpan", color = NoorMidnightNavy, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    )
}
