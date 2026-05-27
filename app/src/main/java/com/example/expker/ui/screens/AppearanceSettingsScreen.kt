package com.example.expker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.expker.ui.viewmodel.ProfileViewModel

@Composable
fun AppearanceSettingsScreen(
    navController: NavController,
    viewModel: ProfileViewModel = viewModel()
) {
    val userProfile by viewModel.userProfile.collectAsState()
    
    val accentColors = listOf(
        0xFF4ADE80L, // Green
        0xFF60A5FAL, // Blue
        0xFFA78BFAL, // Purple
        0xFFFB923CL, // Orange
        0xFFF87171L, // Red
        0xFFF1F589L, // Yellow
        0xFFEC4899L, // Pink
        0xFF06B6D4L  // Cyan
    )

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
                Icon(
                    Icons.Default.ArrowBackIosNew, 
                    contentDescription = "Back", 
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Appearance",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Theme Mode Section
        Text(
            text = "Theme Mode",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        ThemeOption(
            title = "System Default",
            selected = userProfile.themeMode == 0,
            onClick = { viewModel.updateThemeMode(0) }
        )
        ThemeOption(
            title = "Light Mode",
            selected = userProfile.themeMode == 1,
            onClick = { viewModel.updateThemeMode(1) }
        )
        ThemeOption(
            title = "Dark Mode",
            selected = userProfile.themeMode == 2,
            onClick = { viewModel.updateThemeMode(2) }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Accent Color Section
        Text(
            text = "Accent Color",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(accentColors) { colorHex ->
                val isSelected = userProfile.accentColor == colorHex
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color(colorHex))
                        .clickable { viewModel.updateAccentColor(colorHex) }
                        .border(
                            width = if (isSelected) 3.dp else 0.dp,
                            color = MaterialTheme.colorScheme.onBackground,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Icon(
                            Icons.Default.Check, 
                            contentDescription = null, 
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ThemeOption(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
        RadioButton(
            selected = selected,
            onClick = null, // Handled by Row clickable
            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
        )
    }
}
