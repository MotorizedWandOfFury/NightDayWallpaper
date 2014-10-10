package diaz.nightdaywallpaper.wallpaper;

import android.app.IntentService;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.IOException;

import diaz.nightdaywallpaper.ProfileRowView;
import diaz.nightdaywallpaper.datalayer.NightDayDBHandler;


public class NightDayWallpaperService extends IntentService {

    private static final String TAG = "NightDayWallpaperService";

    public static final String ACTION_CHANGE_DAY_WALLPAPER = "Action_Change_Day_Wallpaper",
    ACTION_CHANGE_NIGHT_WALLPAPER = "Action_Change_Night_Wallpaper";

    public NightDayWallpaperService() {
        super("NightDayWallpaperService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.v(TAG, "Received intent with action: " + intent.getAction());

        NightDayDBHandler dbHandler = new NightDayDBHandler(getApplicationContext());
        Uri uri = null;

        if(intent.getAction().contentEquals(ACTION_CHANGE_DAY_WALLPAPER)){
           uri = dbHandler.getWallpaperURI(ProfileRowView.RowType.DAY_ROW);
        } else if(intent.getAction().contentEquals(ACTION_CHANGE_NIGHT_WALLPAPER)){
           uri = dbHandler.getWallpaperURI(ProfileRowView.RowType.NIGHT_ROW);
        }

        Log.v(TAG, "Setting wallpaper to image at URI: " + uri);
        setWallpaperFromUri(uri);
    }

    private void setWallpaperFromUri(Uri uri){
        if(uri == null){
            return;
        }

        try {
            final WallpaperManager wp = WallpaperManager.getInstance(NightDayWallpaperService.this);
            final Bitmap b = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            wp.setBitmap(b);
        } catch (IOException e) {
            Log.e(TAG, "Error occurred while setting wallpaper", e);
        }

    }
}
