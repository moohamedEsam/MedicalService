package com.example.medicalservice.presentation.layout

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.FormatListBulleted
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.common.navigation.Destination
import com.example.common.navigation.NavigationIntent
import com.example.datastore.UserSettings
import com.example.datastore.dataStore
import com.example.functions.handleSnackBarEvent
import com.example.functions.snackbar.SnackBarManager
import com.example.medicalservice.MedicalServiceNavGraph
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
    val userSettings by LocalContext.current.dataStore.data.collectAsStateWithLifecycle(initialValue = UserSettings())
    Log.i("MainLayout", "MedicalServiceLayout: $userSettings")
    MedicalServiceLayout(
        startDestination = if (userSettings.token.isNotEmpty()) Destination.Home.fullRoute else Destination.Login.fullRoute,
        navHostController = navHostController,
        onEvent = viewModel::handleEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicalServiceLayout(
    startDestination: String = Destination.Home.route,
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
        bottomBar = { BottomBar(navHostController = navHostController, onEvent = onEvent) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopBar(
                navHostController = navHostController,
                onSyncClick = { onEvent(MainLayoutScreenEvent.SyncClicked(owner)) })
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

@Composable
fun BottomBar(
    navHostController: NavHostController,
    onEvent: (MainLayoutScreenEvent) -> Unit,
) {
    val navEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute by remember {
        derivedStateOf {
            navEntry?.destination?.route?.takeWhile { it != '/' }
        }
    }
    val userSettings by LocalContext.current.dataStore.data.collectAsStateWithLifecycle(initialValue = UserSettings())
    if (currentRoute == Destination.Login.route || currentRoute == Destination.Register().route) return
    if (userSettings.type != UserType.Donner) return
    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp, start = 16.dp, end = 16.dp)
            .shadow(8.dp, RoundedCornerShape(percent = 15)),
    ) {
        NavigationBarItem(
            selected = currentRoute == Destination.Home.route,
            onClick = { onEvent(MainLayoutScreenEvent.NavigateToHome) },
            icon = { Icon(imageVector = Icons.Outlined.Home, contentDescription = null) },
            label = { Text(text = "Home") },
        )

        NavigationBarItem(
            selected = currentRoute == Destination.DonationsList.route,
            onClick = { onEvent(MainLayoutScreenEvent.NavigateToDonationsList) },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.FormatListBulleted,
                    contentDescription = null
                )
            },
            label = { Text(text = "Donation List") },
        )
        NavigationBarItem(
            selected = currentRoute == Destination.MyDonationsList.route,
            onClick = { onEvent(MainLayoutScreenEvent.NavigateToMyDonations) },
            icon = {
                if (currentRoute != Destination.MyDonationsList.route)
                    Icon(imageVector = Icons.Outlined.FavoriteBorder, contentDescription = null)
                else
                    Icon(imageVector = Icons.Filled.Favorite, contentDescription = null)
            },
            label = { Text(text = "My Donations") },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    navHostController: NavHostController,
    onSyncClick: () -> Unit
) {
    val navEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute by remember {
        derivedStateOf {
            navEntry?.destination?.route?.takeWhile { it != '/' }
        }
    }
    if (currentRoute == Destination.Login.route || currentRoute == Destination.Register().route) return
    CenterAlignedTopAppBar(
        title = { Text(text = currentRoute ?: "") },
        actions = {
            IconButton(onClick = onSyncClick) {
                Icon(imageVector = Icons.Outlined.Sync, contentDescription = null)
            }

            IconButton(onClick = {}) {
                Icon(imageVector = Icons.Outlined.Person, contentDescription = null)
            }
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

@Preview(showBackground = true)
@Composable
private fun MainLayoutPreview() {
    Surface {
        MedicalServiceLayout(
            onEvent = {}
        )
    }
}