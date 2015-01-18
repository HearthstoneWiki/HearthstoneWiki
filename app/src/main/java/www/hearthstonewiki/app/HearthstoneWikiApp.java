package www.hearthstonewiki.app;

import android.app.Application;

/**
 * Created by uzzz on 19.01.15.
 */
public class HearthstoneWikiApp extends Application {

    public static boolean checkForUpdates;

    @Override
    public void onCreate() {
        super.onCreate();
        checkForUpdates = true;
    }
}
