package com.example.expker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.expker.ui.components.TransactionRow
import com.example.expker.ui.viewmodel.SortOrder
import com.example.expker.ui.viewmodel.TransactionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    navController: NavController,
    transactionViewModel: TransactionViewModel
) {
    val transactions by transactionViewModel.transactions.collectAsState()
    val searchQuery by transactionViewModel.searchQuery.collectAsState()
    val sortOrder by transactionViewModel.sortOrder.collectAsState()
    var isSearchExpanded by remember { mutableStateOf(false) }
    var showSortMenu by remember { mutableStateOf(false) }

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
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (isSearchExpanded) {
                TextField(
                    value = searchQuery,
                    onValueChange = { transactionViewModel.updateSearchQuery(it) },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    placeholder = { Text("Search by name or amount", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp) },
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
                Text(
                    text = "Transactions",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )
                Row {
                    IconButton(onClick = { isSearchExpanded = true }) {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.onBackground)
                    }
                    Box {
                        IconButton(onClick = { showSortMenu = true }) {
                            Icon(Icons.Default.Sort, contentDescription = "Sort", tint = MaterialTheme.colorScheme.onBackground)
                        }
                        DropdownMenu(
                            expanded = showSortMenu,
                            onDismissRequest = { showSortMenu = false },
                            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                        ) {
                            DropdownMenuItem(
                                text = { Text("Recent", color = if (sortOrder == SortOrder.RECENT) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface) },
                                onClick = {
                                    transactionViewModel.updateSortOrder(SortOrder.RECENT)
                                    showSortMenu = false
                                },
                                leadingIcon = { Icon(Icons.Default.Schedule, contentDescription = null, tint = if (sortOrder == SortOrder.RECENT) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant) }
                            )
                            DropdownMenuItem(
                                text = { Text("Amount: Low to High", color = if (sortOrder == SortOrder.ASCENDING) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface) },
                                onClick = {
                                    transactionViewModel.updateSortOrder(SortOrder.ASCENDING)
                                    showSortMenu = false
                                },
                                leadingIcon = { Icon(Icons.Default.ArrowUpward, contentDescription = null, tint = if (sortOrder == SortOrder.ASCENDING) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant) }
                            )
                            DropdownMenuItem(
                                text = { Text("Amount: High to Low", color = if (sortOrder == SortOrder.DESCENDING) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface) },
                                onClick = {
                                    transactionViewModel.updateSortOrder(SortOrder.DESCENDING)
                                    showSortMenu = false
                                },
                                leadingIcon = { Icon(Icons.Default.ArrowDownward, contentDescription = null, tint = if (sortOrder == SortOrder.DESCENDING) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant) }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (transactions.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = if (searchQuery.isEmpty()) "No transactions found" else "No results for \"$searchQuery\"",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
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
}
