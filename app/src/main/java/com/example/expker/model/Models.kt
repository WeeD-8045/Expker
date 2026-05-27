package com.example.expker.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.expker.ui.theme.*

enum class TransactionType {
    INCOME, EXPENSE
}

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey val id: String,
    val name: String,
    val iconName: String, // Store icon name for reconstruction
    val colorHex: Long // Store color as hex
) {
    // Helper to get actual icon/color in Compose
    val icon: ImageVector get() = IconHelper.getIconByName(iconName)
    val color: Color get() = Color(colorHex.toULong())
}

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey val id: String,
    val title: String,
    val amount: Double,
    val date: String,
    val categoryId: String, // Store category ID for relationship
    val type: TransactionType,
    val tax: Double = 0.0,
    val timestamp: Long = System.currentTimeMillis()
)

data class TransactionWithCategory(
    val transaction: Transaction,
    val category: Category?
)

data class Account(
    val id: String,
    val name: String,
    val balance: Double,
    val lastUpdated: String,
    val icon: ImageVector,
    val color: Color
)

data class UserProfile(
    val name: String,
    val profilePictureUri: String? = null,
    val themeMode: Int = 0, // 0: System, 1: Light, 2: Dark
    val accentColor: Long = 0xFF4ADE80L
)
