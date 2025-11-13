package com.example.truckingtracker.appnavigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 🟥 Theme Colors
val primaryColor = Color(0xFFEB5757)
val surfaceColor @Composable get() = MaterialTheme.colorScheme.surface
val onSurfaceColor @Composable get() = MaterialTheme.colorScheme.onSurface
val secondaryTextColor @Composable get() = MaterialTheme.colorScheme.onSurfaceVariant

@Composable
fun AppDrawer(
    currentRoute: String,
    onItemClick: (DrawerItem) -> Unit,
    onCloseDrawer: () -> Unit
) {
    var settingsExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f))
    ) {
        Surface(
            modifier = Modifier
                .fillMaxHeight()
                .width(280.dp)
                .padding(end = 8.dp),
            color = surfaceColor,
            shadowElevation = 8.dp,
            shape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(surfaceColor)
                    .padding(vertical = 24.dp)
            ) {
                DrawerHeader()

                Spacer(modifier = Modifier.height(20.dp))

                DrawerMenuItem(
                    item = DrawerItem.Home,
                    selected = currentRoute == DrawerItem.Home.route,
                    onClick = { onItemClick(DrawerItem.Home) }
                )

                DrawerMenuItem(
                    item = DrawerItem.Profile,
                    selected = currentRoute == DrawerItem.Profile.route,
                    onClick = { onItemClick(DrawerItem.Profile) }
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { settingsExpanded = !settingsExpanded }
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = if (settingsExpanded) primaryColor else secondaryTextColor
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Settings",
                        color = if (settingsExpanded) primaryColor else onSurfaceColor,
                        fontSize = 16.sp,
                        fontWeight = if (settingsExpanded) FontWeight.Bold else FontWeight.Normal
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = if (settingsExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = secondaryTextColor
                    )
                }

                AnimatedVisibility(
                    visible = settingsExpanded,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Column(modifier = Modifier.padding(start = 56.dp)) {
                        DrawerSubMenuItem(
                            title = "Change Password",
                            icon = Icons.Default.Lock,
                            onClick = { onItemClick(DrawerItem.ChangePassword) }
                        )
                        DrawerSubMenuItem(
                            title = "Logout",
                            icon = Icons.AutoMirrored.Filled.ExitToApp,
                            onClick = { onItemClick(DrawerItem.Logout) }
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                HorizontalDivider(color = onSurfaceColor.copy(alpha = 0.1f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCloseDrawer() }
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Close Drawer",
                        tint = primaryColor
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Close",
                        color = primaryColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun DrawerHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.LocalShipping,
            contentDescription = "App Icon",
            tint = primaryColor,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = "Trucking Tracker",
                color = primaryColor,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(
                text = "Smart Logistics App",
                color = secondaryTextColor,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
fun DrawerMenuItem(
    item: DrawerItem,
    selected: Boolean,
    onClick: () -> Unit
) {
    val bgColor =
        if (selected) primaryColor.copy(alpha = 0.1f)
        else Color.Transparent

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(bgColor)
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.title,
            tint = if (selected) primaryColor else secondaryTextColor
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = item.title,
            color = if (selected) primaryColor else onSurfaceColor,
            fontSize = 16.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun DrawerSubMenuItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = secondaryTextColor,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            color = onSurfaceColor,
            fontSize = 14.sp
        )
    }
}
