package com.nxt.ui

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.StatFs
import android.app.ActivityManager
import android.provider.Settings
import android.net.Uri

data class TvApp(val name: String, val packageName: String)
class AppManager(private val context: Context) {
 fun apps(): List<TvApp> {
  val pm = context.packageManager
  val result = linkedMapOf<String, TvApp>()
  for (category in listOf(Intent.CATEGORY_LEANBACK_LAUNCHER, Intent.CATEGORY_LAUNCHER)) {
   val intent = Intent(Intent.ACTION_MAIN).addCategory(category)
   val matches = if (Build.VERSION.SDK_INT >= 33) pm.queryIntentActivities(intent, PackageManager.ResolveInfoFlags.of(0)) else @Suppress("DEPRECATION") pm.queryIntentActivities(intent, 0)
   for (item in matches) { val pkg = item.activityInfo.packageName; if (pkg != context.packageName) result[pkg] = TvApp(item.loadLabel(pm).toString(), pkg) }
  }
  return result.values.sortedBy { it.name.lowercase() }
 }
 fun launch(pkg: String): Boolean = try { val intent = context.packageManager.getLeanbackLaunchIntentForPackage(pkg) ?: context.packageManager.getLaunchIntentForPackage(pkg); if (intent != null) { context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)); true } else false } catch (_: Exception) { false }
 fun appSettings(pkg: String) { context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$pkg")).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) }
 fun freeStorage(): Long = StatFs(android.os.Environment.getDataDirectory().path).availableBytes
 fun availableMemory(): Long { val info = ActivityManager.MemoryInfo(); (context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).getMemoryInfo(info); return info.availMem }
}
