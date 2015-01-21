package www.hearthstonewiki.app;

import android.app.Application;

import www.hearthstonewiki.services.APIService;

/**
 * Created by uzzz on 19.01.15.
 */
public class HearthstoneWikiApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        APIService.startActionCheckConnection(this);
    }
}
