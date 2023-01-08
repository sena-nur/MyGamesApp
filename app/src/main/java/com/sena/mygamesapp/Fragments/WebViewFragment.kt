package com.sena.mygamesapp.Fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.sena.mygamesapp.R
import com.sena.mygamesapp.databinding.FragmentWebViewBinding


class WebViewFragment : Fragment(R.layout.fragment_web_view) {
    private var _binding: FragmentWebViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentWebViewBinding.bind(view)
        val url = arguments?.getString("url")
        // this will load the url of the website
        binding.webView.loadUrl(url!!)
        // this will enable the javascript settings, it can also allow xss vulnerabilities
        binding.webView.settings.javaScriptEnabled = true
        // if you want to enable zoom feature
        binding.webView.settings.setSupportZoom(true)
        binding.webView.setInitialScale(1);
        binding.webView.settings.loadWithOverviewMode = true
        binding.webView.settings.useWideViewPort = true
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
                if (url.lowercase().startsWith("http") || url.lowercase()
                        .startsWith("https") || url.lowercase().startsWith("file")
                ) {
                    webView.loadUrl(url)
                } else if (Uri.parse(url).getScheme().equals("market")) {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        val activity = view.getContext();
                        activity.startActivity(intent);
                        return true;
                    } catch (e: ActivityNotFoundException)
                    {
                        // Google Play app is not installed, you may want to open the app store link
                        // Link will open your browser
                        val uri = Uri.parse(url);
                        webView.loadUrl("http://play.google.com/store/apps/" + uri.getHost() + "?" + uri.getQuery());
                        return false;
                    }

                } else {
                    try {
                        val uri = Uri.parse(url)
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    } catch (e: Exception) {
                        Log.d("WebView", "Webview Error:" + e.message)
                    }
                }
                return true
            }
            override fun onPageStarted(view: WebView?, url: String?, facIcon: Bitmap?) {
                binding.loadingLayout.isVisible = true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                binding.loadingLayout.isVisible = false
            }
        }
    }

    override fun onDestroyView() {
        //clean up any references to the binding class instance in the fragment's
        super.onDestroyView()
        _binding = null
    }
}