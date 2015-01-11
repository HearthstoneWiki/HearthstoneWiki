package www.hearthstonewiki.services;


import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by uzzz on 15.12.14.
 */
public class APIConnector {

    private static final String API_URL = "http://hearthstonejson.com//json/AllSets.json";
    private HttpURLConnection urlConnection;

    public String getUpdates() {
        JSONObject jsonObj;
        try {

            URL langUrl = new URL(API_URL);
            urlConnection = (HttpURLConnection) langUrl.openConnection();
            InputStream is = urlConnection.getInputStream();
            String jsonStr = readStream(is);
            Log.d("tag", jsonStr);
            urlConnection.disconnect();
            return jsonStr;
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return null;
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