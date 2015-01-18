package www.hearthstonewiki.services;


import android.content.Entity;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by uzzz on 15.12.14.
 */
public class APIConnector {

    private static final int CONNECTION_TIME_OUT = 1000;
    private static final int SOCKET_TME_OUT = 1000;

    public APIConnector() {}

    public String getDataHttpUrlConnection(String urlStr) {
        String jsonStr = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(urlStr);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Accept", "text/plain charset=utf-8");
            urlConnection.connect();
            InputStream is = urlConnection.getInputStream();
            jsonStr = readStream(is);
            urlConnection.disconnect();
            Log.w("urlConnection", jsonStr);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return jsonStr;
    }

    public String getDataHttpClient(String urlStr) {
        String jsonStr = null;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(urlStr);
            request.setHeader("Accept", "text/plain");

            HttpResponse response = client.execute(request);
            InputStream in = response.getEntity().getContent();
            jsonStr = readStream(in);
            Log.w("clientConnection", jsonStr);

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonStr;
    }

    public boolean isConnected() throws IOException {
        HttpGet httpGet = new HttpGet("http://google.com");
        HttpParams httpParameters = new BasicHttpParams();

        int timeoutConnection = CONNECTION_TIME_OUT;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

        int timeoutSocket = SOCKET_TME_OUT;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

        httpClient.execute(httpGet);

        return true;
    }

    private String readStream(InputStream is) {
        byte[] buffer = new byte[2048];
        int length;
        StringBuilder sb = new StringBuilder();
        try {
            while ((length = is.read(buffer)) != -1) {
                sb.append(new String(buffer, 0, length));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}