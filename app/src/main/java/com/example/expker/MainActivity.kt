package com.example.expker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.expker.data.AppDatabase
import com.example.expker.data.CategoryRepository
import com.example.expker.data.TransactionRepository
import com.example.expker.ui.components.CustomBottomNavigation
import com.example.expker.ui.screens.*
import com.example.expker.ui.theme.ExpkerTheme
import com.example.expker.ui.viewmodel.TransactionViewModel
import kotlinx.coroutines.flow.first

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val profileViewModel: com.example.expker.ui.viewmodel.ProfileViewModel = viewModel()
            val userProfile by profileViewModel.userProfile.collectAsState()

            ExpkerTheme(
                themeMode = userProfile.themeMode,
                accentColor = userProfile.accentColor
            ) {
                MainScreen(applicationContext)
            }
        }
    }
}

@Composable
fun MainScreen(context: android.content.Context) {
    val navController = rememberNavController()
    var selectedTab by remember { mutableIntStateOf(0) }
    
    val database = remember { AppDatabase.getDatabase(context) }
    val transactionRepository = remember { TransactionRepository(database.transactionDao(), database.categoryDao()) }
    val categoryRepository = remember { CategoryRepository(database.categoryDao()) }
    
    val transactionViewModel: TransactionViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TransactionViewModel(transactionRepository, categoryRepository) as T
            }
        }
    )

    // Pre-populate database with SampleData if empty
    LaunchedEffect(Unit) {
        val categories = database.categoryDao().getAllCategories().first()
        if (categories.isEmpty()) {
            com.example.expker.model.SampleData.categories.forEach { category ->
                database.categoryDao().insertCategory(
                    com.example.expker.model.Category(
                        id = category.id,
                        name = category.name,
                        iconName = com.example.expker.model.IconHelper.getNameByIcon(category.icon),
                        colorHex = category.color.value.toLong()
                    )
                )
            }
            com.example.expker.model.SampleData.transactions.forEach { sampleTransaction ->
                database.transactionDao().insertTransaction(
                    com.example.expker.model.Transaction(
                        id = sampleTransaction.id,
                        title = sampleTransaction.title,
                        amount = sampleTransaction.amount,
                        date = sampleTransaction.date,
                        categoryId = sampleTransaction.category.id,
                        type = sampleTransaction.type,
                        tax = sampleTransaction.tax,
                        timestamp = sampleTransaction.timestamp
                    )
                )
            }
        }
    }

    val profileViewModel: com.example.expker.ui.viewmodel.ProfileViewModel = viewModel()
    val userProfile by profileViewModel.userProfile.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            CustomBottomNavigation(
                selectedTab = selectedTab,
                profilePictureUri = userProfile.profilePictureUri,
                onTabSelected = { index ->
                    selectedTab = index
                    when (index) {
                        0 -> navController.navigate("home") {
                            popUpTo("home") { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                        1 -> navController.navigate("statistics") {
                            popUpTo("home") { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                        2 -> navController.navigate("transactions") {
                            popUpTo("home") { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                        3 -> navController.navigate("profile") {
                            popUpTo("home") { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                onFabClick = { navController.navigate("add_transaction") }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(navController = navController, startDestination = "home") {
                composable("home") { HomeScreen(navController = navController, transactionViewModel = transactionViewModel) }
                composable("statistics") { StatisticsScreen(transactionViewModel = transactionViewModel) }
                composable("transactions") { TransactionsScreen(navController = navController, transactionViewModel = transactionViewModel) }
                composable("profile") { AccountProfileScreen(navController) }
                composable("support") { SupportScreen(navController) }
                composable("appearance_settings") { AppearanceSettingsScreen(navController) }
                composable(
                    "add_transaction?transactionId={transactionId}",
                    arguments = listOf(navArgument("transactionId") {
                        nullable = true
                        defaultValue = null
                    })
                ) { backStackEntry ->
                    val transactionId = backStackEntry.arguments?.getString("transactionId")
                    AddTransactionScreen(navController, transactionViewModel, transactionId)
                }
            }
        }
    }
}
