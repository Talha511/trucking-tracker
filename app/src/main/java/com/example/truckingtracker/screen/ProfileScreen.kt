package com.example.truckingtracker.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.truckingtracker.ui.theme.TruckingTrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBackClick: () -> Unit = {},
    onEditProfileClick: () -> Unit = {}
) {
    val primaryColor = Color(0xFFEB5757)
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val secondaryTextColor = MaterialTheme.colorScheme.onSurfaceVariant

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Profile", color = onSurfaceColor, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = onSurfaceColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = surfaceColor
                )
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(surfaceColor)
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // Profile Image (Placeholder or from API)
                AsyncImage(
                    model = "https://cdn-icons-png.flaticon.com/512/149/149071.png",
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Name
                Text(
                    text = "Asif",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = onSurfaceColor
                )

                // Email
                Text(
                    text = "asif@gmail.com",
                    fontSize = 16.sp,
                    color = secondaryTextColor
                )

                // CNIC
                Text(
                    text = "CNIC: 3111111111111",
                    fontSize = 16.sp,
                    color = secondaryTextColor,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Edit Profile Button
                Button(
                    onClick = onEditProfileClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                    shape = RoundedCornerShape(8.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        text = "Edit Profile",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                //Back to main screen
                Text(
                    text = "Back to Main",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = secondaryTextColor,
                    modifier = Modifier
                        .clickable(onClick = onBackClick)
                        .padding(vertical = 8.dp)
                )
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewProfileScreen() {
    TruckingTrackerTheme {
        ProfileScreen()
    }
}
