package com.example.truckingtracker.screen

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.truckingtracker.models.PakistanCities
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteMapScreenGoogle(
    onBackClick: () -> Unit = {},
    vehicleName: String,
    vehicleType: String,
    imageRes: Int,
    onVehicleSelected: (pickup: String, dropoff: String, dateTime: String ) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val allCities = remember { PakistanCities.SAMPLE }

    var pickupText by remember { mutableStateOf("") }
    var dropoffText by remember { mutableStateOf("") }

    var expandedPickup by remember { mutableStateOf(false) }
    var expandedDropoff by remember { mutableStateOf(false) }

    var pickupPoint by remember { mutableStateOf<LatLng?>(null) }
    var dropoffPoint by remember { mutableStateOf<LatLng?>(null) }

    var dateTimeText by remember { mutableStateOf("") }
    val dateTimeCalendar = remember { Calendar.getInstance() }

    val karachi = LatLng(24.8607, 67.0011)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(karachi, 5f)
    }

    fun showDateTimePicker() {
        DatePickerDialog(
            context,
            { _, year, month, day ->
                dateTimeCalendar.set(year, month, day)
                TimePickerDialog(
                    context,
                    { _, hour, minute ->
                        dateTimeCalendar.set(Calendar.HOUR_OF_DAY, hour)
                        dateTimeCalendar.set(Calendar.MINUTE, minute)
                        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                        dateTimeText = sdf.format(dateTimeCalendar.time)
                    },
                    dateTimeCalendar.get(Calendar.HOUR_OF_DAY),
                    dateTimeCalendar.get(Calendar.MINUTE),
                    true
                ).show()
            },
            dateTimeCalendar.get(Calendar.YEAR),
            dateTimeCalendar.get(Calendar.MONTH),
            dateTimeCalendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    val primaryColor = Color(0xFFEB5757)
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val secondaryTextColor = MaterialTheme.colorScheme.onSurfaceVariant

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Select Your Route",
                        color = onSurfaceColor,
                        fontWeight = FontWeight.Bold
                    )
                },
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
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    if (pickupText.isNotEmpty() && dropoffText.isNotEmpty() && dateTimeText.isNotEmpty()) {
                        onVehicleSelected(
                            pickupText,
                            dropoffText,
                            dateTimeText,
                        )
                    }
                },
                containerColor = primaryColor,
                contentColor = Color.White,
                modifier = Modifier
                    .wrapContentWidth()
                    .heightIn(min = 50.dp, max = 70.dp),
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Confirm Route",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold)
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Next",
                            tint = Color.White,
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .size(22.dp)
                        )
                    }
                },
                icon = {}
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = surfaceColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(surfaceColor)
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 Pickup City Dropdown
            ExposedDropdownMenuBox(
                expanded = expandedPickup,
                onExpandedChange = { expandedPickup = !expandedPickup }
            ) {
                OutlinedTextField(
                    value = pickupText,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Pickup City") },
                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = primaryColor) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = expandedPickup,
                    onDismissRequest = { expandedPickup = false }
                ) {
                    allCities.forEach { city ->
                        DropdownMenuItem(
                            text = { Text("${city.name}, ${city.province}") },
                            onClick = {
                                pickupText = city.name
                                pickupPoint = LatLng(city.latitude, city.longitude)
                                expandedPickup = false
                                coroutineScope.launch {
                                    cameraPositionState.animate(
                                        update = CameraUpdateFactory.newLatLngZoom(pickupPoint!!, 10f),
                                        durationMs = 1000
                                    )
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            ExposedDropdownMenuBox(
                expanded = expandedDropoff,
                onExpandedChange = { expandedDropoff = !expandedDropoff }
            ) {
                OutlinedTextField(
                    value = dropoffText,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Dropoff City") },
                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = primaryColor) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = expandedDropoff,
                    onDismissRequest = { expandedDropoff = false }
                ) {
                    allCities.forEach { city ->
                        DropdownMenuItem(
                            text = { Text("${city.name}, ${city.province}") },
                            onClick = {
                                dropoffText = city.name
                                dropoffPoint = LatLng(city.latitude, city.longitude)
                                expandedDropoff = false
                                coroutineScope.launch {
                                    cameraPositionState.animate(
                                        update = CameraUpdateFactory.newLatLngZoom(dropoffPoint!!, 10f),
                                        durationMs = 1000
                                    )
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))


            OutlinedTextField(
                value = dateTimeText,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Date & Time") },
                leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = "Date", tint = primaryColor) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { showDateTimePicker() },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState
                ) {
                    pickupPoint?.let {
                        Marker(
                            state = MarkerState(position = it),
                            title = "Pickup: $pickupText"
                        )
                    }
                    dropoffPoint?.let {
                        Marker(
                            state = MarkerState(position = it),
                            title = "Dropoff: $dropoffText"
                        )
                    }
                    if (pickupPoint != null && dropoffPoint != null) {
                        Polyline(
                            points = listOf(pickupPoint!!, dropoffPoint!!),
                            color = Color.Red,
                            width = 8f
                        )
                    }
                }
            }
        }
    }
}
