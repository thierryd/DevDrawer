package com.owentech.DevDrawer.receivers;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.owentech.DevDrawer.R;
import com.owentech.DevDrawer.appwidget.DDWidgetProvider;
import com.owentech.DevDrawer.utils.AppConstants;
import com.owentech.DevDrawer.utils.Database;
import com.owentech.DevDrawer.utils.NotificationHelper;

/**
 * Created with IntelliJ IDEA.
 * User: owent
 * Date: 31/01/2013
 * Time: 16:30
 * To change this template use File | Settings | File Templates.
 */
public class AppUninstalledReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // App has been removed, if it is in the app table remove from the widget
        String uninstalledPackage = intent.getData().getSchemeSpecificPart();

        if (Database.getInstance(context).getAppsCount() != 0) {
            if (Database.getInstance(context).doesAppExistInDb(uninstalledPackage)) {
                Database.getInstance(context).deleteAppFromDb(uninstalledPackage);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                    int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, DDWidgetProvider.class));
                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.listView);
                }
                int match = Database.getInstance(context).parseAndMatch(uninstalledPackage, AppConstants.NOTIFICATION);
                NotificationHelper.removeNotification(context, match);
            }
        }
    }
}
