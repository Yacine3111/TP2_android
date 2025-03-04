package com.example.tp2_android;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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
    ProgressBar progressBar;
    String homePath;
    InputMethodManager inputMethodManager;

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
        progressBar=findViewById(R.id.progress_bar);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request){
                Uri uri=request.getUrl();
                return checkUrl(uri);
            }
            @Override
            public void onPageFinished(WebView view,String url){
                searchBar.setQuery(url,false);
                checkHomePath();
                checkBackOrNext();
            }
        });

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view,int newProgress){
                progressBar.setProgress(newProgress);
                if(newProgress==100){
                    progressBar.setVisibility(ProgressBar.GONE);
                    webView.setVisibility(WebView.VISIBLE);
                }else{
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                    webView.setVisibility(WebView.GONE);
                }
            }
        });

        //permet de vérifier si on a le focus sur la barre de recherche et cache le clavier si le
        //focus change
        searchBar.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                hideKeyboard();
            }
        });

        //utilisé pour pouvoir utilisé le bouton entrer et le bouton recherche du clavier de l'émulateur
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Uri uri=Uri.parse(query);
                checkUrl(uri);
                hideKeyboard();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        webView.getSettings().setJavaScriptEnabled(true);

        homePath="https://www.google.com/";
        webView.loadUrl(homePath);

        navigateBtn.setOnClickListener(v->{
            Uri uri= Uri.parse(searchBar.getQuery().toString());
            checkUrl(uri);
        });

        homeBtn.setOnClickListener(v-> webView.loadUrl(homePath));


        newHomeBtn.setOnClickListener(v->{
            homePath=searchBar.getQuery().toString();
            alertMessage(getString(R.string.message_new_home_page));
            checkHomePath();
        });

        previousBtn.setOnClickListener(v-> webView.goBack());

        nextBtn.setOnClickListener(v-> webView.goForward());

    }

    //on vérifie si l'url est valide pour la recherche soit ajout le bon shcéma soit on
    // redirige si c'est possiblev ers la bonne activié
    public boolean checkUrl(Uri uri){
        String scheme=uri.getScheme();
        String pagePath = uri.toString();
        if(uri.toString().isEmpty()){
            alertMessage(getString(R.string.empty_search_bar));
            return false;
        }
        else if(scheme==null) {
            pagePath="https://" + uri;
            webView.loadUrl(pagePath);
            return false;
        } else if (scheme.equals("http")||scheme.equals("https")) {
            webView.loadUrl(pagePath);
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

    public void hideKeyboard(){
        inputMethodManager.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);
    }

    //on Vérifie si on est dans le home path pour désactivé les boutons home et newHome
    public void checkHomePath(){
        if(searchBar.getQuery().toString().equals(homePath)){
            homeBtn.setEnabled(false);
            newHomeBtn.setEnabled(false);

            homeBtn.setBackgroundResource(R.drawable.ic_home_disabled_btn);
            newHomeBtn.setBackgroundResource(R.drawable.ic_new_home_disabled_btn);
        }else{
            homeBtn.setEnabled(true);
            newHomeBtn.setEnabled(true);

            homeBtn.setBackgroundResource(R.drawable.ic_home_btn);
            newHomeBtn.setBackgroundResource(R.drawable.ic_new_home_btn);
        }
    }

    //on vérifie si on peut aller en arrière ou en avant pour soit
    // activer ou désactiver les bouton previous et next
    public void checkBackOrNext(){
        if(webView.canGoBack()){
            previousBtn.setBackgroundResource(R.drawable.ic_previous_btn);
            previousBtn.setEnabled(true);
        }else{
            previousBtn.setBackgroundResource(R.drawable.ic_previous_disabled_btn);
            previousBtn.setEnabled(false);
        }

        if(webView.canGoForward()){
            nextBtn.setBackgroundResource(R.drawable.ic_next_btn);
            nextBtn.setEnabled(true);
        }else{
            nextBtn.setBackgroundResource(R.drawable.ic_next_disabled_btn);
            nextBtn.setEnabled(false);
        }
    }
}
