package diaz.nightdaywallpaper.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

import java.util.Calendar;

import diaz.nightdaywallpaper.ProfileRowView;
import diaz.nightdaywallpaper.constants.Constants;
import diaz.nightdaywallpaper.wallpaper.NightDayWallpaperService;


public class AlarmReceiver extends BroadcastReceiver {

    private static AlarmManager am;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().contentEquals("android.intent.action.BOOT_COMPLETED")){

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

            boolean enableDayAlarm = pref.getBoolean(Constants.DAY_ALARM_ENABLED_KEY, false);
            boolean enableNightAlarm = pref.getBoolean(Constants.DAY_ALARM_ENABLED_KEY, false);

            if(enableDayAlarm){
                configureAlarm(context, true, ProfileRowView.RowType.DAY_ROW);
            }

            if(enableNightAlarm){
                configureAlarm(context, true, ProfileRowView.RowType.NIGHT_ROW);
            }

        }
    }

    public static void configureAlarm(Context context, boolean enable, ProfileRowView.RowType rowType){
        ComponentName alarmReceiver = new ComponentName(context, AlarmReceiver.class);

        int flag = enable ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED; //if on is true, enable receiver, else disable
        context.getPackageManager().setComponentEnabledSetting(alarmReceiver, flag, PackageManager.DONT_KILL_APP);

        if(enable){
            enableAlarm(context, rowType);
        } else {
            disableAlarm(context, rowType);
        }
    }

    private static void enableAlarm(Context c, ProfileRowView.RowType rowType){
        am = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        PendingIntent alarmIntent = null;

        switch (rowType){
            case DAY_ROW:
                cal.set(Calendar.HOUR_OF_DAY, 7);
                alarmIntent = getDayWallpaperIntent(c);
                break;
            case NIGHT_ROW:
                cal.set(Calendar.HOUR_OF_DAY, 19);
                alarmIntent = getNightWallpaperIntent(c);
                break;
        }

        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
    }
    public static void disableAlarm(Context c, ProfileRowView.RowType rowType){
        am = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);

        switch (rowType){
            case DAY_ROW:
                am.cancel(getDayWallpaperIntent(c));
                break;
            case NIGHT_ROW:
                am.cancel(getNightWallpaperIntent(c));
                break;
        }
    }

    private static PendingIntent getDayWallpaperIntent(Context c){
        Intent timerIntent = new Intent(c, NightDayWallpaperService.class);
        timerIntent.setAction(NightDayWallpaperService.ACTION_CHANGE_DAY_WALLPAPER);
        PendingIntent pendingIntent = PendingIntent.getService(c, 0, timerIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        return pendingIntent;
    }

    private static PendingIntent getNightWallpaperIntent(Context c){
        Intent timerIntent = new Intent(c, NightDayWallpaperService.class);
        timerIntent.setAction(NightDayWallpaperService.ACTION_CHANGE_NIGHT_WALLPAPER);
        PendingIntent pendingIntent = PendingIntent.getService(c, 0, timerIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        return pendingIntent;
    }
}
