package com.example.truckingtracker.screen

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.example.truckingtracker.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// --- Color Setup ---
private val PrimaryRed = Color(0xFFEB5757)
private val LightBackground = Color(0xFFF5F5F5)
private val DarkBackground = Color(0xFF121212)
private val SurfaceLight = Color.White
private val SurfaceDark = Color(0xFF1E1E1E)

// --- Data Model ---
data class ServiceItem(val name: String, val imageResId: Int)

val ServiceData = listOf(
    ServiceItem("Small Pickup", R.drawable.pickupsmall_vector),
    ServiceItem("Medium Pickup", R.drawable.pickupmedium_vector),
    ServiceItem("Heavy Vehicle", R.drawable.pickupheavy_vector),
    ServiceItem("Train Cargo", R.drawable.pickuptrain_vector),
    ServiceItem("Air Cargo", R.drawable.pickupair_vector)
)

val CarouselImages = listOf(
    R.drawable.registerscreen_bg,
    R.drawable.babekhyber_img,
    R.drawable.chamancity_img,
    R.drawable.delhigate_img
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SjTransportersScreen(
    onClickDrawer: () -> Unit,
    moveToRouteMapScreen:(vehicleName: String, vehicleType: String, imageRes: Int) -> Unit
) {
    val isDark = isSystemInDarkTheme()

    val colors = if (isDark) {
        darkColorScheme(
            primary = PrimaryRed,
            background = DarkBackground,
            surface = SurfaceDark,
            onSurface = Color.White
        )
    } else {
        lightColorScheme(
            primary = PrimaryRed,
            background = LightBackground,
            surface = SurfaceLight,
            onSurface = Color.Black
        )
    }

    // ✅ These must be INSIDE composable:
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedService by remember { mutableStateOf<ServiceItem?>(null) }

    MaterialTheme(colorScheme = colors) {
        Scaffold(
            topBar = { TopAppBarSection { onClickDrawer() } },
            modifier = Modifier.fillMaxSize()
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                HeaderImageCarousel()
                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Services",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(Modifier.height(8.dp))

                ServicesGrid(
                    services = ServiceData,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    onServiceClick = { selectedService = it }
                )

                Spacer(Modifier.height(24.dp))

              //  ValidationExample(Modifier.padding(horizontal = 16.dp))
               // Spacer(Modifier.height(32.dp))
            }

            // ✅ Bottom Sheet logic
            if (selectedService != null) {
                ServiceDetailsBottomSheet(
                    service = selectedService!!,
                    onDismiss = {
                        coroutineScope.launch { sheetState.hide() }
                        selectedService = null
                    },
                    onConfirm = { vehicleName, vehicleType, imageRes ->
                        moveToRouteMapScreen(vehicleName, vehicleType, imageRes)
                        coroutineScope.launch {
                            sheetState.hide()

                        }
                        selectedService = null
                        // You can show a Toast or log result here:
                        // Toast.makeText(context, "Selected: $selectedVehicle", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarSection(
    onClickDrawer: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                "SJ Transporters",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        },
        navigationIcon = {
            IconButton(onClick = onClickDrawer) {
                Icon(
                    Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        actions = {
            IconButton(onClick = { }) {
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Composable
fun HeaderImageCarousel() {
    val imageList = CarouselImages
    var currentIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            currentIndex = (currentIndex + 1) % imageList.size
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Crossfade(targetState = currentIndex, label = "ImageFade") { page ->
                    Image(
                        painter = painterResource(id = imageList[page]),
                        contentDescription = "Carousel Image $page",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    imageList.forEachIndexed { index, _ ->
                        val color = if (index == currentIndex)
                            MaterialTheme.colorScheme.primary
                        else
                            Color.White.copy(alpha = 0.5f)

                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .clip(CircleShape)
                                .background(color)
                                .size(8.dp)
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.annoucement_vector),
                contentDescription = "News Icon",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "Afghanistan and Pakistan agree to enhance bilateral trade",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ServicesGrid(
    services: List<ServiceItem>,
    modifier: Modifier = Modifier,
    onServiceClick: (ServiceItem) -> Unit
) {
    val gridItems = services.subList(0, services.size - 1)
    val fullWidthItem = services.last()

    Column() {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier,
            contentPadding = PaddingValues(12.dp)
        ) {
            items(gridItems.size) { index ->
                ServiceCard(gridItems[index], onServiceClick = onServiceClick)
            }
            item(span = { GridItemSpan(2) }) {
                ServiceCard(fullWidthItem, isFullWidth = true, onServiceClick = onServiceClick)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

    }
}
/*
@Composable
fun ServiceCard(item: ServiceItem, isFullWidth: Boolean = false, onServiceClick: (ServiceItem) -> Unit) {
    Card(
        modifier = Modifier
            .run { if (isFullWidth) fillMaxWidth() else aspectRatio(1f) }
            .height(if (isFullWidth) 120.dp else 160.dp)
            .clickable { onServiceClick(item) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = item.imageResId),
                contentDescription = item.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(if (isFullWidth) 0.8f else 1f)
                    .padding(bottom = 4.dp)
            )
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}*/

@Composable
fun ServiceCard(item: ServiceItem, isFullWidth: Boolean = false, onServiceClick: (ServiceItem) -> Unit) {
    Box(
        modifier = Modifier
            .run { if (isFullWidth) fillMaxWidth() else aspectRatio(1f) }
            .height(160.dp)
            .clickable { onServiceClick(item) },
        contentAlignment = Alignment.TopCenter
    ) {
        Card(
            modifier = Modifier
                .padding(top = 45.dp) // Push card down so half image shows above
                .height(160.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(8.dp)

                )
            }
        }
        Image(
            painter = painterResource(id = item.imageResId),
            contentDescription = item.name,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(140.dp)
                .align(Alignment.TopCenter)
                .padding(top = 12.dp)

        )
    }
}
@Composable
fun ValidationExample(modifier: Modifier = Modifier) {
    var trackingId by remember { mutableStateOf("") }
    var isValid by remember { mutableStateOf(true) }
    var validationMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    fun validateAndSubmit() {
        isValid = true
        validationMessage = ""

        when {
            trackingId.isBlank() -> {
                isValid = false
                validationMessage = "Tracking ID cannot be empty."
            }
            trackingId.length != 10 -> {
                isValid = false
                validationMessage = "Tracking ID must be 10 characters."
            }
            !trackingId.all { it.isLetterOrDigit() } -> {
                isValid = false
                validationMessage = "Tracking ID can only contain letters and numbers."
            }
            else -> {
                isLoading = true
                validationMessage = "Searching for shipment..."
                scope.launch {
                    delay(1500)
                    isLoading = false
                    validationMessage = "Shipment Found! Status: Delivered."
                }
            }
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            "Track Shipment",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = trackingId,
            onValueChange = {
                trackingId = it.uppercase()
                if (!isValid || validationMessage.contains("Found")) {
                    isValid = true
                    validationMessage = ""
                }
            },
            label = { Text("Enter 10-Character Tracking ID") },
            isError = !isValid,
            supportingText = {
                if (validationMessage.isNotEmpty()) {
                    Text(
                        text = validationMessage,
                        color = when {
                            !isValid -> MaterialTheme.colorScheme.error
                            isLoading -> MaterialTheme.colorScheme.primary
                            else -> MaterialTheme.colorScheme.primary
                        }
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = ::validateAndSubmit,
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 3.dp
                )
            } else {
                Text(
                    "Track Shipment",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 1500)
@Composable
fun LightPreview() {
    SjTransportersScreen(onClickDrawer = {}){
        vehicleName, vehicleType, imageRes ->
    }
}

