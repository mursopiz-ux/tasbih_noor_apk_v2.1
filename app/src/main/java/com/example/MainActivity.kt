package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.NoorUiEvent
import com.example.ui.NoorViewModel
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.DhikrLibraryScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.MainCounterScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SplashLoadingScreen
import com.example.ui.screens.WiridHubScreen
import com.example.ui.screens.WiridModuleScreen
import com.example.ui.theme.NoorGold
import com.example.ui.theme.NoorTheme
import kotlinx.coroutines.flow.collectLatest

enum class NoorTab(val label: String) {
    DASHBOARD("Beranda"),
    TASBIH("Tasbih"),
    WIRID("Wirid"),
    HISTORY("Riwayat"),
    SETTINGS("Pengaturan")
}

sealed class NoorSubRoute {
    data object None : NoorSubRoute()
    data class WiridModule(val moduleType: String) : NoorSubRoute()
    data object DhikrLibrary : NoorSubRoute()
}

class MainActivity : ComponentActivity() {
    private val viewModel: NoorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val settings by viewModel.settings.collectAsState()
            val systemDark = isSystemInDarkTheme()
            val isDarkTheme = when (settings.themeMode) {
                "DARK" -> true
                "LIGHT" -> false
                else -> systemDark
            }

            var showSplash by remember { mutableStateOf(true) }

            NoorTheme(darkTheme = isDarkTheme) {
                if (showSplash) {
                    SplashLoadingScreen(onFinished = { showSplash = false })
                } else {
                    NoorAppRoot(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun NoorAppRoot(viewModel: NoorViewModel) {
    val context = LocalContext.current
    var currentTab by remember { mutableStateOf(NoorTab.DASHBOARD) }
    var subRoute by remember { mutableStateOf<NoorSubRoute>(NoorSubRoute.None) }

    val counterState by viewModel.counterState.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is NoorUiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is NoorUiEvent.TargetCompleted -> {
                    Toast.makeText(
                        context,
                        "✨ Target tercapai! ${event.target}x ${event.dhikrName}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    // Back navigation handling across sub-screens & tabs
    if (subRoute != NoorSubRoute.None) {
        BackHandler {
            subRoute = NoorSubRoute.None
        }
    } else if (currentTab != NoorTab.DASHBOARD) {
        BackHandler {
            currentTab = NoorTab.DASHBOARD
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
                modifier = Modifier.testTag("main_bottom_nav")
            ) {
                // 1. Beranda (Dashboard)
                NavigationBarItem(
                    selected = currentTab == NoorTab.DASHBOARD && subRoute == NoorSubRoute.None,
                    onClick = {
                        subRoute = NoorSubRoute.None
                        currentTab = NoorTab.DASHBOARD
                    },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == NoorTab.DASHBOARD && subRoute == NoorSubRoute.None) Icons.Filled.Home else Icons.Outlined.Home,
                            contentDescription = "Beranda",
                            tint = if (currentTab == NoorTab.DASHBOARD && subRoute == NoorSubRoute.None) NoorGold else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    label = {
                        Text(
                            text = NoorTab.DASHBOARD.label,
                            fontSize = 10.sp,
                            fontWeight = if (currentTab == NoorTab.DASHBOARD && subRoute == NoorSubRoute.None) FontWeight.Bold else FontWeight.Normal,
                            color = if (currentTab == NoorTab.DASHBOARD && subRoute == NoorSubRoute.None) NoorGold else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = NoorGold.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.testTag("nav_item_dashboard")
                )

                // 2. Digital Tasbih
                NavigationBarItem(
                    selected = currentTab == NoorTab.TASBIH && subRoute == NoorSubRoute.None,
                    onClick = {
                        subRoute = NoorSubRoute.None
                        currentTab = NoorTab.TASBIH
                    },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == NoorTab.TASBIH && subRoute == NoorSubRoute.None) Icons.Filled.RadioButtonChecked else Icons.Outlined.RadioButtonUnchecked,
                            contentDescription = "Tasbih",
                            tint = if (currentTab == NoorTab.TASBIH && subRoute == NoorSubRoute.None) NoorGold else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    label = {
                        Text(
                            text = NoorTab.TASBIH.label,
                            fontSize = 10.sp,
                            fontWeight = if (currentTab == NoorTab.TASBIH && subRoute == NoorSubRoute.None) FontWeight.Bold else FontWeight.Normal,
                            color = if (currentTab == NoorTab.TASBIH && subRoute == NoorSubRoute.None) NoorGold else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = NoorGold.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.testTag("nav_item_counter")
                )

                // 3. Wirid (Al-Ghazali & Additional Modules)
                val isWiridSelected = (currentTab == NoorTab.WIRID && subRoute == NoorSubRoute.None) ||
                    subRoute is NoorSubRoute.WiridModule || subRoute is NoorSubRoute.DhikrLibrary
                NavigationBarItem(
                    selected = isWiridSelected,
                    onClick = {
                        subRoute = NoorSubRoute.None
                        currentTab = NoorTab.WIRID
                    },
                    icon = {
                        Icon(
                            imageVector = if (isWiridSelected) Icons.Filled.MenuBook else Icons.Outlined.MenuBook,
                            contentDescription = "Wirid",
                            tint = if (isWiridSelected) NoorGold else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    label = {
                        Text(
                            text = NoorTab.WIRID.label,
                            fontSize = 10.sp,
                            fontWeight = if (isWiridSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isWiridSelected) NoorGold else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = NoorGold.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.testTag("nav_item_wirid")
                )

                // 4. Riwayat & Statistik
                NavigationBarItem(
                    selected = currentTab == NoorTab.HISTORY && subRoute == NoorSubRoute.None,
                    onClick = {
                        subRoute = NoorSubRoute.None
                        currentTab = NoorTab.HISTORY
                    },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == NoorTab.HISTORY && subRoute == NoorSubRoute.None) Icons.Filled.BarChart else Icons.Outlined.BarChart,
                            contentDescription = "Riwayat",
                            tint = if (currentTab == NoorTab.HISTORY && subRoute == NoorSubRoute.None) NoorGold else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    label = {
                        Text(
                            text = NoorTab.HISTORY.label,
                            fontSize = 10.sp,
                            fontWeight = if (currentTab == NoorTab.HISTORY && subRoute == NoorSubRoute.None) FontWeight.Bold else FontWeight.Normal,
                            color = if (currentTab == NoorTab.HISTORY && subRoute == NoorSubRoute.None) NoorGold else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = NoorGold.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.testTag("nav_item_history")
                )

                // 5. Pengaturan
                NavigationBarItem(
                    selected = currentTab == NoorTab.SETTINGS && subRoute == NoorSubRoute.None,
                    onClick = {
                        subRoute = NoorSubRoute.None
                        currentTab = NoorTab.SETTINGS
                    },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == NoorTab.SETTINGS && subRoute == NoorSubRoute.None) Icons.Filled.Settings else Icons.Outlined.Settings,
                            contentDescription = "Pengaturan",
                            tint = if (currentTab == NoorTab.SETTINGS && subRoute == NoorSubRoute.None) NoorGold else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    label = {
                        Text(
                            text = NoorTab.SETTINGS.label,
                            fontSize = 10.sp,
                            fontWeight = if (currentTab == NoorTab.SETTINGS && subRoute == NoorSubRoute.None) FontWeight.Bold else FontWeight.Normal,
                            color = if (currentTab == NoorTab.SETTINGS && subRoute == NoorSubRoute.None) NoorGold else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = NoorGold.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.testTag("nav_item_settings")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val route = subRoute) {
                is NoorSubRoute.WiridModule -> {
                    WiridModuleScreen(
                        moduleType = route.moduleType,
                        viewModel = viewModel,
                        onBack = { subRoute = NoorSubRoute.None },
                        onOpenInTasbih = { dhikr, effectiveTarget ->
                            viewModel.selectDhikr(dhikr, effectiveTarget)
                            subRoute = NoorSubRoute.None
                            currentTab = NoorTab.TASBIH
                        }
                    )
                }

                is NoorSubRoute.DhikrLibrary -> {
                    DhikrLibraryScreen(
                        viewModel = viewModel,
                        onDhikrSelected = {
                            subRoute = NoorSubRoute.None
                            currentTab = NoorTab.TASBIH
                        },
                        onBack = { subRoute = NoorSubRoute.None }
                    )
                }

                NoorSubRoute.None -> {
                    when (currentTab) {
                        NoorTab.DASHBOARD -> {
                            DashboardScreen(
                                viewModel = viewModel,
                                onOpenTasbih = { currentTab = NoorTab.TASBIH },
                                onOpenWiridModule = { moduleType ->
                                    subRoute = NoorSubRoute.WiridModule(moduleType)
                                },
                                onOpenWiridHub = { currentTab = NoorTab.WIRID },
                                onOpenHistory = { currentTab = NoorTab.HISTORY },
                                onOpenLibrary = { subRoute = NoorSubRoute.DhikrLibrary },
                                onOpenSettings = { currentTab = NoorTab.SETTINGS }
                            )
                        }

                        NoorTab.TASBIH -> {
                            MainCounterScreen(
                                viewModel = viewModel,
                                counterState = counterState,
                                animationIntensity = settings.animationIntensity,
                                keepScreenOn = settings.keepScreenOn,
                                onNavigateToSettings = { currentTab = NoorTab.SETTINGS },
                                onNavigateToLibrary = { subRoute = NoorSubRoute.DhikrLibrary }
                            )
                        }

                        NoorTab.WIRID -> {
                            WiridHubScreen(
                                viewModel = viewModel,
                                onOpenModule = { moduleType ->
                                    subRoute = NoorSubRoute.WiridModule(moduleType)
                                },
                                onOpenLibrary = { subRoute = NoorSubRoute.DhikrLibrary }
                            )
                        }

                        NoorTab.HISTORY -> {
                            HistoryScreen(viewModel = viewModel)
                        }

                        NoorTab.SETTINGS -> {
                            SettingsScreen(
                                viewModel = viewModel,
                                onOpenLibrary = { subRoute = NoorSubRoute.DhikrLibrary }
                            )
                        }
                    }
                }
            }
        }
    }
}
