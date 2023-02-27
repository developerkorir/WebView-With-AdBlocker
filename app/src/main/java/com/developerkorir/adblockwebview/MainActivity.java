package com.developerkorir.adblockwebview;

import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.anythink.banner.api.ATBannerExListener;
import com.anythink.banner.api.ATBannerView;
import com.anythink.core.api.ATAdInfo;
import com.anythink.core.api.ATNetworkConfirmInfo;
import com.anythink.interstitial.api.ATInterstitial;
import com.anythink.interstitial.api.ATInterstitialListener;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private SwipeRefreshLayout swipeRefreshLayout;
    ATBannerView mBannerView;

    ATInterstitial mInterstitialAd;
    String url = "https://www.scorebat.com/embed/livescore/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webview);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);

        new AdBlockerWeb.init(MainActivity.this).initializeWebView(webView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setWebViewClient(new myWebViewClient());
        webView.loadUrl(url);

        BannerAd();
        InterstitialAd();

        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(true);
            new Handler().postDelayed(() -> {
                swipeRefreshLayout.setRefreshing(false);
                webView.reload();
            }, 3000);
        });
        swipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_orange_dark),
                getResources().getColor(android.R.color.holo_green_dark),
                getResources().getColor(android.R.color.holo_red_dark)
        );
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else if (mInterstitialAd.isAdReady()) {
            mInterstitialAd.show(this);
            mInterstitialAd.setAdListener(new ATInterstitialListener() {
                @Override
                public void onInterstitialAdLoaded() {

                }

                @Override
                public void onInterstitialAdLoadFail(com.anythink.core.api.AdError adError) {

                }

                @Override
                public void onInterstitialAdClicked(ATAdInfo atAdInfo) {

                }

                @Override
                public void onInterstitialAdShow(ATAdInfo atAdInfo) {

                }

                @Override
                public void onInterstitialAdClose(ATAdInfo atAdInfo) {
                    onBackPressed();
                }

                @Override
                public void onInterstitialAdVideoStart(ATAdInfo atAdInfo) {

                }

                @Override
                public void onInterstitialAdVideoEnd(ATAdInfo atAdInfo) {
                    onBackPressed();
                }

                @Override
                public void onInterstitialAdVideoError(com.anythink.core.api.AdError adError) {

                }
            });
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setCancelable(false);
            builder.setMessage("Do you want to Exit?");
            builder.setPositiveButton("Yes", (dialog, which) -> finish());
            builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();

        }
    }

    private class myWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @SuppressWarnings("deprecation")
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {

            return AdBlockerWeb.blockAds(view, url) ? AdBlock.createEmptyResource() :
                    super.shouldInterceptRequest(view, url);

        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            webView.loadUrl("file:///android_asset/html/default_error_page.html");

        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.cancel();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

    @Override
    protected void onDestroy() {
        webView.clearCache(true);
        webView.clearHistory();
        super.onDestroy();
    }

    private void BannerAd() {
        final FrameLayout frameLayout = findViewById(R.id.adview_container);
        mBannerView = new ATBannerView(this);
        mBannerView.setPlacementId("b63f792fd8d220");
        // Banner height on phones and tablets is 50 and 90, respectively
        int heightPx = getResources().getDimensionPixelSize(R.dimen.banner_height);
        frameLayout.addView(mBannerView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, heightPx));
        mBannerView.setBannerAdListener(new ATBannerExListener() {
            @Override
            public void onBannerLoaded() {

            }

            @Override
            public void onBannerFailed(com.anythink.core.api.AdError adError) {

            }

            @Override
            public void onBannerClicked(ATAdInfo atAdInfo) {

            }

            @Override
            public void onBannerShow(ATAdInfo atAdInfo) {

            }

            @Override
            public void onBannerClose(ATAdInfo atAdInfo) {

            }

            @Override
            public void onBannerAutoRefreshed(ATAdInfo atAdInfo) {

            }

            @Override
            public void onBannerAutoRefreshFail(com.anythink.core.api.AdError adError) {

            }

            @Override
            public void onDeeplinkCallback(boolean b, ATAdInfo atAdInfo, boolean b1) {

            }

            @Override
            public void onDownloadConfirm(Context context, ATAdInfo atAdInfo, ATNetworkConfirmInfo atNetworkConfirmInfo) {

            }
        });

    }

    public void InterstitialAd() {
        mInterstitialAd = new ATInterstitial(this, "b63f794ce7f3ed");
        mInterstitialAd.load(this);
    }


    public static class AdBlockerWeb {

        static Map<String, Boolean> loadedUrls = new HashMap<>();

        public static boolean blockAds(WebView view, String url) {
            boolean ad;
            if (!loadedUrls.containsKey(url)) {
                ad = AdBlock.isAd(url);
                loadedUrls.put(url, ad);
            } else {
                ad = loadedUrls.get(url);
            }
            return ad;
        }

        public static class init {
            Context context;

            public init(Context context) {
                AdBlock.init(context);
                this.context = context;
            }

            public void initializeWebView(WebView mWebView) {
                mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                mWebView.getSettings().setSupportZoom(true);
                mWebView.getSettings().setBuiltInZoomControls(true);
                mWebView.getSettings().setDisplayZoomControls(false);

            }
        }
    }

    public static class UrlUtil {
        public static String getHost(String url) throws MalformedURLException {
            return new URL(url).getHost();
        }
    }

    public static class AdBlock {
        private static final String AD_HOST = "adhost";
        private static final Set<String> AD_HOSTS = new HashSet<>();

        public static void init(final Context context) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        loadFromAssets(context);
                    } catch (IOException e) {
                        // null
                    }
                    return null;
                }
            }.execute();
        }

        @WorkerThread
        private static void loadFromAssets(Context context) throws IOException {
            InputStream stream = context.getAssets().open(AD_HOST);
            InputStreamReader inputStreamReader = new InputStreamReader(stream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) AD_HOSTS.add(line);
            bufferedReader.close();
            inputStreamReader.close();
            stream.close();
        }

        private static boolean isAdHost(String host) {
            if (TextUtils.isEmpty(host)) {
                return false;
            }
            int index = host.indexOf(".");
            return index >= 0 && (AD_HOSTS.contains(host) ||
                    index + 1 < host.length() && isAdHost(host.substring(index + 1)));
        }

        public static boolean isAd(String url) {
            try {
                return isAdHost(UrlUtil.getHost(url));
            } catch (MalformedURLException e) {
                return false;
            }

        }

        public static WebResourceResponse createEmptyResource() {
            return new WebResourceResponse("text/plain", "utf-8", new ByteArrayInputStream("".getBytes()));
        }

    }
}
