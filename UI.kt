package com.example.safescan

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

//bottomPart
@Composable
fun SafeScanApp() {

    var screen by remember { mutableStateOf("dashboard") }

    val threatCount by remember { derivedStateOf { ThreatManager.threatCount } }

    Scaffold(
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(16.dp)
        ) {
            Text("SafeScan", color = Color.White, fontSize = 20.sp)
        }

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

// implication
object ThreatManager {
    var threatCount by mutableStateOf(0)

    fun updateThreat(count: Int) {
        threatCount = count
    }
}


//ScanPart
@Composable
fun ScanScreen(onStartScan: () -> Unit) {

    Column(Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(16.dp)
        ) {
            Text("Scan", color = Color.White)
        }

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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(16.dp)
        ) {
            Text("Task Manager", color = Color.White)
        }

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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(16.dp)
        ) {
            Text("Quarantine", color = Color.White)
        }

        // empty
    }
}


//ReportsPart
@Composable
fun ReportsScreen() {

    Column {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(16.dp)
        ) {
            Text("Reports", color = Color.White)
        }

        // empty
    }
}
// use for test
data class AppInfo(
    val name: String,
    val packageName: String,
    val icon: android.graphics.drawable.Drawable
)

fun getInstalledApps(context: android.content.Context): List<AppInfo> {
    return emptyList()
}
