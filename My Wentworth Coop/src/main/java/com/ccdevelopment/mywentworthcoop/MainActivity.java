package com.ccdevelopment.mywentworthcoop;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadWebViews();

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    private void loadWebViews() {
        WebView viewPhotoVideo = (WebView) this.findViewById(R.id.webViewPhotoVideo);
        WebView viewJournal = (WebView) this.findViewById(R.id.webViewJournal);
        WebView viewPeople = (WebView) this.findViewById(R.id.webViewPeople);
        WebView viewAssignments = (WebView) this.findViewById(R.id.webViewAssignments);

        viewPhotoVideo.loadUrl("file:///android_asset/index1.html");
        viewJournal.loadUrl("file:///android_asset/index2.html");
        viewPeople.loadUrl("file:///android_asset/index3.html");
        viewAssignments.loadUrl("file:///android_asset/index4.html");

        // view.getSettings().setJavaScriptEnabled(true);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

//        MenuItem item1 = menu.findItem(R.id.action_mainmenu);
//        Intent intent1 = new Intent(this, MainActivity.class );
//        item1.setIntent(intent1);

        MenuItem item2 = menu.findItem(R.id.action_login);
        Intent intent2 = new Intent(this, LoginActivity.class );
        item2.setIntent(intent2);

        MenuItem item3 = menu.findItem(R.id.action_login);
        Intent intent3 = new Intent(this, PhotoVideoActivity.class );
        item3.setIntent(intent3);

        MenuItem item4 = menu.findItem(R.id.action_login);
        Intent intent4 = new Intent(this, JournalActivity.class );
        item4.setIntent(intent4);

        MenuItem item5 = menu.findItem(R.id.action_login);
        Intent intent5 = new Intent(this, PeopleActivity.class );
        item5.setIntent(intent5);

        MenuItem item6 = menu.findItem(R.id.action_login);
        Intent intent6 = new Intent(this, AssignmentsActivity.class );
        item6.setIntent(intent6);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
//            case R.id.action_mainmenu:
//                startActivity(new Intent(this, MainActivity.class));
//                return true;

            case R.id.action_login:
                startActivity(new Intent(this, LoginActivity.class));
                return true;

            case R.id.action_photoVideo:
                startActivity(new Intent(this, PhotoVideoActivity.class));
                return true;

            case R.id.action_journal:
                startActivity(new Intent(this, JournalActivity.class));
                return true;

            case R.id.action_people:
                startActivity(new Intent(this, PeopleActivity.class));
                return true;

            case R.id.action_assignments:
                startActivity(new Intent(this, AssignmentsActivity.class));
                return true;

            case R.id.action_settings:
                Toast.makeText(getBaseContext(), "Settings", Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }





    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_main, container, false);
        }
    }

}
