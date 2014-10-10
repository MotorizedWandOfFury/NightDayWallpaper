package diaz.nightdaywallpaper.datalayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import diaz.nightdaywallpaper.ProfileRowView;


public class NightDayDBHandler extends SQLiteOpenHelper {

    private final String TAG = "NightDayDBHandler";

    private final static int DB_VERSION = 1;
    private final static String DB_NAME = "NightDayDatabase";

    private final String TABLE_PROFILES = "ProfileRow";
    private final String COLUMN_ID = "id",
    COLUMN_ROWTYPE = "rowtype",
    COLUMN_URI = "uri",
    COLUMN_THUMBNAIL = "thumbnail";

    public NightDayDBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createProfilesTable = "CREATE TABLE " + TABLE_PROFILES + "( "
                + COLUMN_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_ROWTYPE + " TEXT, "
                + COLUMN_URI +" TEXT, "
                + COLUMN_THUMBNAIL + " BLOB"
                +")";

        Log.i(TAG, "Ran query: \n" + createProfilesTable);
        db.execSQL(createProfilesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

    public void insertProfileRow(ProfileRowView.RowType rowType, Uri uri, byte[] thumbnail){
        insertProfileRow(new NightDayDBRowModel(rowType.name(), uri.toString(), thumbnail));
    }

    public void insertProfileRow(NightDayDBRowModel model){

        Log.v(TAG, "Inserting ProfileRow (RowType: "+model.getRowType()
                +", URI: "+model.getUri()
                +", Thumbnail: {size: "+model.getThumbnail().length+"})");

        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ROWTYPE, model.getRowType());
        cv.put(COLUMN_URI, model.getUri());
        cv.put(COLUMN_THUMBNAIL, model.getThumbnail());

        db.insert(TABLE_PROFILES, null, cv);
        db.close();
    }

    public Uri getWallpaperURI(ProfileRowView.RowType rowType){

        Log.v(TAG, "Retrieving Wallpaper URI of row type " + rowType.name());

        SQLiteDatabase db = getReadableDatabase();

        String[] projection = new String[]{COLUMN_URI};
        String where = COLUMN_ROWTYPE + "=?";
        String[] condition = new String[]{rowType.name()};
        Cursor c = db.query(TABLE_PROFILES, projection, where, condition, null, null, null, null);

        if(c != null){
            c.moveToFirst();
        }

        if(c.getCount() == 0){
            return null;
        }

        Uri uri = Uri.parse(c.getString(0));
        c.close();
        db.close();

        Log.v(TAG, "Retrieved " + uri);

        return uri;
    }

    public Bitmap getThumbnailBitmap(ProfileRowView.RowType rowType){

        Log.v(TAG, "Retrieving Thumbnail bitmap of row type " + rowType.name());

        SQLiteDatabase db = getReadableDatabase();

        String[] projection = new String[]{COLUMN_THUMBNAIL};
        String where = COLUMN_ROWTYPE + "=?";
        String[] condition = new String[]{rowType.name()};
        Cursor c = db.query(TABLE_PROFILES, projection, where, condition, null, null, null, null);

        if(c != null){
            c.moveToFirst();
        }

        if(c.getCount() == 0){
            return null;
        }

        byte[] buffer = c.getBlob(0);
        Log.v(TAG, "Retrieved buffer of size " + buffer.length);
        Bitmap b = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);

        c.close();
        db.close();

       Log.v(TAG, "Retrieved bitmap of size " + b.getByteCount());

        return b;
    }


}
