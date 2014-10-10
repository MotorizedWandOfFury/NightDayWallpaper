package diaz.nightdaywallpaper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileRowView extends RelativeLayout implements View.OnClickListener, View.OnLongClickListener {

    private TextView _title, _time;
    private ImageView _image;

    public static enum RowType {
        DAY_ROW,
        NIGHT_ROW
    }

    private RowType _type;

    private final ProfileRowPresenter _presenter;

    public ProfileRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        _presenter = new ProfileRowPresenter(this);
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();

        _title = (TextView) findViewById(R.id.titleTextView);
        _time = (TextView) findViewById(R.id.timeTextView);
        _image = (ImageView) findViewById(R.id.thumbnailImageView);

        setOnLongClickListener(this);
        setOnClickListener(this);
    }

    public void onImageReceived(Intent data){
        _presenter.onImageReceived(data);
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(getContext(), "Long click this area to configure " + getTitle() +" wallpaper", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onLongClick(View view) {
        _presenter.onRowClicked();
        return true; //consumes onLongClick event so onClick event won't be fired after completing a long click
    }

    public RowType getType() {
        return _type;
    }

    public void setType(RowType type) {
        _type = type;
    }

    public String getTitle(){
        return _title.getText().toString();
    }

    public void setTitle(String title){
        _title.setText(title);
    }

    public void setTime(String time){
        _time.setText(time);
    }

    public void setImage(Bitmap b){
        _image.setImageBitmap(b);
    }
}
