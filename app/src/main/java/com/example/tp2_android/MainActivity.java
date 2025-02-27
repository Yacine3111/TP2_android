package com.example.tp2_android;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    SearchView searchBar;
    ImageButton navigateBtn,nextBtn,previousBtn,homeBtn,newHomeBtn;
    WebView webView;
    String homePath;

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
        homeBtn=findViewById(R.id.home_btn);
        previousBtn=findViewById(R.id.previous_btn);
        nextBtn=findViewById(R.id.next_btn);
        newHomeBtn=findViewById(R.id.new_home_btn);
        webView=findViewById(R.id.web_view);


        webView.getSettings().setJavaScriptEnabled(true);

        homePath="https://www.google.com";
        webView.loadUrl(homePath);


        navigateBtn.setOnClickListener(v->{
            Uri uri= Uri.parse(searchBar.getQuery().toString());
            checkUrl(webView,uri);
        });

        homeBtn.setOnClickListener(v->{
            webView.loadUrl(homePath);
        });

        newHomeBtn.setOnClickListener(v->{
            homePath=searchBar.getQuery().toString();
            alertMessage(getString(R.string.message_new_home_page));
        });


        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request){
                Uri uri=request.getUrl();
                return checkUrl(view,uri);
            }
            @Override
            public void onPageFinished(WebView view,String url){
                searchBar.setQuery(url,false);
            }
        });


    }

    public boolean checkUrl(WebView view,Uri uri){
        String scheme=uri.getScheme();
        String pagePath = uri.toString();
        if(scheme==null) {
            pagePath="https://" + uri;
            view.loadUrl(pagePath);
            return false;
        } else if (scheme.equals("http")||scheme.equals("https")) {
            view.loadUrl(pagePath);
            return false;
        }else{
            Intent intent=new Intent(Intent.ACTION_VIEW,uri);
            try{
                startActivity(intent);
            }catch (ActivityNotFoundException e){
                alertMessage(getString(R.string.message_no_app_found)+" "+scheme);
            }
            return true;
        }
    }
    public void alertMessage(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}