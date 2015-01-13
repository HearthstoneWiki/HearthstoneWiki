package www.hearthstonewiki.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.InetAddress;

import www.hearthstonewiki.db.tables.CardDataTable;


public class APIService extends IntentService {

    private static final String ACTION_GET_UPDATE = "www.hearthstonewiki.services.action.GET_UPDATE";
    private static final String ACTION_CHECK_UPDATE = "www.hearthstonewiki.services.action.CHECK_UPDATE";
    private static final String ACTION_CHECK_CONNECTION = "www.hearthstonewiki.service.action.CHECK_CONNECTION";

    public static final String CONNECTION_STATUS = "www.hearthstonewiki.service.msg.ConnectionStatus";
    public static final String CONNECTION_STATUS_INTENT = "www.hearthstonewiki.service.intent.ConnectionStatus";

    APIConnector mApiConnector;
    DataParser mDataParser;


    public static void startActionGetUpdate(Context context) {
        Intent intent = new Intent(context, APIService.class);
        intent.setAction(ACTION_GET_UPDATE);
        context.startService(intent);
    }

    public static void startActionCheckConnection(Context context) {
        Intent intent = new Intent(context, APIService.class);
        intent.setAction(ACTION_CHECK_CONNECTION);
        context.startService(intent);
    }

    public APIService() {
        super("APIService");
        mApiConnector = new APIConnector();
        mDataParser = new DataParser();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_UPDATE.equals(action)) {
                handleActionGetUpdate();
            }
            else if (ACTION_CHECK_UPDATE.equals(action)) {
                handleActionCheckUpdate();
            }
            else if (ACTION_CHECK_CONNECTION.equals(action)) {
                handleActionCheckConnection();
            }
        }
    }


    private void handleActionCheckUpdate() {

    }


    private void handleActionGetUpdate() {

        String s = mApiConnector.getUpdates();
        if(s != null)
            mDataParser.fillDB(s);

    }

    private void handleActionCheckConnection() {
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        Intent intent = new Intent(CONNECTION_STATUS_INTENT);

        if ( (activeNetwork != null) && activeNetwork.isConnected() && activeNetwork.isAvailable()) {


            HttpGet httpGet = new HttpGet("http://google.com");
            HttpParams httpParameters = new BasicHttpParams();

            int timeoutConnection = 100;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

            int timeoutSocket = 200;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

            DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
            try {
                HttpResponse response = httpClient.execute(httpGet);
                intent.putExtra(CONNECTION_STATUS, "true");
            }
            catch (ClientProtocolException e) {
                e.printStackTrace();
                intent.putExtra(CONNECTION_STATUS, "false");
            } catch (IOException e) {
                e.printStackTrace();
                intent.putExtra(CONNECTION_STATUS, "false");
            }
        }
        else {
            intent.putExtra(CONNECTION_STATUS, "false");
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    public class DataParser {

        final String[] PROJECTION = new String[] {
                CardDataTable._ID,
        };

        public void fillDB(String json) {
            try {
                Log.d("tag", json);
                JSONObject jsonObj = new JSONObject(json);
                JSONArray arr = jsonObj.getJSONArray(JSONCardData.CARD_ARRAY);

                for(int i = 0; i < arr.length(); i++) {
                    String text;
                    String name = arr.getJSONObject(i).getString(JSONCardData.NAME);
                    try {
                        text = arr.getJSONObject(i).getString(JSONCardData.TEXT_DESCRIPTION);
                    }
                    catch (JSONException e) {
                        text = "";
                    }

                    String id = arr.getJSONObject(i).getString(JSONCardData.ID);

                    ContentValues v = new ContentValues();

                    v.put(CardDataTable._ID, i);
                    v.put(CardDataTable.COLUMN_ID, id);
                    v.put(CardDataTable.COLUMN_NAME, name);
                    v.put(CardDataTable.COLUMN_TEXT, text);

                    String args[] = {String.valueOf(i)};
                    Cursor c = getContentResolver().query(
                            CardDataTable.CONTENT_ID_URI_BASE,
                            PROJECTION,
                            null,
                            args,
                            null);
                    if(c.getCount() == 0) {
                        getContentResolver().insert(CardDataTable.CONTENT_URI, v);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
