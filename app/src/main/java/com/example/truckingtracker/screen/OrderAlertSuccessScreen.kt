package com.example.truckingtracker.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.truckingtracker.ui.theme.TruckingTrackerTheme

@Composable
fun OrderAlertSuccessScreen(
    onDoneClick: () -> Unit = {}
) {
    // Define colors
    val successColor = Color(0xFF27AE60) // A nice green color for success
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val primaryColor = Color(0xFFEB5757) // Re-using your primary color for the button

    Scaffold(
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(surfaceColor)
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center // Center content vertically
            ) {
                // 1. Success Icon
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "Success Checkmark",
                    tint = successColor,
                    modifier = Modifier.size(120.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // 2. Success Message Title
                Text(
                    text = "Order Alert Successful!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = onSurfaceColor
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 3. Sub-message
                Text(
                    text = "Your new order alert has been created and is now active.",
                    fontSize = 16.sp,
                    color = onSurfaceColor.copy(alpha = 0.7f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(80.dp))

                // 4. Action Button
                Button(
                    onClick = onDoneClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        text = "Done",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewOrderAlertSuccessScreen() {
    TruckingTrackerTheme {
        OrderAlertSuccessScreen()
    }
}