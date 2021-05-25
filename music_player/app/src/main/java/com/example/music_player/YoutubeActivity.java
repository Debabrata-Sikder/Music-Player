package com.example.music_player;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class YoutubeActivity extends AppCompatActivity {
    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);
        webview = findViewById(R.id.webviewport);
        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl("https://www.youtube.com");
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }
    @Override
    public void onBackPressed() {
        if (webview.canGoBack())
        {
            webview.goBack();
        }
        else
        {
            super.onBackPressed();
        }
    }
}
