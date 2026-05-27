package com.example.expker.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.expker.model.Category
import com.example.expker.model.TransactionType
import com.example.expker.ui.theme.*
import com.example.expker.ui.viewmodel.TransactionViewModel

@Composable
fun AddTransactionScreen(
    navController: NavController,
    viewModel: TransactionViewModel = viewModel(),
    transactionId: String? = null
) {
    val categories by viewModel.categories.collectAsState()
    val transactionToEdit = if (transactionId != null) {
        val transactions by viewModel.transactions.collectAsState()
        transactions.find { it.transaction.id == transactionId }?.transaction
    } else null

    var title by remember(transactionToEdit) { mutableStateOf(transactionToEdit?.title ?: "") }
    var amount by remember(transactionToEdit) { mutableStateOf(transactionToEdit?.amount?.toString() ?: "") }
    var selectedCategory by remember(transactionToEdit, categories) { 
        mutableStateOf(categories.find { it.id == transactionToEdit?.categoryId }) 
    }
    var transactionType by remember(transactionToEdit) { mutableStateOf(transactionToEdit?.type ?: TransactionType.EXPENSE) }
    val context = LocalContext.current

    var showAddCategoryDialog by remember { mutableStateOf(false) }
    var newCategoryName by remember { mutableStateOf("") }
    var selectedNewCategoryIcon by remember { mutableStateOf(Icons.Default.Category) }
    val availableIcons = listOf(
        Icons.Default.ShoppingCart, Icons.Default.Fastfood, Icons.Default.DirectionsCar,
        Icons.Default.Movie, Icons.Default.AccountBalance, Icons.Default.Work,
        Icons.Default.LocalHospital, Icons.Default.School, Icons.Default.House,
        Icons.Default.Flight, Icons.Default.SportsEsports, Icons.Default.BeachAccess,
        Icons.Default.Category
    )

    if (showAddCategoryDialog) {
        val primaryColor = MaterialTheme.colorScheme.primary
        AlertDialog(
            onDismissRequest = { showAddCategoryDialog = false },
            title = { Text("Add Category Group", color = MaterialTheme.colorScheme.onSurface) },
            text = {
                Column {
                    TextField(
                        value = newCategoryName,
                        onValueChange = { newCategoryName = it },
                        placeholder = { Text("Group Name", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Select Icon", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelSmall)
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        modifier = Modifier.height(150.dp).padding(top = 8.dp)
                    ) {
                        items(availableIcons) { icon ->
                            Box(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(if (selectedNewCategoryIcon == icon) primaryColor.copy(alpha = 0.2f) else Color.Transparent)
                                    .clickable { selectedNewCategoryIcon = icon },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = icon, 
                                    contentDescription = null, 
                                    tint = if (selectedNewCategoryIcon == icon) primaryColor else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newCategoryName.isNotEmpty()) {
                            viewModel.addCategory(newCategoryName, selectedNewCategoryIcon, primaryColor)
                            showAddCategoryDialog = false
                            newCategoryName = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                ) {
                    Text("Add", color = MaterialTheme.colorScheme.onPrimary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddCategoryDialog = false }) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }

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
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", tint = MaterialTheme.colorScheme.onBackground)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (transactionId != null) "Edit Transaction" else "Add Transaction",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Transaction Type Switcher
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(24.dp))
                    .background(if (transactionType == TransactionType.EXPENSE) AccentRed else Color.Transparent)
                    .clickable { transactionType = TransactionType.EXPENSE },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Expense",
                    color = if (transactionType == TransactionType.EXPENSE) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(24.dp))
                    .background(if (transactionType == TransactionType.INCOME) AccentGreen else Color.Transparent)
                    .clickable { transactionType = TransactionType.INCOME },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Income",
                    color = if (transactionType == TransactionType.INCOME) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Name Input
        Text("Transaction Name", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        TextField(
            value = title,
            onValueChange = { title = it },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp).clip(RoundedCornerShape(16.dp)),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            ),
            placeholder = { Text("e.g. Shopping, Lunch", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Amount Input
        Text("Amount", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        TextField(
            value = amount,
            onValueChange = { if (it.all { char -> char.isDigit() || char == '.' }) amount = it },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp).clip(RoundedCornerShape(16.dp)),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            ),
            prefix = { Text("$", color = MaterialTheme.colorScheme.onSurface) },
            placeholder = { Text("0.00", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)) }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Category Picker
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Select Category", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            TextButton(onClick = { showAddCategoryDialog = true }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                    Text("Add Group", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp)
                }
            }
        }
        
        LazyVerticalGrid(
            columns = GridCells.Adaptive(80.dp),
            contentPadding = PaddingValues(top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(categories) { category ->
                CategoryItem(
                    category = category,
                    isSelected = selectedCategory?.id == category.id,
                    onClick = { selectedCategory = category }
                )
            }
        }

        // Save Button
        Button(
            onClick = {
                val amt = amount.toDoubleOrNull() ?: 0.0
                if (title.isNotEmpty() && amt > 0 && selectedCategory != null) {
                    if (transactionId != null) {
                        viewModel.editTransaction(transactionId, title, amt, selectedCategory!!, transactionType)
                        Toast.makeText(context, "Transaction Updated", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.addTransaction(title, amt, selectedCategory!!, transactionType)
                        Toast.makeText(context, "Transaction Saved", Toast.LENGTH_SHORT).show()
                    }
                    navController.popBackStack()
                } else {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = if (transactionId != null) "Update Transaction" else "Save Transaction",
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun CategoryItem(category: Category, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) category.color.copy(alpha = 0.1f) else Color.Transparent)
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(if (isSelected) category.color else MaterialTheme.colorScheme.surfaceVariant, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = category.name,
                tint = if (isSelected) Color.White else category.color,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = category.name,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1
        )
    }
}
