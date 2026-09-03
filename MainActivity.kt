package com.onlinedoctor.app
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.webkit.*

class MainActivity: Activity(){
 private lateinit var web:WebView; private var chooser:ValueCallback<Array<Uri>>?=null
 override fun onCreate(b:Bundle?){super.onCreate(b); web=WebView(this); setContentView(web); requestPerms();
  web.settings.javaScriptEnabled=true; web.settings.domStorageEnabled=true; web.settings.allowFileAccess=true; web.settings.mediaPlaybackRequiresUserGesture=false
  web.webViewClient=WebViewClient(); web.webChromeClient=object:WebChromeClient(){
   override fun onPermissionRequest(r:PermissionRequest){runOnUiThread{r.grant(r.resources)}}
   override fun onShowFileChooser(v:WebView?, cb:ValueCallback<Array<Uri>>?, p:FileChooserParams?):Boolean{chooser?.onReceiveValue(null);chooser=cb;val i=p?.createIntent()?:Intent(Intent.ACTION_GET_CONTENT).apply{type="image/*"};startActivityForResult(i,501);return true}
  }; web.loadUrl("file:///android_asset/www/index.html") }
 private fun requestPerms(){val ps=arrayOf(Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO).filter{checkSelfPermission(it)!=PackageManager.PERMISSION_GRANTED};if(ps.isNotEmpty())requestPermissions(ps.toTypedArray(),44)}
 override fun onActivityResult(r:Int,c:Int,d:Intent?){super.onActivityResult(r,c,d);if(r==501){chooser?.onReceiveValue(if(c==RESULT_OK)WebChromeClient.FileChooserParams.parseResult(c,d) else null);chooser=null}}
 override fun onBackPressed(){if(::web.isInitialized&&web.canGoBack())web.goBack() else super.onBackPressed()}
}
