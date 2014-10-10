package diaz.nightdaywallpaper;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import diaz.nightdaywallpaper.datalayer.NightDayDBHandler;

public class ProfileViewContainer extends LinearLayout{

    ProfileRowView _dayRow, _nightRow;

    public ProfileViewContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        NightDayDBHandler dbHandler = new NightDayDBHandler(getContext());

        _dayRow = (ProfileRowView) getChildAt(0);
        _nightRow = (ProfileRowView) getChildAt(1);

        _dayRow.setTitle("Day");
        _dayRow.setTime("@7:00am");
        _dayRow.setImage(dbHandler.getThumbnailBitmap(ProfileRowView.RowType.DAY_ROW));
        _dayRow.setType(ProfileRowView.RowType.DAY_ROW);

        _nightRow.setTitle("Night");
        _nightRow.setTime("@7:00pm");
        _nightRow.setImage(dbHandler.getThumbnailBitmap(ProfileRowView.RowType.NIGHT_ROW));
        _nightRow.setType(ProfileRowView.RowType.NIGHT_ROW);
    }

    //return false to Activity so it can pass onActivityResult to superclass if it can't be handled here
    public boolean onActivityResult(int requestCode, int resultCode, Intent data){
        if(_dayRow == null || _nightRow == null){
            return false;
        }

        if(requestCode == ProfileRowView.RowType.DAY_ROW.ordinal()){
            _dayRow.onImageReceived(data);
            return true;
        } else if (requestCode == ProfileRowView.RowType.NIGHT_ROW.ordinal()){
            _nightRow.onImageReceived(data);
            return true;
        }

        return false;

    }

}
