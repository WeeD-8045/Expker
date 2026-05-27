package com.example.expker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expker.model.Account
import com.example.expker.model.Transaction
import com.example.expker.model.TransactionType
import com.example.expker.ui.theme.*

import java.util.Locale

@Composable
fun MainBalanceCard(
    totalBalance: Double,
    monthlyChange: Double,
    income: Double,
    expenses: Double,
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val cardGradient = Brush.verticalGradient(
        colors = listOf(primaryColor.copy(alpha = 0.8f), primaryColor)
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(cardGradient)
                .padding(24.dp)
        ) {
            Column {
                Text(
                    text = "Total Balance",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Black.copy(alpha = 0.5f)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "$${String.format(Locale.US, "%,.2f", totalBalance)}",
                        style = MaterialTheme.typography.displayLarge,
                        color = Color.Black,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 36.sp
                    )
                }
                Surface(
                    color = Color.Black.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "+$${monthlyChange.toInt()}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IncomeExpenseSummary(
                        label = "Income",
                        amount = income,
                        icon = Icons.Default.ArrowDownward,
                        iconBackgroundColor = Color.Black.copy(alpha = 0.1f),
                        isIncome = true
                    )
                    IncomeExpenseSummary(
                        label = "Expenses",
                        amount = expenses,
                        icon = Icons.Default.ArrowUpward,
                        iconBackgroundColor = Color.Black.copy(alpha = 0.1f),
                        isIncome = false
                    )
                }
            }
        }
    }
}

@Composable
fun IncomeExpenseSummary(
    label: String,
    amount: Double,
    icon: ImageVector,
    iconBackgroundColor: Color,
    isIncome: Boolean
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(iconBackgroundColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = "${if (isIncome) "+" else "-"}$${amount.toInt()}",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Black.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
fun AccountCard(account: Account, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .width(160.dp)
            .height(100.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDarkBlue)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(account.color.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = account.icon,
                        contentDescription = null,
                        tint = account.color,
                        modifier = Modifier.size(14.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = account.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Medium
                )
            }
            Column {
                Text(
                    text = "$${String.format(Locale.US, "%,.0f", account.balance)}",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Synced ${account.lastUpdated}",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun TransactionRow(
    transactionWithCategory: com.example.expker.model.TransactionWithCategory,
    onEdit: (com.example.expker.model.Transaction) -> Unit = {},
    onDelete: (com.example.expker.model.Transaction) -> Unit = {}
) {
    val transaction = transactionWithCategory.transaction
    val category = transactionWithCategory.category
    var showMenu by remember { mutableStateOf(false) }

    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .combinedClickable(
                    onClick = { },
                    onLongClick = { showMenu = true }
                )
                .padding(vertical = 12.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = category?.icon ?: Icons.Default.Category,
                    contentDescription = null,
                    tint = category?.color ?: Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = category?.name ?: "Unknown",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${if (transaction.type == com.example.expker.model.TransactionType.EXPENSE) "-" else "+"}$${String.format(java.util.Locale.US, "%.2f", transaction.amount)}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (transaction.type == com.example.expker.model.TransactionType.EXPENSE) Color(0xFFF87171) else Color(0xFF4ADE80),
                    fontWeight = FontWeight.Bold
                )
                if (transaction.tax > 0) {
                    Text(
                        text = "Tax: $${transaction.tax}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
        ) {
            DropdownMenuItem(
                text = { Text("Edit", color = MaterialTheme.colorScheme.onSurface) },
                onClick = {
                    showMenu = false
                    onEdit(transaction)
                },
                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
            )
            DropdownMenuItem(
                text = { Text("Delete", color = Color.Red) },
                onClick = {
                    showMenu = false
                    onDelete(transaction)
                },
                leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red) }
            )
        }
    }
}

@Composable
fun CustomBottomNavigation(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    onFabClick: () -> Unit,
    profilePictureUri: String? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            shape = RoundedCornerShape(32.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 8.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                IconButton(onClick = { onTabSelected(0) }) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Home",
                        tint = if (selectedTab == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = { onTabSelected(1) }) {
                    Icon(
                        imageVector = Icons.Default.BarChart,
                        contentDescription = "Statistics",
                        tint = if (selectedTab == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.width(48.dp)) // Space for FAB
                IconButton(onClick = { onTabSelected(2) }) {
                    Icon(
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = "Transactions",
                        tint = if (selectedTab == 2) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = { onTabSelected(3) }) {
                    if (profilePictureUri != null) {
                        coil.compose.AsyncImage(
                            model = profilePictureUri,
                            contentDescription = "Profile",
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .border(
                                    width = if (selectedTab == 3) 2.dp else 0.dp,
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = CircleShape
                                ),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                            alpha = if (selectedTab == 3) 1f else 0.6f
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = if (selectedTab == 3) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = onFabClick,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-10).dp)
                .size(56.dp),
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(32.dp))
        }
    }
}
