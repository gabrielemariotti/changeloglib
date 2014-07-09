/*******************************************************************************
 * Copyright (c) 2013 Gabriele Mariotti.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package it.gmariotti.changelog.demo;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import it.gmariotti.changelog.demo.fragment.BaseFragment;
import it.gmariotti.changelog.demo.fragment.CustomLayoutFragment;
import it.gmariotti.changelog.demo.fragment.CustomLayoutRowFragment;
import it.gmariotti.changelog.demo.fragment.CustomXmlFileFragment;
import it.gmariotti.changelog.demo.fragment.DialogStandardFragment;
import it.gmariotti.changelog.demo.fragment.StandardFragment;
import it.gmariotti.changelog.demo.fragment.WithoutBulletPointFragment;
import it.gmariotti.changelog.demo.iabutils.IabHelper;
import it.gmariotti.changelog.demo.iabutils.IabResult;
import it.gmariotti.changelog.demo.iabutils.IabUtil;


/**
 *  Main Activity
 *
 *  @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class MainActivity extends ActionBarActivity {

    private ListView mDrawerList;
    private DrawerLayout mDrawer;
    private CustomActionBarDrawerToggle mDrawerToggle;
    private int mCurrentTitle;
    private int mSelectedFragment;

    private IabHelper mHelper;

    private static String TAG= "MainActivity";

    //Used in savedInstanceState
    private static String BUNDLE_SELECTEDFRAGMENT="BDL_SELFRG";
    private static final int CASE_STD=0;
    private static final int CASE_CUSTOM_XML=1;
    private static final int CASE_WITHOUT_BULLET=2;
    private static final int CASE_CUSTOM_HEADER=3;
    private static final int CASE_CUSTOM_ROW=4;
    private static final int CASE_DIALOG=5;
    private static final int CASE_DEBUG=6;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_SELECTEDFRAGMENT,mSelectedFragment);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_changelog_main_activity);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // set a custom shadow_changelogdemo_customlayout that overlays the main content when the drawer
        // opens
        mDrawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        _initMenu();
        mDrawerToggle = new CustomActionBarDrawerToggle(this, mDrawer);
        mDrawer.setDrawerListener(mDrawerToggle);
        // ---------------------------------------------------------------
        // ...
        String base64EncodedPublicKey= IabUtil.key;

        // compute your public key and store it in base64EncodedPublicKey
        mHelper = new IabHelper(this, base64EncodedPublicKey);
        mHelper.enableDebugLogging(true);

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    Log.d(TAG, "Problem setting up In-app Billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // Hooray, IAB is fully set up!
                IabUtil.getInstance().retrieveData(mHelper);
            }
        });

        //-----------------------------------------------------------------
        BaseFragment baseFragment=null;
        if (savedInstanceState!=null){
            mSelectedFragment = savedInstanceState.getInt(BUNDLE_SELECTEDFRAGMENT);
            baseFragment= selectFragment(mSelectedFragment);
        }else{
            baseFragment=new StandardFragment();
        }
        if (baseFragment!=null)
            openFragment(baseFragment);
        //-----------------------------------------------------------------
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        //boolean drawerOpen = mDrawer.isDrawerOpen(mDrawerList);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
		/*
		 * The action bar home/up should open or close the drawer.
		 * ActionBarDrawerToggle will take care of this.
		 */
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {

            //About
            case R.id.menu_about:
                Utils.showAbout(this);
                return true;

            //Real library changelog
            case R.id.menu_changelog:
                Utils.showChangeLog(this);
                return true;

            case R.id.menu_beer:
                IabUtil.showBeer(this, mHelper);
                return true;
        }


        // Handle your other action bar items...
        return super.onOptionsItemSelected(item);
    }

    private class CustomActionBarDrawerToggle extends ActionBarDrawerToggle {

        public CustomActionBarDrawerToggle(Activity mActivity,DrawerLayout mDrawerLayout){
            super(
                    mActivity,
                    mDrawerLayout,
                    R.drawable.ic_navigation_drawer,
                    R.string.demo_changelog_app_name,
                    mCurrentTitle);
        }

        @Override
        public void onDrawerClosed(View view) {
            getSupportActionBar().setTitle(getString(mCurrentTitle));
            supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            getSupportActionBar().setTitle(getString(R.string.demo_changelog_app_name));
            supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // Highlight the selected item, update the title, and close the drawer
            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            BaseFragment baseFragment=selectFragment(position);

            //Save mSelectedFragment, but discard Dialog
            if (position!=CASE_DIALOG)
                mSelectedFragment=position;

            if (baseFragment!=null)
                openFragment(baseFragment);
            mDrawer.closeDrawer(mDrawerList);
        }
    }


    private BaseFragment selectFragment(int position){
        BaseFragment baseFragment=null;

        switch (position) {
            default:
            case CASE_STD:
                baseFragment=new StandardFragment();
                break;
            case CASE_CUSTOM_XML:
                baseFragment = new CustomXmlFileFragment();
                break;
            case CASE_WITHOUT_BULLET:
                baseFragment = new WithoutBulletPointFragment();
                break;
            case CASE_CUSTOM_HEADER:
                baseFragment = new CustomLayoutFragment();
                break;
            case CASE_CUSTOM_ROW:
                baseFragment = new CustomLayoutRowFragment();
                break;
            case CASE_DIALOG:
                openDialogFragment(new DialogStandardFragment());
                break;
            case CASE_DEBUG:
                baseFragment = new DebugParseFragment();
                break;
        }
        return baseFragment;
    }


    private void openDialogFragment(DialogStandardFragment dialogStandardFragment) {
        if (dialogStandardFragment!=null){
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment prev = fm.findFragmentByTag("changelogdemo_dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            //ft.addToBackStack(null);

            dialogStandardFragment.show(ft,"changelogdemo_dialog");
        }
    }

    private void openFragment(BaseFragment baseFragment){
        if (baseFragment!=null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.fragment_main,baseFragment);
            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            mCurrentTitle=baseFragment.getTitleResourceId();
        }
    }


    public static final String[] options = {
            "Standard example",
            "Custom xml file example",
            "Without Bullet point example",
            "Custom layout header example",
            "Custom layout row example",
            "Standard Dialog example",
            "Debug"};


    private void _initMenu(){
        mDrawerList = (ListView) findViewById(R.id.drawer);

        if (mDrawerList!=null){
            mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, options));

            mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        }
        else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

    public IabHelper getHelper() {
        return mHelper;
    }


}