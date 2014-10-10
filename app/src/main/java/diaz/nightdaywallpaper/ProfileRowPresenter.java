package diaz.nightdaywallpaper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import diaz.nightdaywallpaper.alarm.AlarmReceiver;
import diaz.nightdaywallpaper.constants.Constants;
import diaz.nightdaywallpaper.datalayer.NightDayDBHandler;


public class ProfileRowPresenter {


    public static final String TAG = "ProfileRowPresenter";
    private final ProfileRowView _row;

    public ProfileRowPresenter(ProfileRowView row){
        _row = row;
    }

    public void onRowClicked(){

        //check if alarm has already been enabled
        //if it has not, launch image picker
        //else launch dialog with options to change wallpaper or cancel alarm

        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(_row.getContext());

        boolean alarmEnabled = false;

        switch (_row.getType()){
            case DAY_ROW:
                alarmEnabled = pref.getBoolean(Constants.DAY_ALARM_ENABLED_KEY, false);
                break;
            case NIGHT_ROW:
                alarmEnabled = pref.getBoolean(Constants.NIGHT_ALARM_ENABLED_KEY, false);
                break;
        }


        if(alarmEnabled){
            new AlertDialog.Builder(_row.getContext())
            .setTitle("Configure " + _row.getTitle())
            .setItems(new String[]{"Change Wallpaper", "Disable"}, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    switch (which) {
                        case 0:
                            launchImagePicker();
                            break;
                        case 1:
                            AlarmReceiver.configureAlarm(_row.getContext(), false, _row.getType());
                            _row.setImage(null);
                            switch(_row.getType()){
                                case DAY_ROW:
                                    pref.edit().putBoolean(Constants.DAY_ALARM_ENABLED_KEY, false).commit();
                                    break;
                                case NIGHT_ROW:
                                    pref.edit().putBoolean(Constants.NIGHT_ALARM_ENABLED_KEY, false).commit();
                                    break;
                            }
                            break;
                    }
                }
            }).setNegativeButton("Cancel", null)
            .create().show();

        } else {
            launchImagePicker();
        }

    }

    private void launchImagePicker(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        ((Activity) _row.getContext()).startActivityForResult(intent, _row.getType().ordinal());
    }

    public void onImageReceived(Intent data){
        try {
            Uri uri = data.getData();

            Log.v(TAG, "Image URI is: "+uri);

            InputStream imageStream = _row.getContext().getContentResolver().openInputStream(data.getData());
            Bitmap thumbnail = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeStream(imageStream), 100, 100);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            byte[] thumbnailAsBytes = outputStream.toByteArray();

            NightDayDBHandler dbHandler = new NightDayDBHandler(_row.getContext());
            dbHandler.insertProfileRow(_row.getType(), uri, thumbnailAsBytes);

            _row.setImage(thumbnail);

            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(_row.getContext()).edit();

            if(_row.getType() == ProfileRowView.RowType.DAY_ROW){
                editor.putBoolean(Constants.DAY_ALARM_ENABLED_KEY, true);
            } else if(_row.getType() == ProfileRowView.RowType.NIGHT_ROW){
                editor.putBoolean(Constants.NIGHT_ALARM_ENABLED_KEY, true);
            }

            editor.commit();
            AlarmReceiver.configureAlarm(_row.getContext(), true, _row.getType());

            outputStream.close();
            imageStream.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found ", e);
        } catch (IOException e) {
            Log.e(TAG, "Couldn't close inputstream ", e);
        }
    }


}
