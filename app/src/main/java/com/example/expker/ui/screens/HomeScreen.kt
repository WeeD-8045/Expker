package com.example.expker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.expker.ui.components.MainBalanceCard
import com.example.expker.ui.components.TransactionRow
import com.example.expker.ui.theme.*
import com.example.expker.ui.viewmodel.ProfileViewModel
import com.example.expker.ui.viewmodel.TransactionViewModel

@Composable
fun HomeScreen(
    navController: NavController = rememberNavController(),
    profileViewModel: ProfileViewModel = viewModel(),
    transactionViewModel: TransactionViewModel = viewModel()
) {
    val userProfile by profileViewModel.userProfile.collectAsState()
    val transactions by transactionViewModel.transactions.collectAsState()
    val totalBalance by transactionViewModel.totalBalance.collectAsState()
    val totalIncome by transactionViewModel.totalIncome.collectAsState()
    val totalExpenses by transactionViewModel.totalExpenses.collectAsState()

    val searchQuery by transactionViewModel.searchQuery.collectAsState()
    var isSearchExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isSearchExpanded) {
                TextField(
                    value = searchQuery,
                    onValueChange = { transactionViewModel.updateSearchQuery(it) },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    placeholder = { Text("Search transactions", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                    trailingIcon = {
                        IconButton(onClick = { 
                            isSearchExpanded = false
                            transactionViewModel.updateSearchQuery("")
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "Close Search", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                    ),
                    singleLine = true
                )
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val greetings = listOf("Hi", "Namaste", "Hola", "Hello", "Greetings", "Welcome", "Bonjour", "Ciao", "Ahoj", "Zdravo")
                    val randomGreeting = remember { greetings.random() }
                    Text(
                        text = "$randomGreeting ${userProfile.name}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row {
                    IconButton(onClick = { isSearchExpanded = true }) {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.onBackground)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Main Balance Card
        MainBalanceCard(
            totalBalance = totalBalance,
            monthlyChange = 230.0, // Keeping dummy for now
            income = totalIncome,
            expenses = totalExpenses
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Transactions Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "Transactions",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "$${String.format(java.util.Locale.US, "%.2f", totalExpenses)}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = "Today",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Transactions List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(transactions) { item ->
                TransactionRow(
                    transactionWithCategory = item,
                    onEdit = { transaction ->
                        navController.navigate("add_transaction?transactionId=${transaction.id}")
                    },
                    onDelete = { transaction ->
                        transactionViewModel.deleteTransaction(transaction.id)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    ExpkerTheme {
        HomeScreen()
    }
}
