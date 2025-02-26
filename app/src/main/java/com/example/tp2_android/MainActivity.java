package com.example.tp2_android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    SearchView searchBar;
    ImageButton navigateBtn;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        searchBar=findViewById(R.id.search_bar);
        navigateBtn=findViewById(R.id.navigate_btn);
        webView=findViewById(R.id.web_view);

        webView.getSettings().setJavaScriptEnabled(true);


        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request){
                Uri uri=request.getUrl();
                String scheme=uri.getScheme();
                if(scheme==null){
                    view.loadUrl("https://"+uri);
                    return false;
                }else if(scheme.equals("http")||scheme.equals("https")){
                    return false;
                }else{

                    return true;
                }
            }
        });

        webView.loadUrl("google.ca");


        navigateBtn.setOnClickListener(v->{
            webView.loadUrl(searchBar.getQuery().toString());
        });



    }
}