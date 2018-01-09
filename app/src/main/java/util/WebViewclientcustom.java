package util;

import android.content.Context;
import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.tuch.tuchbrowser.BrowserActivity;


public class WebViewclientcustom extends WebViewClient {
    private Context context;
    CronologiaDbAdapter cron;
    public WebViewclientcustom(Context context) {
        this.context = context;
        cron = new CronologiaDbAdapter(context);
    }
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        BrowserActivity.barraRicerca.setText(url);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (!url.equalsIgnoreCase("about:blank")) {
            cron.addVisitato(url);
        }
    }
}
