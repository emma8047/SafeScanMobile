package com.example.safescan

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.foundation.Canvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.layout.statusBarsPadding


//bottomPart
@Composable
fun SafeScanApp() {

    var screen by remember { mutableStateOf("dashboard") }

    var isLoggedIn by remember { mutableStateOf(false) }

    var userAvatar by remember { mutableStateOf<Int?>(null) }

    val threatCount by remember { derivedStateOf { ThreatManager.threatCount } }

    Scaffold(
        topBar = {
            TopBar(
                isLoggedIn = isLoggedIn,
                userAvatar = userAvatar,
                onProfileClick = {
                    if (isLoggedIn) {
                        screen = "profile"
                    } else {
                        screen = "login"
                    }
                }
            )
        },

        bottomBar = {
            NavigationBar {

                NavigationBarItem(
                    selected = screen == "dashboard",
                    onClick = { screen = "dashboard" },
                    icon = { Icon(Icons.Default.Home, "") },
                    label = { Text("Dashboard") }
                )

                NavigationBarItem(
                    selected = screen == "scan",
                    onClick = { screen = "scan" },
                    icon = { Icon(Icons.Default.Search, "") },
                    label = { Text("Scan") }
                )

                NavigationBarItem(
                    selected = screen == "tasks",
                    onClick = { screen = "tasks" },
                    icon = { Icon(Icons.AutoMirrored.Filled.List, "") },
                    label = { Text("Tasks") }
                )

                NavigationBarItem(
                    selected = screen == "quarantine",
                    onClick = { screen = "quarantine" },
                    icon = { Icon(Icons.Default.Warning, "") },
                    label = { Text("Quarantine") }
                )

                NavigationBarItem(
                    selected = screen == "reports",
                    onClick = { screen = "reports" },
                    icon = { Icon(Icons.Default.Info, "") },
                    label = { Text("Reports") }
                )
            }
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            when (screen) {

                "dashboard" -> DashboardScreen(threatCount) {}

                "scan" -> ScanScreen {
                    // TODO: implication
                }

                "tasks" -> TaskManagerScreen()

                "quarantine" -> QuarantineScreen()

                "reports" -> ReportsScreen()

                "login" -> LoginScreen(
                    onLoginSuccess = {
                        isLoggedIn = true
                        userAvatar = android.R.drawable.sym_def_app_icon
                        screen = "dashboard"
                    }
                )

                "profile" -> ProfileScreen {
                    isLoggedIn = false
                    userAvatar = null
                    screen = "dashboard"
                }

            }
        }
    }
}
//TopBar
@Composable
fun TopBar(
    isLoggedIn: Boolean,
    userAvatar: Int?,
    onProfileClick: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "SafeScan",
                    color = Color.White,
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.weight(1f))

                if (isLoggedIn && userAvatar != null) {
                    Image(
                        painter = painterResource(userAvatar),
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .clickable { onProfileClick() }
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Login",
                        tint = Color.White,
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { onProfileClick() }
                    )
                }
            }
        }
    }
}



//Dashboard
@Composable
fun DashboardScreen(threatCount: Int, onThreatUpdate: (Int) -> Unit) {

    val color = when {
        threatCount <= 0 -> Color.Green
        threatCount in 1..3 -> Color.Yellow
        else -> Color.Red
    }

    val text = when {
        threatCount <= 0 -> "Secure"
        threatCount in 1..3 -> "Threat"
        else -> "Danger"
    }

    Column(Modifier.fillMaxSize()) {

        Spacer(Modifier.height(40.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {

            Canvas(modifier = Modifier.size(220.dp)) {
                drawArc(
                    color = color,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(40f)
                )
            }

            Text(text, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
    }
}




//ScanPart
@Composable
fun ScanScreen(onStartScan: () -> Unit) {

    Column(Modifier.fillMaxSize()) {

        Spacer(Modifier.height(100.dp))

        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Button(onClick = {
                // TODO: implication
                onStartScan()
            }) {
                Text("System Check")
            }
        }
    }
}



//TaskManagerPart
@Composable
fun TaskManagerScreen() {

    val context = LocalContext.current
    var apps by remember { mutableStateOf<List<AppInfo>>(emptyList()) }
    var selected by remember { mutableStateOf<AppInfo?>(null) }

    LaunchedEffect(Unit) {
        apps = getInstalledApps(context)
    }

    Column {

        LazyColumn {
            items(apps) { app ->
                ListItem(
                    headlineContent = { Text(app.name) },
                    leadingContent = {
                        Image(
                            painter = painterResource(android.R.drawable.sym_def_app_icon),
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )
                    },
                    modifier = Modifier.clickable { selected = app }
                )
            }
        }
    }

    selected?.let {
        AppDetailDialog(it) { selected = null }
    }
}

//Dialog Inside the Taskmanager
@Composable
fun AppDetailDialog(app: AppInfo, onClose: () -> Unit) {

    val context = LocalContext.current

    val size = remember {
        try {
            val file = context.packageManager
                .getApplicationInfo(app.packageName, 0)
                .sourceDir
            java.io.File(file).length() / (1024 * 1024)
        } catch (e: Exception) {
            0
        }
    }

    Dialog(onDismissRequest = onClose) {

        Card {
            Column(Modifier.padding(20.dp)) {

                Text(app.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)

                Spacer(Modifier.height(10.dp))

                Text("Size: ${size} MB")

                Spacer(Modifier.height(20.dp))

                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomEnd) {
                    Image(
                        painter = painterResource(android.R.drawable.sym_def_app_icon),
                        contentDescription = null,
                        modifier = Modifier.size(60.dp)
                    )
                }
            }
        }
    }
}


//QuarantinePart
@Composable
fun QuarantineScreen() {

    Column {
        // empty
    }
}


//ReportsPart
@Composable
fun ReportsScreen() {

    Column {
        // empty
    }
}

//Login
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.height(80.dp))

        Text("Login", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(40.dp))

        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") }
        )

        Spacer(Modifier.height(16.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") }
        )

        Spacer(Modifier.height(30.dp))

        Button(onClick = { onLoginSuccess() }) {
            Text("Login")
        }

        Spacer(Modifier.height(20.dp))

        Button(onClick = {
            // TODO: Google login
        },
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = "Google Login",
                tint = Color.Unspecified,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

//Profile
@Composable
fun ProfileScreen(onLogout: () -> Unit) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.height(100.dp))

        Text("User Profile", fontSize = 24.sp)

        Spacer(Modifier.height(30.dp))

        Button(onClick = onLogout) {
            Text("Logout")
        }
    }
}



// implication

// For DashBoard
object ThreatManager {
    var threatCount by mutableStateOf(0)

    fun updateThreat(count: Int) {
        threatCount = count
    }
}

// For TaskManager
data class AppInfo(
    val name: String,
    val packageName: String,
    val icon: android.graphics.drawable.Drawable
)
fun getInstalledApps(context: android.content.Context): List<AppInfo> {
    return emptyList()
}
