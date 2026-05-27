package com.example.expker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.expker.model.Category
import com.example.expker.ui.components.DonutChart
import com.example.expker.ui.components.SimpleLineChart
import com.example.expker.ui.theme.AccentGreen
import com.example.expker.ui.theme.ExpkerTheme
import com.example.expker.ui.viewmodel.TransactionViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.Locale

@Composable
fun StatisticsScreen(transactionViewModel: TransactionViewModel = viewModel()) {
    var selectedTab by remember { mutableIntStateOf(0) }

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
            IconButton(onClick = { }) {
                Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", tint = MaterialTheme.colorScheme.onBackground)
            }
            Text(
                text = "Statistics",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
            Row {
                IconButton(onClick = { }) {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.onBackground)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tabs (v2)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(4.dp)
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                // Tab 1: Categories
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (selectedTab == 0) MaterialTheme.colorScheme.primary else Color.Transparent)
                        .clickable { selectedTab = 0 },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Categories",
                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == 0) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                // Tab 2: Monthly Spending
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (selectedTab == 1) MaterialTheme.colorScheme.primary else Color.Transparent)
                        .clickable { selectedTab = 1 },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Monthly Spending",
                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == 1) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (selectedTab == 0) {
            CategoryView(transactionViewModel)
        } else {
            MonthlySummaryView(transactionViewModel)
        }
    }
}

@Composable
fun CategoryView(viewModel: TransactionViewModel) {
    val transactions by viewModel.transactions.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val totalExpenses by viewModel.totalExpenses.collectAsState()
    
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        DonutChart(
            data = categories.map { category ->
                val catExpenses = transactions
                    .filter { it.transaction.categoryId == category.id && it.transaction.type == com.example.expker.model.TransactionType.EXPENSE }
                    .sumOf { it.transaction.amount }
                category.color to (if (totalExpenses > 0) (catExpenses / totalExpenses).toFloat() else 0f)
            },
            totalAmount = String.format(Locale.US, "%.2f", totalExpenses),
            month = "",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(40.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories) { category ->
                val categoryTransactions = transactions
                    .filter { it.transaction.categoryId == category.id && it.transaction.type == com.example.expker.model.TransactionType.EXPENSE }
                val catExpenses = categoryTransactions.sumOf { it.transaction.amount }
                val percentage = if (totalExpenses > 0) (catExpenses / totalExpenses * 100).toInt() else 0
                CategoryStatCard(
                    category = category, 
                    amount = catExpenses, 
                    percentage = percentage,
                    categoryTransactions = categoryTransactions
                )
            }
        }
    }
}

@Composable
fun MonthlySummaryView(viewModel: TransactionViewModel) {
    val totalExpenses by viewModel.totalExpenses.collectAsState()

    Column {
        Text(
            text = "Avg. monthly expenses",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "$${String.format(Locale.US, "%.2f", totalExpenses)}",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.ExtraBold
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null, tint = AccentGreen, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "$230 than last month",
                style = MaterialTheme.typography.bodySmall,
                color = AccentGreen
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        SimpleLineChart(
            incomeData = listOf(10f, 20f, 15f, 40f, 30f, 50f, 45f),
            expenseData = listOf(5f, 15f, 10f, 25f, 20f, 35f, 30f),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Time filters
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            listOf("7D", "1M", "3M", "6M", "1Y").forEach { label ->
                val isSelected = label == "6M"
                Surface(
                    onClick = { /* Handle filter */ },
                    shape = RoundedCornerShape(20.dp),
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.weight(1f).height(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelMedium,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Insights
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            InsightCard(
                icon = Icons.Default.TrendingUp,
                text = "Your income peaked in August at $4,200",
                modifier = Modifier.weight(1f)
            )
            InsightCard(
                icon = Icons.Default.Savings,
                text = "You saved an average of $4,120 per month",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun CategoryStatCard(
    category: Category, 
    amount: Double, 
    percentage: Int,
    categoryTransactions: List<com.example.expker.model.TransactionWithCategory>
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded }
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(category.color.copy(alpha = 0.1f), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = category.icon, contentDescription = null, tint = category.color)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = category.name, 
                        color = MaterialTheme.colorScheme.onSurface, 
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "${categoryTransactions.size} Transactions", 
                        color = MaterialTheme.colorScheme.onSurfaceVariant, 
                        fontSize = 12.sp
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "-$${String.format(Locale.US, "%.2f", amount)}", 
                        color = Color(0xFFF87171),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Icon(
                        if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown, 
                        contentDescription = null, 
                        tint = MaterialTheme.colorScheme.onSurfaceVariant, 
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                // Mini circular progress
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = { percentage / 100f },
                        modifier = Modifier.size(36.dp),
                        color = category.color,
                        strokeWidth = 4.dp,
                        trackColor = category.color.copy(alpha = 0.1f)
                    )
                    Text(
                        text = "$percentage%", 
                        color = MaterialTheme.colorScheme.onSurface, 
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            androidx.compose.animation.AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp)
                ) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f), 
                        thickness = 1.dp, 
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    categoryTransactions.forEach { item ->
                        com.example.expker.ui.components.TransactionRow(
                            transactionWithCategory = item
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InsightCard(icon: ImageVector, text: String, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = modifier.height(130.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon, 
                    contentDescription = null, 
                    tint = MaterialTheme.colorScheme.primary, 
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = text, 
                style = MaterialTheme.typography.bodySmall, 
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StatisticsPreview() {
    ExpkerTheme {
        StatisticsScreen(viewModel())
    }
}
