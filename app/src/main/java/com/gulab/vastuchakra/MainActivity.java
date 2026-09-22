package com.gulab.vastuchakra;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.util.Base64;
import android.content.Intent;
import android.net.Uri;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends Activity {
    private WebView webView;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        webView = new WebView(this);
        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setAllowFileAccess(true);
        s.setAllowContentAccess(true);
        s.setBuiltInZoomControls(false);
        s.setDisplayZoomControls(false);
        s.setMediaPlaybackRequiresUserGesture(false);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        webView.addJavascriptInterface(new SaveBridge(), "AndroidSave");
        setContentView(webView);
        webView.loadUrl("file:///android_asset/index.html");
    }

    private File rootDir() {
        File d = new File(getFilesDir(), "saved");
        if (!d.exists()) d.mkdirs();
        return d;
    }

    private String safeName(String s) {
        if (s == null) return "";
        s = s.trim().replaceAll("[\\\\/:*?\"<>|]", "_");
        if (s.length() > 60) s = s.substring(0,60).trim();
        return s;
    }

    public class SaveBridge {
        @JavascriptInterface public String savePdf(String folderName, String base64) {
            try {
                String name = safeName(folderName);
                if (name.isEmpty()) return "ERROR:Folder name required";
                File dir = new File(rootDir(), name);
                if (!dir.exists() && !dir.mkdirs()) return "ERROR:Folder create nahi hua";
                String b = base64 == null ? "" : base64.replaceFirst("^data:application/pdf;base64,", "");
                byte[] bytes = Base64.decode(b, Base64.DEFAULT);
                File out = new File(dir, "Gulab_Vastu_Chakra_HD.pdf");
                try (FileOutputStream fos = new FileOutputStream(out)) { fos.write(bytes); }
                return "OK:" + name;
            } catch (Exception e) { return "ERROR:" + e.getMessage(); }
        }

        @JavascriptInterface public String listSaved() {
            File[] dirs = rootDir().listFiles(File::isDirectory);
            List<String> names = new ArrayList<>();
            if (dirs != null) for (File d : dirs) {
                File pdf = new File(d, "Gulab_Vastu_Chakra_HD.pdf");
                if (pdf.exists()) names.add(d.getName());
            }
            Collections.sort(names, String.CASE_INSENSITIVE_ORDER);
            StringBuilder j = new StringBuilder("[");
            for (int i=0;i<names.size();i++) { if(i>0)j.append(','); j.append('"').append(names.get(i).replace("\\","\\\\").replace("\"","\\\"")).append('"'); }
            return j.append(']').toString();
        }

        @JavascriptInterface public void openSaved(String folderName) {
            try {
                File pdf = new File(new File(rootDir(), safeName(folderName)), "Gulab_Vastu_Chakra_HD.pdf");
                if (!pdf.exists()) return;
                Uri uri = FileProvider.getUriForFile(MainActivity.this, getPackageName()+".fileprovider", pdf);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setDataAndType(uri, "application/pdf");
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            } catch (Exception ignored) {}
        }
    }

    @Override public void onBackPressed() {
        if (webView != null && webView.canGoBack()) webView.goBack(); else super.onBackPressed();
    }
}
