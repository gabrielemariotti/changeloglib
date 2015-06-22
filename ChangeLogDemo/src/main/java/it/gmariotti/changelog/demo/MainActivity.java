/**
 * ****************************************************************************
 * Copyright (c) 2013 Gabriele Mariotti.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ****************************************************************************
 */
package it.gmariotti.changelog.demo;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;

import it.gmariotti.changelog.demo.fragment.BaseFragment;
import it.gmariotti.changelog.demo.fragment.CustomLayoutFragment;
import it.gmariotti.changelog.demo.fragment.CustomLayoutRowFragment;
import it.gmariotti.changelog.demo.fragment.CustomXmlFileFragment;
import it.gmariotti.changelog.demo.fragment.DialogMaterialFragment;
import it.gmariotti.changelog.demo.fragment.MaterialFragment;
import it.gmariotti.changelog.demo.fragment.StandardFragment;
import it.gmariotti.changelog.demo.fragment.WithoutBulletPointFragment;
import it.gmariotti.changelog.demo.iabutils.IabHelper;
import it.gmariotti.changelog.demo.iabutils.IabResult;
import it.gmariotti.changelog.demo.iabutils.IabUtil;
import it.gmariotti.changelog.demo.utils.PrefUtils;
import it.gmariotti.changelog.demo.utils.UtilsBase;


/**
 *  Main Activity
 *
 *  @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class MainActivity extends AppCompatActivity {

    private UtilsBase mUtils;

    private DrawerLayout mDrawerLayout;
    private BaseFragment mSelectedFragment;
    private IabHelper mHelper;

    private static String TAG = "MainActivity";
    private static String BUNDLE_SELECTEDFRAGMENT = "BDL_SELFRG";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_SELECTEDFRAGMENT, mSelectedFragment.getSelfNavDrawerItem());
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_changelog_main_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        // ---------------------------------------------------------------
        // ...
        String base64EncodedPublicKey = IabUtil.key;

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

        if (savedInstanceState != null) {
            int mSelectedFragmentIndex = savedInstanceState.getInt(BUNDLE_SELECTEDFRAGMENT);
            mSelectedFragment = selectFragment(mSelectedFragmentIndex);
        } else {
            mSelectedFragment = new StandardFragment();
        }
        if (mSelectedFragment != null)
            openFragment(mSelectedFragment);


        //-----------------------------------------------------------------
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }


    /**
     * Resolves the fragment
     *
     * @param itemId
     * @return
     */
    private BaseFragment selectFragment(int itemId) {
        BaseFragment baseFragment = null;

        switch (itemId) {
            default:
            case R.id.nav_ex_standard:
                baseFragment = new StandardFragment();
                break;
            case R.id.nav_ex_material:
                baseFragment = new MaterialFragment();
                break;
            case R.id.nav_ex_without_bull:
                baseFragment = new WithoutBulletPointFragment();
                break;
            case R.id.nav_ex_custom_xml:
                baseFragment = new CustomXmlFileFragment();
                break;
            case R.id.nav_ex_custom_header:
                baseFragment = new CustomLayoutFragment();
                break;
            case R.id.nav_ex_custom_row:
                baseFragment = new CustomLayoutRowFragment();
                break;
        }
        return baseFragment;
    }


    /**
     * Opens the dialog
     *
     * @param dialogStandardFragment
     */
    private void openDialogFragment(DialogFragment dialogStandardFragment) {
        if (dialogStandardFragment != null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment prev = fm.findFragmentByTag("changelogdemo_dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            dialogStandardFragment.show(ft, "changelogdemo_dialog");
        }
    }

    /**
     * Adds the fragment to MainContent
     *
     * @param baseFragment
     */
    private void openFragment(BaseFragment baseFragment) {
        if (baseFragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_main, baseFragment);
            fragmentTransaction.commit();
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
        } else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        selectNavigationItem(menuItem.getItemId());
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });

        // When the user runs the app for the first time, we want to land them with the
        // navigation drawer open. But just the first time.
        if (!PrefUtils.isWelcomeDone(this)) {
            // first run of the app starts with the nav drawer open
            PrefUtils.markWelcomeDone(this);
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    /**
     * Handles the navigation view item click
     *
     * @param itemId item id
     */
    private void selectNavigationItem(int itemId) {

        BaseFragment fg = null;
        switch (itemId) {
            case R.id.nav_ex_standard:
            case R.id.nav_ex_material:
            case R.id.nav_ex_custom_xml:
            case R.id.nav_ex_custom_header:
            case R.id.nav_ex_custom_row:
            case R.id.nav_ex_without_bull:
                fg = selectFragment(itemId);
                break;
            case R.id.nav_ex_dialo:
                openDialogFragment(new DialogMaterialFragment());
                break;
            case R.id.nav_other_donate:
                IabUtil.showBeer(this, mHelper);
                break;
            case R.id.nav_other_info:
                Utils.showAbout(this);
                break;
            case R.id.nav_other_github:
                String url = "https://github.com/gabrielemariotti/changeloglib/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
        }
        if (fg != null) {
            mSelectedFragment = fg;
            openFragment(fg);
        }
    }


    public IabHelper getHelper() {
        return mHelper;
    }


}