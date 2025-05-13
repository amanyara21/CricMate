package com.aman.cricmate.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.aman.cricmate.model.Constants
import com.aman.cricmate.viewModel.UserProfileViewModel
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun PlayerProfileScreen(
    navController: NavController,
    userId: String,
    isMe: Boolean,
    viewModel: UserProfileViewModel = hiltViewModel()
) {
    val playerDetails by viewModel.playerDetails.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val logout = viewModel.logout

    val formattedDate = playerDetails?.dob?.let {
        SimpleDateFormat(
            "dd-MM-yyyy",
            Locale.getDefault()
        ).format(it)
    }
    LaunchedEffect(userId) {
        viewModel.getPlayerDetails(userId)
    }
    LaunchedEffect(logout) {
        if(logout){
            navController.navigate("Login"){
                popUpTo("main"){inclusive=true}
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFE3F2FD), Color(0xFFBBDEFB))))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Player Profile",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(vertical = 16.dp)
        )

        when {
            isLoading -> CircularProgressIndicator()

            error != null -> Text(text = error ?: "Error", color = Color.Red)

            playerDetails != null -> {
                val details = playerDetails!!

                AsyncImage(
                    model = Constants.serverUrl+ details.user.profilePhoto,
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(4.dp, Color(0xFF1976D2), CircleShape)
                        .shadow(10.dp, CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(18.dp))

                Text(text = "Name : ${details.user.name}")
                Text(text = "Email : ${details.user.email}")

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InfoCard(
                        title = "Name ",
                        value = details.user.name,
                        icon = Icons.Default.Person,
                        modifier = Modifier.weight(1f),
                        cardColor = Color(0xFFD1C4E9)
                    )
                    InfoCard(
                        title = "DOB",
                        value = formattedDate!!,
                        icon = Icons.Default.DateRange,
                        modifier = Modifier.weight(1f),
                        cardColor = Color(0xFFFFF59D)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InfoCard(
                        title = "Bowling Type",
                        value = details.bowlingType,
                        icon = Icons.Default.Add,
                        modifier = Modifier.weight(1f),
                        cardColor = Color(0xFFB2EBF2)
                    )
                    InfoCard(
                        title = "Bowling Arm",
                        value = details.bowlingArm,
                        icon = Icons.Default.AddCircle,
                        modifier = Modifier.weight(1f),
                        cardColor = Color(0xFFFFCCBC)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                CoachesCard(
                    title = "Coaches",
                    coaches = details.coaches.map { it.name },
                    icon = Icons.Default.Person,
                    modifier = Modifier.fillMaxWidth(),
                    cardColor = Color(0xFFDCEDC8)
                )
                if(isMe) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { viewModel.userLogout() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD32F2F),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .height(56.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Logout",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Logout",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InfoCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    cardColor: Color
) {
    Card(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = title, tint = Color.Black, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(title, fontWeight = FontWeight.Medium, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF212121))
        }
    }
}

@Composable
fun CoachesCard(
    title: String,
    coaches: List<String>,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    cardColor: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = title, tint = Color.Black, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(title, fontWeight = FontWeight.Medium, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            coaches.forEach {
                Text(
                    text = "- $it",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF424242),
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }
}

