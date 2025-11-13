package com.example.truckingtracker.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.truckingtracker.reterofit.BookingItem
import com.example.truckingtracker.viewmodels.AuthViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingRecordScreen(
    onBackClick: () -> Unit = {},
    viewModel: AuthViewModel = viewModel(),
    userId: Int = 2
) {
    val primaryColor = Color(0xFFEB5757)
    var bookingList by remember { mutableStateOf<List<BookingItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Helper function to load data
    val fetchData: () -> Unit = {
        isLoading = true
        scope.launch {
            viewModel.loadBookings(
                context = context,
                userId = userId,
                onResult = { list, error ->
                    bookingList = list
                    errorMessage = error
                    isLoading = false
                }
            )
        }
    }

    // ✅ Pull-to-refresh setup
    val pullToRefreshState = rememberPullToRefreshState()

    // Initial API call
    LaunchedEffect(Unit) {
        fetchData()
    }

    Scaffold(
        // ... (TopAppBar content remains the same) ...
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Booking History",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = primaryColor)
            )
        }
    ) { padding ->
        // 🚀 CORRECT M3 Implementation: PullToRefreshBox wraps the entire content
        PullToRefreshBox(
            isRefreshing = isLoading,
            state = pullToRefreshState,
            onRefresh = fetchData, // Use the defined helper function
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            // Box is now inside the PullToRefreshBox to center non-scrollable content
            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    // Only show the CircularProgressIndicator when loading and no data exists
                    isLoading && bookingList.isEmpty() -> {
                        CircularProgressIndicator(
                            color = primaryColor,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    errorMessage != null -> {
                        Text(
                            text = errorMessage ?: "Error fetching data",
                            color = Color.Red,
                            fontSize = 16.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    bookingList.isEmpty() -> {
                        Text(
                            "No bookings found",
                            color = Color.Gray,
                            fontSize = 16.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(bookingList) { record ->
                                BookingCard(record)
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun BookingCard(record: BookingItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = record.vehicle_name ?: "N/A",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF333333)
            )
            Text(record.vehicle_type ?: "", color = Color.Gray, fontSize = 14.sp)

            HorizontalDivider(
                color = Color.LightGray.copy(alpha = 0.6f),
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Using "N/A" as a default value if the API returns null for a field
            Text("Pickup: ${record.pickup ?: record.pickup_location ?: "N/A"}")
            Text("Dropoff: ${record.dropoff ?: record.dropoff_location ?: "N/A"}")
            Text("Date: ${record.date ?: "N/A"}  Time: ${record.time ?: "N/A"}")
            Text("Weight: ${record.weight ?: "N/A"} KG")
            Text("Goods: ${record.goods ?: "N/A"}")
            Text("Payer: ${record.payer ?: "N/A"}")
            Text("Labourers: ${record.labour_count ?: "N/A"}")
            Text("Time Relaxation: ${record.time_relaxation ?: "N/A"}")
            if (!record.additional_info.isNullOrEmpty())
                Text("Note: ${record.additional_info}", color = Color.DarkGray)
        }
    }
}