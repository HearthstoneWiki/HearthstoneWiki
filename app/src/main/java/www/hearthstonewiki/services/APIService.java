package www.hearthstonewiki.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import www.hearthstonewiki.db.DatabaseHelper;
import www.hearthstonewiki.db.tables.APIVersionTable;
import www.hearthstonewiki.db.tables.CardDataTable;
import www.hearthstonewiki.db.tables.SetsTable;


public class APIService extends IntentService {

    private static final String ACTION_GET_UPDATE = "www.hearthstonewiki.services.action.GET_UPDATE";
    private static final String ACTION_CHECK_UPDATE = "www.hearthstonewiki.services.action.CHECK_UPDATE";
    private static final String ACTION_CHECK_CONNECTION = "www.hearthstonewiki.service.action.CHECK_CONNECTION";

    public static final String CONNECTION_STATUS = "www.hearthstonewiki.service.msg.ConnectionStatus";
    public static final String CONNECTION_STATUS_INTENT = "www.hearthstonewiki.service.intent.ConnectionStatus";

    private static final String API_URL = "http://hearthstonejson.com";
    private static final String ALL_CARDS_PATH = API_URL + "/json/AllSets.json";
    private static final String SET_LIST_PATH = API_URL + "/json/SetList.json";
    private static final String VERSION_PATH = API_URL + "/json/version.json";

    private static final int CONNECTION_TIME_OUT = 100;
    private static final int SOCKET_TME_OUT = 100;

    private APIConnector mApiConnector;
    private DataProcessor mDataProcessor;

    public static void startActionGetUpdate(Context context) {
        Intent intent = new Intent(context, APIService.class);
        intent.setAction(ACTION_GET_UPDATE);
        context.startService(intent);
    }

    public static void startActionCheckUpdate(Context context) {
        Intent intent = new Intent(context, APIService.class);
        intent.setAction(ACTION_CHECK_UPDATE);
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
        mDataProcessor = new DataProcessor();
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
        String versionJSON = mApiConnector.getDataHttpClient(VERSION_PATH);

        if(versionJSON != null) {
            int status = mDataProcessor.checkVersion(versionJSON);
            String setsJSON, cardsJSON;
            switch(status) {
                case DataProcessor.ACTUAL:
                    setsJSON = mApiConnector.getDataHttpClient(SET_LIST_PATH);
                    cardsJSON = mApiConnector.getDataHttpClient(ALL_CARDS_PATH);
                    mDataProcessor.updateSchema(setsJSON, cardsJSON);

                    break;

                case DataProcessor.NEED_TO_INITIALIZE:
                    mDataProcessor.updateVersion(versionJSON);
                    setsJSON = mApiConnector.getDataHttpClient(SET_LIST_PATH);
                    cardsJSON = mApiConnector.getDataHttpClient(ALL_CARDS_PATH);
                    mDataProcessor.updateSchema(setsJSON, cardsJSON);
                    break;

                case DataProcessor.NEED_TO_UPDATE:
                    setsJSON = mApiConnector.getDataHttpClient(SET_LIST_PATH);
                    cardsJSON = mApiConnector.getDataHttpClient(ALL_CARDS_PATH);
                    mDataProcessor.updateSchema(setsJSON, cardsJSON);
                    break;

                case DataProcessor.API_ERROR:
                    break;

                default:
                    throw new IllegalArgumentException("Wrong status number: " + status);
            }

        }
    }


    private void handleActionGetUpdate() {
        String cardsJSON = mApiConnector.getDataHttpClient(VERSION_PATH);
        if (cardsJSON != null)
            mDataProcessor.checkVersion(cardsJSON);
    }

    private void handleActionCheckConnection() {
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        Intent intent = new Intent(CONNECTION_STATUS_INTENT);

        if ( (activeNetwork != null) && activeNetwork.isConnected() && activeNetwork.isAvailable()) {


            HttpGet httpGet = new HttpGet("http://google.com");
            HttpParams httpParameters = new BasicHttpParams();

            int timeoutConnection = CONNECTION_TIME_OUT;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

            int timeoutSocket = SOCKET_TME_OUT;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

            DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
            try {
                httpClient.execute(httpGet);
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

    public class DataProcessor {

        final ArrayList<String> unnecessarySets = new ArrayList<String>() {{
            add("Debug");
            add("Credits");
            add("System");
        }};

        final String[] PROJECTION = new String[] {
                CardDataTable._ID,
        };

        final String[] VERSION_PROJECTION = new String[] {
                APIVersionTable.COLUMN_VERSION,
        };

        final String[] SETS_PROJECTION = new String[] {
                SetsTable.COLUMN_SET_NAME
        };

        public static final int API_ERROR = -1;
        public static final int ACTUAL = 0;
        public static final int NEED_TO_UPDATE = 1;
        public static final int NEED_TO_INITIALIZE = 2;

        private DatabaseHelper mDbHelper;

        public DataProcessor() {

        }

        private String getVersion(String jsonVersion) throws JSONException {
            JSONObject version = new JSONObject(jsonVersion);
            return version.getString(JSONData.VESRSION);
        }

        public void updateVersion(String jsonVersion) {
            try {
                String api_version = getVersion(jsonVersion);
                mDbHelper = new DatabaseHelper(getApplicationContext());
                SQLiteDatabase db = mDbHelper.getReadableDatabase();

                db.delete(APIVersionTable.TABLE_NAME,
                        null,
                        null
                );

                ContentValues cv = new ContentValues();

                cv.put(APIVersionTable.COLUMN_VERSION, api_version);
                cv.put(APIVersionTable._ID, 0);
                db.insert(APIVersionTable.TABLE_NAME,
                        null,
                        cv
                        );

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public int checkVersion(String jsonVersion) {
            Cursor cursor = null;
            try {
                String apiVersion = getVersion(jsonVersion);
                mDbHelper = new DatabaseHelper(getApplicationContext());
                SQLiteDatabase db = mDbHelper.getReadableDatabase();

                cursor = db.query(
                        APIVersionTable.TABLE_NAME,
                        VERSION_PROJECTION,
                        null,
                        null,
                        null,
                        null,
                        APIVersionTable.DEFAULT_SORT_ORDER
                );

                if(cursor.getCount() == 0) {
                    return NEED_TO_INITIALIZE;
                }
                cursor.moveToFirst();
                String applicationVersion = cursor.getString(0);

                if(apiVersion.equals(applicationVersion)) {
                    return ACTUAL;
                }
                else {
                    return NEED_TO_UPDATE;
                }

            } catch (JSONException e) {
                e.printStackTrace();
                return API_ERROR;
            } finally {
                if (cursor != null)
                    cursor.close();
            }

        }

        private void updateSets(String setsJSON) {
            String all_sets = setsJSON.substring(1,setsJSON.length() - 1);

            all_sets = all_sets.replaceAll("\"", "");
            List<String> setsArr = Arrays.asList(all_sets.split(","));
            ArrayList<String> sets = new ArrayList<String>();
            for (String aSetsArr : setsArr) {
                if(!unnecessarySets.contains(aSetsArr)) {
                    sets.add(aSetsArr);
                }
            }

            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            db.delete(SetsTable.TABLE_NAME,
                    null,
                    null
            );

            for (int i = 0; i < sets.size(); i++) {
                ContentValues cv = new ContentValues();
                cv.put(SetsTable._ID, i);
                cv.put(SetsTable.COLUMN_SET_NAME, sets.get(i));
                db.insert(SetsTable.TABLE_NAME,
                        null,
                        cv
                );
            }
        }

        private int updateCards(String cardsJSON) {
            Cursor cursor = null;
            try {
                SQLiteDatabase db = mDbHelper.getReadableDatabase();
                cursor = db.query(SetsTable.TABLE_NAME,
                        SETS_PROJECTION,
                        null,
                        null,
                        null,
                        null,
                        SetsTable.DEFAULT_SORT_ORDER
                );
                cursor.moveToFirst();
                JSONObject cards = new JSONObject(cardsJSON);
                for(int i = 0; i < cursor.getCount(); i++) {
                    JSONArray setCards = cards.getJSONArray(cursor.getString(0));
                    for(int num = 0; num < setCards.length(); num++) {
                        JSONObject card = setCards.getJSONObject(num);
                        String id = card.getString(JSONData.ID);
                        Cursor cardCursor = getContentResolver().query(
                                Uri.withAppendedPath(CardDataTable.CARD_URI_ID,id),
                                PROJECTION,
                                null,
                                null,
                                null
                        );

                        if(cardCursor.getCount() == 0) {
                            ContentValues cv = new ContentValues();
                            cv.put(CardDataTable._ID, id);
                            cv.put(CardDataTable.COLUMN_NAME, card.getString(JSONData.NAME));
                            if(card.has(JSONData.TEXT_DESCRIPTION)) {
                                cv.put(CardDataTable.COLUMN_TEXT, card.getString(JSONData.TEXT_DESCRIPTION));
                            }
                            else {
                                cardCursor.close();
                                continue;
                            }
                            if(card.has(JSONData.COST)) {
                                cv.put(CardDataTable.COLUMN_COST, card.getInt(JSONData.COST));
                            }
                            else {
                                cardCursor.close();
                                continue;
                            }
                            if(card.has(JSONData.CLASS)) {
                                cv.put(CardDataTable.COLUMN_CLASS, card.getString(JSONData.CLASS));
                            }
                            else {
                                cv.put(CardDataTable.COLUMN_CLASS, "base");
                            }
                            getContentResolver().insert(CardDataTable.CARD_URI, cv);
                        }
                        cardCursor.close();
                    }
                    cursor.moveToNext();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(cursor != null)
                    cursor.close();
                mDbHelper.close();
            }
            return 0;
        }

        public void updateSchema(String setsJSON, String cardsJSON) {
            updateSets(setsJSON);
            updateCards(cardsJSON);
        }
    }
}
