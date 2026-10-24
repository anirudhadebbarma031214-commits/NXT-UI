package com.nxt.ui
// A regular third-party launcher cannot reliably draw above all apps without special permissions.
// Standby is intentionally an in-activity composable, not an intrusive background overlay service.
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*
@Composable fun StandbyScreen(onExit: () -> Unit) {
 var tick by remember { mutableLongStateOf(System.currentTimeMillis()) }
 LaunchedEffect(Unit) { while (true) { delay(1000); tick = System.currentTimeMillis() } }
 androidx.compose.foundation.layout.Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
   Text(SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(tick)), fontSize = 110.sp, fontWeight = FontWeight.Light, color = Color.White)
   Text(SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault()).format(Date(tick)), fontSize = 26.sp, color = Color(0xFF8BDFFF))
   Spacer(Modifier.height(32.dp))
   FocusTile(onClick = onExit) { Text("Return to NXT UI", color = Color.White) }
  }
 }
}
