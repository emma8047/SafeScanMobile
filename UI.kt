package com.example.securitydashboard

import android.os.Bundle
import android.content.Context
import android.content.pm.ApplicationInfo
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import kotlinx.coroutines.delay
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.window.*
import androidx.compose.ui.res.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.graphics.painter.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.drawscope.Stroke

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            SecurityApp()

        }
    }
}

@BottomPart
fun SafeScanApp() {

    var screen by remember { mutableStateOf("dashboard") }

    var risk by remember { mutableStateOf(0.1f) }

    Scaffold(

        bottomBar = {

            NavigationBar {

                NavigationBarItem(
                    selected = screen == "dashboard",
                    onClick = { screen = "dashboard" },
                    icon = { Icon(Icons.Default.Home,"") },
                    label = { Text("Dashboard") }
                )

                NavigationBarItem(
                    selected = screen == "scan",
                    onClick = { screen = "scan" },
                    icon = { Icon(Icons.Default.Search,"") },
                    label = { Text("Scan") }
                )

                NavigationBarItem(
                    selected = screen == "tasks",
                    onClick = { screen = "tasks" },
                    icon = { Icon(Icons.Default.List,"") },
                    label = { Text("Tasks") }
                )

                NavigationBarItem(
                    selected = screen == "quarantine",
                    onClick = { screen = "quarantine" },
                    icon = { Icon(Icons.Default.Warning,"") },
                    label = { Text("Quarantine") }
                )

                NavigationBarItem(
                    selected = screen == "reports",
                    onClick = { screen = "reports" },
                    icon = { Icon(Icons.Default.Info,"") },
                    label = { Text("Reports") }
                )

            }

        }

    ) { padding ->

        Box(modifier = Modifier.padding(padding)) {

            when(screen) {

                "dashboard" -> DashboardScreen(risk) { risk = it }

                "scan" -> ScanScreen()

                "tasks" -> TaskManagerScreen()

                "quarantine" -> QuarantineScreen()

                "reports" -> ReportsScreen()

            }

        }

    }

}

@DashboardPart
fun DashboardScreen() {

    var risk by remember { mutableStateOf(0.3f) }

    LaunchedEffect(Unit){

        while(true){

            risk = (10..90).random() / 100f
            delay(2000)

        }

    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){

        Text(
            "System Risk Level",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(30.dp))

        RiskCircle(risk)

    }

}

@RiskCirclePart
@InsideDashboardPart
fun RiskCircle(risk: Float) {

    Canvas(
        modifier = Modifier.size(220.dp)
    ){

        val sweep = risk * 360f

        drawArc(
            color = Color.Green,
            startAngle = -90f,
            sweepAngle = 360f - sweep,
            useCenter = false,
            style = Stroke(40f)
        )

        drawArc(
            color = Color.Red,
            startAngle = -90f + (360f - sweep),
            sweepAngle = sweep,
            useCenter = false,
            style = Stroke(40f)
        )

    }

}

data class AppInfo(

    val name:String,
    val packageName:String,
    val version:String,
    val icon:Drawable

)

fun getInstalledApps(context: Context): List<AppInfo> {

    val pm = context.packageManager

    val apps = pm.getInstalledApplications(0)

    return apps.map {

        val name = pm.getApplicationLabel(it).toString()

        val version = try {
            pm.getPackageInfo(it.packageName,0).versionName ?: "1.0"
        } catch (e:Exception){
            "1.0"
        }

        val icon = pm.getApplicationIcon(it)

        AppInfo(name,it.packageName,version,icon)

    }.sortedBy { it.name }

}

@ScanPart
fun ScanScreen() {

    var scanning by remember { mutableStateOf(false) }

    var text by remember { mutableStateOf("Press Start") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {

        Spacer(Modifier.height(100.dp))

        Text(text, fontSize = 30.sp)

        Spacer(Modifier.height(30.dp))

        Button(onClick = {

            scanning = true
            text = "Scanning..."

        }) {

            Text("Start Scan")

        }

        if(scanning) {

            LaunchedEffect(Unit) {

                delay(2000)

                text = "SYSTEM CHECK COMPLETE"

                scanning = false

            }

        }

    }

}


@TaskManagerPart
fun TaskManagerScreen() {

    val context = LocalContext.current

    var apps by remember { mutableStateOf<List<AppInfo>>(emptyList()) }

    var selected by remember { mutableStateOf<AppInfo?>(null) }

    LaunchedEffect(Unit){

        apps = getInstalledApps(context)

        if(apps.isNotEmpty())
            selected = apps[0]

    }

    LazyColumn{

        items(apps){

            ListItem(

                headlineContent = { Text(it.name) },

                supportingContent = { Text(it.packageName) },

                leadingContent = {

                    Image(
                        painter = rememberDrawablePainter(it.icon),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )

                },

                modifier = Modifier.clickable {

                    selected = it

                }

            )

        }

    }

    selected?.let {

        AppDetailDialog(it)

    }

}

@InsideTaskManagerPart
fun AppDetailDialog(app: AppInfo) {

    var cpu by remember { mutableStateOf(5) }
    var ram by remember { mutableStateOf(120) }

    LaunchedEffect(Unit){

        while(true){

            cpu = (1..30).random()
            ram = (80..500).random()

            delay(1200)

        }

    }

    Box(
        Modifier
            .fillMaxWidth()
            .background(Color(0x88000000))
    ){

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ){

            Column(
                Modifier.padding(20.dp)
            ){

                Row(verticalAlignment = Alignment.CenterVertically){

                    Image(
                        painter = rememberDrawablePainter(app.icon),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp)
                    )

                    Spacer(Modifier.width(12.dp))

                    Text(
                        app.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )

                }

                Spacer(Modifier.height(16.dp))

                Text("Package: ${app.packageName}")

                Spacer(Modifier.height(6.dp))

                Text("Version: ${app.version}")

                Spacer(Modifier.height(16.dp))

                Text("CPU Usage: $cpu%")

                LinearProgressIndicator(
                    progress = cpu / 100f,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                Text("RAM Usage: $ram MB")

            }

        }

    }

}

@QuarantinePart
fun QuarantineScreen() {

    Column {

        ListItem(
            headlineContent = { Text("Sketchy Cleaner") },
            supportingContent = { Text("Suspicious network activity") }
        )

        ListItem(
            headlineContent = { Text("Flash Boost Pro") },
            supportingContent = { Text("Adware detected") }
        )

    }

}

@ReportsPart
fun ReportsScreen() {

    Column {

        ListItem(
            headlineContent = { Text("Scans Done") },
            supportingContent = { Text("19 completed") }
        )

        ListItem(
            headlineContent = { Text("Threats Found") },
            supportingContent = { Text("247 detected") }
        )

        ListItem(
            headlineContent = { Text("Apps Blocked") },
            supportingContent = { Text("24 blocked") }
        )

    }

}