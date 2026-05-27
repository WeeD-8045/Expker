package com.example.expker.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.expker.ui.theme.*

data class SampleCategory(
    val id: String,
    val name: String,
    val icon: ImageVector,
    val color: Color
)

data class SampleTransaction(
    val id: String,
    val title: String,
    val amount: Double,
    val date: String,
    val category: SampleCategory,
    val type: TransactionType,
    val tax: Double = 0.0,
    val timestamp: Long = System.currentTimeMillis()
)

object SampleData {
    val categories = listOf(
        SampleCategory("1", "Shop", Icons.Default.ShoppingCart, ChartOrange),
        SampleCategory("2", "Transport", Icons.Default.DirectionsCar, ChartBlue),
        SampleCategory("3", "Education", Icons.Default.School, ChartPurple),
        SampleCategory("4", "Rent", Icons.Default.House, ChartPink),
        SampleCategory("5", "Food", Icons.Default.Fastfood, ChartCyan),
        SampleCategory("6", "Games", Icons.Default.SportsEsports, Color(0xFF9C27B0)),
        SampleCategory("7", "Job", Icons.Default.Work, Color(0xFF4CAF50)),
        SampleCategory("8", "Vacations", Icons.Default.BeachAccess, Color(0xFF03A9F4))
    )

    private val now = System.currentTimeMillis()

    val transactions = listOf(
        SampleTransaction("1", "Shop", 120.0, "Today, Sep 10", categories[0], TransactionType.EXPENSE, 0.0, now),
        SampleTransaction("2", "Uber", 18.0, "Today, Sep 10", categories[1], TransactionType.EXPENSE, 0.0, now - 1000),
        SampleTransaction("3", "Salary", 4200.0, "Sep 01", categories[4], TransactionType.INCOME, 0.0, now - 2000),
        SampleTransaction("4", "Rent", 562.0, "Aug 31", categories[3], TransactionType.EXPENSE, 0.0, now - 3000)
    )

    val accounts = listOf(
        Account("1", "Chase", 2340.0, "2 min ago", Icons.Default.AccountBalance, AccentBlue),
        Account("2", "Wells Fargo", 1445.0, "10 min ago", Icons.Default.AccountBalanceWallet, AccentRed)
    )

    val userProfile = UserProfile("Faiz", null)
}
