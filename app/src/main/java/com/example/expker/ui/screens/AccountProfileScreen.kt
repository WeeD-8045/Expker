package com.example.expker.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.expker.ui.theme.*
import com.example.expker.ui.viewmodel.ProfileViewModel

@Composable
fun AccountProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = viewModel()
) {
    val userProfile by viewModel.userProfile.collectAsState()
    var name by remember(userProfile.name) { mutableStateOf(userProfile.name) }
    val context = LocalContext.current

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let { viewModel.updateProfilePicture(it) }
        }
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
                Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", tint = MaterialTheme.colorScheme.onBackground)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Account",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Profile Picture Selection
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clickable {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                // Background circle
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    if (userProfile.profilePictureUri != null) {
                        AsyncImage(
                            model = userProfile.profilePictureUri,
                            contentDescription = "Profile Picture",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(64.dp)
                        )
                    }
                }
                
                // Pencil icon on the side
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(32.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Change Picture",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Tap to change profile picture",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Name Input
        Text(
            text = "Your Name",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        TextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            ),
            placeholder = { Text("Enter your name", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)) }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Settings Options
        SettingItem(icon = Icons.Default.Palette, title = "Appearance", onClick = { navController.navigate("appearance_settings") })
        SettingItem(icon = Icons.Default.Notifications, title = "Notifications")
        SettingItem(
            icon = Icons.Default.HelpOutline, 
            title = "Support",
            onClick = { navController.navigate("support") }
        )

        Spacer(modifier = Modifier.weight(1f))

        // Save Button
        Button(
            onClick = {
                viewModel.updateName(name)
                Toast.makeText(context, "Saved Successfully", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = "Save Profile",
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun SettingItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector, 
    title: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title, 
            style = MaterialTheme.typography.bodyLarge, 
            color = MaterialTheme.colorScheme.onSurface, 
            modifier = Modifier.weight(1f)
        )
        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    ExpkerTheme {
        AccountProfileScreen(rememberNavController())
    }
}
