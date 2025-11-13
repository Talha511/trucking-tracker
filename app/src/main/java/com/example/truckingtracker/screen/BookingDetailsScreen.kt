package com.example.truckingtracker.screen

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.truckingtracker.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDetailsScreen(
    pickupAddress: String,
    dropOffAddress: String,
    date: String,
    time: String,
    vehicleName: String,
    vehicleType: String,
    vehicleImageRes: Int,
    onBack: () -> Unit = {},
    onSubmit: () -> Unit = {}
) {
    val context = LocalContext.current
    val primaryColor = Color(0xFFEB5757)
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val secondaryTextColor = MaterialTheme.colorScheme.onSurfaceVariant

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val horizontalPadding = if (screenWidth > 600.dp) 48.dp else 16.dp

    // 🔹 Form States
    var weight by remember { mutableStateOf("") }
    var goodsInfo by remember { mutableStateOf("") }
    var payer by remember { mutableStateOf("Customer") }
    var labourCount by remember { mutableStateOf(2) }
    var timeRelaxation by remember { mutableStateOf(true) }
    var additionalInfo by remember { mutableStateOf("") }

    // 🔹 Validation
    var weightError by remember { mutableStateOf(false) }
    var goodsError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Booking Details",
                        color = surfaceColor,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = surfaceColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryColor
                )
            )
        },
        containerColor = surfaceColor
    ) { padding ->
            // White background with rounded top corners
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 0.dp)
                    .clip(RoundedCornerShape(topStart = 60.dp, topEnd = 60.dp)),
                color = surfaceColor
            ) {
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = horizontalPadding, vertical = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp) // 🔸 reduced spacing
                ) {
                    // 🔹 Vehicle Info Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = vehicleName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = onSurfaceColor
                            )
                            Text(
                                text = vehicleType,
                                color = secondaryTextColor,
                                fontSize = 14.sp
                            )
                        }

                        Icon(
                            painter = painterResource(id = vehicleImageRes),
                            contentDescription = "Truck",
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .size(80.dp)
                                .padding(start = 8.dp)
                        )
                    }

                    // 🔹 Pickup & Drop Info (same color as background)
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = surfaceColor),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),// 🔸 changed
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp) // 🔸 flat look
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) { // 🔸 reduced padding
                            Text("Pickup", fontWeight = FontWeight.Bold)
                            Text(pickupAddress, color = secondaryTextColor)
                            Spacer(Modifier.height(15.dp)) // 🔸 smaller space
                            Text("Drop off", fontWeight = FontWeight.Bold)
                            Text(dropOffAddress, color = secondaryTextColor)
                        }
                    }

                    // 🔹 Date & Time
                    Text("Date & Time", fontWeight = FontWeight.Bold)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = {},
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) { Text(date) }

                        OutlinedButton(
                            onClick = {},
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) { Text(time) }
                    }

                    // 🔹 Weight
                    Text("Weight (approximate)", fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = weight,
                        onValueChange = {
                            weight = it
                            weightError = it.isEmpty()
                        },
                        label = { Text("Weight in KG") },
                        isError = weightError,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                    if (weightError)
                        Text("Please enter weight", color = primaryColor, fontSize = 12.sp)

                    // 🔹 Goods Info
                    Text("Goods Information", fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = goodsInfo,
                        onValueChange = {
                            goodsInfo = it
                            goodsError = it.isEmpty()
                        },
                        label = { Text("Describe goods") },
                        isError = goodsError,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                    if (goodsError)
                        Text("Please describe goods", color = primaryColor, fontSize = 12.sp)

                    // 🔹 Who will pay?
                    Text("Who will pay?", fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val colors = RadioButtonDefaults.colors(
                            selectedColor = primaryColor,
                            unselectedColor = secondaryTextColor
                        )
                        listOf("Customer", "Sender", "Receiver").forEach { option ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clickable { payer = option }
                                    .padding(end = 12.dp)
                            ) {
                                RadioButton(
                                    selected = payer == option,
                                    onClick = { payer = option },
                                    colors = colors
                                )
                                Text(option)
                            }
                        }
                    }

                    // 🔹 Labour Count
                    Text("Number of Labourers", fontWeight = FontWeight.Bold)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline, // your primary red outline
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // 🔹 Decrease Button (Left)
                            IconButton(
                                onClick = { if (labourCount > 0) labourCount-- },
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color(0xFFEB5757).copy(alpha = 0.1f))
                                    .size(40.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Remove,
                                    contentDescription = "Decrease",
                                    tint = Color(0xFFEB5757)
                                )
                            }

                            // 🔹 Labour Count (Centered)
                            Text(
                                "$labourCount",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            // 🔹 Increase Button (Right)
                            IconButton(
                                onClick = { labourCount++ },
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color(0xFFEB5757).copy(alpha = 0.1f))
                                    .size(40.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Increase",
                                    tint = Color(0xFFEB5757)
                                )
                            }
                        }
                    }


                    // 🔹 Time Relaxation Toggle
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Time Relaxation", fontWeight = FontWeight.Bold)
                        Switch(
                            checked = timeRelaxation,
                            onCheckedChange = { timeRelaxation = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = primaryColor,
                                uncheckedThumbColor = Color.Gray,
                                uncheckedTrackColor = Color.LightGray
                            )
                        )
                    }

                    // 🔹 Additional Info
                    Text("Additional Info", fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = additionalInfo,
                        onValueChange = { additionalInfo = it },
                        label = { Text("Additional Info (optional)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        shape = RoundedCornerShape(10.dp)
                    )

                    // 🔹 Submit Button
                    Button(
                        onClick = {
                            weightError = weight.isEmpty()
                            goodsError = goodsInfo.isEmpty()

                            if (weightError || goodsError) {
                                Toast.makeText(
                                    context,
                                    "Please fill all required fields",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                onSubmit()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                        shape = RoundedCornerShape(10.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Text(
                            "Submit",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BookingDetailsScreenPreview() {
    BookingDetailsScreen(
        pickupAddress = "174 A, Gulberg 3, Lahore",
        dropOffAddress = "Arfa Karim Tower, Lahore",
        date = "12/11/2025",
        time = "10:30 AM",
        vehicleName = "Mazda Truck",       // ✅ added
        vehicleType = "10-Wheeler",        // ✅ added
        vehicleImageRes = R.drawable.pickupmedium_vector // ✅ your drawable resource
    )
}


