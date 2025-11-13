package com.example.truckingtracker.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceDetailsBottomSheet(
    service: ServiceItem,
    onDismiss: () -> Unit,
    onConfirm: (vehicleName: String, vehicleType: String, imageRes: Int) -> Unit
) {
    // 🚚 Dynamic vehicle options based on service type
    val vehicleOptions = when (service.name) {
        "Small Pickup" -> listOf("Suzuki - Open", "Suzuki - Covered", "Mini Pickup")
        "Medium Pickup" -> listOf("Shehzore - Open", "Shehzore - Close", "Mazda - 12ft")
        "Heavy Vehicle" -> listOf("Mazda - Flatbed", "Mazda - 16ft", "Mazda - 20ft", "Mazda - Open")
        "Train Cargo" -> listOf("Container - 40ft", "Container - 20ft", "Flat Wagon")
        "Air Cargo" -> listOf("Domestic Air Freight", "International Air Freight")
        else -> listOf("FAW - Open", "Mazda - 20ft")
    }

    var selectedOption by remember { mutableStateOf(vehicleOptions.first()) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle = { BottomSheetDefaults.DragHandle() },
        tonalElevation = 8.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Select Vehicle",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 12.dp)
            )
            vehicleOptions.forEach { option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .selectable(
                            selected = selectedOption == option,
                            onClick = { selectedOption = option }
                        )
                        .padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = option,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    RadioButton(
                        selected = selectedOption == option,
                        onClick = { selectedOption = option },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color(0xFFEB5757),
                            unselectedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    )
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    onConfirm(
                        service.name, // vehicleName (e.g., "Medium Pickup")
                        selectedOption, // vehicleType (e.g., "Shehzore - Open")
                        service.imageResId // imageRes (e.g., R.drawable.pickupmedium_vector)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEB5757)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Confirm Vehicle",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Confirm",
                        tint = Color.White,
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .size(22.dp)
                            .align(Alignment.CenterEnd)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewServiceDetailsBottomSheet() {
    MaterialTheme {
        ServiceDetailsBottomSheet(
            service = ServiceItem(
                name = "Medium Pickup",
                imageResId = com.example.truckingtracker.R.drawable.pickupmedium_vector
            ),
            onDismiss = {},
            onConfirm = {vehicleName, vehicleType, imageRes -> }
        )
    }
}
