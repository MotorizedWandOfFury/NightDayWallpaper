package diaz.nightdaywallpaper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;


public class ProfilesViewActivity extends Activity {

    ProfileViewContainer _container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles_view);

        _container = (ProfileViewContainer) findViewById(R.id.container);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        boolean handled = _container.onActivityResult(requestCode, resultCode, data);

        if(!handled){
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

}
