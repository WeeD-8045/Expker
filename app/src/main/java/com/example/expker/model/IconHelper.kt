package com.example.expker.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

object IconHelper {
    fun getIconByName(name: String): ImageVector {
        return when (name) {
            "ShoppingCart" -> Icons.Filled.ShoppingCart
            "Fastfood" -> Icons.Filled.Fastfood
            "DirectionsCar" -> Icons.Filled.DirectionsCar
            "Movie" -> Icons.Filled.Movie
            "AccountBalance" -> Icons.Filled.AccountBalance
            "Work" -> Icons.Filled.Work
            "LocalHospital" -> Icons.Filled.LocalHospital
            "School" -> Icons.Filled.School
            "House" -> Icons.Filled.House
            "Flight" -> Icons.Filled.Flight
            "AttachMoney" -> Icons.Filled.AttachMoney
            "TrendingUp" -> Icons.Filled.TrendingUp
            "AccountBalanceWallet" -> Icons.Filled.AccountBalanceWallet
            "Home" -> Icons.Filled.Home
            "BarChart" -> Icons.Filled.BarChart
            "Settings" -> Icons.Filled.Settings
            "MoreHoriz" -> Icons.Filled.MoreHoriz
            "Search" -> Icons.Filled.Search
            "ArrowBackIosNew" -> Icons.Filled.ArrowBackIosNew
            "ArrowDownward" -> Icons.Filled.ArrowDownward
            "ArrowUpward" -> Icons.Filled.ArrowUpward
            "Edit" -> Icons.Filled.Edit
            "Delete" -> Icons.Filled.Delete
            "Add" -> Icons.Filled.Add
            "SportsEsports" -> Icons.Filled.SportsEsports
            "BeachAccess" -> Icons.Filled.BeachAccess
            else -> Icons.Filled.Category
        }
    }

    fun getNameByIcon(icon: ImageVector): String {
        return when (icon) {
            Icons.Filled.ShoppingCart -> "ShoppingCart"
            Icons.Filled.Fastfood -> "Fastfood"
            Icons.Filled.DirectionsCar -> "DirectionsCar"
            Icons.Filled.Movie -> "Movie"
            Icons.Filled.AccountBalance -> "AccountBalance"
            Icons.Filled.Work -> "Work"
            Icons.Filled.LocalHospital -> "LocalHospital"
            Icons.Filled.School -> "School"
            Icons.Filled.House -> "House"
            Icons.Filled.Flight -> "Flight"
            Icons.Filled.AttachMoney -> "AttachMoney"
            Icons.Filled.TrendingUp -> "TrendingUp"
            Icons.Filled.AccountBalanceWallet -> "AccountBalanceWallet"
            Icons.Filled.Home -> "Home"
            Icons.Filled.BarChart -> "BarChart"
            Icons.Filled.Settings -> "Settings"
            Icons.Filled.MoreHoriz -> "MoreHoriz"
            Icons.Filled.Search -> "Search"
            Icons.Filled.ArrowBackIosNew -> "ArrowBackIosNew"
            Icons.Filled.ArrowDownward -> "ArrowDownward"
            Icons.Filled.ArrowUpward -> "ArrowUpward"
            Icons.Filled.Edit -> "Edit"
            Icons.Filled.Delete -> "Delete"
            Icons.Filled.Add -> "Add"
            Icons.Filled.SportsEsports -> "SportsEsports"
            Icons.Filled.BeachAccess -> "BeachAccess"
            else -> "Category"
        }
    }
}
