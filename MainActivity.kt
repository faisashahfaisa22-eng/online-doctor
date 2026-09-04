package com.onlinedoctor.app

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.webkit.PermissionRequest
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast

class MainActivity : Activity() {
    private lateinit var webView: WebView
    private var fileChooser: ValueCallback<Array<Uri>>? = null
    private var pendingWebPermissionRequest: PermissionRequest? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webView = WebView(this)
        setContentView(webView)
        requestRuntimePermissions()

        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            allowFileAccess = true
            allowContentAccess = true
            mediaPlaybackRequiresUserGesture = false
            mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
            setSupportZoom(false)
        }

        webView.webViewClient = WebViewClient()
        webView.webChromeClient = object : WebChromeClient() {
            override fun onPermissionRequest(request: PermissionRequest) {
                runOnUiThread {
                    val wantsAudio = request.resources.contains(PermissionRequest.RESOURCE_AUDIO_CAPTURE)
                    val wantsVideo = request.resources.contains(PermissionRequest.RESOURCE_VIDEO_CAPTURE)
                    val audioGranted = !wantsAudio || checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                    val cameraGranted = !wantsVideo || checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

                    if (audioGranted && cameraGranted) {
                        val allowed = request.resources.filter {
                            it == PermissionRequest.RESOURCE_AUDIO_CAPTURE ||
                                it == PermissionRequest.RESOURCE_VIDEO_CAPTURE
                        }.toTypedArray()
                        request.grant(allowed)
                    } else {
                        pendingWebPermissionRequest = request
                        requestRuntimePermissions()
                    }
                }
            }

            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                fileChooser?.onReceiveValue(null)
                fileChooser = filePathCallback
                val intent = fileChooserParams?.createIntent()
                    ?: Intent(Intent.ACTION_GET_CONTENT).apply { type = "image/*" }
                return try {
                    startActivityForResult(intent, FILE_CHOOSER_REQUEST)
                    true
                } catch (_: ActivityNotFoundException) {
                    fileChooser?.onReceiveValue(null)
                    fileChooser = null
                    Toast.makeText(this@MainActivity, "No image picker found.", Toast.LENGTH_SHORT).show()
                    false
                }
            }
        }

        webView.loadUrl("file:///android_asset/www/index.html")
    }

    private fun requestRuntimePermissions() {
        val required = arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
            .filter { checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED }
        if (required.isNotEmpty()) requestPermissions(required.toTypedArray(), PERMISSION_REQUEST)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST) {
            val request = pendingWebPermissionRequest ?: return
            pendingWebPermissionRequest = null
            val allowed = request.resources.filter {
                (it == PermissionRequest.RESOURCE_AUDIO_CAPTURE &&
                    checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) ||
                (it == PermissionRequest.RESOURCE_VIDEO_CAPTURE &&
                    checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
            }.toTypedArray()
            if (allowed.isNotEmpty()) request.grant(allowed) else request.deny()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_CHOOSER_REQUEST) {
            val result = if (resultCode == RESULT_OK)
                WebChromeClient.FileChooserParams.parseResult(resultCode, data)
            else null
            fileChooser?.onReceiveValue(result)
            fileChooser = null
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (::webView.isInitialized && webView.canGoBack()) webView.goBack()
        else super.onBackPressed()
    }

    override fun onDestroy() {
        if (::webView.isInitialized) webView.destroy()
        super.onDestroy()
    }

    companion object {
        private const val FILE_CHOOSER_REQUEST = 501
        private const val PERMISSION_REQUEST = 44
    }
}
