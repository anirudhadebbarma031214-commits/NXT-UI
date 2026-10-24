package com.nxt.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import java.text.SimpleDateFormat
import java.util.*

private val white = Color(0xFFF5F9FF)
class MainActivity : ComponentActivity() {
 override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState)
  window.decorView.systemUiVisibility = 5894 or 1024 or 512
  val manager = AppManager(this)
  setContent {
   var standby by remember { mutableStateOf(false) }
   var page by remember { mutableStateOf("Home") }
   var apps by remember { mutableStateOf(manager.apps()) }
   val prefs = remember { getSharedPreferences("nxt", MODE_PRIVATE) }
   val recents = remember { mutableStateListOf<String>() }
   val favorites = remember { mutableStateListOf<String>().apply { addAll(prefs.getString("favorites", "")!!.split('|').filter { it.isNotBlank() }) } }
   fun launch(pkg: String) { if (manager.launch(pkg)) { recents.remove(pkg); recents.add(0, pkg); if (recents.size > 12) recents.removeAt(recents.lastIndex) } }
   BackHandler(standby || page != "Home") { if (standby) standby = false else page = "Home" }
   Box(Modifier.fillMaxSize().background(Brush.linearGradient(listOf(Color(0xFF091225), Color(0xFF162C4E), Color(0xFF0B1222))))) {
    if (standby) StandbyScreen { standby = false } else Column(Modifier.fillMaxSize().padding(horizontal = 46.dp, vertical = 24.dp)) {
     Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
      Text("◈ NXT UI", color = white, fontSize = 30.sp, fontWeight = FontWeight.Bold)
      Text(SimpleDateFormat("EEE, d MMM  •  HH:mm", Locale.getDefault()).format(Date()), color = Color(0xFFB8D8F5), fontSize = 19.sp)
     }
     Spacer(Modifier.height(22.dp))
     Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) { for (tab in listOf("Home", "Apps", "Recent", "System")) FocusTile(onClick = { page = tab; apps = manager.apps() }) { Text(tab, color = white, fontSize = 18.sp) }; FocusTile(onClick = { standby = true }) { Text("Standby ◷", color = white, fontSize = 18.sp) } }
     Spacer(Modifier.height(28.dp))
     Text(when(page) { "Home" -> "Welcome home"; "Apps" -> "Your apps"; "Recent" -> "Recently opened in NXT UI"; else -> "System dashboard" }, color = white, fontSize = 32.sp, fontWeight = FontWeight.SemiBold)
     Spacer(Modifier.height(18.dp))
     if (page == "System") {
      Text("Free storage: ${manager.freeStorage()/1024/1024} MB     Available RAM: ${manager.availableMemory()/1024/1024} MB", color = white, fontSize = 22.sp)
      Spacer(Modifier.height(20.dp))
      Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
       FocusTile(onClick = { startActivity(Intent(Settings.ACTION_SETTINGS)) }) { Text("Android Settings", color = white) }
       FocusTile(onClick = { startActivity(Intent(Settings.ACTION_WIFI_SETTINGS)) }) { Text("Wi-Fi Settings", color = white) }
       FocusTile(onClick = { startActivity(Intent(Settings.ACTION_BLUETOOTH_SETTINGS)) }) { Text("Bluetooth Settings", color = white) }
      }
      Text("HDMI input switching requires TV manufacturer integration.", color = Color.LightGray, modifier = Modifier.padding(top = 20.dp))
     } else {
      val shown = when(page) { "Recent" -> recents.mapNotNull { pkg -> apps.find { it.packageName == pkg } }; "Home" -> favorites.mapNotNull { pkg -> apps.find { it.packageName == pkg } }.ifEmpty { apps.take(10) }; else -> apps }
      LazyVerticalGrid(columns = GridCells.Fixed(5), modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(14.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
       items(shown, key = { it.packageName }) { app -> Column {
        FocusTile(modifier = Modifier.fillMaxWidth().height(92.dp), onClick = { launch(app.packageName) }) {
         Column { Text("▣", fontSize = 26.sp, color = Color(0xFF74D9FF)); Text(app.name, maxLines = 1, overflow = TextOverflow.Ellipsis, color = white, fontSize = 16.sp) }
        }
        if (page == "Apps") Row {
         FocusTile(onClick = { if (favorites.contains(app.packageName)) favorites.remove(app.packageName) else favorites.add(app.packageName); prefs.edit().putString("favorites", favorites.joinToString("|")).apply() }) { Text(if (favorites.contains(app.packageName)) "★" else "☆", color = white) }
         FocusTile(onClick = { manager.appSettings(app.packageName) }) { Text("Info", color = white) }
        }
       } }
      }
      if (page == "Home") { Text("FLOATING DOCK  •  Choose favorites in Apps", color = Color(0xFFA8C8E9), fontSize = 14.sp); Spacer(Modifier.height(8.dp)); Row(Modifier.fillMaxWidth().background(Color(0x66344E70), RoundedCornerShape(26.dp)).padding(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) { for (app in shown.take(7)) FocusTile(onClick = { launch(app.packageName) }) { Text(app.name.take(12), color = white, fontSize = 15.sp) } } }
     }
    }
   }
  }
 }
}
