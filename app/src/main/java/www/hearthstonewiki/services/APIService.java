package www.hearthstonewiki.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;

import www.hearthstonewiki.db.tables.CardDataTable;


public class APIService extends IntentService {

    private static final String ACTION_GET_UPDATE = "www.hearthstonewiki.services.action.GET_UPDATE";
    private static final String ACTION_CHECK_UPDATE = "www.hearthstonewiki.services.action.CHECK_UPDATE";

    APIConnector mApiConnector;
    DataParser mDataParser;


    public static void startActionGetUpdate(Context context) {
        Intent intent = new Intent(context, APIService.class);
        intent.setAction(ACTION_GET_UPDATE);
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
                Log.d("tag", "msg_here");
            } else if (ACTION_CHECK_UPDATE.equals(action)) {
                handleActionCheckUpdate();
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
