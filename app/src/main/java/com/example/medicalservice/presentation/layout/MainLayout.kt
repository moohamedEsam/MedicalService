package com.example.medicalservice.presentation.layout

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.FormatListBulleted
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.common.navigation.Destination
import com.example.common.navigation.NavigationIntent
import com.example.functions.handleSnackBarEvent
import com.example.functions.snackbar.SnackBarManager
import com.example.medicalservice.MedicalServiceNavGraph
import com.example.medicalservice.R
import com.example.model.app.user.UserType
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.androidx.compose.get
import org.koin.androidx.compose.koinViewModel

@Composable
fun MedicalServiceLayout(
    navHostController: NavHostController = rememberNavController(),
    viewModel: MainLayoutViewModel = koinViewModel(),
) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()
    val currentUser by viewModel.user.collectAsStateWithLifecycle()
    MedicalServiceLayout(
        startDestination = if (isLoggedIn) Destination.Home.route else Destination.Login.route,
        navHostController = navHostController,
        onEvent = viewModel::handleEvent,
        userType = currentUser.type
    )
}

@Composable
fun MedicalServiceLayout(
    userType: UserType,
    startDestination: String,
    navHostController: NavHostController = rememberNavController(),
    snackBarManager: SnackBarManager = get(),
    navigationChannel: Channel<NavigationIntent> = get(),
    onEvent: (MainLayoutScreenEvent) -> Unit
) {
    val snackbarHostState by remember {
        mutableStateOf(SnackbarHostState())
    }
    ObserveSnackBarEvents(snackBarManager, snackbarHostState)
    ObserveNavigationChannel(navHostController, navigationChannel)
    val owner = LocalLifecycleOwner.current
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomBar(
                navHostController = navHostController,
                onEvent = onEvent,
                userType = userType
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopBar(
                navHostController = navHostController,
                onSyncClick = { onEvent(MainLayoutScreenEvent.SyncClicked(owner)) },
                onLogoutClick = { onEvent(MainLayoutScreenEvent.OnLogoutClick) },
                onSettingsClick = { onEvent(MainLayoutScreenEvent.OnSettingsClick) },
            )
        },
        content = {
            MedicalServiceNavGraph(
                startDestination = startDestination,
                navHostController = navHostController,
                paddingValues = it,
            )
        },
    )
}

@Composable
private fun ObserveNavigationChannel(
    navHostController: NavHostController,
    navigationChannel: Channel<NavigationIntent>
) {
    val activity = LocalContext.current as? Activity
    LaunchedEffect(key1 = activity, key2 = navHostController, key3 = navigationChannel) {
        navigationChannel.receiveAsFlow().collect { intent ->
            when (intent) {
                is NavigationIntent.Back -> {
                    navHostController.previousBackStackEntry?.arguments?.let { bundle ->
                        intent.arguments.forEach { pair ->
                            bundle.putString(pair.first, pair.second)
                        }
                    }
                    navHostController.popBackStack()
                }

                is NavigationIntent.To -> navHostController.navigate(intent.destination) {
                    launchSingleTop = intent.singleTop
                    if (intent.popUpTo != null)
                        popUpTo(intent.popUpTo!!) { inclusive = intent.inclusive }
                }
            }
        }
    }
}

@Composable
private fun ObserveSnackBarEvents(
    snackBarManager: SnackBarManager,
    snackbarHostState: SnackbarHostState
) {
    LaunchedEffect(key1 = Unit) {
        snackBarManager.getReceiverChannel().collectLatest {
            snackbarHostState.handleSnackBarEvent(it)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BottomBar(
    userType: UserType,
    navHostController: NavHostController,
    onEvent: (MainLayoutScreenEvent) -> Unit,
) {
    val navEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute by remember {
        derivedStateOf {
            navEntry?.destination?.route?.takeWhile { it != '/' }
        }
    }
    if (WindowInsets.isImeVisible) return
    if (!shouldShowLayoutBars(currentRoute)) return
    if (userType == UserType.Doctor) return
    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(bottom = 32.dp, start = 16.dp, end = 16.dp)
            .shadow(8.dp, RoundedCornerShape(percent = 15)),
    ) {
        NavigationBarItem(
            selected = currentRoute == Destination.Home.route,
            onClick = { onEvent(MainLayoutScreenEvent.OnHomeClick) },
            icon = { Icon(imageVector = Icons.Outlined.Home, contentDescription = null) },
            label = { Text(text = "Home") },
        )
        if (userType == UserType.Donner) {
            NavigationBarItem(
                selected = currentRoute == Destination.DonationsList.route,
                onClick = { onEvent(MainLayoutScreenEvent.OnDonationsClick) },
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.FormatListBulleted,
                        contentDescription = null
                    )
                },
                label = { Text(text = "Donations") },
            )
            NavigationBarItem(
                selected = currentRoute == Destination.MyDonationsList.route,
                onClick = { onEvent(MainLayoutScreenEvent.OnSavedClick) },
                icon = {
                    if (currentRoute != Destination.MyDonationsList.route)
                        Icon(imageVector = Icons.Outlined.FavoriteBorder, contentDescription = null)
                    else
                        Icon(imageVector = Icons.Filled.Favorite, contentDescription = null)
                },
                label = { Text(text = "Saved") },
            )
        } else {
            NavigationBarItem(
                selected = currentRoute == Destination.DiagnosisForm.route,
                onClick = { onEvent(MainLayoutScreenEvent.OnDiagnosisClick) },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.diagnosis),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text(text = "diagnosis") },
            )
            NavigationBarItem(
                selected = currentRoute == Destination.UploadPrescription.route,
                onClick = { onEvent(MainLayoutScreenEvent.OnUploadClick) },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.upload),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text(text = "upload") },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    navHostController: NavHostController,
    onSyncClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val navEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute by remember {
        derivedStateOf {
            navEntry?.destination?.route?.takeWhile { it != '/' }
        }
    }
    if (!shouldShowLayoutBars(currentRoute)) return
    TopAppBar(
        title = {
            Text(text = currentRoute ?: "",)
        },
        actions = {
            IconButton(onClick = onSyncClick) {
                Icon(imageVector = Icons.Outlined.Sync, contentDescription = null)
            }

            IconButton(onClick = {}) {
                Icon(imageVector = Icons.Outlined.Person, contentDescription = null)
            }

            MoreIconButton(onSettingsClick = onSettingsClick, onLogoutClick = onLogoutClick)
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        navigationIcon = {
            IconButton(
                onClick = {
                    if (navHostController.previousBackStackEntry != null)
                        navHostController.popBackStack()
                },
            ) {
                Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = null)
            }
        }
    )
}

@Composable
private fun MoreIconButton(
    onSettingsClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(imageVector = Icons.Outlined.MoreVert, contentDescription = null)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .padding(end = 16.dp)
        ) {
            DropdownMenuItem(
                onClick = {
                    onSettingsClick()
                    expanded = false
                },
                text = { Text(text = "Settings") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = null
                    )
                }
            )
            DropdownMenuItem(
                onClick = {
                    onLogoutClick()
                    expanded = false
                },
                text = { Text(text = "Logout") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Logout,
                        contentDescription = null
                    )
                }
            )
        }
    }
}

@Composable
private fun shouldShowLayoutBars(currentRoute: String?) =
    currentRoute !in listOf(
        Destination.Login.route,
        Destination.Register().route,
        Destination.Map.route,
        Destination.Settings.route,
        null
    )