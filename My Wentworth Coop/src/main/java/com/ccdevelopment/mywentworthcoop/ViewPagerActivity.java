package com.ccdevelopment.mywentworthcoop;

import java.util.Locale;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ViewPagerActivity extends FragmentActivity {


    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_viewpager);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.show();

        ///////////actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        /*// When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }*/


        Intent i = getIntent();
        int tabToOpen = i.getIntExtra("FirstTab", -1);
        if (tabToOpen!=-1) {

            mViewPager.setCurrentItem(tabToOpen);
        }
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

        MenuItem item3 = menu.findItem(R.id.action_photoVideo);
        Intent intent3 = new Intent(this, PhotoVideoActivity.class );
        item3.setIntent(intent3);

        MenuItem item4 = menu.findItem(R.id.action_journal);
        Intent intent4 = new Intent(this, JournalActivity.class );
        item4.setIntent(intent4);

        MenuItem item5 = menu.findItem(R.id.action_people);
        Intent intent5 = new Intent(this, PeopleActivity.class );
        item5.setIntent(intent5);

        MenuItem item6 = menu.findItem(R.id.action_assignments);
        Intent intent6 = new Intent(this, AssignmentsActivity.class );
        item6.setIntent(intent6);

        MenuItem item7 = menu.findItem(R.id.action_viewpager);
        Intent intent7 = new Intent(this, ViewPagerActivity.class );
        item7.setIntent(intent7);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_mainmenu:

                return true;

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

            case R.id.action_viewpager:
                startActivity(new Intent(this, ViewPagerActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            if(position == 0){

                fragment = new FragmentPhotoVideo();
            }
            if(position == 1){

                fragment = new FragmentJournal();
            }
            if(position == 2){

                fragment = new FragmentPeople();
            }
            if(position == 3){

                fragment = new FragmentAssignments();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
                case 3:
                    return getString(R.string.title_section4).toUpperCase(l);
            }
            return null;
        }
    }
}
