package com.example.medicalservice.presentation.layout

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.FormatListBulleted
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.functions.handleSnackBarEvent
import com.example.functions.snackbar.BaseSnackBarManager
import com.example.functions.snackbar.SnackBarManager
import com.example.medicalservice.MedicalServiceNavGraph
import com.example.medicalservice.presentation.donationList.DonationListScreenRoute
import com.example.medicalservice.presentation.donationList.navigateToDonationListScreen
import com.example.medicalservice.presentation.home.navigation.HomeScreenRoute
import com.example.medicalservice.presentation.home.navigation.navigateToHomeScreen
import com.example.model.app.UserType
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.get

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicalServiceLayout(
    userType: UserType = UserType.Donner,
    startDestination: String = HomeScreenRoute,
    navHostController: NavHostController = rememberNavController(),
    snackBarManager: SnackBarManager = get(),
) {
    val snackbarHostState by remember {
        mutableStateOf(SnackbarHostState())
    }
    LaunchedEffect(key1 = Unit) {
        snackBarManager.getReceiverChannel().collectLatest {
            snackbarHostState.handleSnackBarEvent(it)
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomBar(navHostController = navHostController) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = { TopBar(navHostController = navHostController) },
        content = {
            MedicalServiceNavGraph(
                startDestination = startDestination,
                navHostController = navHostController,
                paddingValues = it,
                userType = userType,
            )
        },
    )
}

@Composable
fun BottomBar(navHostController: NavHostController) {
    val navEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute by remember {
        derivedStateOf {
            navEntry?.destination?.route?.takeWhile { it != '/' }
        }
    }
    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp, start = 16.dp, end = 16.dp)
            .shadow(8.dp, RoundedCornerShape(percent = 15))
    ) {
        NavigationBarItem(
            selected = currentRoute == HomeScreenRoute,
            onClick = navHostController::navigateToHomeScreen,
            icon = { Icon(imageVector = Icons.Outlined.Home, contentDescription = null) },
            label = { Text(text = "Home") },
        )

        NavigationBarItem(
            selected = currentRoute == DonationListScreenRoute,
            onClick = navHostController::navigateToDonationListScreen,
            icon = {
                Icon(
                    imageVector = Icons.Outlined.FormatListBulleted,
                    contentDescription = null
                )
            },
            label = { Text(text = "Donation List") },
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(imageVector = Icons.Outlined.FavoriteBorder, contentDescription = null) },
            label = { Text(text = "My Donations") },
        )

        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(imageVector = Icons.Outlined.Search, contentDescription = null) },
            label = { Text(text = "Search") },
        )

//        NavigationBarItem(
//            selected = false,
//            onClick = { },
//            icon = { Icon(imageVector = Icons.Outlined.Person, contentDescription = null) },
//            label = { Text(text = "Account") },
//        )


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navHostController: NavHostController) {
    val navEntry by navHostController.currentBackStackEntryAsState()

    val currentRoute by remember {
        derivedStateOf {
            navEntry?.destination?.route?.takeWhile { it != '/' }
        }
    }
    CenterAlignedTopAppBar(
        title = { Text(text = currentRoute ?: "") },
        actions = {
            IconButton(onClick = { }) {
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
            snackBarManager = BaseSnackBarManager()
        )
    }
}