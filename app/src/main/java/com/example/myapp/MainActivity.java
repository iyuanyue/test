import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private static final String PRIMARY_URL = "https://test.com";
    private static final String SECONDARY_URL = "https://backup.com";
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // 执行 ping 测试并加载页面
        new PingTask().execute(PRIMARY_URL);
    }

    // AsyncTask 用于执行 ping 测试
    private class PingTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... urls) {
            String url = urls[0];
            return isHostReachable(url);
        }

        @Override
        protected void onPostExecute(Boolean reachable) {
            if (reachable) {
                // 如果主 URL 可达，则加载主 URL
                loadUrl(PRIMARY_URL);
            } else {
                // 如果主 URL 不可达，则加载备用 URL
                loadUrl(SECONDARY_URL);
            }
        }
    }

    // 使用 Socket 执行 ping 测试
    private boolean isHostReachable(String host) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, 80), 2000);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // 加载指定的 URL
    private void loadUrl(String url) {
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }
}
